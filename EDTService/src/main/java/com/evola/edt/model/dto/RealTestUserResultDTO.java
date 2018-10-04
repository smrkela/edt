package com.evola.edt.model.dto;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="user-result")
public class RealTestUserResultDTO extends DailyTestUserResultDTO{

	private Boolean hasPassedTest;

	public RealTestUserResultDTO() {
		super();
	}

	@XmlAttribute(name="has-passed-test")
	public Boolean getHasPassedTest() {
		return hasPassedTest;
	}

	public void setHasPassedTest(Boolean hasPassedTest) {
		this.hasPassedTest = hasPassedTest;
	}

}
