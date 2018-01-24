package Tools;

//import java.awt.Color;

import application.ChatWindowController;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class UserEntry {
    //private Image profilePhoto;
    private Boolean msgNotification;
    private String userName;
    private Label un;
    private int Id;
    
    
    public UserEntry(String userName,Boolean Notification,GridPane gp,int rownr,int Id) {
    	this.userName = userName; 
    	this.msgNotification = Notification;
    	this.Id = Id;
    	//this.profilePhoto = profilePhoto;
    	constructUserEntry(gp,rownr);
    }
    
    public void setNotifocation(GridPane gp,int rownr) {
    	Circle noti = new Circle(5);
    	gp.add(noti, 2, rownr);
    }
    
    public void removeNotofication(GridPane gp,int rownr) {
    	Circle noti = new Circle(5);
    	noti.setFill(Paint.valueOf("#313149"));
    	gp.add(noti, 2, rownr);
    }
    
    private void setConstraints(GridPane gp) {
    	RowConstraints rc = new RowConstraints();
    	rc.setMinHeight(40);
    	gp.getRowConstraints().add(rc);
    }
    
    private void constructUserEntry(GridPane gp,int rownr) {
    	setConstraints(gp);
    	Circle pp = new Circle(15);
    	//put photo into circle
    	
    	un = new Label(userName);
    	un.setPrefWidth(211);
    	un.getStyleClass().add("users-label");
    	
    	Circle noti = new Circle(5);
    	noti.setFill(Color.RED);
    	gp.add(pp, 0, rownr);
    	gp.add(un, 1, rownr);
    	if(msgNotification) {
	    	gp.add(noti, 2, rownr);
    	}
    }
    
    public Label getUsrLabel() {
    	return un;
    }
    
    public int getId() {
    	return Id;
    }
}
