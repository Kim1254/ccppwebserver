package Firebase;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.json.simple.parser.ParseException;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.DatabaseReference.CompletionListener;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseConfig {
	public static void Initialize() {
		try {
			FileInputStream serviceAccount = new FileInputStream("./src/main/resources/static/firebase/firebaseServiceKey.json");
			
			FirebaseOptions options = new FirebaseOptions.Builder()
					.setCredentials(GoogleCredentials.fromStream(serviceAccount))
					.setDatabaseUrl("https://ccpp-ec74d-default-rtdb.firebaseio.com/")
					.build();
			
			FirebaseApp.initializeApp(options);
		} catch (Exception e) {
			System.out.println("Exception on Init");
		}
	}
}
