package com.project.chat.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.chat.Mapper.UserMapper;
import com.project.chat.Vo.User;
import com.project.chat.Vo.UserPrincipal;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CustomUserDetailService implements UserDetailsService{

	@Autowired
    UserMapper userMapper;

	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
		User user = userMapper.findUserById(id);
		return UserPrincipal.create(user);
	}

	// Custom : 소셜 로그인 type 추가
	public UserDetails loadUserById(String id, String type) {
		User user = userMapper.findUser(id, type);
		
		return UserPrincipal.create(user);
	}
}
