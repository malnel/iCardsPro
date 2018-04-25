package pl.flashcards.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.util.Callback;
import pl.flashcards.Main;
import pl.flashcards.model.Card;
import pl.flashcards.model.Deck;
import pl.flashcards.service.CardService;

public class CardSelectionController {
	
	@FXML
	private Label lbl_deck_name;

    @FXML
    private Button btn_select;

    @FXML
    private TableView<Card> tbl_cards;

    @FXML
    private TableColumn<Card, Boolean> col_select;

    @FXML
    private TableColumn<Card, Integer> col_number;

    @FXML
    private TableColumn<Card, String> col_front;

    @FXML
    private TableColumn<Card, String> col_back;

    @FXML
    private TableColumn<Card, Double> col_skill;

    @FXML
    private TableColumn<Card, Boolean> col_star;

    @FXML
    private TableColumn<Card, Boolean> col_last_answer;

    @FXML
    private Button btn_add_card;

    @FXML
    private Button btn_delete_card;

    @FXML
    private ComboBox<String> cmb_skill_selection;

    @FXML
    private CheckBox cb_starred;

    @FXML
    private CheckBox cb_last_incorrect;

    @FXML
    private Button btn_cancel;

    @FXML
    private Button btn_study;

    @FXML
    void addCardAction(MouseEvent event) {
    		
    		Card card = new Card("front","back",Main.getSelectedDeck().getId(),3);
    		cardService.saveCardToDB(card);
    		
    		fillCardsData();
    	
    }

    @FXML
    void addLastIncorrectAction(MouseEvent event) {

    }

    @FXML
    void addStarredAction(MouseEvent event) {

    }

    @FXML
    void cancelAction(MouseEvent event) throws IOException {

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

		fillCardsData();
    	
    }

    @FXML
    void selectAction(MouseEvent event) {
    		String skillSelection = cmb_skill_selection.getValue();
    		int skillSetting = 0;
    		boolean addStarred = cb_starred.isSelected();
    		boolean addLastAnswerIncorrect = cb_last_incorrect.isSelected();
    		
    		switch (skillSelection) {
    			case "All cards":
    				skillSetting = 5;
    				break;
    			case "Skill 4 or lower":
    				skillSetting = 4;
    				break;
    			case "Skill 3 or lower":
    				skillSetting = 3;
    				break;
    			case "Skill 2 or lower":
    				skillSetting = 2;
    				break;
    			case "Skill 1":
    				skillSetting = 1;
    				break;
    			case "No selection":
    				skillSetting = 0;
    				break;
    			default:
    				skillSetting = 5;
    				break;
    		}
    		
    		filterSelection(skillSetting, addStarred, addLastAnswerIncorrect);
    		System.out.println(cards);
    		//selectCardsWithSkillSetting(skillSetting);
    		    		
    		ObservableList<Card> data = FXCollections.observableArrayList(cards);
    			
    		tbl_cards.setItems(null);
    		tbl_cards.setItems(data);
    		
    		setCellValue();
    		tbl_cards.setEditable(true);
    		editCells();
    		
    		
    		
    }

	private void filterSelection(int skillSetting, boolean addStarred, boolean addLastAnswerIncorrect) {
		
		selectCardsWithSkillSetting(skillSetting);
  	
		if (addStarred && !addLastAnswerIncorrect) {
			for (Card card : cards) {
				if (card.isStarred()) {
					card.setSelected(true);
				} else {
					selectCardsWithSkillSetting(skillSetting);
				}
			}
		}
		
		if (addLastAnswerIncorrect && !addStarred) {
			for (Card card : cards) {
				if (!card.isLastAnswerCorrect()) {
					card.setSelected(true);
				} else {
					selectCardsWithSkillSetting(skillSetting);
				}
			}
		}
		
		if (addLastAnswerIncorrect && addStarred) {
			for (Card card : cards) {
				if (card.isStarred() || !card.isLastAnswerCorrect()) {
					card.setSelected(true);
				} else {
					selectCardsWithSkillSetting(skillSetting);
				}
			}
		}
	}

	private void selectCardsWithSkillSetting(int skillSetting) {
		for (Card card : cards) {
			if (skillSetting >= card.getSkill()) {
				card.setSelected(true);
			} else {
				card.setSelected(false);
			}
		}
	}

    @FXML
    void selectSkillAction(MouseEvent event) {

    }

    @FXML
    void studyAction(MouseEvent event) throws IOException {
    	
    		selectedCards = filterSelectedCards(cards);

    		Parent parent = FXMLLoader.load(getClass().getResource("/view/CardView.fxml"));
		Scene scene = new Scene(parent);
		Main.getPrimaryStage().setScene(scene);

    }
    
