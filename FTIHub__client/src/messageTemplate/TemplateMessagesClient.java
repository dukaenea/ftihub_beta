package messageTemplate;

public class TemplateMessagesClient extends TemplateMessages {
	
	public String signupCredentials(String username,String password,String email) {
		return stringify(type("signup")+",\"username\": \""+username+"\",\"password\":\""+password+"\",\"email\":\""+email+"\"}");
	}

	public String loginCredentials(String username, String password) {
		return stringify(type("login") + ",\"username\": \"" + username + "\", \"password\": \"" + password + "\"}");
	}

	public String message(String message,String name) {
		return stringify(type("global-message") + ",\"message\": \"" + message + "\",\"name\": \""+name+"\"}");
	}

	public String privateMessage(int idOfReceiver, int idOfSender, String privateMessage) {
		return stringify(type("private-message") + ",\"idOfReceiver\": \"" + idOfReceiver + "\",\"idOfSender\":\""
				+ idOfSender + "\"" + ",\"privateMessage\":\"" + privateMessage + "\"}");
	}

	public String responsePingClient(int id) {
		return stringify(type("response-ping") + ",\"id\": \"" + id + "\"}");
	}

	public String disconnect(int id) { 
		return stringify(type("disconnect") + ",\"id\": \"" + id + "\"}");
	}

	public String changeChatTab(int idOfTab,int idOfSender) {
		return stringify(type("change-chat-tab") + ",\"idOfUserTab\": \"" + idOfTab + "\",\"idOfSender\":\""+idOfSender+"\"}");
	}
}
