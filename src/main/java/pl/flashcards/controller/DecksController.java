package pl.flashcards.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

import javax.swing.JFileChooser;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.cell.CheckBoxTableCell;
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


public class DecksController {

	@FXML
	private TableView<DeckDisplay> tbl_decks;
	
	@FXML
    private TableColumn<DeckDisplay, Integer> col_number;

    @FXML
    private TableColumn<DeckDisplay, String> col_deck_name;

    @FXML
    private TableColumn<DeckDisplay, Integer> col_cards;

    @FXML
    private TableColumn<DeckDisplay, Integer> col_starred;

    @FXML
    private TableColumn<DeckDisplay, Double> col_last_score;

    @FXML
    private TableColumn<DeckDisplay, Double> col_average_score;

    @FXML
    private Button btn_logout;

    @FXML
    private Button btn_export;

    @FXML
    private Button btn_show_cards;

    @FXML
    private Button btn_study;

    @FXML
    private Button btn_add_new;

    @FXML
    private Button btn_delete;

    @FXML
    void logoutAction(MouseEvent event) throws IOException {
    		Parent parent = FXMLLoader.load(getClass().getResource("/view/LoginView.fxml"));
		Scene scene = new Scene(parent);
		Main.getPrimaryStage().setScene(scene);
		
		Main.setLoggedInUser(null);
    }
    
    @FXML
    void addNewAction(MouseEvent event) throws IOException {
    		Parent parent = FXMLLoader.load(getClass().getResource("/view/NewDeckView.fxml"));
		Scene scene = new Scene(parent);
		Main.getPrimaryStage().setScene(scene);
    }

    @FXML
    void deleteAction(MouseEvent event) {
    		if (Objects.isNull(tbl_decks.getSelectionModel())
				|| Objects.isNull(tbl_decks.getSelectionModel().getSelectedItem())) {

			Alert error = new Alert(AlertType.ERROR);
			error.setHeaderText("ERROR");
			error.setContentText("Please select Item before clicking Delete");
			error.setTitle("No selected item!");
			error.show();
			return;
		}

		int id = tbl_decks.getSelectionModel().getSelectedItem().getId();
		Deck deck = new Deck(id);
		deckService.deleteDeck(deck);

		fillDecksData();
    }

    @FXML
    void exportToFileAction(MouseEvent event) throws FileNotFoundException {
			
		if (Objects.isNull(tbl_decks.getSelectionModel())
				|| Objects.isNull(tbl_decks.getSelectionModel().getSelectedItem())) {

			Alert error = new Alert(AlertType.ERROR);
			error.setHeaderText("ERROR");
			error.setContentText("Please select deck you want to save.");
			error.setTitle("No selected item!");
			error.show();
			return;
			
		} else {
			
			FileChooser fileChooser = new FileChooser();
			File file = fileChooser.showOpenDialog(null);

			int id = tbl_decks.getSelectionModel().getSelectedItem().getId();
			Deck deck = new Deck(id);
			
			saveDeckToFile(file, deck);
			
			Alert success = new Alert(AlertType.INFORMATION);
			success.setHeaderText("SUCCESS");
			success.setContentText("You have saved your cards into the file.");
			success.setTitle("Cards have been saved.!");
			success.show();
		}

    }

    	private String getCardsData(Deck deck) {
    		
    		String deckName = deckService.selectDeck(deck).getName();
    		String userName = Main.getLoggedInUser().getLogin();

    		String cardsInfo = "# " + deckName;
    		cardsInfo += "\n# Created by " + userName;
    		
    		CardService cardService = new CardService();
    		List <Card> cards = cardService.getAll(deck);
    		
    		for (Card card : cards) {
    			String front = card.getFront();
    			String back = card.getBack();
    			cardsInfo += "\n" + front + " | " + back;
    		}
    			
    		return cardsInfo;
    	}
    	
