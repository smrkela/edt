package com.evola.edt.component;

import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;

import com.evola.edt.model.dto.DrivingSchoolDTO;
import com.evola.edt.model.dto.DrivingSchoolNotificationDTO;
import com.evola.edt.service.dto.helpers.DocumentDTO;
import com.evola.edt.service.util.EdtSignInUtils;
import com.evola.edt.utils.PageCategories;
import com.evola.edt.utils.PageInfo;

@Component
public class PageMetadataManager {

	@Inject
	HttpServletRequest request;

	@Value("${fbLogoImg}")
	private String fbLogoImg;

	@Value("${applicationUrl}")
	private String applicationUrl;

	// naziv parametra u modelu za page info
	private static final String NAME = "pageInfo";

	// sufix za svaki title
	private static final String PREFIX = "Vozači Srbije | ";

	// genericki title
	private static final String TITLE = PREFIX + "Dobrodošli među vozače, učenike, auto škole...";

	// genericki opis
	private static final String DESCRIPTION = "Vozači Srbije je sajt posvećen učenicima autoškola, budućim učenicima, autoškolama, vozačima ali i svima ostalima.";

	public void addHomePage(ModelAndView mav) {

		mav.addObject("pageInfo", createPageInfo("glavna", "Glavna", PageCategories.GLAVNA, PREFIX + "Dobrodošli među vozače, učenike, auto škole...", DESCRIPTION));
	}

	public void addLoginPage(ModelAndView mav) {

		mav.addObject("pageInfo", createPageInfo("login", "Login", PageCategories.GLAVNA, PREFIX + "Uloguj se", "Ulogujte se na sajt Vozaci Srbije i uživajte u brojnim sadržajima."));
	}

	public void addRegisterPage(ModelAndView mv) {

		mv.addObject("pageInfo", createPageInfo("register", "Registracija", PageCategories.GLAVNA, PREFIX + "Registruj se", "Registrujte se na sajt Vozaci Srbije i uživajte u brojnim sadržajima."));
	}

	public void addRegisterSuccessPage(ModelAndView mv) {

		mv.addObject("pageInfo", createPageInfo("registracijaUspesna", "Registracija uspešna", PageCategories.GLAVNA, TITLE, DESCRIPTION));
	}

	public void addForumPage(ModelAndView mv) {

		mv.addObject("pageInfo", createPageInfo("forum", "Forum", PageCategories.FORUM, PREFIX + "Dobrodošli na forum posvećen vozačima, učenicima i autoškolama", "Forum sajta Vozači Srbije posvećen autoškolama, vozačima, polaznicima autoškola i svima koji planiraju da upišu autoškolu."));
	}

	public void addContactPage(ModelAndView mv) {

		mv.addObject("pageInfo",
				createPageInfo("kontakt", "Kontakt", PageCategories.GLAVNA, PREFIX + "Kontaktirajte nas, pitajte, predložite...", "Ukoliko imate bilo kakvih pitanja vezano za autoškole, polaganje, pravila polaganja, upis ili nešto vezano za sam sajt ili aplikaciju slobodno nas pitajte."));
	}

	public void addContactEmailSuccess(ModelAndView mv) {

		mv.addObject("pageInfo", createPageInfo("uspesnoPoslatMail", "Uspešno poslat mail", PageCategories.GLAVNA, PREFIX + "Hvala na poslatoj poruci.", DESCRIPTION));
	}

	public void addPage(ModelAndView mv, String uniqueName, String title, String smallPreview, Long pageId) {

		String fbImage = "resource?path=/pages/"+pageId+"/normalPreview";
		
		mv.addObject("pageInfo", createPageInfo(uniqueName, title, PageCategories.NOVOSTI, PREFIX + title, title, getURL(), smallPreview, fbImage, "article", PageCategories.NOVOSTI));
	}

	public void addPagesPage(ModelAndView mv) {

		mv.addObject(
				"pageInfo",
				createPageInfo("vesti", "Vesti", PageCategories.NOVOSTI, PREFIX + "Vesti", "Vesti o dešavanjima na sajtu, auto školama, učenicima, testovima za polaganje vožnje itd.", getURL(), "Vesti o dešavanjima na sajtu, auto školama, učenicima, testovima za polaganje vožnje itd.",
						getImageURL(), "article", PageCategories.NOVOSTI));
	}

