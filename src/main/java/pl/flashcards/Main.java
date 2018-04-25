package pl.flashcards;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import pl.flashcards.Main;
import pl.flashcards.model.Deck;
import pl.flashcards.model.User;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class Main extends Application {
	
private static Stage primaryStage;

private static User loggedInUser;

private static Deck selectedDeck;
	
	public static Stage getPrimaryStage() {
		return primaryStage;
	}
	
	private static void setPrimaryStage(Stage primaryStage) {
		Main.primaryStage = primaryStage;
	}
	
	public static User getLoggedInUser() {
		return loggedInUser;
	}
	
	public static void setLoggedInUser(User user) {
		Main.loggedInUser = user;
	}
	
	public static Deck getSelectedDeck() {
		return selectedDeck;
	}
	
	public static void setSelectedDeck(Deck deck) {
		Main.selectedDeck = deck;
	}
	
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			setPrimaryStage(primaryStage);
			
			Parent parent = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
			Scene scene = new Scene(parent);
			scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("iCards Pro");
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}