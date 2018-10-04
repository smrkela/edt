package com.evola.edt.forum.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.evola.edt.forum.model.UserForum;
import com.evola.edt.forum.utils.ForumFormattingUtils;
import com.evola.edt.forum.utils.ForumPasswordGenerator;
import com.evola.edt.model.User;
import com.evola.edt.service.rest.ExecutionStatus;

/**
 * @author Daci, 11.12.2013.
 */
public class ForumRepositoryImpl implements ForumRepository {

	// isljucujemo forum dok se ne sredi sve sto treba
	private static final boolean DISABLED = true;

	private JdbcTemplate jdbcTemplate;
	private int usernameCount = 0;
	private int userEmailCount = 0;
	private int newUserID = 0;
	private int userID = 0;
	private int numberOfPosts = -1;

	/**
	 * Setted by Spring from applicationContext-service.xml
	 * 
	 * @param dataSource
	 *            - definisan u applicationContext-service.xml
	 */
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/*
	 * (non-Javadoc) Kreiranje korisnika na Forumu na osnovu zahteva preko EDT-a
	 * i u toku sinhronizacije
	 * 
	 * @see
	 * com.evola.edt.forum.repository.ForumRepository#createUser(com.evola.edt
	 * .model.User)
	 */
	@Override
	public ExecutionStatus createUser(User user, boolean banUser, String defaultPassword) {

		ExecutionStatus status = new ExecutionStatus();

		if (DISABLED) {

			status.setStatus(ExecutionStatus.Status.OK);
			return status;
		}

		String sqlStatement = null;

		// provera da li postoji vec korisnik sa istim username-om
		sqlStatement = "SELECT COUNT(USERNAME_CLEAN) AS USERNAME FROM phpbb_users WHERE USERNAME_CLEAN = '"
				+ user.getUsername().toLowerCase() + "'";

		try {
			jdbcTemplate.query(sqlStatement, new RowMapper<UserForum>() {

				@Override
				public UserForum mapRow(ResultSet resultSet, int rowNum) throws SQLException {
					usernameCount = Integer.valueOf(resultSet.getInt("USERNAME"));
					return null;
				}

			});

			if (usernameCount > 0) {
				status.setMessage("Korisnik sa upisanim username-om [" + user.getUsername()
						+ "] je već registrovan na forumu VozaciSrbije.com!");
				status.setStatus(ExecutionStatus.Status.NOTOK);

				logAction(1, 0, "EDTService.ForumRepository.createUser(User user)", status.getMessage());

				return status;
			}
		} catch (Exception e) {
			status.setMessage(e.getCause().getLocalizedMessage());
			status.setStatus(ExecutionStatus.Status.NOTOK);

			logAction(1, 0, "EDTService.ForumRepository.createUser(User user)", status.getMessage());

			return status;
		}

		// provera da li postoji vec korisnik sa istom email adresom
		sqlStatement = "SELECT COUNT(USER_EMAIL) AS EMAIL FROM phpbb_users WHERE USER_EMAIL = '" + user.getEmail()
				+ "'";

		try {
			jdbcTemplate.query(sqlStatement, new RowMapper<UserForum>() {

				@Override
				public UserForum mapRow(ResultSet resultSet, int rowNum) throws SQLException {
					userEmailCount = Integer.valueOf(resultSet.getInt("EMAIL"));
					return null;
				}

			});

			if (userEmailCount > 0) {
				status.setMessage("Korisnik sa upisanim email-om [" + user.getEmail()
						+ "] je već registrovan na forumu VozaciSrbije.com!");
				status.setStatus(ExecutionStatus.Status.NOTOK);

				logAction(1, 0, "EDTService.ForumRepository.createUser(User user)", status.getMessage());

				return status;
			}
		} catch (Exception e) {
			status.setMessage(e.getCause().getLocalizedMessage());
			status.setStatus(ExecutionStatus.Status.NOTOK);

			logAction(1, 0, "EDTService.ForumRepository.createUser(User user)", status.getMessage());

			return status;
		}

		// kreiranje novog korisnika na forumu
		sqlStatement = "INSERT INTO phpbb_users VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, "
				+ "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		int user_type = 0;
		int group_id = 2;
		String user_permissions = "00000000006xrqeiww\ni1cjyo000000\nqlaq52000000\ni1cjyo000000\ni1cjyo000000\ni1cjyo000000\ni1cjyo000000\nqlaq52000000\n\nqlctzq000000\ni1cjyo000000\ni1cjyo000000\ni1cjyo000000\ni1cjyo000000\ni1cjyo000000\ni1cjyo000000\ni1cjyo000000\ni1cjyo000000";
		Long user_regdate = ForumFormattingUtils.formatUnixDate();
		String user_password = null;

		if (banUser != true && !defaultPassword.isEmpty()) { // ako korisnik
																// nije setovan
																// za banovanje
																// i ako ima
																// password
																// prosledjen
																// onda je u
																// pitanju
																// sinhronizacija
																// baza
			user_password = new ForumPasswordGenerator().phpbb_hash(defaultPassword);
		} else {
			user_password = new ForumPasswordGenerator().phpbb_hash(user.getPassword()); // ako
																							// je
																							// korisnik
																							// setovan
																							// za
																							// banovanje
																							// i
																							// nema
																							// default
																							// password
																							// onda
																							// je
																							// regularno
																							// kreiranje
																							// korisnika
		}

		Long user_passchg = user_regdate;
		Long user_email_hash = new Long(0);
		Long user_lastvisit = new Long(0);

		try {
			jdbcTemplate.update(sqlStatement, null, user_type, group_id, user_permissions, 0, "127.0.0.1",
					user_regdate, user.getUsername(), user.getUsername().toLowerCase(), user_password, user_passchg, 0,
					user.getEmail(), user_email_hash, "", user_lastvisit, 0, 0, "", "", 0, 0, 0, 0, 0, 0, 0, "en",
					"0.00", 0, "D M d, Y g:i a", 25, 0, "", 0, 0, 0, 0, -3, 0, 0, "t", "d", 0, "t", "a", 0, 1, 0, 1, 1,
					1, 1, 230271, "", 0, 0, 0, "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 1, 0, 0);
			// userID = null
		} catch (Exception e) {
			status.setMessage(e.getCause().getLocalizedMessage());
			status.setStatus(ExecutionStatus.Status.NOTOK);

			logAction(1, 0, "EDTService.ForumRepository.createUser(User user)", status.getMessage());

			return status;
		}

		// uzima ID novo kreiranog korisnika na forumu zbog logovanja i
		// BAN-ovanja
		sqlStatement = "SELECT user_id FROM phpbb_users WHERE username_clean = '" + user.getUsername()
				+ "' AND user_email = '" + user.getEmail() + "'";

		try {
			jdbcTemplate.query(sqlStatement, new RowMapper<UserForum>() {

				@Override
				public UserForum mapRow(ResultSet resultSet, int rowNum) throws SQLException {
					newUserID = Integer.valueOf(resultSet.getString("user_id"));
					return null;
				}

			});

			if (newUserID == 0) {
				status.setMessage("Greška pri rezervaciji ID-a za novog korisnika na forumu VozaciSrbije.com!");
				status.setStatus(ExecutionStatus.Status.NOTOK);

				logAction(1, 0, "EDTService.ForumRepository.createUser(User user)", status.getMessage());

				return status;
			}

			logAction(1, newUserID, "EDTService.ForumRepository.createUser(User user)",
					"Korisnik [" + user.getUsername() + ", " + user.getEmail() + "] uspešno registrovan na Forum!");
		} catch (Exception e) {
			status.setMessage(e.getCause().getLocalizedMessage());
			status.setStatus(ExecutionStatus.Status.NOTOK);

			logAction(1, 0, "EDTService.ForumRepository.createUser(User user)", status.getMessage());

			return status;
		}

		// banovanje korisnika ukoliko je tako trazeno u suprotnom samo vratiti
		// OK odgovor
		if (banUser) {
			ExecutionStatus executionStatus = banUser(user, newUserID, "Username banned via user management",
					"Korisnik ban-ovan do trenutka potvrde registracije i aktivacije naloga na sajtu VozaciSrbije.com.");

			if (executionStatus.getStatus() == ExecutionStatus.Status.NOTOK) {
				status.setStatus(executionStatus.getStatus());
				status.setMessage(executionStatus.getMessage());
			} else {
				status.setStatus(ExecutionStatus.Status.OK);
			}
		} else {
			status.setStatus(ExecutionStatus.Status.OK);
		}

		return status;
	}

	/**
	 * Uklanjanje zabrane (ban-a) korisniku za pristup Forumu
	 * 
	 * @param user
	 *            - korisnik kome je potrebno ukloniti zabranu
	 * @return ExecutionStatus - status izvrsenja akcije
	 */
	public ExecutionStatus unbanUser(User user) {

		ExecutionStatus status = new ExecutionStatus();

		if (DISABLED) {

			status.setStatus(ExecutionStatus.Status.OK);
			return status;
		}

		// Naci ID korisnika koji treba da se unban-uje na Forumu (ako ne
		// postoji, prijaviti gresku)
		String sqlStatement = "SELECT user_id FROM phpbb_users WHERE username_clean = '"
				+ user.getUsername().toLowerCase() + "' AND user_email = '" + user.getEmail().toLowerCase() + "'";

		try {
			jdbcTemplate.query(sqlStatement, new RowMapper<UserForum>() {

				@Override
				public UserForum mapRow(ResultSet resultSet, int rowNum) throws SQLException {
					userID = Integer.valueOf(resultSet.getInt("user_id"));
					return null;
				}

			});

			if (userID == 0) {
				status.setMessage("Korisnik sa username-om [" + user.getUsername() + "] i e-mailom [" + user.getEmail()
						+ "] nije ban-ovan ili ne postoji na Forumu VozaciSrbije.com!");
				status.setStatus(ExecutionStatus.Status.NOTOK);

				logAction(1, 0, "EDTService.ForumRepository.unbanUser(User user)", status.getMessage());

				return status;
			}
		} catch (Exception e) {
			status.setMessage(e.getCause().getLocalizedMessage());
			status.setStatus(ExecutionStatus.Status.NOTOK);

			logAction(1, 0, "EDTService.ForumRepository.unbanUser(User user)", status.getMessage());

			return status;
		}

		// unban-ovanje korisnika
		sqlStatement = "DELETE FROM phpbb_banlist WHERE ban_userid = ?";

		try {
			jdbcTemplate.update(sqlStatement, userID);

			logAction(1, userID, "EDTService.ForumRepository.unbanUser(User user)", "Korisniku [" + user.getUsername()
					+ ", " + user.getEmail() + "] uspešno uklonjen ban na Forumu!");

		} catch (Exception e) {
			status.setMessage(e.getCause().getLocalizedMessage());
			status.setStatus(ExecutionStatus.Status.NOTOK);

			logAction(1, userID, "EDTService.ForumRepository.unbanUser(User user)", status.getMessage());

			return status;
		}

		status.setStatus(ExecutionStatus.Status.OK);

		return status;
	}

	/**
	 * Promena sifre korisnika
	 * 
	 * @param user
	 *            - korisnik kome je potrebno ukloniti zabranu
	 * @return ExecutionStatus - status izvrsenja akcije
	 */
	public ExecutionStatus resetPassword(User user) {

		ExecutionStatus status = new ExecutionStatus();

		if (DISABLED) {

			status.setStatus(ExecutionStatus.Status.OK);
			return status;
		}

		// Naci ID korisnika kome treba da se promeni sifra na Forumu (ako ne
		// postoji, prijaviti gresku)
		String sqlStatement = "SELECT user_id FROM phpbb_users WHERE username_clean = '"
				+ user.getUsername().toLowerCase() + "' AND user_email = '" + user.getEmail().toLowerCase() + "'";

		try {
			jdbcTemplate.query(sqlStatement, new RowMapper<UserForum>() {

				@Override
				public UserForum mapRow(ResultSet resultSet, int rowNum) throws SQLException {
					userID = Integer.valueOf(resultSet.getInt("user_id"));
					return null;
				}

			});

			if (userID == 0) {
				status.setMessage("Korisnik sa username-om [" + user.getUsername() + "] i e-mailom [" + user.getEmail()
						+ "] ne postoji na Forumu VozaciSrbije.com! Promena šifre nije moguća!");
				status.setStatus(ExecutionStatus.Status.NOTOK);

				logAction(1, 0, "EDTService.ForumRepository.resetPassword(User user)", status.getMessage());

				return status;
			}
		} catch (Exception e) {
			status.setMessage(e.getCause().getLocalizedMessage());
			status.setStatus(ExecutionStatus.Status.NOTOK);

			logAction(1, 0, "EDTService.ForumRepository.resetPassword(User user)", status.getMessage());

			return status;
		}

		// promena sifre
		String user_password = new ForumPasswordGenerator().phpbb_hash(user.getPassword());
		sqlStatement = "UPDATE phpbb_users SET user_password = ? WHERE user_id = ?";

		try {
			jdbcTemplate.update(sqlStatement, user_password, userID);

			logAction(1, userID, "EDTService.ForumRepository.resetPassword(User user)",
					"Korisniku [" + user.getUsername() + ", " + user.getEmail() + "] je promenjena šifra na Forumu!");

		} catch (Exception e) {
			status.setMessage(e.getCause().getLocalizedMessage());
			status.setStatus(ExecutionStatus.Status.NOTOK);

			logAction(1, userID, "EDTService.ForumRepository.resetPassword(User user)", status.getMessage());

			return status;
		}

		status.setStatus(ExecutionStatus.Status.OK);

		return status;
	}

	/**
	 * Vraca broj poruka (postova) na Forumu u proteklih 24 sati
	 * 
	 * @return Long - broj postova
	 */
	public Long getNumberOfPreviousPosts() {

		if (DISABLED) {

			return Math.round(Math.random() * 10);
		}

		Long current_time = ForumFormattingUtils.formatUnixDate();
		Long previous_time = ForumFormattingUtils.getUnixDateBefore();

		String sqlStatement = "SELECT COUNT(*) AS NUMOFPOSTS FROM phpbb_posts WHERE post_time BETWEEN '"
				+ previous_time + "' AND '" + current_time + "'";

		try {
			jdbcTemplate.query(sqlStatement, new RowMapper<UserForum>() {

				@Override
				public UserForum mapRow(ResultSet resultSet, int rowNum) throws SQLException {
					numberOfPosts = Integer.valueOf(resultSet.getInt("NUMOFPOSTS"));
					return null;
				}

			});
		} catch (Exception e) {
			return new Long(-1);
		}

		return new Long(numberOfPosts);
	}

	/**
	 * Ban-ovanje (novo kreiranog) korisnika na Forumu do potvrde registracije
	 * 
	 * @param user
	 *            - korisnik koji se ban-uje
	 * @param ban_reason
	 *            - kako je korisnik ban-ovan (preko sajta VozaciSrbije.com
	 * @param ban_give_reason
	 *            - razlog za banovanje
	 * @return ExecutionStatus - status izvrsavanja (OK/NOTOK)
	 */
	private ExecutionStatus banUser(User user, int userID, String ban_reason, String ban_give_reason) {
		ExecutionStatus status = new ExecutionStatus();

		// ban-ovanje korisnika
		String sqlStatement = "INSERT INTO phpbb_banlist VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

		Long ban_start = ForumFormattingUtils.formatUnixDate();

		try {
			jdbcTemplate.update(sqlStatement, null, userID, "", "", ban_start, 0, 0, ban_reason, ban_give_reason); // za
																													// banID
																													// stavljen
																													// null

			logAction(1, userID, "EDTService.ForumRepository.banUser(args)", "Korisnik [" + user.getUsername() + ", "
					+ user.getEmail() + "] uspešno ban-ovan na Forumu do potvrde registracije i aktivacije naloga!");

		} catch (Exception e) {
			status.setMessage(e.getCause().getLocalizedMessage());
			status.setStatus(ExecutionStatus.Status.NOTOK);

			logAction(1, userID, "EDTService.ForumRepository.banUser(args)", status.getMessage());

			return status;
		}

		status.setStatus(ExecutionStatus.Status.OK);

		return status;
	}

	/**
	 * Logovanje svih akcija (uspesnih/neuspesnih) u Forum
	 * 
	 * @param log_type
	 *            - po default-u treba da je setovano na 1
	 * @param userID
	 *            - ID korisnika za kojeg je vezana akcija (registracija,
	 *            ban-ovanje, unban-ovanje, ...)
	 * @param log_operation
	 *            - log operacija (metoda koja je uradila akciju)
	 * @param log_data
	 *            - detalji uradjene operacije
	 * @return ExecutionStatus - status izvrsavanja (OK/NOTOK)
	 */
	private ExecutionStatus logAction(int log_type, int userID, String log_operation, String log_data) {

		ExecutionStatus status = new ExecutionStatus();

		String sqlStatement = "INSERT INTO phpbb_log VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

		Long log_time = ForumFormattingUtils.formatUnixDate();

		try {
			jdbcTemplate.update(sqlStatement, null, log_type, userID, 0, 0, 0, "127.0.0.1", log_time, log_operation,
					log_data); // log_id = null
		} catch (Exception e) {
			status.setMessage(e.getCause().getLocalizedMessage());
			status.setStatus(ExecutionStatus.Status.NOTOK);
			return status;
		}

		status.setStatus(ExecutionStatus.Status.OK);

		return status;
	}

}