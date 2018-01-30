package Tools;

import java.io.IOException;
import java.util.LinkedList;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

public class ChatLog {
	
    private int peerId;
    private LinkedList<ChatBubble> cll;
    //private GridPane gp;
    private VBox vb;
    private int rownr;
    private Mode mode;
    
    public ChatLog(int peerId,ScrollPane sp,Mode mode) {
    	this.peerId = peerId;
    	this.mode = mode;
    	cll = new LinkedList<ChatBubble>();
    	vb = new VBox();
    	vb.setPrefWidth(sp.getWidth()-20);
    	//initGridPane(sp);
    	rownr=0;
    }
     
//    private void initGridPane(ScrollPane sp) {
//    	gp = new GridPane();
//    	gp.setPrefHeight(sp.getHeight());
//    	gp.setPrefWidth(sp.getWidth());
//      	//System.out.println(sp.getChildrenUnmodifiable());
//    	ColumnConstraints col1 = new ColumnConstraints();
//    	ColumnConstraints col2 = new ColumnConstraints();
//    	col1.setHalignment(HPos.LEFT);
//    	col1.setPrefWidth(sp.getWidth()/2);
//    	col2.setHalignment(HPos.RIGHT);
//    	col2.setPrefWidth(sp.getWidth()/2-20);
//    	gp.getColumnConstraints().addAll(col1,col2);
//    	gp.getStyleClass().add("paneRight");
//
//    }
    
    public void bindGridToScroll(ScrollPane sp) {
    	sp.setContent(this.vb);
    }
    
    public void restoreChatLog(JSONArray messages) {
    	
    	for(int i=0;i<messages.length();i++) {
    		JSONObject entry = (JSONObject) messages.get(i);
    		for(int j=0;j<100;j++) {
    			
    		}
    		if(entry.has("name"))
    			addChatBubble(entry.getString("message"),Role.PEER,entry.getString("name"));
    		else if(entry.getInt("id_sender")==peerId) {
    			addChatBubble(entry.getString("message"), Role.PEER,"");
    		}
    		else {
    			addChatBubble(entry.getString("message"), Role.USER,"");
    		}
    		
    	}
    	
    }
    
    public void addChatBubble(String msg, Role role,String peerId) {
    	ChatBubble cb = new ChatBubble(msg,role,mode,peerId);
    	cb.createBubble(vb, rownr);
    	cll.add(cb);
    	rownr++;
    }
    
    public VBox getGridPane() {
    	return vb;
    }
    
    public int getPeerId() {
    	return peerId;
    }
}