	public void addInformationsPage(ModelAndView mv) {

		mv.addObject("pageInfo", createPageInfo("informacije", "Informacije", PageCategories.INFORMACIJE, PREFIX + "Informacije", "Informacije o autoškolama, polaganju vožnje, dokumentima, procedurama itd. Ovaj deo sajta Vozači Srbije je "
				+ "specijalizovan i sadrži sve informacije potrebne kandidatima kako bi se na vreme mogli pripremiti za sve situacije " + "u vezi sa polaganjem vožnje.", getURL(), "Informacije o autoškolama, polaganju vožnje, dokumentima, procedurama itd. Ovaj deo sajta Vozači Srbije je "
				+ "specijalizovan i sadrži sve informacije potrebne kandidatima kako bi se na vreme mogli pripremiti za sve situacije " + "u vezi sa polaganjem vožnje.", getImageURL(), "article", PageCategories.INFORMACIJE));
	}

	public void addHelpsPage(ModelAndView mv) {

		mv.addObject("pageInfo",
				createPageInfo("pomoc", "Pomoć", PageCategories.POMOC, PREFIX + "Pomoć", "Informacije o sajtu, često postavljena pitanja, saveti, uputstva itd.", getURL(), "Informacije o sajtu, često postavljena pitanja, saveti, uputstva itd.", getImageURL(), "article", PageCategories.POMOC));

		// mv.addObject("pageInfo", createPageInfo("pomoc", "Pomoć",
		// PageCategories.POMOC, PREFIX
		// + "Pomoć",
		// "Informacije o sajtu, često postavljena pitanja, saveti, uputstva itd."));
	}

	public void addInformationPage(ModelAndView mv, String uniqueName, String title, String fbDescription) {

		mv.addObject("pageInfo", createPageInfo(uniqueName, title, PageCategories.INFORMACIJE, PREFIX + title, title, getURL(), fbDescription, getImageURL(), "article", PageCategories.INFORMACIJE));

	}

	public void addHelpPage(ModelAndView mv, String uniqueName, String title) {

		mv.addObject(
				"pageInfo",
				createPageInfo(uniqueName, title, PageCategories.POMOC, PREFIX + "Pomoć - " + title, "Informacije o sajtu, često postavljena pitanja, saveti, uputstva itd.", getURL(), "Informacije o sajtu, često postavljena pitanja, saveti, uputstva itd.", getImageURL(), "article",
						PageCategories.POMOC));

		// mv.addObject("pageInfo", createPageInfo(uniqueName, title,
		// PageCategories.POMOC, PREFIX + title, title));
	}

	public void addLearningPage(ModelAndView mav, String title, String fbTitle) {

		mav.addObject(
				"pageInfo",
				createPageInfo("priprema-za-ucenje", title, PageCategories.UCENJE, PREFIX + "Učenje i priprema za polaganje vožnje online - " + fbTitle, "Besplatno učenje i priprema za polaganje vožnje online. Sva pitanja sa odgovorima za pripremu i polaganje vožnje " + "na jednom mestu. "
						+ fbTitle, getURL(), "Besplatno učenje i priprema za polaganje vožnje online. Sva pitanja sa odgovorima za pripremu i polaganje vožnje " + "na jednom mestu. " + fbTitle, getImageURL(), "article", PageCategories.UCENJE));
	}

	public void addAboutApplication(ModelAndView mav) {

		mav.addObject(
				"pageInfo",
				createPageInfo("priprema-za-vozacki-ispit-testovi-aplikacija", "Aplikacija za učenje i testiranje", PageCategories.UCENJE, PREFIX + "Aplikacija za učenje, testiranje i pripremu za polaganje teorijskog vozačkog ispita.",
						"Web aplikacija za zabavno učenje, testiranje, praćenje napretka, statistike, komunikacije sa drugim učenicima i još mnogo toga...", getURL(), "Web aplikacija za zabavno učenje, testiranje, praćenje napretka, statistike, komunikacije sa drugim učenicima i još mnogo toga...",
						getImageURL(), "article", PageCategories.UCENJE));
	}

	public void addPrepareForLearningPage(ModelAndView mv) {

		mv.addObject(
				"pageInfo",
				createPageInfo("priprema-za-ucenje", "Priprema za učenje", PageCategories.UCENJE, PREFIX + "Priprema za besplatno učenje online, vozački ispit i testove", "Priprema za učenje, izbor kategorije vožnje i grupe pitanja za online učenje.", getURL(),
						"Priprema za učenje, izbor kategorije vožnje i grupe pitanja za online učenje.", getImageURL(), "article", PageCategories.UCENJE));

		// mv.addObject("pageInfo", createPageInfo("priprema-za-ucenje",
		// "Priprema za ucenje", PageCategories.UCENJE, PREFIX
		// + "Priprema za besplatno učenje online",
		// "Priprema za učenje, izbor kategorije vožnje i grupe pitanja za online učenje."));
	}

