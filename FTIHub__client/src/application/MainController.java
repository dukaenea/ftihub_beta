package application;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import client.Client;
import client.ClientInstance;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import messageTemplate.*;

public class MainController {
	
	@FXML
	private TextField username,email,loginusername;
	@FXML
	private PasswordField password,confirmpassword,loginpassword;
	@FXML
	private Pane paneRightli;
	
	
	public static SceneManager sm;
	private final String hostAddress = "localhost";
	private final int hostListenport = 8192;
	private TemplateMessagesClient Template = new TemplateMessagesClient();
	private static Client client;
	private ClientInstance ci;
	private static String users;
	private static ParseMessages JSON=new ParseMessages();
	
	
	
	public void signUpSubmitClicked(ActionEvent e) {
		
		String psw = password.getText();
		String confpsw = confirmpassword.getText();
		String eml = email.getText();
		String name = username.getText();
		
		if(psw.equals(confpsw)) {
			
			if(validateFields(eml,psw)) {
				
				Client potentialClient = new Client(name,psw,hostAddress,hostListenport);
				boolean connect=potentialClient.openConnection();
		    	if (!connect) {
					//Nuk behet do connection me serverin
					System.err.println("Connection failed!");
				
				}else {
		        potentialClient.send(Template.signupCredentials(name,psw,eml).getBytes());
		        listenForLoginOrSignup(potentialClient);
			}
		  }
		}
	} 
	
	
	
	
	public void logInClicked(ActionEvent e) {
		
	     if(!( loginusername.getText().isEmpty() || loginpassword.getText().isEmpty())){
	        
	        client = new Client(loginusername.getText(),loginpassword.getText(),hostAddress,hostListenport);
	    	boolean connect=client.openConnection();
	    	if (!connect) {
				//Nuk behet do connection me serverin
				System.err.println("Connection failed!");
			
			}else {
	        client.send(Template.loginCredentials(client.getName(),client.getPassword()).getBytes());
	        listenForLoginOrSignup(client);
				new ClientInstance(client);
			}
	    	
		}
	}
	
	
	
	
	     
	
	public void signUpClicked(ActionEvent e) {
		paneRightli.setVisible(false);
	}
	
	
	
	
	public void backToLogInClicked(ActionEvent e) {
		paneRightli.setVisible(true);
	}
	
	
	
	
	private boolean validateFields(String email, String psw) {
		
		Pattern p = Pattern.compile("[a-zA-Z1-9]{8,32}");
		Pattern e = Pattern.compile("@.");
		Matcher mp = p.matcher(psw);
		Matcher me = e.matcher(email);
		if(mp.find() && me.find())
			return true;
		else 
			return false;
	}
	
	
	
	

	public static SceneManager getLoginScene() {
		return sm;
	}
	
	
	
	
	
	public static Client getClient() {
		return client;
	}
	
	
	
	public static String getUsers() {
		return users;
	}
	
	
	
	
	public static void createWindow(JSONObject message) {
		ChatWindowController.setUsersContainer(message);
        sm = new SceneManager(new Stage(),"/application/chattwindow.fxml","application.css",800,460);
		sm.initScene();
		sm.showScene();
		Main.getMainScene().hideScene();
		
	}
	
	
	
	
	private void listenForLoginOrSignup(Client client) {
		while(true) {
			String string = client.receive();
			   if(JSON.getTypeOfMessage(string).equals("signup-stat")) {
				   JSONObject parsedMessage = JSON.parse(string);
				   if(parsedMessage.getInt("stat")==1)
					   paneRightli.setVisible(true);
				   else {
					   System.out.println("An account with that username already exists");
					   username.setText("");
				   }
				   break;
			   }
			
			   if(JSON.getTypeOfMessage(string).equals("login-success")) {
				JSONObject parsedMessage = JSON.parse(string);
				client.setId(parsedMessage.getInt("id"));
			   }
			   
			   else if(JSON.getTypeOfMessage(string).equals("login-fail")) {
				System.out.println("Wrong username or password!");
				break;	
				}
			   else if(JSON.getTypeOfMessage(string).equals("all-users")) {
				JSONObject message=JSON.parse(string);
				createWindow(message);
				break;
				}
		}
	}
}
