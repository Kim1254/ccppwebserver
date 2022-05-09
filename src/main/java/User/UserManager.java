package User;

import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Firebase.FirebaseConfig;
import util.DataHandler;
import util.RSA;
import util.RSA.RSAException;
import util.DataHandler.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.websocket.Session;

public class UserManager {
	private Session session = null;

	private static RSA.Decrypt decrypter = new RSA.Decrypt(1711, 445); // 클라이언트에서 받은 내용 복호화
	private static RSA.Encrypt encrypter = new RSA.Encrypt(1457, 1129); // 클라이언트에 보낼 내용 암호화
	
	private String userId = null;
	
	public UserManager(Session session) {
		this.session = session;
	}
	
	public interface CallBack<Ty> {
        void onSuccess(Ty _data);
        void onFailure();
	}
	
	public void handleMessage(String message) throws Exception {
		String plain = decrypter.doDecrypt(message, message.length());
		JSONObject data = DataHandler.parse(plain);
		
		switch (REQUEST_TYPE.valueOf((String)data.get("request"))) {
		case LOGIN:
			login(data, new LoginCallback());
			break;
		default:
			break;
		}
	}
	
	public class LoginCallback implements CallBack<String> {
		@Override
		public void onSuccess(String _data) {
			String response = DataHandler.packData(REQUEST_TYPE.LOGIN, DATA_TYPE.BOOLEAN, true, _data);
			sendDataEncrypted(response);
		}

		@Override
		public void onFailure() {
			String response = DataHandler.packData(REQUEST_TYPE.LOGIN, DATA_TYPE.BOOLEAN, false);
			sendDataEncrypted(response);
		}
	}
	
	public void login(JSONObject data, final CallBack<String> callback) {
		try {
			if (DATA_TYPE.valueOf((String)data.get("type")) != DATA_TYPE.OBJECT) {
				callback.onFailure();
				return;
			}
			
			String value = (String)data.get("value");
			// String cipher = encrypter.doEncrypt(value, value.length()); // No use
			
			FirebaseConfig.Initialize();
			
			FirebaseDatabase database = FirebaseDatabase.getInstance();
			DatabaseReference reference = database.getReference();
			
			reference.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
				@Override
				public void onDataChange(DataSnapshot snapshot) {
					DataSnapshot e;
					Iterator<DataSnapshot> iter = snapshot.getChildren().iterator();
					while (iter.hasNext()) {
						e = iter.next();
						if (e.child("uId").getValue(String.class).contentEquals(value)) {
							userId = e.getKey();
							callback.onSuccess(userId);
							return;
						}
					}
					
					if (userId == null) {
						String key = reference.child("user").push().getKey();
						Map<String, Object> keyMap = new HashMap<String, Object>();
						keyMap.put("uId", value);

						Map<String, Object> dbMap = new HashMap<String, Object>();
						dbMap.put("/user/" + key, keyMap);
						reference.updateChildren(dbMap, null);
						callback.onSuccess(key);
						return;
					}
				}

				@Override
				public void onCancelled(DatabaseError error) {
					callback.onFailure();
				}
			});
		} catch (Exception e) {
			callback.onFailure();
		}
	}
	
	public void sendData(String context) {
		try {
			session.getBasicRemote().sendText(context);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendDataEncrypted(String plainText) {
		try {
			sendData(encrypter.doEncrypt(plainText, plainText.length()));
		} catch (RSAException e) {
			e.printStackTrace();
		}
	}
}