	public void addRecentlyActiveUsersPage(ModelAndView mv) {

		mv.addObject(
				"pageInfo",
				createPageInfo("aktivni-ucenici", "Aktivni učenici", PageCategories.UCENJE, PREFIX + "Lista učenika koji su bili aktivni poslednjih 7 dana", "Učenici koji su bili aktivni poslednjih 7 dana i koristili aplikaciju za učenje i testiranje.", getURL(),
						"Učenici koji su bili aktivni poslednjih 7 dana i koristili aplikaciju za učenje i testiranje.", getImageURL(), "article", PageCategories.UCENJE));
	}

	public void addDrivingSchoolListPage(ModelAndView mav, String title) {

		mav.addObject(
				"pageInfo",
				createPageInfo("lista-autoskola", title, PageCategories.AUTOSKOLE, PREFIX + "Spisak auto škola Srbije", "Spisak auto škola u Srbiji sa osnovnim informacijama, kontaktom, telefonom, adresom, mapom, obaveštenjima, slikama itd.", getURL(),
						"Spisak auto škola u Srbiji sa osnovnim informacijama, kontaktom, telefonom, adresom, mapom, obaveštenjima, slikama itd.", getImageURL(), "article", PageCategories.AUTOSKOLE));

		// mav.addObject(

		// "pageInfo",
		// createPageInfo(
		// "lista-autoskola",
		// title,
		// PageCategories.AUTOSKOLE,
		// PREFIX + "Spisak auto škola Srbije",
		// "Spisak auto škola u Srbiji sa osnovnim informacijama, kontaktom, telefonom, adresom, mapom, obaveštenjima, slikama itd."));
	}

	public void addDrivingSchoolBasicInfoPage(ModelAndView mav, DrivingSchoolDTO dto) {

		mav.addObject(
				"pageInfo",
				createPageInfo("profil", dto.getName(), PageCategories.AUTOSKOLE, PREFIX + "Osnovne informacije o auto školi " + dto.getName(), "Auto škola " + dto.getName() + " - Osnovne informacije o auto školi", getURL(), "Auto škola " + dto.getName() + " - Osnovne informacije o auto školi",
						getImageURL(), "article", PageCategories.AUTOSKOLE));

		// mav.addObject("pageInfo",

		// createPageInfo("profil", "O auto školi", PageCategories.AUTOSKOLE,
		// PREFIX + dto.getName()
		// + " - Osnovne informacije", PREFIX + dto.getName() +
		// " - Osnovne informacije"));
	}

	public void addDrivingSchoolContactPage(ModelAndView mav, DrivingSchoolDTO dto) {

		mav.addObject(
				"pageInfo",
				createPageInfo("kontakt", "Kontakt", PageCategories.AUTOSKOLE, PREFIX + "Informacije o kontaktu u auto školi " + dto.getName(), "Auto škola " + dto.getName() + " - Kontakt, radno vreme, lokacija, o nama, ...", getURL(), "Auto škola " + dto.getName()
						+ " - Kontakt, radno vreme, lokacija, o nama, ...", getImageURL(), "article", PageCategories.AUTOSKOLE));

		// mav.addObject("pageInfo", createPageInfo("kontakt", "Kontakt",
		// PageCategories.AUTOSKOLE, PREFIX + dto.getName()
		// + " - Kontakt, radno vreme, lokacija", PREFIX + dto.getName() +
		// " - Kontakt, radno vreme, lokacija"));
	}

	public void addDrivingSchoolTeamPage(ModelAndView mav, DrivingSchoolDTO dto) {

		mav.addObject(
				"pageInfo",
				createPageInfo("tim", "Tim", PageCategories.AUTOSKOLE, PREFIX + "Informacije o članovima i instruktorima u auto školi " + dto.getName(), "Auto škola " + dto.getName() + " - Članovi i instruktori auto škole", getURL(), "Auto škola " + dto.getName()
						+ " - Članovi i instruktori auto škole", getImageURL(), "article", PageCategories.AUTOSKOLE));

		// mav.addObject("pageInfo",

		// createPageInfo("tim", "Tim", PageCategories.AUTOSKOLE, PREFIX +
		// dto.getName()
		// + " - Članovi i instruktori auto škole", PREFIX + dto.getName()
		// + " - Članovi i instruktori auto škole"));
	}

	public void addDrivingSchoolCarsPage(ModelAndView mav, DrivingSchoolDTO dto) {

		mav.addObject(
				"pageInfo",
				createPageInfo("vozni-park", "Vozni park", PageCategories.AUTOSKOLE, PREFIX + "Vozni park auto škole " + dto.getName(), "Auto škola " + dto.getName() + " - Vozila auto škole", getURL(), "Auto škola " + dto.getName() + " - Vozila auto škole", getImageURL(), "article",
						PageCategories.AUTOSKOLE));

		// mav.addObject("pageInfo",

		// createPageInfo("vozni-park", "Vozni park", PageCategories.AUTOSKOLE,
		// PREFIX + dto.getName()
		// + " - Vozila auto škole", PREFIX + dto.getName() +
		// " - Vozila auto škole"));
	}

