package com.example.demo.service;

import com.example.demo.exception.UserException;
import com.example.demo.model.dto.UserCert;

public interface UserCertService {
	public UserCert getCert()throws UserException;
}
