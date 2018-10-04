package com.evola.edt.managers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.evola.edt.model.ActivityMessage;
import com.evola.edt.model.BaseEntity;
import com.evola.edt.model.DailyTestUserResult;
import com.evola.edt.model.DrivingSchool;
import com.evola.edt.model.DrivingSchoolNotification;
import com.evola.edt.model.LearningSession;
import com.evola.edt.model.MarathonTest;
import com.evola.edt.model.Page;
import com.evola.edt.model.PageComment;
import com.evola.edt.model.QuestionMessage;
import com.evola.edt.model.RealTestUserResult;
import com.evola.edt.model.TestingSession;
import com.evola.edt.model.User;
import com.evola.edt.repository.ActivityMessageRepository;
import com.evola.edt.repository.UserRepository;
import com.evola.edt.service.dto.ActivityMessageDTO;
import com.evola.edt.web.security.SecurityUtils;
import com.evola.edt.web.security.UserCredentialsManager;

@Component
public class ActivityManager {

	public static final String LOGGED_IN = "logged_in";
	public static final String REGISTERED = "registered";
	public static final String DONE_DAILY_TEST = "done_daily_test";
	public static final String DONE_REAL_TEST = "done_real_test";
	public static final String LEARNED = "learned";
	public static final String TESTED = "tested";
	public static final String ADDED_DRIVING_SCHOOL_NOTIFICATION = "school_notification";
	public static final String DISCUSSED_QUESTION = "discussed_question";
	public static final String NEWS_ADDED = "news_added";
	public static final String INFORMATIONS_ADDED = "informations_added";
	public static final String COMMENTED_NEWS = "commented_news";
	public static final String STARTED_MARATHON_TEST = "started_marathon_test";

	@Inject
	private UserCredentialsManager mCredentials;

	@Inject
	private UserRepository rUser;

	@Inject
	private ActivityMessageRepository rActivityMessage;

	@Inject
	private ApplicationContext context;

	public void loggedIn(User user) {

		rActivityMessage.save(createMessage(user, LOGGED_IN));
	}

	public void registered(User user) {

		rActivityMessage.save(createMessage(user, REGISTERED, user));
	}

	public void doneDailyTest(DailyTestUserResult result) {

		rActivityMessage.save(createMessage(result, DONE_DAILY_TEST));
	}
	
	public void startedMarathonTest(MarathonTest test) {

		rActivityMessage.save(createMessage(test,  STARTED_MARATHON_TEST));
	}

	public void doneRealTest(RealTestUserResult result) {

		rActivityMessage.save(createMessage(result, DONE_REAL_TEST));
	}

	public void learned(LearningSession session) {

		rActivityMessage.save(createMessage(session, LEARNED));
	}

	public void tested(TestingSession session) {

		rActivityMessage.save(createMessage(session, TESTED));
	}

	public void drivingSchoolNotification(DrivingSchoolNotification notification) {

		rActivityMessage.save(createMessage(notification, ADDED_DRIVING_SCHOOL_NOTIFICATION));
	}

	public void discussedQuestion(QuestionMessage message) {

		rActivityMessage.save(createMessage(message, DISCUSSED_QUESTION));
	}

	public void newsAdded(Page news) {

		rActivityMessage.save(createMessage(news, NEWS_ADDED));
	}

	public void informationsAdded(Page informations) {

		rActivityMessage.save(createMessage(informations, INFORMATIONS_ADDED));
	}

	public void commentedNews(PageComment comment) {

		rActivityMessage.save(createMessage(comment, COMMENTED_NEWS));
	}

	@Transactional
	public List<ActivityMessageDTO> getPublicActivity() {

		List<String> includedTypes = Arrays.asList(DONE_DAILY_TEST, LEARNED, TESTED, DONE_REAL_TEST, STARTED_MARATHON_TEST);

		org.springframework.data.domain.Page<ActivityMessage> page = rActivityMessage.findByType(includedTypes, new PageRequest(0, 20, new Sort(Sort.Direction.DESC, "creationDate")));

		List<ActivityMessage> messages = page.getContent();

		List<ActivityMessageDTO> result = translateMessages(messages);

		return result;
	}

	@Transactional
	public List<ActivityMessageDTO> getAdministrationActivity(Date fromDate, Date toDate) {

		List<String> includedTypes = Arrays.asList(LOGGED_IN, REGISTERED, DONE_DAILY_TEST, LEARNED, TESTED, ADDED_DRIVING_SCHOOL_NOTIFICATION, DISCUSSED_QUESTION, NEWS_ADDED, INFORMATIONS_ADDED, COMMENTED_NEWS, DONE_REAL_TEST);

		org.springframework.data.domain.Page<ActivityMessage> page = rActivityMessage.findByTypeAndDate(includedTypes, fromDate, toDate, new PageRequest(0, 200, new Sort(Sort.Direction.DESC, "creationDate")));

		List<ActivityMessage> messages = page.getContent();

		List<ActivityMessageDTO> result = translateMessages(messages);

		return result;
	}

