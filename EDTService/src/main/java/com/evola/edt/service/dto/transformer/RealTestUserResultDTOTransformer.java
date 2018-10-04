package com.evola.edt.service.dto.transformer;

import java.util.Arrays;

import javax.inject.Inject;
import javax.inject.Named;

import com.evola.edt.model.RealTestUserResult;
import com.evola.edt.model.dto.RealTestUserResultDTO;
import com.evola.edt.model.dto.UserDTO;

@Named
public class RealTestUserResultDTOTransformer implements DTOTransformer<RealTestUserResultDTO, RealTestUserResult> {

	@Inject
	UserDTOTransformer userDTOTransformer;

	@Override
	public RealTestUserResultDTO transformToDTO(RealTestUserResult e, String... fetchFields) {
		
		if (e == null) {
			return null;
		}

		RealTestUserResultDTO d = new RealTestUserResultDTO();
		d.setCorrectAnswers(e.getCorrectAnswers());
		d.setCorrectPercent(e.getCorrectPercent());
		d.setCreationDate(e.getCreationDate());
		d.setId(e.getId());
		d.setTimeTaken(e.getTimeTaken());
		d.setWrongAswers(e.getWrongAswers());
		d.setPoints(e.getPoints());
		d.setHasPassedTest(e.getHasPassedTest());

		if (Arrays.asList(fetchFields).contains("user")) {

			UserDTO user = userDTOTransformer.transformToDTO(e.getUser());
			d.setUser(user);
		}

		return d;
	}

	@Override
	public RealTestUserResult transformToEntity(RealTestUserResultDTO e) {
		
		if (e == null) {
			return null;
		}

		RealTestUserResult d = new RealTestUserResult();
		d.setCorrectAnswers(e.getCorrectAnswers());
		d.setCorrectPercent(e.getCorrectPercent());
		d.setCreationDate(e.getCreationDate());
		d.setId(e.getId());
		d.setTimeTaken(e.getTimeTaken());
		d.setWrongAswers(e.getWrongAswers());
		d.setPoints(e.getPoints());
		d.setHasPassedTest(e.getHasPassedTest());

		return d;
	}

}
