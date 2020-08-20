package com.project.chat.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.project.chat.Service.UserService;
import com.project.chat.Vo.User;



@RestController
public class UserController {
	
	@Autowired
	UserService userService;
	
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

}
