package com.project.chat.Mapper;

import java.util.List;

import com.project.chat.Vo.User;

public interface UserMapper {
	
	public List<User> getUsers();
	
	public User getUser(String id);
	
	public int checkUser(User user);
	
	public int postUser(User user);
	
	public int putUser(User user);
	
	public int deleteUser(String id);

	public User findUserById(String id);
	
	public User findUser(String id, String type);

}
