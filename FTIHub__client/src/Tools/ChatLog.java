package Tools;

import java.util.LinkedList;

import javafx.geometry.HPos;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;

public class ChatLog {
	
    private int peerId;
    private LinkedList<ChatBubble> cll;
    private GridPane gp;
    private int rownr;
    private Mode mode;
    
    public ChatLog(int peerId,ScrollPane sp,Mode mode) {
    	this.peerId = peerId;
    	this.mode = mode;
    	cll = new LinkedList<ChatBubble>();
    	initGridPane(sp);
    }
     
    private void initGridPane(ScrollPane sp) {
    	gp = new GridPane();
    	gp.setPrefHeight(sp.getHeight());
    	gp.setPrefWidth(sp.getWidth());
      	//System.out.println(sp.getChildrenUnmodifiable());
    	ColumnConstraints col1 = new ColumnConstraints();
    	ColumnConstraints col2 = new ColumnConstraints();
    	col1.setHalignment(HPos.LEFT);
    	col1.setPrefWidth(sp.getWidth()/2);
    	col2.setHalignment(HPos.RIGHT);
    	col2.setPrefWidth(sp.getWidth()/2-20);
    	gp.getColumnConstraints().addAll(col1,col2);
    	gp.getStyleClass().add("paneRight");

    }
    
    public void bindGridToScroll(ScrollPane sp) {
    	sp.setContent(this.gp);
    }
    
    public void restoreChatLog(ScrollPane sp) {
    	System.out.println(gp.getChildren().size());
    	gp.getChildren().clear();
    	System.out.println(gp.getChildren().size());
    	for(rownr = 0;rownr<cll.size();rownr++) {
    		cll.get(rownr).createBubble(gp, rownr);
    	}
    }
    
    public void addChatBubble(String msg, Role role,String peerId) {
    	ChatBubble cb = new ChatBubble(msg,role,mode,peerId);
    	cb.createBubble(gp, rownr);
    	cll.add(cb);
    	rownr++;
    }
    
    public GridPane getGridPane() {
    	return gp;
    }
    
    public int getPeerId() {
    	return peerId;
    }
}
