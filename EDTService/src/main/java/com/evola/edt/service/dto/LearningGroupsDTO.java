package com.evola.edt.service.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "learning-groups")
public class LearningGroupsDTO {

	private List<LearningGroupDTO> groups;

	@XmlElement(name="group")
	public List<LearningGroupDTO> getGroups() {
		return groups;
	}

	public void setGroups(List<LearningGroupDTO> groups) {
		this.groups = groups;
	}
	


}
