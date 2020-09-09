package com.project.chat.Controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.project.chat.Service.KakaoUserService;
import com.project.chat.Service.NaverUserService;
import com.project.chat.Service.UserService;
import com.project.chat.Vo.ResponseWrapper;
import com.project.chat.Vo.User;
import com.project.chat.exception.ApiErrorResponse;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@RestController
//@RequestMapping("/api")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@Autowired
	KakaoUserService kakaoUserService;
	
	@Autowired
	NaverUserService naverUserService;
	
	
	@RequestMapping(value = "/responseNaver", method = { RequestMethod.GET, RequestMethod.POST })
	public int response(HttpServletRequest request) throws Exception {
		//System.out.println(request.getParameter("code"));
		//login.naverLogin(request.getParameter("code"));
		return naverUserService.naverLogin(request.getParameter("code"));
	}
	
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
	public ResponseWrapper<?> oauthKakaoLogin(@RequestParam("code") String code, HttpSession session) throws Exception {
		// 1. Client쪽에 카카오로그인 페이지를 띄우고 동의하기 버튼을 누르면 해당 URI로 Code를 포함하여 Redirect됨
		
		log.info("code : " + code);
		
		// 2. code를 기반으로 사용자 token받아오기
		String access_Token = kakaoUserService.getAccessToken(code);
		
		// 3. token을 기반으로 사용자 정보 받아와서 DB정보와 비교, 없으면 insert
		String token = kakaoUserService.getUserInfo(access_Token);
		
		log.info("jwt token : " + token);
		
		
		if(token == null) {
			return new ResponseWrapper(new ApiErrorResponse(ApiErrorResponse.BAD_REQUEST, ApiErrorResponse.BAD_REQUEST_MSG), token);
		}
		else {
			return new ResponseWrapper(new ApiErrorResponse(ApiErrorResponse.OK, ApiErrorResponse.OK_MSG), token);
		}
		
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
	
	@RequestMapping(value = "/session")
	public String getSessionInfo(HttpSession session) throws Exception {
		// 임시 테스트용 Session정보 확인
		log.info("session - userId : " + session.getAttribute("userId"));
		log.info("session - userEmail : " + session.getAttribute("userEmail"));
		log.info("session - access_Token : " + session.getAttribute("access_Token"));
		
		return session.toString();
	}
	
	@RequestMapping(value = "/signin", method = RequestMethod.POST)
	public ResponseWrapper<?> authenticateUser(@RequestBody HashMap<String,String> userInfoMap) {
		User userInfo = new User();
		userInfo.setId(userInfoMap.get("id").toString());
		userInfo.setPw(userInfoMap.get("pw").toString());
		log.info("signin - normal : " + userInfo.toString());
		
		return new ResponseWrapper(new ApiErrorResponse(ApiErrorResponse.OK, ApiErrorResponse.OK_MSG), userService.signIn(userInfo));
	}
	
	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public ResponseEntity<?> registerUser(@RequestBody User userInfo) {
		
		return userService.registerUser(userInfo);
	}
	
}
