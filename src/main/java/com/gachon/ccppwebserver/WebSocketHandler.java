package com.gachon.ccppwebserver;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.springframework.stereotype.Service;

import User.UserManager;
import util.RSA;

// Referenced: https://jobtc.tistory.com/59

@Service
@ServerEndpoint(value="/ws")
public class WebSocketHandler {
	private static Map<Session, UserManager> clients = Collections.synchronizedMap(new HashMap<Session, UserManager>());
	
	@OnMessage
	public void handleMessage(String message, Session session) throws IOException {
		System.out.println("Message: " + session + ", " + message);
		try {
			clients.get(session).handleMessage(message);
		} catch (Exception e) {
		}
	}
	
	@OnOpen
	public void handleOpen(Session session) {
		if (!clients.containsKey(session)) {
			System.out.println("Open: " + session);
			clients.put(session, new UserManager(session));
		}
	}
	
	@OnClose
	public void handleClose(Session session) {
		System.out.println("Close: " + session);
		clients.remove(session);
	}
	
	@OnError
	public void handleError(Session session, Throwable t) {
		System.out.println("Error: " + session);
		t.printStackTrace();
	}
}
