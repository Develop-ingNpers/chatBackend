package com.project.chat.Vo;

import com.project.chat.exception.ApiErrorResponse;

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
public class ResponseWrapper<T> {
	
	private ApiErrorResponse apiErrorResponse;
	private String token;
}