	private List<ActivityMessageDTO> translateMessages(List<ActivityMessage> messages) {

		List<ActivityMessageDTO> result = new LinkedList<ActivityMessageDTO>();

		for (ActivityMessage message : messages) {

			ActivityMessageDTO object = translateMessage(message);

			if (object != null)
				result.add(object);
		}

		return result;
	}

	private ActivityMessageDTO translateMessage(ActivityMessage message) {

		String repositoryName = message.getObjectName() + "Repository";

		Class<? extends JpaRepository<? extends BaseEntity, Long>> repositoryClass;

		try {

			repositoryClass = (Class<? extends JpaRepository<? extends BaseEntity, Long>>) Class.forName("com.evola.edt.repository." + repositoryName);

		} catch (ClassNotFoundException e) {

			throw new IllegalArgumentException("Repository " + repositoryName + " does not exist.");
		}

		CrudRepository<? extends BaseEntity, Long> repository = (CrudRepository<? extends BaseEntity, Long>) context.getBean(repositoryClass);

		BaseEntity entity = repository.findOne(message.getObjectId());

		// mozda je obrisan objekat
		if (entity == null)
			return null;

		String text = null;
		String link = null;
		String linkText = null;
		String author = message.getAuthor().getUsername();
		String timeAgo = getTimeAgoText(message.getCreationDate());
		String formattedTime = getFormattedTime(message.getCreationDate());

		switch (message.getType()) {

			case LOGGED_IN: {
				User user = (User) entity;

				if (user.getIsMale())
					text = getMessageUsername(user) + " se ulogovao.";
				else
					text = getMessageUsername(user) + " se ulogovala.";

				break;
			}
			case REGISTERED: {
				User user = (User) entity;

				if (user.getIsMale())
					text = getMessageUsername(user) + " se registrovao.";
				else
					text = getMessageUsername(user) + " se registrovala.";

				break;
			}
			case DONE_DAILY_TEST: {

				DailyTestUserResult result = (DailyTestUserResult) entity;
				User user = result.getUser();

				Integer points = result.getPoints();

				if (user.getIsMale())
					text = getMessageUsername(user) + " je uradio test dana i sakupio " + points + " poena.";
				else
					text = getMessageUsername(user) + " je uradila test dana i sakupila " + points + " poena.";

				// test-dana/rezultat/2014-11-25/jovanagraovac/

				link = "/test-dana/rezultat/" + getDateAsLink(result.getTest().getDate()) + "/" + user.getUsername();
				linkText = "pogledaj rezultat";

				break;
			}
			case DONE_REAL_TEST: {

				RealTestUserResult result = (RealTestUserResult) entity;
				User user = result.getUser();

				Integer points = result.getPoints();
				String username = getMessageUsername(user);

				if (user.getIsMale()) {

					if (result.getHasPassedTest())
						text = username + " je položio test "+result.getTest().getName()+" sa " + points + " poena.";
					else
						text = username + " je pao test "+result.getTest().getName()+" sa " + points + " poena.";
				} else {

					if (result.getHasPassedTest())
						text = username + " je položila test "+result.getTest().getName()+" sa " + points + " poena.";
					else
						text = username + " je pala test "+result.getTest().getName()+" sa " + points + " poena.";
				}

				// test-dana/rezultat/2014-11-25/jovanagraovac/

				link = "/testovi/rezultat/" + result.getId();
				linkText = "pogledaj rezultat";

				break;
			}
			case LEARNED: {

				LearningSession session = (LearningSession) entity;
				User user = session.getUser();

				text = getMessageUsername(user) + " uči preko aplikacije.";

				break;
			}
			case TESTED: {

				TestingSession session = (TestingSession) entity;
				User user = session.getUser();

				text = getMessageUsername(user) + " se testira preko aplikacije.";

				break;
			}
			case ADDED_DRIVING_SCHOOL_NOTIFICATION: {

				DrivingSchoolNotification notification = (DrivingSchoolNotification) entity;
				DrivingSchool school = notification.getSchool();

				text = "Auto škola " + school.getName() + " - " + school.getCity() + " je dodala novo obaveštenje: " + notification.getTitle();

				// http://www.vozacisrbije.com/spisak-auto-skola/auto-skola-de-boj-batajnica/obavestenja-auto-skole/obavestenje/14
				link = "/spisak-auto-skola/" + school.getUniqueName() + "/obavestenja-auto-skole/obavestenje/" + notification.getId();
				linkText = "pogledaj obaveštenje";

				break;
			}
			case DISCUSSED_QUESTION: {

				QuestionMessage questionMessage = (QuestionMessage) entity;

				text = author;

				if (message.getAuthor().getIsMale())
					text += " je diskutovao ";
				else
					text += " je diskutovala ";

				text += "na pitanje " + questionMessage.getQuestion().getText() + ": ";
				text += questionMessage.getMessage();

				break;

			}
			case NEWS_ADDED: {

				Page news = (Page) entity;

				text = "Postavljena je nova vest: " + news.getTitle();

				// http://www.vozacisrbije.com/vesti/besplatna-premium-licenca-za-auto-skole
				link = "/vesti/" + news.getUniqueName();
				linkText = "pogledaj vest";

				break;
			}
			case INFORMATIONS_ADDED: {

				Page informations = (Page) entity;

				text = "Postavljena je nova stranica informacija: " + informations.getTitle();

				// http://www.vozacisrbije.com/informacije/cena-upisa-auto-skole
				link = "/informacije/" + informations.getUniqueName();
				linkText = "pogledaj stranicu";

				break;
			}
			case COMMENTED_NEWS: {

				PageComment comment = (PageComment) entity;

				text = author;

				if (message.getAuthor().getIsMale())
					text += " je komentarisao vest ";
				else
					text += " je komentarisala vest ";

				text += "'" + comment.getPage().getTitle() + "': ";
				text += comment.getComment();

				// http://www.vozacisrbije.com/vesti/besplatna-premium-licenca-za-auto-skole
				link = "/vesti/" + comment.getPage().getUniqueName();
				linkText = "pogledaj vest";

				break;
			}
			case STARTED_MARATHON_TEST:{
				
				MarathonTest result = (MarathonTest) entity;
				User user = result.getUser();

				if (user.getIsMale())
					text = getMessageUsername(user) + " je započeo maratonski test.";
				else
					text = getMessageUsername(user) + " je započela maratonski test.";

				//maraton test nema svoj prikaz

				link = "/maraton";
				linkText = "idi na maraton";

				break;
			}
		}

		// ako nemamo tekst onda nemamo poruku uopste, moramo dodatno obraditi
		// taj tip poruke
		if (text == null)
			return null;

		// sada formiramo konacan podatak
		ActivityMessageDTO dto = new ActivityMessageDTO();
		dto.setAuthorId(message.getAuthor().getId());
		dto.setMessage(text);
		dto.setLink(link);
		dto.setLinkText(linkText);
		dto.setTimeAgoString(timeAgo);
		dto.setFormattedTime(formattedTime);

		return dto;
	}

