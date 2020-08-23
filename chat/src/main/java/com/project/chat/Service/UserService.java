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
import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.project.chat.Mapper.UserMapper;
import com.project.chat.Vo.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
	
	@Autowired 
	UserMapper userMapper;
	
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

}
