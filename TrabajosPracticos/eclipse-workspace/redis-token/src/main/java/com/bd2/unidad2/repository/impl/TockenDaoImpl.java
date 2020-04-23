package com.bd2.unidad2.repository.impl;

import com.bd2.unidad2.model.Usuario;
import com.bd2.unidad2.repository.TokenDao;
import com.bd2.unidad2.repository.exceptions.IncorrectToken;

import io.lettuce.core.api.sync.RedisCommands;

public class TockenDaoImpl implements TokenDao {
	ConnectionManager connectionManager = ConnectionManager.getInstance("localhost",6379,"masterkey");
	RedisCommands<String, String> con = connectionManager.getSyncConnection();

	@Override
	public String getMailWithToken(String token) {
		Boolean encontrado = false;
		String mail = null;
		for (String clave : con.keys("*")) {
			if (token.equals(con.hget(clave, "Token"))){
				mail = con.hget(clave, "Email");
				encontrado = true;
				break;
			}
		}
		if (!encontrado)
			throw new IncorrectToken("El token ingresado no existe");
		return mail;
	}

}