	public void addDrivingSchoolNotificationsPage(ModelAndView mav, DrivingSchoolDTO dto) {

		mav.addObject(
				"pageInfo",
				createPageInfo("obavestenja", "Obaveštenja", PageCategories.AUTOSKOLE, PREFIX + "Obaveštenja auto škole " + dto.getName(), "Auto škola " + dto.getName() + " - Obaveštenja auto škole, novosti, datum polaganja, vožnje itd.", getURL(), "Auto škola " + dto.getName()
						+ " - Obaveštenja auto škole, novosti, datum polaganja, vožnje itd.", getImageURL(), "article", PageCategories.AUTOSKOLE));

		// mav.addObject("pageInfo",

		// createPageInfo("obavestenja", "Obaveštenja",
		// PageCategories.AUTOSKOLE,
		// PREFIX + dto.getName()
		// + " - Obaveštenja auto škole", PREFIX + dto.getName()
		// +
		// " - Obaveštenja auto škole, novosti, datum polaganja, vožnje itd."));
	}

	public void addDrivingSchoolSingleNotificationPage(ModelAndView mav, DrivingSchoolDTO dto, DrivingSchoolNotificationDTO notificationDTO) {

		mav.addObject(
				"pageInfo",
				createPageInfo("obavestenja", notificationDTO.getTitle(), PageCategories.AUTOSKOLE, PREFIX + "Obaveštenje auto škole " + dto.getName() + " - " + notificationDTO.getTitle(), "Auto škola " + dto.getName() + " - Obaveštenja auto škole, novosti, datum polaganja, vožnje itd.", getURL(),
						"Auto škola " + dto.getName() + " - Obaveštenja auto škole, novosti, datum polaganja, vožnje itd.", getImageURL(), "article", PageCategories.AUTOSKOLE));

		// mav.addObject("pageInfo",

		// createPageInfo("obavestenja", "Obaveštenje",
		// PageCategories.AUTOSKOLE,
		// PREFIX + dto.getName() + " - "
		// + notificationDTO.getTitle(), PREFIX + dto.getName()
		// +
		// " - Obaveštenja auto škole, novosti, datum polaganja, vožnje itd."));
	}

	public void addDrivingSchoolMarksPage(ModelAndView mav, DrivingSchoolDTO dto) {

		mav.addObject(
				"pageInfo",
				createPageInfo("ocene", "Ocene", PageCategories.AUTOSKOLE, PREFIX + "Prikaz ocena auto škole " + dto.getName(), "Auto škola " + dto.getName() + " - Ocene auto škole, utisci prethodnih i sadašnjih učenika, kritike, pohvale i komentari.", getURL(), "Auto škola " + dto.getName()
						+ " - Ocene auto škole, utisci prethodnih i sadašnjih učenika, kritike, pohvale i komentari.", getImageURL(), "article", PageCategories.AUTOSKOLE));

		// mav.addObject("pageInfo", createPageInfo("ocene", "Ocene",
		// PageCategories.AUTOSKOLE, PREFIX + dto.getName()
		// + " - Ocene auto škole", PREFIX + dto.getName()
		// +
		// " - Ocene auto škole, utisci prethodnih i sadašnjih učenika, kritike, pohvale i komentari."));
	}

	public void addDrivingSchoolGalleryPage(ModelAndView mav, DrivingSchoolDTO dto) {

		mav.addObject(
				"pageInfo",
				createPageInfo("galerija", "Galerija", PageCategories.AUTOSKOLE, PREFIX + "Albumi i slike auto škole " + dto.getName(), "Auto škola " + dto.getName() + " - Slike autoškole, vozila, lokacije, osoblja, instruktora, učenika, polaganja itd.", getURL(), "Auto škola " + dto.getName()
						+ " - Slike autoškole, vozila, lokacije, osoblja, instruktora, učenika, polaganja itd.", getImageURL(), "article", PageCategories.AUTOSKOLE));

		// mav.addObject("pageInfo", createPageInfo("galerija", "Galerija",
		// PageCategories.AUTOSKOLE, PREFIX + dto.getName()
		// + " - Albumi i slike auto škole", PREFIX + dto.getName()
		// +
		// " - Slike autoškole, vozila, lokacije, osoblja, instruktora, učenika, polaganja itd."));
	}

