package com.securephone.client.network;

import com.securephone.client.models.ChatMessage;
import com.securephone.client.models.Contact;
import com.securephone.client.models.UserSession;
import com.securephone.client.utils.Logger;
import com.securephone.shared.protocol.ChatPacket;
import com.securephone.shared.protocol.MessageType;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ConnectionManager {

	public interface AuthListener {
		void onLoginSuccess(UserSession session);
		void onLoginRequires2FA(String message);
		void onLoginFailed(String reason);
		void onRegisterSuccess(String message);
		void onRegisterFailed(String reason);
	}

	public interface ChatListener {
		void onMessage(ChatMessage message);
	}

	public interface ContactListener {
		void onContactList(List<Contact> contacts);
	}

	public interface StatusListener {
		void onStatus(String status);
	}

	public interface ErrorListener {
		void onError(String message);
	}

	public interface NotificationListener {
		void onNotification(String type, String title, String message, JSONObject data);
	}

	public interface CallListener {
		void onCallIncoming(String callerId);
		void onCallAccepted();
		void onCallRejected(String reason);
		void onCallEnded();
		void onCallError(String error);
	}

	private final WebSocketClient chatClient = new WebSocketClient();
	private final AudioClient audioClient = new AudioClient();
	private final com.securephone.client.video.VideoClient videoClient = new com.securephone.client.video.VideoClient();

	private AuthListener authListener;
	private ChatListener chatListener;
	private ContactListener contactListener;
	private StatusListener statusListener;
	private ErrorListener errorListener;
	private NotificationListener notificationListener;
	private CallListener callListener;

	private UserSession session = new UserSession();

	// Call state tracking
	private String currentCallId = null;
	private String currentCallType = null;  // "audio" or "video"
	private String currentCallRemoteUser = null;

	private String host = "localhost";
	private int chatPort = 8081;
	private int audioPort = 50000;
	private int videoPort = 50020;
	private int timeoutMs = 10000;
	private boolean enableTwoFaByDefault = true;

	public ConnectionManager() {
		loadConfig();
		chatClient.setMessageListener(this::handleMessage);
		chatClient.setConnectionListener(new WebSocketClient.ConnectionListener() {
			@Override
			public void onConnected() {
				if (statusListener != null) {
					statusListener.onStatus("connecte");
				}
			}

			@Override
			public void onDisconnected() {
				if (statusListener != null) {
					statusListener.onStatus("deconnecte");
				}
			}
		});
	}

	public void setAuthListener(AuthListener listener) {
		this.authListener = listener;
	}

	public void setChatListener(ChatListener listener) {
		this.chatListener = listener;
	}

	public void setContactListener(ContactListener listener) {
		this.contactListener = listener;
	}

	public void setStatusListener(StatusListener listener) {
		this.statusListener = listener;
	}

	public void setErrorListener(ErrorListener listener) {
		this.errorListener = listener;
	}

	public void setNotificationListener(NotificationListener listener) {
		this.notificationListener = listener;
	}

	public void setCallListener(CallListener listener) {
		this.callListener = listener;
	}

	// ========== CALL MANAGEMENT ==========

	public void initiateCall(String remoteName, String callType) {
		Logger.info("📞 Tentative d'appel " + callType + " vers " + remoteName);
		Logger.info("   Session utilisateur: " + session.getUsername());
		Logger.info("   WebSocket connecté: " + chatClient.isConnected());
		
		if (!chatClient.isConnected()) {
			Logger.error("❌ WebSocket non connecté! Impossible d'initier l'appel");
			return;
		}
		
		this.currentCallRemoteUser = remoteName;
		this.currentCallType = callType;
		this.currentCallId = java.util.UUID.randomUUID().toString();
		
		ChatPacket packet = new ChatPacket();
		packet.setType(MessageType.CALL_INITIATE);
		JSONObject data = new JSONObject();
		data.put("callId", currentCallId);
		data.put("callType", callType);
		data.put("callerName", session.getUsername());
		data.put("targetName", remoteName);
		packet.setData(data);
		
		try {
			String jsonMsg = packet.toJson();
			Logger.info("📤 Envoi message CALL_INITIATE: " + jsonMsg);
			chatClient.send(jsonMsg);
			Logger.info("✅ Message CALL_INITIATE envoyé avec succès");
		} catch (Exception e) {
			Logger.error("❌ Erreur envoi appel: " + e.getMessage());
			e.printStackTrace();
		}
		Logger.info("📞 Appel " + callType + " initialisé avec " + remoteName);
	}

	public void acceptCall(String callType) {
		if (currentCallId == null) {
			Logger.error("❌ Pas d'appel actif à accepter");
			return;
		}
		
		Logger.info("✅ Acceptation appel " + callType + " (ID: " + currentCallId + ")");
		
		this.currentCallType = callType;
		ChatPacket packet = new ChatPacket();
		packet.setType(MessageType.CALL_ACCEPT);
		JSONObject data = new JSONObject();
		data.put("callId", currentCallId);
		data.put("acceptedType", callType);
		data.put("accepterName", session.getUsername());
		packet.setData(data);
		
		try {
			String jsonMsg = packet.toJson();
			Logger.info("📤 Envoi message CALL_ACCEPT: " + jsonMsg);
			chatClient.send(jsonMsg);
			Logger.info("✅ Message CALL_ACCEPT envoyé");
		} catch (Exception e) {
			Logger.error("❌ Erreur envoi acceptation appel: " + e.getMessage());
		}
		
		// Start audio/video streams
		try {
			Logger.info("🎯 Démarrage flux " + callType + " vers " + host + ":" + ("audio".equals(callType) ? audioPort : videoPort));
			if ("audio".equals(callType)) {
				Logger.info("   Configuring audio client...");
				audioClient.configure(host, audioPort, session.getUserId());
				Logger.info("   Starting audio receive...");
				audioClient.startReceiving();
				Logger.info("   Starting audio capture...");
				audioClient.startCapture();
				Logger.info("✅ Flux audio démarré");
			} else if ("video".equals(callType)) {
				Logger.info("   Configuring video client...");
				videoClient.configure(host, videoPort, session.getUserId());
				Logger.info("   Starting video...");
				videoClient.start();
				Logger.info("✅ Flux vidéo démarré");
			}
		} catch (Exception e) {
			Logger.error("❌ Erreur démarrage média: " + e.getMessage());
			e.printStackTrace();
		}
		
		Logger.info("✅ Appel accepté");
	}

	public void rejectCall(String reason) {
		if (currentCallId == null) return;
		
		ChatPacket packet = new ChatPacket();
		packet.setType(MessageType.CALL_REJECT);
		JSONObject data = new JSONObject();
		data.put("callId", currentCallId);
		data.put("reason", reason);
		data.put("rejectorName", session.getUsername());
		packet.setData(data);
		
		try {
			chatClient.send(packet.toJson());
		} catch (Exception e) {
			Logger.error("❌ Erreur envoi rejet appel: " + e.getMessage());
		}
		
		currentCallId = null;
		currentCallType = null;
		currentCallRemoteUser = null;
		
		Logger.info("❌ Appel rejeté: " + reason);
	}

	public void endCall() {
		if (currentCallId == null) return;
		
		ChatPacket packet = new ChatPacket();
		packet.setType(MessageType.CALL_END);
		JSONObject data = new JSONObject();
		data.put("callId", currentCallId);
		data.put("endedBy", session.getUsername());
		packet.setData(data);
		
		try {
			chatClient.send(packet.toJson());
		} catch (Exception e) {
			Logger.error("❌ Erreur envoi fin appel: " + e.getMessage());
		}
		
		// Stop audio/video streams
		try {
			audioClient.stopReceiving();
			audioClient.stopCapture();
			videoClient.stop();
		} catch (Exception e) {
			Logger.error("❌ Erreur arrêt média: " + e.getMessage());
		}
		
		currentCallId = null;
		currentCallType = null;
		currentCallRemoteUser = null;
		
		Logger.info("🔴 Appel terminé");
	}

	public String getCurrentCallType() {
		return currentCallType;
	}

	public String getCurrentCallRemoteUser() {
		return currentCallRemoteUser;
	}

	public boolean hasActiveCall() {
		return currentCallId != null;
	}

	public boolean connect() {
		try {
			chatClient.connect(host, chatPort, timeoutMs);
			return true;
		} catch (Exception e) {
			Logger.error("Echec connexion chat: " + e.getMessage());
			return false;
		}
	}

	public boolean isConnected() {
		return chatClient.isConnected();
	}

	public void disconnect() {
		chatClient.disconnect();
		audioClient.stopReceiving();
		videoClient.stop();
	}

	public void login(String username, String password, String totpCode) {
		try {
			ChatPacket packet = new ChatPacket(MessageType.LOGIN_REQUEST);
			JSONObject data = new JSONObject();
			data.put("username", username);
			data.put("password", password);
			if (totpCode != null && !totpCode.isEmpty()) {
				data.put("totp", totpCode);
			}
			packet.setData(data);
			chatClient.send(packet.toJson());
		} catch (Exception e) {
			if (authListener != null) {
				authListener.onLoginFailed("Erreur de connexion");
			}
		}
	}

	public void register(String username, String password, String email) {
		try {
			ChatPacket packet = new ChatPacket(MessageType.REGISTER_REQUEST);
			JSONObject data = new JSONObject();
			data.put("username", username);
			data.put("password", password);
			if (email != null && !email.isEmpty()) {
				data.put("email", email);
			}
			data.put("enable_2fa", enableTwoFaByDefault);
			packet.setData(data);
			chatClient.send(packet.toJson());
		} catch (Exception e) {
			if (authListener != null) {
				authListener.onRegisterFailed("Erreur d'inscription");
			}
		}
	}
	public void verify2FA(String code) {
		try {
			if (!chatClient.isConnected()) {
				Logger.error("❌ WebSocket non connecté!");
				if (authListener != null) {
					authListener.onLoginFailed("Connexion perdue");
				}
				return;
			}
			
			ChatPacket packet = new ChatPacket(MessageType.VERIFY_2FA);
			JSONObject data = new JSONObject();
			data.put("code", code);
			packet.setData(data);
			chatClient.send(packet.toJson());
		} catch (Exception e) {
			Logger.error("❌ Erreur envoi 2FA: " + e.getMessage());
			if (authListener != null) {
				authListener.onLoginFailed("Erreur vérification 2FA: " + e.getMessage());
			}
		}
	}
	public void sendChatMessage(String content, String receiverName) {
		if (!session.isLoggedIn()) {
			return;
		}
		try {
			ChatPacket packet = new ChatPacket(MessageType.TEXT_MESSAGE);
			JSONObject data = new JSONObject();
			data.put("sender_id", session.getUserId());
			data.put("sender_name", session.getUsername());
			if (receiverName != null && !receiverName.isEmpty()) {
				data.put("receiver_name", receiverName);
			}
			data.put("content", content);
			data.put("timestamp", System.currentTimeMillis());
			packet.setData(data);
			chatClient.send(packet.toJson());
		} catch (Exception e) {
			Logger.error("Erreur envoi message: " + e.getMessage());
		}
	}

	public void logout() {
		try {
			chatClient.send(new ChatPacket(MessageType.LOGOUT).toJson());
		} catch (Exception ignored) {
		}
		session = new UserSession();
		disconnect();
	}

	public void requestContacts() {
		if (!session.isLoggedIn()) {
			return;
		}
		try {
			ChatPacket packet = new ChatPacket(MessageType.CONTACT_LIST);
			JSONObject data = new JSONObject();
			data.put("action", "request");
			packet.setData(data);
			chatClient.send(packet.toJson());
		} catch (Exception e) {
			Logger.error("Erreur demande contacts: " + e.getMessage());
		}
	}

	public void searchContacts(String query, SearchResultListener listener) {
		if (!session.isLoggedIn()) {
			if (listener != null) {
				listener.onSearchResult(new ArrayList<>());
			}
			return;
		}
		try {
			String url = "http://" + host + ":8000/api/contacts/search?q=" + 
				java.net.URLEncoder.encode(query, "UTF-8") + 
				"&session_id=" + session.getSessionId();
			
			java.net.URL apiUrl = new java.net.URL(url);
			java.net.HttpURLConnection conn = (java.net.HttpURLConnection) apiUrl.openConnection();
			conn.setRequestMethod("GET");
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			
			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				java.io.BufferedReader reader = new java.io.BufferedReader(
					new java.io.InputStreamReader(conn.getInputStream())
				);
				StringBuilder response = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				reader.close();
				
				JSONObject json = new JSONObject(response.toString());
				List<SearchResult> results = new ArrayList<>();
				
				if ("success".equals(json.optString("status"))) {
					JSONArray users = json.optJSONArray("users");
					if (users != null) {
						for (int i = 0; i < users.length(); i++) {
							JSONObject user = users.getJSONObject(i);
							SearchResult result = new SearchResult(
								user.optInt("id"),
								user.optString("username"),
								user.optString("email"),
								user.optString("status", "offline"),
								user.optBoolean("is_contact", false)
							);
							results.add(result);
						}
					}
				}
				
				if (listener != null) {
					listener.onSearchResult(results);
				}
			} else {
				Logger.error("Erreur recherche contacts: " + responseCode);
				if (listener != null) {
					listener.onSearchResult(new ArrayList<>());
				}
			}
		} catch (Exception e) {
			Logger.error("Erreur recherche contacts: " + e.getMessage());
			if (listener != null) {
				listener.onSearchResult(new ArrayList<>());
			}
		}
	}

	public void addContact(int contactId, String nickname, AddContactListener listener) {
		if (!session.isLoggedIn()) {
			if (listener != null) {
				listener.onAddContactResult(false, "Non connecté");
			}
			return;
		}
		try {
			String url = "http://" + host + ":8000/api/contacts/add?session_id=" + 
				session.getSessionId() + "&contact_id=" + contactId + 
				"&nickname=" + java.net.URLEncoder.encode(nickname, "UTF-8");
			
			java.net.URL apiUrl = new java.net.URL(url);
			java.net.HttpURLConnection conn = (java.net.HttpURLConnection) apiUrl.openConnection();
			conn.setRequestMethod("POST");
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			
			int responseCode = conn.getResponseCode();
			if (responseCode == 200) {
				java.io.BufferedReader reader = new java.io.BufferedReader(
					new java.io.InputStreamReader(conn.getInputStream())
				);
				StringBuilder response = new StringBuilder();
				String line;
				while ((line = reader.readLine()) != null) {
					response.append(line);
				}
				reader.close();
				
				JSONObject json = new JSONObject(response.toString());
				boolean success = "success".equals(json.optString("status"));
				String message = json.optString("message", success ? "Contact ajouté" : "Erreur");
				
				if (listener != null) {
					listener.onAddContactResult(success, message);
				}
			} else {
				if (listener != null) {
					listener.onAddContactResult(false, "Erreur serveur");
				}
			}
		} catch (Exception e) {
			Logger.error("Erreur ajout contact: " + e.getMessage());
			if (listener != null) {
				listener.onAddContactResult(false, e.getMessage());
			}
		}
	}

	public static class SearchResult {
		public int id;
		public String username;
		public String email;
		public String status;
		public boolean isContact;

		public SearchResult(int id, String username, String email, String status, boolean isContact) {
			this.id = id;
			this.username = username;
			this.email = email;
			this.status = status;
			this.isContact = isContact;
		}
	}

	public interface SearchResultListener {
		void onSearchResult(List<SearchResult> results);
	}

	public interface AddContactListener {
		void onAddContactResult(boolean success, String message);
	}

	public void startAudio() {
		if (!session.isLoggedIn()) {
			return;
		}
		try {
			audioClient.configure(host, audioPort, session.getUserId());
			audioClient.startReceiving();
			audioClient.startCapture();
		} catch (Exception e) {
			Logger.error("Erreur audio: " + e.getMessage());
		}
	}

	public void stopAudio() {
		audioClient.stopCapture();
	}

	public void startVideo() {
		if (!session.isLoggedIn()) {
			return;
		}
		try {
			videoClient.configure(host, videoPort, session.getUserId());
			videoClient.start();
		} catch (Exception e) {
			Logger.error("Erreur video: " + e.getMessage());
		}
	}

	public void stopVideo() {
		videoClient.stop();
	}

	public com.securephone.client.video.VideoClient getVideoClient() {
		return videoClient;
	}

	private void handleMessage(String message) {
		try {
			ChatPacket packet = ChatPacket.fromJson(message);
			MessageType type = packet.getType();
			JSONObject data = packet.getData();

			if (type == MessageType.LOGIN_RESPONSE) {
				handleLoginResponse(data);
				return;
			}

			if (type == MessageType.REGISTER_RESPONSE) {
				handleRegisterResponse(data);
				return;
			}

			if (type == MessageType.VERIFY_2FA_RESPONSE) {
				handleVerify2FAResponse(data);
				return;
			}

			if (type == MessageType.TEXT_MESSAGE) {
				// Check if this is a notification or a regular message
				String notificationType = data.optString("type", "");
				if ("message".equals(notificationType)) {
					// It's a notification
					if (notificationListener != null) {
						notificationListener.onNotification("message", 
							data.optString("title", "Message"),
							data.optString("message", ""),
							data);
					}
				} else if (!notificationType.isEmpty()) {
					// It's some other notification
					if (notificationListener != null) {
						notificationListener.onNotification(notificationType,
							data.optString("title", "Notification"),
							data.optString("message", ""),
							data);
					}
				} else {
					// Regular chat message
					String sender = data.optString("sender_name", "inconnu");
					String content = data.optString("content", "");
					long timestamp = data.optLong("timestamp", System.currentTimeMillis());
					ChatMessage chatMessage = new ChatMessage(sender, content, timestamp, "");
					if (chatListener != null) {
						chatListener.onMessage(chatMessage);
					}
				}
				return;
			}

			if (type == MessageType.CONTACT_REQUEST) {
				if (notificationListener != null) {
					notificationListener.onNotification("contact_request",
						data.optString("title", "Contact Request"),
						data.optString("message", ""),
						data);
				}
				return;
			}

			if (type == MessageType.CONTACT_ACCEPT) {
				if (notificationListener != null) {
					notificationListener.onNotification("contact_accepted",
						data.optString("title", "Contact Accepted"),
						data.optString("message", ""),
						data);
				}
				return;
			}

			if (type == MessageType.CONTACT_REJECT) {
				if (notificationListener != null) {
					notificationListener.onNotification("contact_rejected",
						data.optString("title", "Contact Rejected"),
						data.optString("message", ""),
						data);
				}
				return;
			}

			if (type == MessageType.CONTACT_LIST) {
				JSONArray array = data.optJSONArray("contacts");
				List<Contact> contacts = new ArrayList<>();
				if (array != null) {
					for (int i = 0; i < array.length(); i++) {
						JSONObject obj = array.getJSONObject(i);
						String id = String.valueOf(obj.optInt("id"));
						String name = obj.optString("username");
						String status = obj.optString("status", "offline");
						contacts.add(new Contact(id, name, "", status, ""));
					}
				}
				if (contactListener != null) {
					contactListener.onContactList(contacts);
				}
				return;
			}

			if (type == MessageType.ERROR) {
				String error = data.optString("message", "Erreur");
				if (errorListener != null) {
					errorListener.onError(error);
				}
				return;
			}

		if (type == MessageType.CALL_INITIATE) {
			currentCallId = data.optString("callId");
			currentCallRemoteUser = data.optString("callerName");
			currentCallType = data.optString("callType");
			Logger.info("📞 Appel entrant de " + currentCallRemoteUser + " (" + currentCallType + ")");
			Logger.info("   Call ID: " + currentCallId);
			if (callListener != null) {
				Logger.info("   Callback d'appel entrant en cours...");
				callListener.onCallIncoming(currentCallRemoteUser);
			} else {
				Logger.error("   ❌ callListener est NULL!");
			}
			return;
		}

		if (type == MessageType.CALL_ACCEPT) {
			String accepterName = data.optString("accepterName");
			String acceptedType = data.optString("acceptedType");
			Logger.info("✅ Appel accepté par " + accepterName + " (" + acceptedType + ")");
			if (callListener != null) {
				Logger.info("   Callback d'appel accepté en cours...");
				callListener.onCallAccepted();
			} else {
				Logger.error("   ❌ callListener est NULL!");
			}
			// Start audio/video based on accepted type
			try {
				Logger.info("🎵 Démarrage flux " + acceptedType + " vers " + host + ":" + ("audio".equals(acceptedType) ? audioPort : videoPort));
				if ("audio".equals(acceptedType)) {
					Logger.info("   Configuring audio client...");
					audioClient.configure(host, audioPort, session.getUserId());
					Logger.info("   Starting audio receive...");
					audioClient.startReceiving();
					Logger.info("   Starting audio capture...");
					audioClient.startCapture();
					Logger.info("✅ Flux audio démarré");
				} else if ("video".equals(acceptedType)) {
					Logger.info("   Configuring video client...");
					videoClient.configure(host, videoPort, session.getUserId());
					Logger.info("   Starting video...");
					videoClient.start();
					Logger.info("✅ Flux vidéo démarré");
				}
			} catch (Exception e) {
				Logger.error("❌ Erreur démarrage média: " + e.getMessage());
				e.printStackTrace();
			}
			return;
		}

		if (type == MessageType.CALL_REJECT) {
			String reason = data.optString("reason", "Non spécifiée");
			Logger.info("❌ Appel rejeté: " + reason);
			currentCallId = null;
			currentCallType = null;
			currentCallRemoteUser = null;
			if (callListener != null) {
				callListener.onCallRejected(reason);
			}
			return;
		}

		if (type == MessageType.CALL_END) {
			String endedBy = data.optString("endedBy");
			Logger.info("🔴 Appel terminé par " + endedBy);
			try {
				Logger.info("   Arrêt flux audio...");
				audioClient.stopReceiving();
				audioClient.stopCapture();
				Logger.info("   Arrêt flux vidéo...");
				videoClient.stop();
				Logger.info("✅ Arrêt flux complet");
			} catch (Exception e) {
				Logger.error("❌ Erreur arrêt média: " + e.getMessage());
			}
			currentCallId = null;
			currentCallType = null;
			currentCallRemoteUser = null;
			if (callListener != null) {
				callListener.onCallEnded();
			}
			return;
		}

		} catch (Exception e) {
			Logger.error("Erreur traitement message: " + e.getMessage());
		}
	}

	private void handleLoginResponse(JSONObject data) {
		String status = data.optString("status", "error");
		if ("2fa_required".equals(status)) {
			String baseMessage = data.optString("message", "2FA requis");
			String code = data.optString("code", "");
			String message = code.isEmpty() ? baseMessage : baseMessage + " (code: " + code + ")";
			if (authListener != null) {
				authListener.onLoginRequires2FA(message);
			}
			return;
		}

		if (!"success".equals(status)) {
			if (authListener != null) {
				authListener.onLoginFailed(data.optString("message", "Erreur login"));
			}
			return;
		}

		String sessionId = data.optString("session_id");
		int userId = data.optInt("user_id");
		String username = data.optString("username");
		session = new UserSession(sessionId, userId, username);

		if (authListener != null) {
			authListener.onLoginSuccess(session);
		}
	}

	private void handleRegisterResponse(JSONObject data) {
		String status = data.optString("status", "error");
		if ("success".equals(status)) {
			if (authListener != null) {
				authListener.onRegisterSuccess(data.optString("message", "Inscription OK"));
			}
		} else {
			if (authListener != null) {
				authListener.onRegisterFailed(data.optString("message", "Erreur inscription"));
			}
		}
	}

	private void handleVerify2FAResponse(JSONObject data) {
		String status = data.optString("status", "error");
		if ("success".equals(status)) {
			String sessionId = data.optString("session_id");
			int userId = data.optInt("user_id");
			String username = data.optString("username");
			session = new UserSession(sessionId, userId, username);

			if (authListener != null) {
				authListener.onLoginSuccess(session);
			}
		} else {
			if (authListener != null) {
				authListener.onLoginFailed(data.optString("message", "Vérification 2FA échouée"));
			}
		}
	}

	private void loadConfig() {
		Properties props = new Properties();
		try (FileInputStream fis = new FileInputStream("resources/config.properties")) {
			props.load(fis);
		} catch (Exception e) {
			Logger.warn("Config manquante, valeurs par defaut utilisees");
		}

		host = props.getProperty("server.host", host);
		timeoutMs = parseInt(props.getProperty("websocket.timeout"), timeoutMs);

		String wsUrl = props.getProperty("websocket.url", "ws://" + host + ":" + chatPort + "/chat");
		try {
			URI uri = URI.create(wsUrl.replace("ws://", "http://").replace("wss://", "https://"));
			if (uri.getHost() != null) {
				host = uri.getHost();
			}
			if (uri.getPort() > 0) {
				chatPort = uri.getPort();
			}
		} catch (Exception e) {
			Logger.warn("URL websocket invalide, fallback sur host/port par defaut");
		}

		audioPort = parseInt(props.getProperty("udp.audio.port.start"), audioPort);
		videoPort = parseInt(props.getProperty("udp.video.port.start"), videoPort);
		enableTwoFaByDefault = Boolean.parseBoolean(props.getProperty("security.2fa.enabled", "true"));
	}

	private int parseInt(String value, int fallback) {
		if (value == null) {
			return fallback;
		}
		try {
			return Integer.parseInt(value.trim());
		} catch (NumberFormatException e) {
			return fallback;
		}
	}
}
