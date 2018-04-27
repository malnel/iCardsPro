package pl.flashcards.controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;

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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
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
    private Button btn_show_stats;

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
    void showStatsAction(MouseEvent event) throws IOException {
    		
		if (Objects.isNull(tbl_decks.getSelectionModel())
				|| Objects.isNull(tbl_decks.getSelectionModel().getSelectedItem())) {

			Alert error = new Alert(AlertType.ERROR);
			error.setHeaderText("ERROR");
			error.setContentText("Please select the deck.");
			error.setTitle("No selected item!");
			error.show();
			return;
		}

		int id = tbl_decks.getSelectionModel().getSelectedItem().getId();
		Deck deck = new Deck(id);
		deck = deckService.selectDeck(deck);
	
		countDeckStats(deck);	
    }

	private void countDeckStats(Deck deck) {
		CardService cardService = new CardService();
		List <Card> cards = cardService.getAll(deck);
		
		int numbOfAll = cards.size();
		int numbOfStarred = 0;
		int numbOfCorrect = 0;
		int numbOf5 = 0;
		int numbOf4 = 0;
		int numbOf3 = 0;
		int numbOf2 = 0;
		int numbOf1 = 0;
		
		for (Card card : cards) {
			if (card.isStarred()) {
				numbOfStarred++;
			}
			if (card.isLastAnswerCorrect()) {
				numbOfCorrect++;
			}
			if (5 == card.getSkill()) {
				numbOf5++;
				continue;
			}
			if (4 == card.getSkill()) {
				numbOf4++;
				continue;
			}
			if (3 == card.getSkill()) {
				numbOf3++;
				continue;
			}
			if (2 == card.getSkill()) {
				numbOf2++;
				continue;
			}
			if (1 == card.getSkill()) {
				numbOf1++;
				continue;
			}
		}
		
		Alert stats = new Alert(AlertType.INFORMATION);
		stats.setHeaderText("Statistics");
		stats.setContentText(
				    "\t\t\t         Deck:\t" + deck.getName()
				+ "\n\t\t\t        Cards:\t" + numbOfAll
				+ "\n\t\t\t      Starred:\t" + numbOfStarred
				+ "\n  Last answered correctly:\t" + numbOfCorrect
				+ "\n\n\t\t      Cards score"
				+ "\n\t\t\t\t skill 5:\t" + numbOf5
				+ "\n\t\t\t\t skill 4:\t" + numbOf4
				+ "\n\t\t\t\t skill 3:\t"  + numbOf3
				+ "\n\t\t\t\t skill 2:\t"  + numbOf2
				+ "\n\t\t\t\t skill 1:\t"  + numbOf1);
		stats.setTitle("Here are statistics of this deck.");
		stats.show();
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
    
    private DeckService deckService = new DeckService();
    private List<DeckDisplay> deckDisplays;
        
    public void initialize() {
    		fillDecksData();

		setCellValue();
		
		tbl_decks.setEditable(true);

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




