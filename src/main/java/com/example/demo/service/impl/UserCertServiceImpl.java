package com.example.demo.service.impl;

import java.util.Collections;
import java.util.Optional;

import com.example.demo.exception.UserException;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.exception.PasswordErrorException;
import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.dto.UserCert;
import com.example.demo.model.entity.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserCertService;
import com.example.demo.util.HashUtil;
@Service
public class UserCertServiceImpl implements  UserCertService, UserDetailsService {

	@Autowired
	UserRepository userRepository;
	@Override
	
	public UserCert getCert(String username, String password)  {//舊的session比對使用者
		Optional<User>optUser= userRepository.findByUserName(username);
		if(optUser.isEmpty()) {
			throw new UserNotFoundException("登入失敗，無此使用者");
		}
		User user=optUser.get();
			String salt=user.getSalt();
			String hashpassword=HashUtil.hashPassword(password, salt);
			if(!hashpassword.equals(user.getHashPassword())) {
				throw new PasswordErrorException("密碼錯誤");
			}
			UserCert userCert=new UserCert(user.getId(),user.getUserName(),user.getEmailConfirmOK(),user.getRole().name(),user.getIsBanned());
			return userCert;

		
	
	}

	@Override
	public UserCert findUserByUsername(String username) throws UserException {
		Optional<User>optUser= userRepository.findByUserName(username);
		if(optUser.isEmpty()) {
			throw new UserNotFoundException("登入失敗，無此使用者");
		}
		User user=optUser.get();
		UserCert userCert=new UserCert(user.getId(),user.getUserName(),user.getEmailConfirmOK(),user.getRole().name(),user.getIsBanned());
		return userCert;
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user=userRepository.findByUserName(username).orElseThrow(()->new UsernameNotFoundException("找不到使用者："+username));
	return new org.springframework.security.core.userdetails.User(user.getUserName(),user.getHashPassword(), Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+user.getRole().name())));
	}
}
