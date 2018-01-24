package application;


import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;

import org.json.JSONArray;
import org.json.JSONObject;

import Tools.ChatLog;
import Tools.Mode;
import Tools.Role;
import Tools.UserEntry;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;
import messageTemplate.ParseMessages;
import messageTemplate.TemplateMessagesClient;


public class ChatWindowController implements Initializable{
	
	@FXML
	private  TextField txtSearch, txtMessage;
	@FXML
	private GridPane chatArea,usersArea;
	@FXML
	private  Circle imgOtherProfilePicture,imgProfilePicture;
	@FXML
	private  Button btnSend,btnBackToLogin,globalArea,pinButton;
	@FXML
	public   ScrollPane chatScroll;
	@FXML 
	private  Label lblOtherUser,labeluser;
	@FXML
	private  ImageView pin;
	
	
	private static ChatLog currentchat;
	private static HashMap<String,ChatLog> chatlogs = new HashMap<String,ChatLog>(); 
	private String peerid = "Peer"; //dummy code realisht merret nga objekti qe vjen nga server
	private boolean clicked  = false;
	private static JSONArray users = new JSONArray();
	private ParseMessages JSON=new ParseMessages();
	private TemplateMessagesClient Template = new TemplateMessagesClient();
	private static CountDownLatch clLatch = new CountDownLatch(1);
	private static CountDownLatch uaLatch = new CountDownLatch(1);
	
	
	public static void countdownclLatch() {
		clLatch.countDown();
	}
	
	
	
