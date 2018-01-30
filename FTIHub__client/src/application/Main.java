package application;
	
import javafx.application.Application;
import javafx.stage.Stage;



public class Main extends Application {
	
	public static SceneManager mainScene;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) {
	    mainScene = new SceneManager(primaryStage,"/application/Login-Signup.fxml","application.css",600,400);
	    mainScene.initScene();
	    mainScene.showScene();
	    mainScene.killScene();
	}
	
	public static SceneManager getMainScene() {
		return mainScene;
	}
	
}
