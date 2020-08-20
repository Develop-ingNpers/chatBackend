package com.project.chat.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.chat.Mapper.UserMapper;
import com.project.chat.Vo.User;

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
