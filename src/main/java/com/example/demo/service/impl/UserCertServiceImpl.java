package com.example.demo.service.impl;

import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserCertServiceImpl implements UserCertService{

	@Autowired
	UserRepository userRepository;
	@Override
	@Transactional(readOnly = true)
	public UserCert getCert(String username, String password)  {
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
			UserCert userCert=new UserCert(user.getId(),user.getUserName(),user.getEmailConfirmOK(),user.getRole().name());
			return userCert;

		
	
	}

	

}
