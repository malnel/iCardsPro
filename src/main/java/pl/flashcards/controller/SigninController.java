package pl.flashcards.controller;

import java.io.IOException;
import java.util.Objects;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import pl.flashcards.Main;
import pl.flashcards.model.User;
import pl.flashcards.service.SigninService;

public class SigninController {

    @FXML
    private TextField tf_name;

    @FXML
    private TextField tf_login;

    @FXML
    private PasswordField pf_password;

    @FXML
    private TextField tf_password;

    @FXML
    private Button btn_show;

    @FXML
    private TextField tf_email;

    @FXML
    private Button btn_signin;

    @FXML
    private Button btn_back;

    /**
     * If password field is visible then set it invisible and set textfield visible. Change button text to Hide.
     * Else if password field is not visible then set it visible and set textfield invisible. Change button to Show.
     * @param event
     */
    @FXML
    void showAction(MouseEvent event) {
     	
	    	if (pf_password.isVisible()) {
	    		pf_password.setVisible(false);
	        	tf_password.setVisible(true);
	        	tf_password.setText(pf_password.getText());
	        	btn_show.setText("Hide");
	    	} else if (!pf_password.isVisible()) {
	    		pf_password.setVisible(true);
	    		tf_password.setVisible(false);
	    		pf_password.setText(tf_password.getText());
	    		btn_show.setText("Show");
	    	}
    
    }

    /**
     * If form is incomplete, then show alert.
     * Else check if login already exists. If true, then show alert.
     * Else save new user to DB. Show alert success.
     * @param event
     */
    @FXML
    void signinAction(MouseEvent event) {
     	if (isNotCompleted()) {
			showAlertFormNotCompleted();
		} else {
			SigninService signinService = new SigninService();
			User user = getFormData();
			if (signinService.isExistingLogin(user.getLogin())) {
				showAlertUserAlreadyExists();
			} else {
				signinService.save(user);
				showAlertAddedUser();
			}
		}
    }
    
    /**
     * Return User object. Assign field values from form.
     * @return
     */
    private User getFormData() {
		String login = tf_login.getText();
		String password = pf_password.isVisible() ? pf_password.getText() : tf_password.getText();
		String name = tf_name.getText();
		String email = tf_email.getText();
		
		return new User(login, password, name, email);
	}
    
    /**
     * Check if all information in form is complete.
     * @return
     */
	private boolean isNotCompleted() {
		return "".equals(tf_login.getText()) 
				|| ("".equals(tf_password.getText()) && "".equals(pf_password.getText()))
				|| "".equals(tf_name.getText()) 
				|| "".equals(tf_email.getText());
	}

    @FXML
    void toLoginAction(MouseEvent event) throws IOException {
    		Parent parent = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
		Scene scene = new Scene(parent);
		Main.getPrimaryStage().setScene(scene);
    }

    private void showAlertFormNotCompleted() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("Error");
		alert.setTitle("Form is not complete");
		alert.setContentText("Complete the form to add a new user");
		alert.show();
	}
    
    private void showAlertUserAlreadyExists() {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setHeaderText("User already exists.");
		alert.setTitle("Error.");
		alert.setContentText("Try a different login to create a new user.");
		alert.show();
	}
    
    private void showAlertAddedUser() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setHeaderText("Success!");
		alert.setTitle("New user added.");
		alert.setContentText("Log in to use the app.");
		alert.show();
	}

}
