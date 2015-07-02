package com.liulianggu.beans;

import java.util.List;

public class Friends {
	private List<String> groupNames;
	private List<List<FriendItem>> allFriends;

	public List<String> getGroupNames() {
		return groupNames;
	}

	public void setGroupNames(List<String> groupNames) {
		this.groupNames = groupNames;
	}

	public List<List<FriendItem>> getAllFriends() {
		return allFriends;
	}

	public void setFriends(List<List<FriendItem>> allFriends) {
		this.allFriends = allFriends;
	}

}
