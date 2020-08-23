package com.project.chat.Controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.chat.Service.KakaoUserService;
import com.project.chat.Service.UserService;
import com.project.chat.Vo.User;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
public class UserController {
	
	@Autowired
	UserService userService;
	@Autowired
	KakaoUserService kakaoUserService;
	
	@RequestMapping(value = "/getUsers", method = RequestMethod.GET)
	public List<User> getUsers() throws Exception {
		return userService.getUsers();
	}
	
	@RequestMapping(value = "/getUser/{id}", method = RequestMethod.GET)
	public User getUser(String id) throws Exception {
		return userService.getUser(id);
	}
	
	@RequestMapping(value = "/postUser", method = RequestMethod.POST)
	public int postUser(@RequestBody User user) throws Exception {
		return userService.postUser(user);
	}
	
	@RequestMapping(value = "/putUser", method = RequestMethod.PUT)
	public int putUser(@RequestBody User user) throws Exception {
		return userService.putUser(user);
	}
	
	@RequestMapping(value = "/deleteUser/{id}", method = RequestMethod.DELETE)
	public int deleteUser(String id) throws Exception {
		return userService.deleteUser(id);
	}
	
	@RequestMapping(value = "/login/social/kakao")
	public int oauthKakaoLogin(@RequestParam("code") String code, HttpSession session) throws Exception {
		// 1. Client쪽에 카카오로그인 페이지를 띄우고 동의하기 버튼을 누르면 해당 URI로 Code를 포함하여 Redirect됨
		
		log.info("code : " + code);
		
		// 2. code를 기반으로 사용자 token받아오기
		String access_Token = kakaoUserService.getAccessToken(code);
		
		// 3. token을 기반으로 사용자 정보 받아와서 DB정보와 비교, 없으면 insert
		User userInfo = kakaoUserService.getUserInfo(access_Token);
		
		log.info("userInfo : " + userInfo);
		
		if(userInfo == null)
			return -1;
		else
			return 1;
	}
	
	@RequestMapping(value = "/logout/social/kakao/simple")
	public int oauthKakaoLogout(HttpSession session) throws Exception {
		// 서비스 로그아웃
		int result = kakaoUserService.kakaoLogout((String)session.getAttribute("access_Token"));
		
		log.info("Logout - userId : " + session.getAttribute("userId"));
	    session.removeAttribute("access_Token");
	    session.removeAttribute("userEmail");
	    session.removeAttribute("userId");
	    
		return result;
	}
	
	@RequestMapping(value = "/logout/social/kakao")
	public int oauthKakaoLogout2(/* @RequestParam("state") String state */) throws Exception {
		// 카카오계정과 함께 로그아웃 기능제공
		log.info("oauthKakaoLogout2");
		//log.info("state : " + state);
		
		return 1;
	}
	
	@RequestMapping(value = "/session")
	public String getSessionInfo(HttpSession session) throws Exception {
		// 임시 테스트용 Session정보 확인
		log.info("session - userId : " + session.getAttribute("userId"));
		log.info("session - userEmail : " + session.getAttribute("userEmail"));
		log.info("session - access_Token : " + session.getAttribute("access_Token"));
		
		return session.toString();
	}
	
}
