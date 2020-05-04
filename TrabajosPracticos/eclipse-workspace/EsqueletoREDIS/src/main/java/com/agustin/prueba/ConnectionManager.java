package com.agustin.prueba;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

public class ConnectionManager {

	private static ConnectionManager cm = null;
	
	private RedisClient client = RedisClient.create("redis://:"
												+"masterkey"
												+"@"
												+"localhost"
												+":"
												+Integer.toString(6379)
												+"/1");
	
	private StatefulRedisConnection<String, String> connection = null;
	
	public ConnectionManager() {
		connection = client.connect();
	}
	
	public static ConnectionManager getInstance() {
		if (cm == null)
			cm = new ConnectionManager();
		return cm;
	}
	
	public RedisCommands<String, String> getSyncConnection() {
		return connection.sync();
	}
	
	public void close() {
		connection.close();
	}
	
}
