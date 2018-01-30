package client;

import org.json.JSONArray;
import org.json.JSONObject;

import Tools.ChatLog;
import Tools.Role;
import application.ChatWindowController;
import messageTemplate.ParseMessages;
import messageTemplate.TemplateMessagesClient;
import javafx.application.Platform;

public class ClientInstance implements Runnable{
	private Thread run, listen;
	private Client client;
	private boolean running = false;
	//private OnlineUsers users;
	private ParseMessages JSON;
	private TemplateMessagesClient Template;
	private int currentTabIdUser;
	
	public ClientInstance(Client client) {
		//setTitle("FTI-HUB");
		this.client=client;
		this.JSON=new ParseMessages();
		this.Template=new TemplateMessagesClient();
		//boolean connect = client.openConnection();
		//this.users = new OnlineUsers();
		running = true;
		run = new Thread(this, "Running");
		run.start();
	}
	

	private void send(String message,boolean text) {
		if(text) {
			if(JSON.parse(message).has("message")){
				if (JSON.parse(message).getString("message").equals("")) return;
			}else if(JSON.parse(message).has("private-message")) {
				if (JSON.parse(message).getString("private-message").equals("")) return;
			}
		}
		client.send(message.getBytes());
	}
	
	
	private void listen() {
		listen = new Thread("Listen") {
			public void run() {
				while(running) {
					String string = client.receive().split("/e/")[0];
					switch(JSON.getTypeOfMessage(string)) {
					case  "global-message":
						JSONObject globalcontent = JSON.parse(string);
						System.out.println(string+"nga switcjhhuashdashd");
						  Platform.runLater(new Runnable() {
					            @Override
					            public void run() {
					            	 ChatWindowController.getChatLogsContainers().get("-1").addChatBubble(globalcontent.getString("message"), Role.PEER,globalcontent.getString("name"));
					            }
					       });
					    break;
					case "private-message":
						System.out.println(string);
						 JSONObject privatecontent = JSON.parse(string);
						 String idOfSender=privatecontent.getString("idOfSender");
						 String pm = privatecontent.getString("privateMessage");
						 Platform.runLater(new Runnable() {
					            @Override
					            public void run() {
					            	if(ChatWindowController.getChatLogsContainers().get(idOfSender)!=null){
					            	   ChatWindowController.getChatLogsContainers().get(idOfSender).addChatBubble(pm, Role.PEER,"");
					            	}
//					            	else {
//					            		//System.out.println("here");
//					            		JSONArray ja = ChatWindowController.getUsers();
//					            		for(int i=0;i<ja.length();i++) {
//					            			JSONObject jo = ja.getJSONObject(i);
//					            			if(jo.getString("id").equals(idOfSender)) {
//					            				jo.put("noti", "true");
//					            				ja.put(i, jo);
//					            				break;
//					            			}
//					            		}
//					            		if(ChatWindowController.getChatLogsContainers().get(idOfSender)!=null)
//					            		ChatWindowController.getChatLogsContainers().get(idOfSender).addChatBubble(pm, Role.PEER,"");
//					            		ChatWindowController.countdownuaLatch();
//					            	}
					            }
					       });
						 break;
				     case "ping":
						String text=Template.responsePingClient(client.getId());
						send(text,false);
						break;  
				     case "new-tab":
				    	 JSONObject chathistory = JSON.parse(string);
				    	 System.out.println(string);
				    	 String idofSender = chathistory.getString("idsender");
				    	 //System.out.println( ChatWindowController.getChatLogsContainers().get(idofSender).getPeerId());
				    	 Platform.runLater(new Runnable() {
					            @Override
					            public void run() {
					            	 ChatWindowController.getChatLogsContainers().get(idofSender).restoreChatLog(chathistory.getJSONArray("messages"));
					            	 ChatWindowController.countdownclLatch();
					            }
					       });
				    	 
				}
			  }
			}
		};
		listen.start();
	}
	
	@Override
	public void run() {
		listen();
	}

}
