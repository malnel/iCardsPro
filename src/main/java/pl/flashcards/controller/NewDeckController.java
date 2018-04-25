package pl.flashcards.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import java.util.StringTokenizer;

import javax.swing.JFileChooser;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import pl.flashcards.Main;
import pl.flashcards.model.Card;
import pl.flashcards.model.Deck;
import pl.flashcards.model.DeckDisplay;
import pl.flashcards.service.CardService;
import pl.flashcards.service.DeckService;

public class NewDeckController {

    @FXML
    private TextField tf_deck_name;

    @FXML
    private Button btn_import;

    @FXML
    private TableView<Card> tbl_cards;

    @FXML
    private TableColumn<Card, Integer> col_number;

    @FXML
    private TableColumn<Card, String> col_front;

    @FXML
    private TableColumn<Card, String> col_back;

    @FXML
    private TextArea tf_front;

    @FXML
    private TextArea tf_back;

    @FXML
    private Button btn_add_card;

    @FXML
    private Button btn_delete_card;

    @FXML
    private Button btn_cancel;

    @FXML
    private Button btn_add_deck;

    @FXML
    void addCardAction(MouseEvent event) {
    		if ("".equals(tf_front.getText()) || "".equals(tf_back.getText())) {
    			Alert alert = new Alert(AlertType.ERROR);
    			alert.setHeaderText("Error");
    			alert.setContentText("Type front and back side of your card.");
    			alert.setTitle("Card is not filled");
    			alert.show();
    		} else if (isStringLengthOk(tf_front.getText()) &&
    				   isStringLengthOk(tf_back.getText())) {
    			
    			Card card = new Card(tf_front.getText(), tf_back.getText(), deck.getId(), 3);
    			cardService.saveCardToDB(card);
    			fillDecksData();
    			setCellValue();
    			tf_front.setText("");
    			tf_back.setText("");
    		} else {
    			Alert alert = new Alert(AlertType.ERROR);
    			alert.setHeaderText("Error");
    			alert.setContentText("Card value is too long. Use max 255 characters.");
    			alert.setTitle("Card value too long.");
    			alert.show();
    		}
    }

    @FXML
    void addDeckAction(MouseEvent event) throws IOException {
    		
    		if("".equals(tf_deck_name.getText())) {
    			Alert alert = new Alert(AlertType.ERROR);
    			alert.setHeaderText("Error");
    			alert.setContentText("Enter name of the deck.");
    			alert.setTitle("No deck name.");
    			alert.show();
    		} else {
    			
    			deckService = new DeckService();
        		deckService.setDeckName(deck, tf_deck_name.getText());
    		
	    		Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText("Success!");
			alert.setContentText("New deck of flashcards was added to your list.");
			alert.setTitle("New deck added.");
			alert.show();
	    	
	    		Parent parent = FXMLLoader.load(getClass().getResource("/view/DecksView.fxml"));
			Scene scene = new Scene(parent);
			Main.getPrimaryStage().setScene(scene);
    		}
    }

    @FXML
    void cancelAction(MouseEvent event) throws IOException {
    		
    		deckService = new DeckService();
    		deckService.deleteDeck(deck);
    		
    		Parent parent = FXMLLoader.load(getClass().getResource("/view/DecksView.fxml"));
    		Scene scene = new Scene(parent);
    		Main.getPrimaryStage().setScene(scene);
    }

    @FXML
    void deleteCardAction(MouseEvent event) {
    	
    		if (Objects.isNull(tbl_cards.getSelectionModel())
				|| Objects.isNull(tbl_cards.getSelectionModel().getSelectedItem())) {

			Alert error = new Alert(AlertType.ERROR);
			error.setHeaderText("ERROR");
			error.setContentText("Please select Item before clicking Delete");
			error.setTitle("No selected item!");
			error.show();
			return;
		}

		int id = tbl_cards.getSelectionModel().getSelectedItem().getId();
		Card card = new Card(id);
		cardService.deleteCard(card);

		fillDecksData();
		setCellValue();

    }

    @FXML
      void importAction(MouseEvent event) {

    		FileChooser fileChooser = new FileChooser();
		File file = fileChooser.showOpenDialog(null);
    		
		try {
			loadCardsFromFile(file);
			fillDecksData();
			setCellValue();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
    }
    
    private Deck deck;
    
    private CardService cardService;
    
    private DeckService deckService;
    
    public void initialize() {
    		deck = new Deck();
    		deck.setUserId(Main.getLoggedInUser().getId());
		
		saveDeckToDB(deck);
    	
    		fillDecksData();

		setCellValue();
		
		tf_front.clear();
		tf_back.clear();
		
    }
    
    private void fillDecksData() {
 
		cardService = new CardService();
		List<Card> cards = cardService.getAll(deck);

		ObservableList<Card> data = FXCollections.observableArrayList(cards);
		
		Integer number = 1;
		if (data.size()>0) {
			for (Card card : data) {
				card.setNumber(number);
				number++;
		}
			
		tbl_cards.setItems(null);
		tbl_cards.setItems(data);
		}
    }

	private void setCellValue() {
		col_number.setCellValueFactory(new PropertyValueFactory<>("number"));
		col_front.setCellValueFactory(new PropertyValueFactory<>("front"));
		col_back.setCellValueFactory(new PropertyValueFactory<>("back"));
	}
	
	private void saveDeckToDB(Deck deck) {
			DeckService deckService = new DeckService();
			deckService.save(deck);
	}
	
	private Boolean isStringLengthOk(String string) {
		Boolean result;
		if (255 < string.length()) {
			result = false;
		} else {
			result = true;
		}
		return result;
	}
	
	 public void loadCardsFromFile(File file) throws IOException {
	      Card card;
	      String line;
	      StringTokenizer tokenizer;
	      BufferedReader reader = new BufferedReader(new FileReader(file));

	      // Clear the deck.


	      // Try reading from the file. If an error occurs, close the file.

	      reader = new BufferedReader(new FileReader(file));

	      try {
	         while (true) {
	            // Read a line. If it is null, then break.

	            line = reader.readLine();

	            if (line == null) {
	               break;
	            }

	            line = line.trim();

	            // Iterate through the lines, ignoring lines that start with #.

	            if (! line.startsWith("#")) {
	               // Create a tokenizer for the line, using the vertical bar (|)
	               // as the delimiter.

	               tokenizer = new StringTokenizer(line, "|");

	               // If there are two token on the line (e.g. A | a), create a new
	               // flash card, set the front to the first token, set the back to the
	               // second token, then add the card to the deck.

	               if (tokenizer.countTokens() >= 2) {
	                  card = new Card(
	                     tokenizer.nextToken().trim(),
	                     tokenizer.nextToken().trim(),
	                     deck.getId(),
	                     3);
	                  cardService.saveCardToDB(card);
	               }
	            }
	         }
	      } finally {
	         reader.close();
	      }
	   }
	
}