	public void addDrivingSchoolAlbumPage(ModelAndView mav, DrivingSchoolDTO dto, DocumentDTO documentDTO) {

		mav.addObject(
				"pageInfo",
				createPageInfo("galerija", "Galerija - album " + documentDTO.getName(), PageCategories.AUTOSKOLE, PREFIX + "Pregled albuma " + documentDTO.getName() + " auto škole " + dto.getName(), "Auto škola " + dto.getName()
						+ " - Slike autoškole, vozila, lokacije, osoblja, instruktora, učenika, polaganja itd.", getURL(), "Auto škola " + dto.getName() + " - Slike autoškole, vozila, lokacije, osoblja, instruktora, učenika, polaganja itd.", getImageURL(), "article", PageCategories.AUTOSKOLE));

		// mav.addObject("pageInfo", createPageInfo("galerija", "Galerija",
		// PageCategories.AUTOSKOLE, PREFIX + dto.getName()
		// + " - Pregled albuma " + documentDTO.getName(), PREFIX +
		// dto.getName()
		// +
		// " - Slike autoškole, vozila, lokacije, osoblja, instruktora, učenika, polaganja itd."));
	}

	public void addDrivingSchoolPricesPage(ModelAndView mav, DrivingSchoolDTO dto) {

		mav.addObject(
				"pageInfo",
				createPageInfo("cenovnik", "Cenovnik", PageCategories.AUTOSKOLE, PREFIX + "Cenovnik, upis i uslovi auto škole " + dto.getName(), "Auto škola " + dto.getName() + " - Cena upisa, vožnje, teorijskog ispita, praktičnog ispita, uslovi upisa itd.", getURL(), "Auto škola " + dto.getName()
						+ " - Cena upisa, vožnje, teorijskog ispita, praktičnog ispita, uslovi upisa itd.", getImageURL(), "article", PageCategories.AUTOSKOLE));

		// mav.addObject("pageInfo", createPageInfo("cenovnik", "Cenovnik",
		// PageCategories.AUTOSKOLE, PREFIX + dto.getName()
		// + " - Cenovnik, upis i uslovi", PREFIX + dto.getName()
		// +
		// " - Cena upisa, vožnje, teorijskog ispita, praktičnog ispita, uslovi upisa itd."));
	}

	public void addDailyTestPage(ModelAndView mv) {

		mv.addObject(
				"pageInfo",
				createPageInfo("test-dana", "Test dana", PageCategories.UCENJE, PREFIX + "Test dana", "Svakog dana objavljujemo po jedan test kojim možete da testirate svoje znanje, takmičite se sa ostalima i uporedite svoje znanje u odnosu na druge.", getURL(),
						"Svakog dana objavljujemo po jedan test kojim možete da testirate svoje znanje, takmičite se sa ostalima i uporedite svoje znanje u odnosu na druge.", getImageURL(), "article", PageCategories.UCENJE));
	}

	public void addDailyTestUsersPage(ModelAndView mv, String dateString) {

		mv.addObject(
				"pageInfo",
				createPageInfo("test-dana", "Takmičari testa dana - " + dateString, PageCategories.UCENJE, PREFIX + "Učesnici testa dana", "Pregled svih učesnika testa dana i njihovih ostvarenih rezultata za datum " + dateString, getURL(),
						"Pregled svih učesnika testa dana i njihovih ostvarenih rezultata za datum " + dateString, getImageURL(), "article", PageCategories.UCENJE));

		// mv.addObject(

		// "pageInfo",
		// createPageInfo(
		// "test-dana",
		// "Takmičari testa dana - "+dateString,
		// PageCategories.UCENJE,
		// PREFIX + "Učesnici testa dana",
		// "Pregled svih učesnika testa dana i njihovih ostvarenih rezultata."));
	}

	public void addRealTestUsersPage(ModelAndView mv, String testName) {

		mv.addObject(
				"pageInfo",
				createPageInfo("testovi", "Test " + testName, PageCategories.UCENJE, PREFIX + "Test "+testName, "Pregled svih učesnika testa " + testName + " i njihovih ostvarenih rezultata", getURL(), "Pregled svih učesnika testa " + testName + " i njihovih ostvarenih rezultata",
						getImageURL(), "article", PageCategories.UCENJE));
	}

	public void addDoDailyTestPage(ModelAndView mv) {

		mv.addObject(
				"pageInfo",
				createPageInfo("test-dana", "Test dana - polaganje", PageCategories.UCENJE, PREFIX + "Test dana - polaganje", "Svakog dana objavljujemo po jedan test kojim možete da testirate svoje znanje, takmičite se sa ostalima i uporedite svoje znanje u odnosu na druge.", getURL(),
						"Svakog dana objavljujemo po jedan test kojim možete da testirate svoje znanje, takmičite se sa ostalima i uporedite svoje znanje u odnosu na druge.", getImageURL(), "article", PageCategories.UCENJE));
	}