    	private void saveDeckToFile(File file, Deck deck) throws FileNotFoundException {
    			
    			PrintWriter writer = new PrintWriter(file);
    			    			
    			String value = getCardsData(deck);
    			
    			writer.println(value);

    			writer.close();
    	}

	@FXML
    void showCardsAction(MouseEvent event) {
    		
    }

    @FXML
    void studyAction(MouseEvent event) throws IOException {
    	
    		if (Objects.isNull(tbl_decks.getSelectionModel())
				|| Objects.isNull(tbl_decks.getSelectionModel().getSelectedItem())) {

    			Alert error = new Alert(AlertType.ERROR);
			error.setHeaderText("ERROR");
			error.setContentText("Please select Item before clicking Study");
			error.setTitle("No selected item!");
			error.show();
			return;

		}

		int id = tbl_decks.getSelectionModel().getSelectedItem().getId();
		Deck deck = new Deck(id);
		Main.setSelectedDeck(deckService.selectDeck(deck));
		System.out.println("Selected deck is: " + Main.getSelectedDeck().getName());

		fillDecksData();
		
		Parent parent = FXMLLoader.load(getClass().getResource("/view/CardSelectionView.fxml"));
		Scene scene = new Scene(parent);
		Main.getPrimaryStage().setScene(scene);
  
    }
    
    private static DeckService deckService = new DeckService();
    private List<DeckDisplay> deckDisplays;
        
    public void initialize() {
    		fillDecksData();

		setCellValue();
		
		// włączenie edytowania w tabeli
		tbl_decks.setEditable(true);

		// metoda odpowiedzialna za obsługę edycji na poszczególnych polach
		//editCells();
		
    }
    
    private void fillDecksData() {
 
		List<Deck> decks = deckService.getAll();
		deckDisplays = deckService.getDeckDisplayAll(decks);

		ObservableList<DeckDisplay> data = FXCollections.observableArrayList(deckDisplays);
		
		Integer number = 1;
		if (data.size()>0) {
			for (DeckDisplay deckDisplay : data) {
				deckDisplay.setNumber(number);
				number++;
			}
		}
		tbl_decks.setItems(null);
		tbl_decks.setItems(data);
	}

	private void setCellValue() {

		col_number.setCellValueFactory(new PropertyValueFactory<>("number"));
		col_deck_name.setCellValueFactory(new PropertyValueFactory<>("name"));
		col_cards.setCellValueFactory(new PropertyValueFactory<>("numberOfCards"));
		col_starred.setCellValueFactory(new PropertyValueFactory<>("numberOfStarred"));
		col_last_score.setCellValueFactory(new PropertyValueFactory<>("lastScore"));
		col_average_score.setCellValueFactory(new PropertyValueFactory<>("averageScore"));
	}
	
//	private void editSelectCell() {
//		col_select.setCellValueFactory(new Callback<CellDataFeatures<DeckDisplay, Boolean>, ObservableValue<Boolean>>() {
//
//			@Override
//			public ObservableValue<Boolean> call(CellDataFeatures<DeckDisplay, Boolean> param) {
//				DeckDisplay deckDisplay = param.getValue();
//
//				SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(deckDisplay.isSelected());
//				booleanProp.addListener(new ChangeListener<Boolean>() {
//
//					@Override
//					public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
//							Boolean newValue) {
//						deckDisplay.setSelected(newValue);
//
//						//deckService.update(deckDisplay);
//					}
//				});
//				return booleanProp;
//			}
//		});

//		col_select.setCellFactory(new Callback<TableColumn<DeckDisplay, Boolean>, TableCell<DeckDisplay, Boolean>>() {
//			@Override
//			public TableCell<DeckDisplay, Boolean> call(TableColumn<DeckDisplay, Boolean> p) {
//				CheckBoxTableCell<DeckDisplay, Boolean> cell = new CheckBoxTableCell<DeckDisplay, Boolean>();
//				cell.setAlignment(Pos.CENTER);
//				return cell;
//			}
//		});
//	}
	
//	private void editCells() {
//		editSelectCell();
//	}

	
}




