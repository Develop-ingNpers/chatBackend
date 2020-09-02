package com.project.chat.Vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
public class UserPrincipal  implements UserDetails{
	private String id;
	
	private String type;
	
	@JsonIgnore
	private String name;

	@JsonIgnore
	private String email;

	@JsonIgnore
	private String pw;

	private Collection<? extends GrantedAuthority> authorities;

	public static UserPrincipal create(User user) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		log.info("UserPrincipal - create: "+user.toString());
		return new UserPrincipal(user.getId(), user.getType(), user.getName(), user.getEmail(), user.getPw(), authorities);
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			log.info("UserPrincipal - equals true");
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			log.info("UserPrincipal - equals false >> "+ o.toString() + "///"+this.getClass().toString());
			return false;
		}
		UserPrincipal that = (UserPrincipal) o;
		return Objects.equals(id, that.id);
	}

	@Override
	public int hashCode() {

		return Objects.hash(id);
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return pw;
	}

}
