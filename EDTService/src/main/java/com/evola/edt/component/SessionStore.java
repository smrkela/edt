package com.evola.edt.component;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Component;

import com.evola.edt.component.store.LearningStoreData;
import com.evola.edt.component.store.MarathonStoreData;

@Component
public class SessionStore {

	@Inject
	private HttpSession session;

	public void save(String key, Object value) {

		if (session == null)
			return;

		try {

			session.setAttribute(key, value);

		} catch (IllegalStateException e) {

			// illegal state is ok
		}
	}

	public Object load(String key) {

		if (session == null)
			return null;

		Object data = null;

		try {
			
			data = session.getAttribute(key);
		
		} catch (IllegalStateException e) {

			// illegal state is ok
			data = null;
		}

		return data;
	}

	public void saveLearning(Integer startingIndex, Long questionCategoryId, Long drivingCategoryId) {

		LearningStoreData data = new LearningStoreData();

		data.setDrivingCategoryId(drivingCategoryId);
		data.setQuestionCategoryId(questionCategoryId);
		data.setStartIndex(startingIndex);

		save("LEARNING_DATA", data);
	}

	public LearningStoreData loadLearning() {

		return (LearningStoreData) load("LEARNING_DATA");
	}

	public void saveMarathon(String marathonId, List<Long> questionIds) {

		MarathonStoreData data = new MarathonStoreData();
		
		data.setMarathonId(marathonId);
		data.setQuestionIds(questionIds);
		data.setCurrentQuestionIndex(0);
		
		save("MARATHON_DATA", data);
	}

	public MarathonStoreData loadMarathon() {

		return (MarathonStoreData) load("MARATHON_DATA");
	}

}
