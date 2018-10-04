package com.evola.edt.service.dto.transformer;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;

import com.evola.edt.model.DailyTestUserResult;
import com.evola.edt.model.dto.DailyTestUserResultDTO;
import com.evola.edt.model.dto.UserDTO;

/**
 * @author Nikola 09.04.2013.
 * 
 */
@Named
public class DailyTestUserResultDTOTransformer implements DTOTransformer<DailyTestUserResultDTO, DailyTestUserResult> {

	@Inject
	UserDTOTransformer userDTOTransformer;

	@Override
	public DailyTestUserResultDTO transformToDTO(DailyTestUserResult e, String... fetchFields) {
		
		if (e == null) {
			return null;
		}

		DailyTestUserResultDTO d = new DailyTestUserResultDTO();
		d.setCorrectAnswers(e.getCorrectAnswers());
		d.setCorrectPercent(e.getCorrectPercent());
		d.setCreationDate(e.getCreationDate());
		d.setId(e.getId());
		d.setTimeTaken(e.getTimeTaken());
		d.setWrongAswers(e.getWrongAswers());
		d.setPoints(e.getPoints());

		if (Arrays.asList(fetchFields).contains("user")) {

			UserDTO user = userDTOTransformer.transformToDTO(e.getUser());
			d.setUser(user);
		}

		return d;
	}

	@Override
	public DailyTestUserResult transformToEntity(DailyTestUserResultDTO e) {
		
		if (e == null) {
			return null;
		}

		DailyTestUserResult d = new DailyTestUserResult();
		d.setCorrectAnswers(e.getCorrectAnswers());
		d.setCorrectPercent(e.getCorrectPercent());
		d.setCreationDate(e.getCreationDate());
		d.setId(e.getId());
		d.setTimeTaken(e.getTimeTaken());
		d.setWrongAswers(e.getWrongAswers());
		d.setPoints(e.getPoints());

		return d;
	}

}
