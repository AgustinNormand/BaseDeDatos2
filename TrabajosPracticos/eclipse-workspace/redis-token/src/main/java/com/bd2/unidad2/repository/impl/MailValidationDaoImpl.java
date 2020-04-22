package com.bd2.unidad2.repository.impl;

import com.bd2.unidad2.repository.MailValidationDao;

import io.lettuce.core.api.sync.RedisCommands;

public class MailValidationDaoImpl implements MailValidationDao{
	ConnectionManager connectionManager = ConnectionManager.getInstance("localhost",6379,"masterkey");
	RedisCommands<String, String> con = connectionManager.getSyncConnection();

	@Override
	public void validatedMail(String mail) {
		if (con.exists(mail) == 1) {
			con.persist(mail);
		}
		
	}

}
