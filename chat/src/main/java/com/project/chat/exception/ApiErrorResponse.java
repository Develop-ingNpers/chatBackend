package com.project.chat.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
public class ApiErrorResponse {

	public static int OK 			= 200;
	public static int BAD_REQUEST 	= 400;
	public static int UNAUTHORIZED 	= 401;
	public static int FORBIDDEN 	= 403;
	public static int NOT_FOUND 	= 404;
	public static int SERVER_ERROR 	= 500;

	public static String OK_MSG 			= "OK";
	public static String BAD_REQUEST_MSG 	= "[ApiErrorResponse] Bad Request";
	public static String UNAUTHORIZED_MSG 	= "[ApiErrorResponse] Unauthorized";
	public static String ACCESSDENIED_MSG 	= "[ApiErrorResponse] Access Denied";
	
	public static String FORBIDDEN_MSG 		= "[ApiErrorResponse] Forbidden";
	public static String NOT_FOUND_MSG 		= "[ApiErrorResponse] Not Found";
	public static String SERVER_ERROR_MSG 	= "[ApiErrorResponse] Server Error";

	private int status;
	private String message;
}