	public void addDoRealTestPage(ModelAndView mv, String testName) {

		mv.addObject(
				"pageInfo",
				createPageInfo("testovi", "Test " + testName + " - polaganje", PageCategories.UCENJE, PREFIX + "Test " + testName + " - polaganje",
						"Na našem sajtu možete pronaći mnoštvo testova kojima možete poboljšati svoje znanje, takmičiti se sa ostalima i uporediti svoje znanje u odnosu na druge.", getURL(),
						"Na našem sajtu možete pronaći mnoštvo testova kojima možete poboljšati svoje znanje, takmičiti se sa ostalima i uporediti svoje znanje u odnosu na druge.", getImageURL(), "article", PageCategories.UCENJE));
	}

	public void addDailyTestTopListPage(ModelAndView mv) {

		mv.addObject(
				"pageInfo",
				createPageInfo("test-dana", "Top lista testova dana", PageCategories.UCENJE, PREFIX + "Top lista testova dana", "Top lista korisnika sa ukupnim brojem poena koje su osvojili polažući test dana.", getURL(),
						"Top lista korisnika sa ukupnim brojem poena koje su osvojili polažući test dana.", getImageURL(), "article", PageCategories.UCENJE));
	}

	public void addAllDailyTestsPage(ModelAndView mv) {

		mv.addObject("pageInfo", createPageInfo("test-dana", "Test dana - svi testovi", PageCategories.UCENJE, PREFIX + "Test dana - svi testovi", "Svakog dana objavljujemo po jedan test kojim možete da testirate svoje znanje, takmičite se sa ostalima i uporedite svoje znanje "
				+ "u odnosu na druge. Takođe, dostupna je lista svih testova dana sa mogućnošću polaganja bilo kojeg od njih.", getURL(), "Svakog dana objavljujemo po jedan test kojim možete da testirate svoje znanje, takmičite se sa ostalima i uporedite svoje znanje "
				+ "u odnosu na druge. Takođe, dostupna je lista svih testova dana sa mogućnošću polaganja bilo kojeg od njih.", getImageURL(), "article", PageCategories.UCENJE));
	}

	public void addDrivingSchoolsForCityPage(ModelAndView mav, String city) {

		mav.addObject("pageInfo",
				createPageInfo("autoskole-po-gradu", "Auto škole za grad " + city, PageCategories.AUTOSKOLE, PREFIX + "Spisak auto škola za grad " + city, "Spisak auto škola za grad " + city + " sa osnovnim informacijama, kontaktom, telefonom, adresom, mapom, obaveštenjima, slikama itd."));
	}

	public void addDrivingSchoolsMapPage(ModelAndView mav) {

		mav.addObject(
				"pageInfo",
				createPageInfo("mapa-auto-skola", "Mapa auto škola u Srbiji", PageCategories.AUTOSKOLE, PREFIX + "Mapa auto škola u Srbiji", "Mapa auto škola u Srbiji sa tačnim pozicijama na karti Srbije", getURL(), "Mapa auto škola u Srbiji sa tačnim pozicijama na karti Srbije", getImageURL(),
						"article", PageCategories.AUTOSKOLE));

		// mav.addObject(
		// "pageInfo",
		// createPageInfo(
		// "mapa-auto-skola",
		// "Mapa auto škola u Srbiji",
		// PageCategories.AUTOSKOLE,
		// PREFIX + "Mapa auto škola u Srbiji",
		// "Mapa auto škola u Srbiji sa tačnim pozicijama na karti Srbije"));
	}

	public void addDailyTestUserResult(ModelAndView mv, String username, String dateString) {

		mv.addObject(
				"pageInfo",
				createPageInfo("test-dana", "Test dana - rezultat polaganja testa od dana " + dateString + " za korisnika " + username, PageCategories.UCENJE, PREFIX + "Test dana - rezultat polaganja testa od dana " + dateString + " za korisnika " + username, "Detaljni rezultat testa dana.",
						getURL(), "Detaljni rezultat testa dana.", getImageURL(), "article", PageCategories.UCENJE));
	}

	public void addRealTestUserResult(ModelAndView mv, String username, String testName) {

		mv.addObject(
				"pageInfo",
				createPageInfo("testovi", "Test - rezultat polaganja testa '" + testName + "' za korisnika " + username, PageCategories.UCENJE, PREFIX + "Test - rezultat polaganja testa '" + testName + "' za korisnika " + username, "Detaljni rezultat testa.", getURL(), "Detaljni rezultat testa.",
						getImageURL(), "article", PageCategories.UCENJE));
	}

	public void addDailyTestUserResultWhenNotLoggedIn(ModelAndView mv, String username, String dateString) {

		mv.addObject(
				"pageInfo",
				createPageInfo("test-dana", "Test dana - rezultat polaganja testa od dana " + dateString + " za korisnika " + username, PageCategories.UCENJE, PREFIX + "Test dana - rezultat polaganja testa od dana " + dateString + " za korisnika " + username, "Detaljni rezultat testa dana.",
						getURL(), "Detaljni rezultat testa dana.", getImageURL(), "article", PageCategories.UCENJE));
	}

