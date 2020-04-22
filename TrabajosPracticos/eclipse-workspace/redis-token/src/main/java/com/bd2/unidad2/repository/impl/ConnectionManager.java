package com.bd2.unidad2.repository.impl;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class ConnectionManager {
	static ConnectionManager cm = null;
	static StatefulRedisConnection<String, String> connection = null;
	
	
	public static ConnectionManager getInstance(String ip, int puerto, String password) {
		if (cm == null) {
			cm = new ConnectionManager(ip,puerto,password);
		}
		return cm;
	}
	
	public static ConnectionManager getInstance(String ip, int puerto) {
		if (cm == null) {
			cm = new ConnectionManager(ip,puerto);
		}
		return cm;
	}
	
	public RedisCommands<String, String> getSyncConnection() {
		return connection.sync();
	}

	public void close() {
		connection.close();
	}
	public ConnectionManager(String ip, int puerto, String password) {
		RedisClient client = RedisClient.create("redis://:"
												+password
												+"@"
												+ip
												+":"
												+Integer.toString(puerto)
												+"/3");
    	connection = client.connect();
	}
	
	public ConnectionManager(String ip, int puerto) {
		RedisClient client = RedisClient.create("redis://"
												+"@"
												+ip
												+":"
												+Integer.toString(puerto)
												+"/3");
    	connection = client.connect();
	}

}
