package com.evola.edt.managers;

import java.util.Date;

import javax.inject.Inject;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.facebook.api.FacebookProfile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.evola.edt.forum.repository.ForumRepository;
import com.evola.edt.model.User;
import com.evola.edt.repository.UserRepository;
import com.evola.edt.service.rest.ExecutionStatus;
import com.evola.edt.utils.social.SocialMediaService;

@Component
public class RegistrationManager {
	
	static int LAST_NAME_MAX_CHARS = 4;
	static int USERNAME_MAX_CHARS = 14;
	static int USERNAME_MIN_CHARS = 4;

	@Inject
	private UserRepository rUser;

	@Inject
	private ForumRepository rForum;

	@Inject
	private EmailManager emailManager;
	
	@Inject
	private ActivityManager mActivity;

	public User createFromSocialConnection(Connection<?> connection) {

		ConnectionKey key = connection.getKey();

		String signInProvider = key.getProviderId();

		// ako imamo validan sign in provider onda nam neki podaci ne trebaju
		boolean isValidSignInProvider = SocialMediaService.FACEBOOK.name().equals(signInProvider.toUpperCase());

		if (!isValidSignInProvider)
			throw new IllegalArgumentException("Podržani provider je FACEBOOK.");

		// generisemo random password duzine 10
		String password = RandomStringUtils.randomAlphanumeric(10).toUpperCase();

		UserProfile socialMediaProfile = connection.fetchUserProfile();

		String email = socialMediaProfile.getEmail();
		String firstName = socialMediaProfile.getFirstName();
		String lastName = socialMediaProfile.getLastName();
		String username = socialMediaProfile.getUsername();

		// ako nemamo username, generisemo ga
		if (StringUtils.isBlank(username)) {

			username = generateUsername(firstName, lastName);

			// ako nam izgenerisani username nije unique, trazimo unique
			User byUsername = rUser.findByUsername(username);

			if (byUsername != null)
				username = generateUniqueUsername(username);
		}

		boolean isMale = true;

		try {

			Object o = connection.getApi();
			Facebook fbi = (Facebook) o;

			FacebookProfile userProfile = fbi.userOperations().getUserProfile();

			isMale = "male".equals(userProfile.getGender());

		} catch (Exception e) {
		}

		SocialMediaService provider = SocialMediaService.valueOf(key.getProviderId().toUpperCase());

		User user = new User();
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setUsername(username);
		user.setEmail(email);
		user.setIsMale(isMale);
		user.setSignInProvider(provider);
		user.setPassword(password);
		user.setRegistrationDate(new Date());
		user.setLearnedQuestions(0);
		user.setTestedQuestions(0);
		user.setPoints(0);
		// user preko providera nam je odmah enabled
		user.setEnabled(true);

		boolean canAutoRegister = validateSocialUser(user);

		// ako nema dobre podatke za auto register vracamo null kako bi isli na
		// login
		if (canAutoRegister == false)
			return null;

		// Kreira se korisnik na Forum
		ExecutionStatus status = rForum.createUser(user, false, password);

		// svakako radimo enkripciju cak i random sifre
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		user.setPassword(encoder.encode(password));

		emailManager.sendSocialRegistrationEmail(user, status, password);

		user = rUser.save(user);
		
		mActivity.registered(user);

		updateLoginTime(user.getId());

		// // build an Authentication object with the user's info
		// UsernamePasswordAuthenticationToken authentication = new
		// UsernamePasswordAuthenticationToken(user, user.getPassword(),
		// user.getAuthorities());
		// authentication.setDetails(new
		// WebAuthenticationDetailsSource().buildDetails((HttpServletRequest)
		// request));
		//
		// SecurityContextHolder.getContext().setAuthentication(authentication);
		//
		// ProviderSignInUtils.handlePostSignUp(user.getUsername(), webRequest);

		return user;
	}

	public boolean validateSocialUser(User user) {

		// za social user-a bitno nam je da ima podesen username i email

		if (StringUtils.isBlank(user.getUsername()))
			return false;

		if (StringUtils.isBlank(user.getEmail()))
			return false;

		boolean existsByUsername = rUser.countByUsername(user.getUsername()) > 0;

		if (existsByUsername) {
			throw new IllegalStateException("Postoji korisnik sa datim korisničkim imenom.");
		}

		boolean existsByEmail = rUser.countByEmail(user.getEmail()) > 0;

		if (existsByEmail) {
			throw new IllegalStateException("Postoji korisnik sa datom email adresom.");
		}

		return true;
	}

	@Transactional
	public void updateLoginTime(Long id) {

		User findById = rUser.findById(id);

		findById.setLastLogin(new Date());
	}

	/**
	 * Metoda generise korisnicko ime od imena i prezimena. Logika kaze:
	 * korisnicko ime mora imati izmedju 6 i 14 karaktera ukupno. Pravi se
	 * koristeci ime + prezime + redni broj ako ima ponavljanja. Ne uzima se
	 * nikad celo prezime vec maksimalno 4 karaktera.
	 * 
	 * @param firstName
	 * @param lastName
	 * @return
	 */
	String generateUsername(String firstName, String lastName) {

		if (StringUtils.isBlank(firstName))
			firstName = "";

		if (StringUtils.isBlank(lastName))
			lastName = "";

		StringBuilder sb = new StringBuilder();

		sb.append(firstName);

		if (lastName.length() > LAST_NAME_MAX_CHARS)
			lastName = lastName.substring(0, LAST_NAME_MAX_CHARS);

		sb.append(lastName);

		if (sb.length() > USERNAME_MAX_CHARS)
			sb.delete(USERNAME_MAX_CHARS, sb.length());

		// ako nam je broj karaktera manji od minimuma onda moramo dodati
		// znakove
		if (sb.length() < USERNAME_MIN_CHARS) {

			sb.append(RandomStringUtils.random(USERNAME_MIN_CHARS - sb.length(), false, true));
		}

		// sada imamo validan broj karaktera no treba da proverimo da li je
		// username unique

		String username = sb.toString();

		return username.toLowerCase();
	}

	private String generateUniqueUsername(String username) {

		// idemo redom kroz brojeve i trazimo prvi za koji nam je slobodan

		Integer counter = 1;

		// idemo max kroz N ponavljanja da bi izbegli beskonacnu petlju

		int limit = 5000;

		String testedUsername = null;
		User testedUser = null;

		while (limit > 0) {

			testedUsername = username + counter;

			testedUser = rUser.findByUsername(testedUsername);

			if (testedUser == null) {

				//imamo slobodan username
				
				break;
				
			} else {

				counter++;
				limit--;
			}
		}

		return testedUsername;
	}

}
