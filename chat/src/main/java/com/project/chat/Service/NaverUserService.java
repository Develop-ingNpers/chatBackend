package com.project.chat.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.project.chat.Mapper.UserMapper;
import com.project.chat.Vo.User;

@Service
public class NaverUserService {
	
	//public static String naverClientId = "UJDafTNvyeDfOJNTsIpV";
	//public static String naverClientSecret ="XqTYM5w7N6";
	
	@Value("${login.naver.client-id}")
	private String naverClientId;
	
	@Value("${login.naver.client-secret}")
	private String naverClientSecret;
	
	@Value("${login.naver.get-token-url}")
	private String getTokenUrl;
	
	@Value("${login.naver.get-detail-url}")
	private String getDetailUrl;
	
	@Autowired
	private UserMapper userMapper;
	
	
	public int naverLogin(String code) throws Exception {
		String accessToken = getAccessToken(code);
		String id = getDetail(accessToken);
		System.out.println("아이디는 "+id);
		User user = new User();
		user.setId(id);
		user.setAuthCode(001);
		user.setType("naver");
		user.setPw(id);
		
		int check = userMapper.checkUser(user);
		
		if(check==0) {
			int resultCode = userMapper.postUser(user);
			return resultCode;
		}
		else {
			return 0;
		}
		
	}
	
	public String getAccessToken(String code) throws Exception {
		String apiUrl = getTokenUrl;
		
		apiUrl+="&client_id="+naverClientId;
		apiUrl+="&client_secret="+naverClientSecret;
		apiUrl+="&code="+code;
		URL url = new URL(apiUrl);
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("GET");
		int responseCode = con.getResponseCode();
		
		BufferedReader br;
		System.out.println("response"+responseCode);
		if(responseCode==200) {
			br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		}else {
			br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
		}
		String inputLine;
		StringBuffer response = new StringBuffer();
		while((inputLine = br.readLine()) != null) {
			response.append(inputLine);
		}
		br.close();
		System.out.print(response.toString());
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(response.toString());
		String token = String.valueOf( jsonObject.get("access_token")) ;
		
		return token;
	}
	
	
	public String getDetail(String token) throws Exception {
		
		String header ="Bearer "+token;
		String apiUrl = getDetailUrl;
		
		URL url = new URL(apiUrl);
		HttpURLConnection con = (HttpURLConnection)url.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("Authorization", header);
		int responseCode = con.getResponseCode();
		
		BufferedReader br;
		System.out.println("response"+responseCode);
		if(responseCode==200) {
			br = new BufferedReader(new InputStreamReader(con.getInputStream()));
		}else {
			br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
		}
		String inputLine;
		StringBuffer response = new StringBuffer();
		while((inputLine = br.readLine()) != null) {
			response.append(inputLine);
		}
		br.close();
		System.out.print(response.toString());
		
		JSONParser jsonParser = new JSONParser();
		JSONObject jsonObject = (JSONObject) jsonParser.parse(response.toString());
		
		jsonObject = (JSONObject) jsonParser.parse(String.valueOf( jsonObject.get("response")));
		
		return jsonObject.get("id").toString();
	}

}
