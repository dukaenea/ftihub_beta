package Tools;

import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;

public class ChatBubble {
	
    private Label msg;
    private RowConstraints rc;
    private double labelWidth;
    private Role role; 
    private Mode mode;
    private String peerId;

    ChatBubble(String msg,Role role,Mode mode,String peerId){
    	this.msg = new Label(msg);
    	this.msg.setWrapText(true);
    	this.rc = new RowConstraints();
    	this.role = role;
    	this.mode = mode;
    	this.peerId = peerId;
    }
    
    public void createBubble(GridPane gp, int rownr) {
    	if(role == Role.USER) {
    		msg.getStyleClass().add("chat-bubble-right");
    		gp.getRowConstraints().add(constructForUser(gp.getWidth()));
        	gp.add(msg, 1, rownr);
    	}
    	else if(role == Role.PEER) {
    		if(mode == Mode.GLOBAL) 
				msg.setText(peerId+"\n\n"+msg.getText());
			msg.getStyleClass().add("chat-bubble-left");
    		gp.getRowConstraints().add(constructForPeer(gp.getWidth()));
        	gp.add(msg, 0, rownr);			
    	
    	}
    }
    
    public void createBubbleGlobal(GridPane gp,int rownr) {
    	if(role == Role.USER) {
    		msg.getStyleClass().add("chat-bubble-right");
    		gp.getRowConstraints().add(constructForUser(gp.getWidth()));
        	gp.add(msg, 1, rownr);
    	}
    }
    
    private RowConstraints constructForUser(double width) {
    	labelWidth = width;
    	rc.setMinHeight(Math.pow(calcHeight(msg.getText()), 1.1));
    	return rc;
    }
    
    private RowConstraints constructForPeer(double width) {
    	
    	labelWidth = width;
    		rc.setMinHeight(Math.pow(calcHeight(msg.getText()), 1.1));
    	return rc;
    	
    }
    
    private double calcHeight(String s) {
		Text t = new Text(s);
		double stringWidth = t.getLayoutBounds().getWidth();
		double stringHeight = 17;
		if(mode == Mode.GLOBAL)
			return (stringWidth*2*stringHeight)/labelWidth + 50;
		else
			return (stringWidth*2*stringHeight)/labelWidth + 25;
	}
}
