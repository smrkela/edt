package com.evola.edt.component;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.springframework.stereotype.Component;

@Component
public class RecaptchaComponent {

	// za ove specijalne vrednosti dozvoljavamo captch-a prolaz
	private static final String ALLOWED_CHALLENGE = "clkvjawefhzkjnvxcoipf";
	private static final String ALLOWED_RESPONSE = "soidj23-04i0sdifsdfjkj";

	@Inject
	private HttpServletRequest request;

	public void check(String challange, String response) {

		// provera nam puca na null vrednosti a tretiramo ih isto kao i prazne
		// stringove

		if (challange == null)
			challange = "";

		if (response == null)
			response = "";

		// mozda su specijalne vrednosti pa treba da pustimo
		if (ALLOWED_CHALLENGE.equals(challange) && ALLOWED_RESPONSE.equals(response))
			return;

		String remoteAddr = request.getRemoteAddr();

		ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
		reCaptcha.setPrivateKey("6Lfqau8SAAAAAL8TDmTvwne90dq6z3cjJpkY2-zR");

		ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challange, response);

		if (reCaptchaResponse.isValid()) {

		} else {

			throw new RuntimeException("Pogrešno unet sigurnosni kod. Resetujte kod i probajte ponovo.");
		}
	}

}