	public void addRealTestUserResultWhenNotLoggedIn(ModelAndView mv, String username, String testName) {

		mv.addObject(
				"pageInfo",
				createPageInfo("testovi", "Test - rezultat polaganja testa '" + testName + "' za korisnika " + username, PageCategories.UCENJE, PREFIX + "Test - rezultat polaganja testa '" + testName + "' za korisnika " + username, "Detaljni rezultat testa.", getURL(), "Detaljni rezultat testa.",
						getImageURL(), "article", PageCategories.UCENJE));
	}

	public void addPrivacyPolicy(ModelAndView mv) {

		mv.addObject(
				"pageInfo",
				createPageInfo("politika-privatnosti", "Politika privatnosti", PageCategories.GLAVNA, PREFIX + "Politika privatnosti sajta Vozači Srbije", "Politika privatnosti sajta Vozači Srbije", getURL(), "Politika privatnosti sajta Vozači Srbije", getImageURL(), "article",
						PageCategories.GLAVNA));

		// mv.addObject(

		// "pageInfo",
		// createPageInfo(
		// "politika-privatnosti",
		// "Politika privatnosti",
		// PageCategories.GLAVNA,
		// PREFIX + "Politika privatnosti",
		// "Politika privatnosti sajta."));
	}

	public void addTermsOfService(ModelAndView mv) {

		mv.addObject("pageInfo",
				createPageInfo("uslovi-koriscenja", "Uslovi korišćenja", PageCategories.GLAVNA, PREFIX + "Uslovi korišćenja sajta Vozači Srbije", "Uslovi korišćenja sajta Vozači Srbije", getURL(), "Uslovi korišćenja sajta Vozači Srbije", getImageURL(), "article", PageCategories.GLAVNA));

		// mv.addObject(
		// "pageInfo",
		// createPageInfo(
		// "uslovi-koriscenja",
		// "Uslovi korišćenja",
		// PageCategories.GLAVNA,
		// PREFIX + "Uslovi korišćenja",
		// "Uslovi korišćenja sajta."));
	}

	public void addDrivingSchoolsNotificationsPage(ModelAndView mav) {

		mav.addObject("pageInfo", createPageInfo("obavestenja", "Obaveštenja auto škola",

		PageCategories.AUTOSKOLE, PREFIX + "Obaveštenja auto škola", "Obaveštenja auto škola, novosti, datumi polaganja, vožnje itd.", getURL(), "Obaveštenja auto škola, novosti, datumi polaganja, vožnje itd.", getImageURL(), "article", PageCategories.AUTOSKOLE));

		// mav.addObject("pageInfo", createPageInfo("obavestenja",
		// "Obaveštenja auto škola", PageCategories.AUTOSKOLE, PREFIX
		// + "Obaveštenja auto škola", PREFIX +
		// "Obaveštenja auto škola, novosti, datumi polaganja, vožnje itd."));

	}

	private String getImageURL() {
		
		String baseUrl = getBaseUrl();

		return baseUrl + "/" + fbLogoImg;
	}

	private String getBaseUrl() {
		
		String baseUrl = String.format("%s://%s:%d%s", request.getScheme(), request.getServerName(), request.getServerPort(), request.getContextPath());
		
		return baseUrl;
	}

	private String getURL() {
		String requestURI = request.getRequestURI();
		String query = request.getQueryString();

		String baseUrl = String.format("%s://%s:%d", request.getScheme(), request.getServerName(), request.getServerPort());
		// String baseUrl = String.format("%s://%s", request.getScheme(),
		// request.getServerName(), request.getServerPort());

		if (StringUtils.isNotBlank(query))
			requestURI = requestURI + "?" + query;

		return baseUrl + requestURI;
	}

	public void addReportsPage(ModelAndView mv) {

		mv.addObject("pageInfo", createPageInfo("reports", "Izveštaji", PageCategories.GLAVNA, PREFIX + "Izveštaji", DESCRIPTION));
	}

	public void addReportsLoggedInUsersPage(ModelAndView mv) {

		mv.addObject("pageInfo", createPageInfo("logged-in-users", "Ulogovani korisnici", PageCategories.GLAVNA, PREFIX + "Ulogovani korisnici", DESCRIPTION));
	}

	public void addReportsRegisteredUsersPage(ModelAndView mv) {

		mv.addObject("pageInfo", createPageInfo("registered-users", "Registrovani korisnici", PageCategories.GLAVNA, PREFIX + "Registrovani korisnici", DESCRIPTION));
	}

	public void addReportsApplicationUsersPage(ModelAndView mv) {

		mv.addObject("pageInfo", createPageInfo("application-users", "Korisnici aplikacije", PageCategories.GLAVNA, PREFIX + "Korisnici aplikacije", DESCRIPTION));
	}