	public static void countdownuaLatch() {
		uaLatch.countDown();
	}
	
	
	public static JSONArray getUsers() {
		return users;
	}
	public static void setUsersContainer(JSONObject user){
		 JSONArray usr = user.getJSONArray("users");
		 users=usr;
	}
	
	
	
	
	public static HashMap<String,ChatLog> getChatLogsContainers(){
		return chatlogs;
	}
	
	
	
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		
		fillUserArea(users);
	    Task getNoti = new Task<Void>() {

			@Override
			protected Void call() throws Exception {
				while(true) {
					uaLatch.await();
					//System.out.println("unlocked");
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							fillUserArea(users);
							uaLatch=new CountDownLatch(1);
						}
						
					});
				}
			}
	    	
	    };
	    new Thread(getNoti).start();
		globalArea.addEventHandler(ActionEvent.ACTION, 
				 				   event -> { switchToGlobal(event);});
		
		txtMessage.addEventHandler(ActionEvent.ACTION,
								   event -> {btnMessageClicked(event);});
		labeluser.setText(MainController.getClient().getName());
		
		txtSearch.addEventHandler(ActionEvent.ACTION, 
									event -> {handleSearch();});
	}	
	
	
	public void handleSearch() {
		String looking = txtSearch.getText();
		if(looking.equals("")) {
			fillUserArea(users);	
		}
		else {
			JSONArray searchResult = new JSONArray();
			for(int i=0;i<users.length();i++) {
				JSONObject usr = users.getJSONObject(i);
				String s = usr.getString("username");
				if(usr.getString("username").toLowerCase().contains(looking.toLowerCase()))
					searchResult.put(usr);
			}
			usersArea.getChildren().clear();
			fillUserArea(searchResult);
		}
	}
	
	
	
	public void fillUserArea(JSONArray usersToFillWith) {
		
		int j=0;
		for(int i=0;i<usersToFillWith.length();i++) {
			//Image img = new Image("Images\\push-pin1.png");
			JSONObject user = (JSONObject) usersToFillWith.get(i);
			if(user.getInt("id")==MainController.getClient().getId())
				continue;
			System.out.println(user.getBoolean("noti"));
			UserEntry ue = new UserEntry(user.getString("username"),user.getBoolean("noti"),usersArea,j,Integer.parseInt(user.getString("id")));
			j++;
			ue.getUsrLabel().addEventHandler(MouseEvent.MOUSE_CLICKED , 
		             event -> {labelContactClicked(event,ue);});
		}
	}
	
	
	
	public void switchToGlobal(ActionEvent e) {
		
		if(!chatlogs.containsKey("-1")) {
			ChatLog cl = new ChatLog(-1,chatScroll,Mode.GLOBAL);
			MainController.getClient().send(Template.changeChatTab(-1, MainController.getClient().getId()).getBytes());
			cl.bindGridToScroll(chatScroll);
			chatlogs.put(Integer.toString(-1), cl);
			currentchat = cl;
		}
		else {
			ChatLog cl = chatlogs.get("-1");
			cl.bindGridToScroll(chatScroll);
			currentchat = cl;
		}
		lblOtherUser.setText("Gobal Chat");
		pinButton.setVisible(true);
	}
	
	
	
	
	public void pinButtonClicked(ActionEvent e) {
		if(clicked == false) {
			 pin.setImage(new Image("Images\\push-pin1.png"));
			clicked = true;
		}
		else if(clicked == true) {
			pin.setImage(new Image("Images\\push-pin-white.png"));
			clicked = false;
		}
	}
	
	
	
	
	
	public  void labelContactClicked(MouseEvent event,UserEntry ue) {
		
		Label usr = (Label) event.getSource();
		
		if(!chatlogs.containsKey(Integer.toString(ue.getId()))) {
			ChatLog cl = new ChatLog(ue.getId(),chatScroll,Mode.PTP);
			MainController.getClient().send(Template.changeChatTab(ue.getId(), MainController.getClient().getId()).getBytes());
	        cl.bindGridToScroll(chatScroll);
	        
	        Task scrollDown = new Task<Void>() {

				@Override
				protected Void call() throws Exception {
					clLatch.await();
					Platform.runLater(new Runnable() {

						@Override
						public void run() {
							chatScroll.setVvalue(chatScroll.getVmax());
							clLatch = new CountDownLatch(1);
						}
						
					});
					return null;
				}
	        	
	        };
	        new Thread(scrollDown).start();
			chatlogs.put(Integer.toString(ue.getId()), cl);
			currentchat = cl;
			
		}
		else {
			ChatLog cl = chatlogs.get(Integer.toString(ue.getId()));
			//cl.restoreChatLog(chatScroll);
			cl.bindGridToScroll(chatScroll);
			currentchat = cl;
		}
		
		lblOtherUser.setText(usr.getText());
		ue.removeNotofication(usersArea, usersArea.getRowIndex(usr));
		pinButton.setVisible(false);
	}
	
	
	
	
	
	
	public void btnMessageClicked(ActionEvent e) {
		
		if(txtMessage.getText() != "") {
			currentchat.addChatBubble(txtMessage.getText(), Role.USER,peerid);
			chatScroll.vvalueProperty().bind(currentchat.getGridPane().heightProperty());
			routeMessage(txtMessage.getText());
		}
		
		txtMessage.setText("");
	}
	
	
	
	public void routeMessage(String text) {
		int currentTabIdUser = currentchat.getPeerId();
		;
		if(currentTabIdUser!=-1) { 	//For private chat
			String privateMessage=Template.privateMessage(currentTabIdUser, MainController.getClient().getId(), text);
			send(privateMessage,true);
		}else {
			//System.out.println(MainController.getClient().getName()+"emri");
			String globalMessage=Template.message(text,MainController.getClient().getName(),clicked);
			send(globalMessage,true);
		}
	}
	
	
	
	
	
	private void send(String message,boolean text) {
		if(text) {
			if(JSON.parse(message).has("message")){
				if (JSON.parse(message).getString("message").equals("")) return;
			}else if(JSON.parse(message).has("private-message")) {
				if (JSON.parse(message).getString("private-message").equals("")) return;
			}
			txtMessage.setText("");
		}
		 MainController.getClient().send(message.getBytes());
	}
	
	
	
	
	public void btnBackToLoginCliked(ActionEvent e) {
		MainController.getLoginScene().hideScene();
		Main.getMainScene().showScene();
	}
	
	

	public static int getCurrentChatLog() {
		if(currentchat!=null)
		return currentchat.getPeerId();
		else 
		return -2;
	}
}
