package com.evola.edt.service.dto;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "leaderboard")
public class LeaderboardDTO {

	private List<LeaderboardUserDTO> users;

	public List<LeaderboardUserDTO> getUsers() {
		return users;
	}

	public void setUsers(List<LeaderboardUserDTO> users) {
		this.users = users;
	}

}
