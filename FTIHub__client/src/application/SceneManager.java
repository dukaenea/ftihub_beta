package application;

import java.io.IOException;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SceneManager {
    Stage stage;
    Scene scene;
    Parent root;
    String fxmlurl;
    String cssurl;
    double width;
    double height;
    
    SceneManager(){
    	this.stage = new Stage();
    }
    
    public SceneManager(Stage stage,String fxmlurl,String cssurl,double width,double height){
    	this.stage = stage;
    	this.fxmlurl = fxmlurl;
    	this.cssurl = cssurl;
    	this.width = width;
    	this.height = height;
    }
    
    public void initScene() {
    	try {
			root = FXMLLoader.load(getClass().getResource(fxmlurl));
			scene = new Scene(root,width,height);
			scene.getStylesheets().add(getClass().getResource(cssurl).toExternalForm());
			stage.setScene(scene);
			stage.setResizable(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
    
    public void showScene() {
    	stage.show();
    	stage.sizeToScene();
    }
    
    public void killScene() {
    	stage.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent arg0) {
				Platform.exit();
				System.exit(0);
			}
    		
    	});
    	//stage.close();

    }
    
    public void hideScene() {
    	stage.hide();
    }
}