    private CardService cardService = new CardService();
    private static List<Card> cards;
	static List<Card> selectedCards = new ArrayList<Card>();
	
	private ObservableList<String> skillSelection = FXCollections.observableArrayList(
			"All cards",
			"Skill 4 or lower",
			"Skill 3 or lower",
			"Skill 2 or lower",
			"Skill 1",
			"No selection");

    
    public void initialize() {
    	
		cmb_skill_selection.setItems(skillSelection);
    	
		fillCardsData();

		setCellValue();
		
		// włączenie edytowania w tabeli
		tbl_cards.setEditable(true);

		// metoda odpowiedzialna za obsługę edycji na poszczególnych polach
		editCells();
	
}
    
    private void fillCardsData() {
    	
    		lbl_deck_name.setText(Main.getSelectedDeck().getName());
    	 
		cards = cardService.getAll(Main.getSelectedDeck());
		
		for (Card card : cards) {
			card.setSelected(true);
		}

		ObservableList<Card> data = FXCollections.observableArrayList(cards);
		
		Integer number = 1;
		if (data.size()>0) {
			for (Card card : data) {
				card.setNumber(number);
				number++;
			}
		}
			
		tbl_cards.setItems(null);
		tbl_cards.setItems(data);
		
	}

	private void setCellValue() {

		col_select.setCellValueFactory(new PropertyValueFactory<>("isSelected"));
		col_number.setCellValueFactory(new PropertyValueFactory<>("number"));
		col_front.setCellValueFactory(new PropertyValueFactory<>("front"));
		col_back.setCellValueFactory(new PropertyValueFactory<>("back"));
		col_skill.setCellValueFactory(new PropertyValueFactory<>("skill"));
		col_star.setCellValueFactory(new PropertyValueFactory<>("isStarred"));
		col_last_answer.setCellValueFactory(new PropertyValueFactory<>("lastAnswerCorrect"));

	}
	
	private void editSelectCell() {
		col_select.setCellValueFactory(new Callback<CellDataFeatures<Card, Boolean>, ObservableValue<Boolean>>() {

			@Override
			public ObservableValue<Boolean> call(CellDataFeatures<Card, Boolean> param) {
				Card card = param.getValue();

				SimpleBooleanProperty booleanProp = new SimpleBooleanProperty(card.isSelected());
				booleanProp.addListener(new ChangeListener<Boolean>() {

					@Override
					public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue,
							Boolean newValue) {
						card.setSelected(newValue);
						
						for (Card c : cards) {
							if (c.getId() == card.getId()) {
								c.setSelected(newValue);
							}
						}
					}
				});
				return booleanProp;
			}
		});

		col_select.setCellFactory(new Callback<TableColumn<Card, Boolean>, TableCell<Card, Boolean>>() {
			@Override
			public TableCell<Card, Boolean> call(TableColumn<Card, Boolean> p) {
				CheckBoxTableCell<Card, Boolean> cell = new CheckBoxTableCell<Card, Boolean>();
				cell.setAlignment(Pos.CENTER);
				return cell;
			}
		});
	}
	
	private void editFrontCell() {
		col_front.setCellFactory(TextFieldTableCell.forTableColumn());
		col_front.setOnEditCommit(new EventHandler<CellEditEvent<Card, String>>() {
			@Override
			public void handle(CellEditEvent<Card, String> card) {
				((Card) card.getTableView().getItems().get(card.getTablePosition().getRow())).setFront(card.getNewValue());

				Card selectedItem = tbl_cards.getSelectionModel().getSelectedItem();
				// update wiersza po edycji pola
				cardService.updateCardToDB(selectedItem);
			}
		});
	}
	
	private void editCells() {
		editSelectCell();
		editFrontCell();
		editBackCell();
	}
	
	private void editBackCell() {
		col_back.setCellFactory(TextFieldTableCell.forTableColumn());
		col_back.setOnEditCommit(new EventHandler<CellEditEvent<Card, String>>() {
			@Override
			public void handle(CellEditEvent<Card, String> card) {
				((Card) card.getTableView().getItems().get(card.getTablePosition().getRow())).setBack(card.getNewValue());

				Card selectedItem = tbl_cards.getSelectionModel().getSelectedItem();
				// update wiersza po edycji pola
				cardService.updateCardToDB(selectedItem);
			}
		});
	}
	
	private List<Card> filterSelectedCards(List<Card> cards) {
		
		selectedCards.clear();
		for (int i = 0; i < cards.size(); i ++) {
			if (cards.get(i).isSelected()) {
				selectedCards.add(cards.get(i));
			}
		}
		
		return selectedCards;
	}

}
