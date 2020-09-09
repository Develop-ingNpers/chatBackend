package com.project.chat.Service;

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
import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.project.chat.Mapper.UserMapper;
import com.project.chat.Vo.ResponseWrapper;
import com.project.chat.Vo.User;

import lombok.extern.slf4j.Slf4j;

/*
 * Kakao Login URL
 * > https://kauth.kakao.com/oauth/authorize?client_id=c2349c66079371ea43050cfcd8c38f19&redirect_uri=http://localhost:8080/login/social/kakao&response_type=code
 * Kakao Logout URL
 * > http://localhost:8080/logout/social/kakao/simple
 * */
@Slf4j
@Service
public class KakaoUserService extends UserService{
	
	@Autowired 
	UserMapper userMapper;
	
	@Value("${spring.url.base}")
	private String baseUrl;
	
	@Value("${login.kakao.client-id}")
	private String kakaoClientId;
	
	@Value("${login.kakao.login-redirect}")
	private String kakaoLoginRedirect;
	
	@Value("${login.kakao.logout-redirect}")
	private String kakaoLogoutRedirect;
	
	@Value("${login.kakao.url.token}")
	private String kakaoUrlToken;
	
	@Value("${login.kakao.url.profile}")
	private String kakaoUrlProfile;
	
	@Value("${login.kakao.url.logout}")
	private String kakaoUrlLogout;
	
	public String getAccessToken (String authorize_code) {
        String access_Token = "";
        String refresh_Token = "";
	    
        try {
            URL url = new URL(kakaoUrlToken);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            
            //    POST 요청을 위해 기본값이 false인 setDoOutput을 true로
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            
            //    POST 요청에 필요로 요구하는 파라미터 스트림을 통해 전송
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
            StringBuilder loginUrl = new StringBuilder()
    				.append("grant_type=authorization_code")
    				.append("&client_id=").append(kakaoClientId)
    				.append("&redirect_uri=").append(baseUrl).append(kakaoLoginRedirect)
    				.append("&code=").append(authorize_code);
            bw.write(loginUrl.toString());
            bw.flush();
            
            //    결과 코드가 200이라면 성공
            int responseCode = conn.getResponseCode();
            log.info("responseCode : " + responseCode);
            
            // 200이 아닌경우 예외처리?
            if(responseCode != 200) {
            	// 토큰 정보가 잘못된 형식 => HTTP Status 400에 code -2가 반환
            	// 토큰 만료 => HTTP Status 401에 code -401 (갱신 필요)
            	// 응답 에러 코드가 -1이라면 카카오 플랫폼 서비스의 일시적 내부 장애 상태
            	// 이 외의 에러는 앱이나 사용자, 토큰 등의 상태가 더이상 유효하지 않아 쓸 수 없는 경우 (로그아웃 처리)
            	return null;
            }
 
            //    요청을 통해 얻은 JSON타입의 Response 메세지 읽어오기
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line = "";
            String result = "";
            
            while ((line = br.readLine()) != null) {
                result += line;
            }
            log.info("response body : " + result);
            
            //    Gson 라이브러리에 포함된 클래스로 JSON파싱 객체 생성
            JsonParser parser = new JsonParser();
            JsonElement element = parser.parse(result);
            
            access_Token = element.getAsJsonObject().get("access_token").getAsString();
            refresh_Token = element.getAsJsonObject().get("refresh_token").getAsString();
            
            log.info("access_token : " + access_Token);
            log.info("refresh_token : " + refresh_Token);
            
            br.close();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        
        return access_Token;
    }
	
	public String getUserInfo (String access_Token) {
	    // 요청하는 클라이언트마다 가진 정보가 다를 수 있기에 HashMap타입으로 선언
	    //HashMap<String, Object> userInfo = new HashMap<>();
	    User userInfo = new User();
	    String token = null;
	    
	    try {
	        URL url = new URL(kakaoUrlProfile);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("POST");
	        
	        // 요청에 필요한 Header에 포함될 내용
	        conn.setRequestProperty("Authorization", "Bearer " + access_Token);
	        
	        int responseCode = conn.getResponseCode();
	        log.info("responseCode : " + responseCode);

            // 200이 아닌경우 예외처리?
            if(responseCode != 200) {
            	// 토큰 정보가 잘못된 형식 => HTTP Status 400에 code -2가 반환
            	// 토큰 만료 => HTTP Status 401에 code -401 (갱신 필요)
            	// 응답 에러 코드가 -1이라면 카카오 플랫폼 서비스의 일시적 내부 장애 상태
            	// 이 외의 에러는 앱이나 사용자, 토큰 등의 상태가 더이상 유효하지 않아 쓸 수 없는 경우 (로그아웃 처리)
            	return null;
            }
	        
	        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        
	        String line = "";
	        String result = "";
	        
	        while ((line = br.readLine()) != null) {
	            result += line;
	        }
	        log.info("response body : " + result);
	        
	        JsonParser parser = new JsonParser();
	        JsonElement element = parser.parse(result);
	        
	        JsonObject properties = element.getAsJsonObject().get("properties").getAsJsonObject();
	        JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();
	        
	        String id = element.getAsJsonObject().get("id").toString();
	        String nickname = properties.getAsJsonObject().get("nickname").getAsString();
	        if(kakao_account.getAsJsonObject().get("email") != null) {
	        	String email = kakao_account.getAsJsonObject().get("email").getAsString();
	        	userInfo.setEmail(email);
	        }
	        
		    userInfo.setId(id);
		    userInfo.setType("kakao");
		    userInfo.setPw(userInfo.getType()+"-"+id);
	        
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    
	    int result = userMapper.checkUser(userInfo);
	    
	    if(result == 1) {
	    	// 카카오 로그인 성공 -> JWT토큰 발급
	    	log.info("기존회원 로그인");
	    	token = signIn(userInfo);
	    }else if(result == 0) {
	    	// 신규회원 DB insert
	    	log.info("신규회원 로그인");
	    	registerUser(userInfo);
	    	// 소셜 로그인이니까 신규 회원정보 등록하고 바로 로그인?
	    	token = signIn(userInfo);
	    }
	    
	    return token;
	}
	
	public int kakaoLogout(String access_Token) {
	    String reqURL = kakaoUrlLogout;
	    try {
	        URL url = new URL(reqURL);
	        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("POST");
	        conn.setRequestProperty("Authorization", "Bearer " + access_Token);
	        
	        int responseCode = conn.getResponseCode();
	        log.info("responseCode : " + responseCode);
	        
	        if(responseCode != 200) {
	        	return -1;
	        }
	        
	        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	        
	        String result = "";
	        String line = "";
	        
	        while ((line = br.readLine()) != null) {
	            result += line;
	        }
	        log.info(result);
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	    	return 1;
	    }
	}

}