	private String getMessageUsername(User user) {

		return "<span style=\"font-weight: bold\">" + user.getUsername() + "</span>";
	}

	private String getFormattedTime(Date creationDate) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		return df.format(creationDate);
	}

	private String getTimeAgoText(Date date) {

		String text = "";

		if (date != null) {
			long seconds = (new Date().getTime() - date.getTime()) / 1000;

			if (seconds < 60) {

				text = "pre " + seconds + " " + getTimeExpression(seconds, "sekund", "sekunde", "sekundi");
			} else {

				long minutes = seconds / 60;

				if (minutes < 60) {

					text = "pre " + minutes + " " + getTimeExpression(minutes, "minut", "minute", "minuta");
				} else {

					long hours = minutes / 60;

					if (hours < 24) {

						text = "pre " + hours + " " + getTimeExpression(hours, "sat", "sata", "sati");
					} else {

						long days = hours / 24;

						text = "pre " + days + " " + getTimeExpression(days, "dan", "dana", "dana");
					}
				}
			}
		}

		return text;
	}

	private String getTimeExpression(long number, String one, String twoToFour, String aboveFour) {

		int lastDigit = (int) (number % 10);

		if (lastDigit == 1)
			return one;

		if (lastDigit >= 2 && lastDigit <= 4)
			return twoToFour;

		return aboveFour;
	}

	private String getDateAsLink(Date date) {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");

		return df.format(date);
	}

	private ActivityMessage createMessage(BaseEntity entity, String type) {

		return createMessage(entity, type, null, getAuthor());
	}

	private ActivityMessage createMessage(BaseEntity entity, String type, User author) {

		return createMessage(entity, type, null, author);
	}

	private ActivityMessage createMessage(BaseEntity entity, String type, String content, User author) {

		ActivityMessage message = new ActivityMessage();
		message.setType(type);
		message.setContent(content);
		message.setCreationDate(new Date());
		message.setAuthor(author);
		message.setObjectName(entity.getClass().getSimpleName());
		message.setObjectId(entity.getId());

		return message;
	}

	private User getAuthor() {

		return rUser.findOne(SecurityUtils.getUserId());
	}

}
