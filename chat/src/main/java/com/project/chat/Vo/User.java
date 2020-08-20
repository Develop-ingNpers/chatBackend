package com.project.chat.Vo;

import java.util.Date;

import lombok.Data;

@Data
public class User {
	private String id;
	private String name;
	private String pw;
	private int failCnt;
	private int authCode;
	private Date regDate;
	private Date uptDate;
	
}
