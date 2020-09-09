package com.project.chat.Service;

import java.net.URI;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.project.chat.Mapper.UserMapper;
import com.project.chat.Vo.ResponseWrapper;
import com.project.chat.Vo.User;
import com.project.chat.exception.ApiErrorResponse;
import com.project.chat.security.JwtAuthenticationResponse;
import com.project.chat.security.JwtTokenProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
	
	@Autowired 
	UserMapper userMapper;
	
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	JwtTokenProvider tokenProvider;
	
	public List<User> getUsers(){
		return userMapper.getUsers();
	}
	
	public User getUser(String id) {
		return userMapper.getUser(id);
	}
	
	public int postUser(User user) {
		return userMapper.postUser(user);
	}
	
	public int putUser(User user) {
		return userMapper.putUser(user);
	}
	
	public int deleteUser(String id) {
		return userMapper.deleteUser(id);
	}
	
	public String signIn(User userInfo) {
		
		log.info("UserService - signIn: "+userInfo.toString());
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(userInfo.getId(),userInfo.getPw()));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String jwt = tokenProvider.generateToken(authentication);
		
		log.info("UserService - jwt: "+jwt);
		
		//ResponseWrapper<?> wrapper = new ResponseWrapper(new ApiErrorResponse(ApiErrorResponse.OK, ApiErrorResponse.OK_MSG), jwt);
		
		return jwt;
	}
	
	public ResponseEntity<?> registerUser(User userInfo) {
		
		// password 형식 : kakao-1457537572 || naver-31306944
		userInfo.setPw(userInfo.getType()+"-"+userInfo.getId());
		userInfo.setPw(passwordEncoder.encode(userInfo.getPw()));

		log.info("UserService - registerUser: "+userInfo.toString());
		
		userMapper.postUser(userInfo);
		
		// 신규 회원이 소셜로그인을 한경우 바로 로그인까지 해야하므로 비밀번호 암호화 원복
		userInfo.setPw(userInfo.getType()+"-"+userInfo.getId());

		return ResponseEntity.ok("User registered successfully");
	}

}
