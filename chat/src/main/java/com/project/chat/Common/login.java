package com.project.chat.Common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class login {
	
	public static String naverClientId = "UJDafTNvyeDfOJNTsIpV";
	public static String naverClientSecret ="XqTYM5w7N6";
	
	
	public static void naverLogin(String code) throws Exception {
		
		/*StringBuilder loginUrl = new StringBuilder()
				.append("https://nid.naver.com/oauth2.0/token?grant_type=authorization_code")
				.append("&client_id=")
				.append(naverClientId)
				.append("&client_secret=")
				.append(naverClientSecret)
				.append("&code=")
				.append(code);
				
		System.out.println("송아지"+loginUrl);*/
		
		String apiUrl = "https://nid.naver.com/oauth2.0/token?grant_type=authorization_code";
		
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
		System.out.println("송송"+token);
		
		
		String header ="Bearer "+token;
		String apiUrl2 = "https://openapi.naver.com/v1/nid/me";
		
		System.out.println("송채린");
		URL url2 = new URL(apiUrl2);
		System.out.println("송채린2");
		HttpURLConnection con2 = (HttpURLConnection)url2.openConnection();
		System.out.println("송채린3");
		con2.setRequestMethod("GET");
		System.out.println("송채린4");
		con2.setRequestProperty("Authorization", header);
		System.out.println("송채린5");
		int responseCode2 = con2.getResponseCode();
		
		BufferedReader br2;
		System.out.println("response"+responseCode2);
		if(responseCode2==200) {
			br2 = new BufferedReader(new InputStreamReader(con2.getInputStream()));
		}else {
			br2 = new BufferedReader(new InputStreamReader(con2.getErrorStream()));
		}
		String inputLine2;
		StringBuffer response2 = new StringBuffer();
		while((inputLine2 = br2.readLine()) != null) {
			response2.append(inputLine2);
		}
		br2.close();
		System.out.print(response2.toString());
	}

}