	public void addReportsLicensedDrivingSchoolsPage(ModelAndView mv) {

		mv.addObject("pageInfo", createPageInfo("licenced-driving-schools", "Licencirane auto škole", PageCategories.GLAVNA, PREFIX + "Licencirane auto škole", DESCRIPTION));
	}

	public void addReportsDrivingSchoolsLoginPage(ModelAndView mv) {

		mv.addObject("pageInfo", createPageInfo("driving-schools-login", "Login auto škola", PageCategories.GLAVNA, PREFIX + "Login auto škola", DESCRIPTION));
	}

	public void addReportsActivityPage(ModelAndView mv) {

		mv.addObject("pageInfo", createPageInfo("activity", "Aktivnost sajta", PageCategories.GLAVNA, PREFIX + "Aktivnost sajta", DESCRIPTION));
	}

	private PageInfo createPageInfo(String pageId, String pageTitle, String pageCategory, String pageHtmlTitle, String pageHtmlDescription) {

		PageInfo pageInfo = new PageInfo(pageId, pageTitle, pageCategory, pageHtmlTitle, pageHtmlDescription);

		afterPageInfoCreated(pageInfo);

		return pageInfo;
	}

	private Object createPageInfo(String pageId, String pageTitle, String pageCategory, String pageHtmlTitle, String pageHtmlDescription, String url, String smallPreview, String imageURL, String string2, String novosti2) {

		PageInfo pageInfo = new PageInfo(pageId, pageTitle, pageCategory, pageHtmlTitle, pageHtmlDescription, url, smallPreview, imageURL, string2, novosti2);

		afterPageInfoCreated(pageInfo);

		return pageInfo;
	}

	private void afterPageInfoCreated(PageInfo pageInfo) {

		// dodajemo i js connect podatke ukoliko ih imamo
		// if (request != null) {
		//
		// HttpSession session = request.getSession();
		// String jsConnectString = (String)
		// session.getAttribute(EdtSignInUtils.JS_CONNECT_ATTRIBUTE);
		//
		// if (jsConnectString != null) {
		//
		// pageInfo.setJsConnectString(jsConnectString);
		// }
		// }
	}

	public void addQuestionView(ModelAndView mav, String text) {

		mav.addObject(
				"pageInfo",
				createPageInfo("pregled-pitanja", text, PageCategories.UCENJE, PREFIX + "Učenje i priprema za polaganje vožnje online - " + text, "Besplatno učenje i priprema za polaganje vožnje online. Sva pitanja sa odgovorima za pripremu i polaganje vožnje " + "na jednom mestu. " + text,
						getURL(), "Besplatno učenje i priprema za polaganje vožnje online. Sva pitanja sa odgovorima za pripremu i polaganje vožnje " + "na jednom mestu. " + text, getImageURL(), "article", PageCategories.UCENJE));
	}

	public void addInvalidQuestionView(ModelAndView mav) {

		String text = "Pogrešno pitanje";

		mav.addObject(
				"pageInfo",
				createPageInfo("pregled-pitanja", text, PageCategories.UCENJE, PREFIX + "Učenje i priprema za polaganje vožnje online - " + text, "Besplatno učenje i priprema za polaganje vožnje online. Sva pitanja sa odgovorima za pripremu i polaganje vožnje " + "na jednom mestu. " + text,
						getURL(), "Besplatno učenje i priprema za polaganje vožnje online. Sva pitanja sa odgovorima za pripremu i polaganje vožnje " + "na jednom mestu. " + text, getImageURL(), "article", PageCategories.UCENJE));
	}

	public void addLearningSelectionPage(ModelAndView mv) {

		mv.addObject(
				"pageInfo",
				createPageInfo("ucenje-izbor", "Priprema za učenje", PageCategories.UCENJE, PREFIX + "Priprema za besplatno učenje online, vozački ispit i testove", "Priprema za učenje, izbor kategorije vožnje i grupe pitanja za online učenje.", getURL(),
						"Priprema za učenje, izbor kategorije vožnje i grupe pitanja za online učenje.", getImageURL(), "article", PageCategories.UCENJE));

	}

	public void addMarathonOverviewPage(ModelAndView mv) {

		mv.addObject(
				"pageInfo",
				createPageInfo("maraton", "Maraton test", PageCategories.UCENJE, PREFIX + "Maraton test", "Polažite neograničen test i testirajte svoje znanje, takmičite se sa ostalima i uporedite svoje znanje u odnosu na druge.", getURL(),
						"Polažite neograničen test i testirajte svoje znanje, takmičite se sa ostalima i uporedite svoje znanje u odnosu na druge.", getImageURL(), "article", PageCategories.UCENJE));
	}

}
