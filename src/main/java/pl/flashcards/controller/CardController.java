package pl.flashcards.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;
import pl.flashcards.Main;
import pl.flashcards.model.Card;
import pl.flashcards.model.Deck;
import pl.flashcards.service.CardService;
import pl.flashcards.service.DeckService;

public class CardController {

    @FXML
    private CheckBox cb_type;

    @FXML
    private Label lbl_front;

    @FXML
    private Label lbl_back;

    @FXML
    private TextField tf_answer;

    @FXML
    private Label lbl_numb_cards;

    @FXML
    private Label lbl_numb_answered;

    @FXML
    private Label lbl_numb_remaining;

    @FXML
    private Label lbl_numb_starred;

    @FXML
    private Circle circle1;

    @FXML
    private Circle circle2;

    @FXML
    private Circle circle3;

    @FXML
    private Circle circle4;

    @FXML
    private Circle circle5;

    @FXML
    private ImageView star_white;

    @FXML
    private ImageView star_filled;

    @FXML
    private Label lbl_answer;
    
    @FXML
    private Label lbl_typo;

    @FXML
    private Button btn_cancel;

    @FXML
    private Label lbl_deck_name;

    @FXML
    private Button btn_flip_all;

    @FXML
    private Button btn_wrong;

    @FXML
    private Button btn_flip;

    @FXML
    private Button btn_right;

    @FXML
    private Button btn_shuffle;
    
    @FXML
    private Button btn_next;

    @FXML
    private Button btn_previous;

    @FXML
    void enableTypeAction(MouseEvent event) {
    		boolean enableType;
    		if (cb_type.isSelected()) {
    			tf_answer.setDisable(false);
    		} else {
    			tf_answer.setDisable(true);
    			tf_answer.setText("");
    		}
    }

    @FXML
    void flipAction(MouseEvent event) {
    		if (lbl_front.isVisible()) {
    			lbl_front.setVisible(false);
    			lbl_back.setVisible(true);
    		} else {
    			lbl_front.setVisible(true);
    			lbl_back.setVisible(false);
    		}
    		
    		if (cb_type.isSelected()) {
    			String answer = tf_answer.getText();
    			if (activeCard.getBack().equals(answer)) {
    				rightAction(event);
    			} else {
    				lbl_typo.setVisible(true);
    				btn_flip.setVisible(false);
            		btn_right.setVisible(true);
            		btn_wrong.setVisible(true);
            		btn_next.setVisible(false);
            		btn_previous.setVisible(false);
    			}
    		} else {

        		btn_flip.setVisible(false);
        		btn_right.setVisible(true);
        		btn_wrong.setVisible(true);
        		btn_next.setVisible(false);
        		btn_previous.setVisible(false);
    		}

    		
    }

    @FXML
    void flipAllAction(MouseEvent event) {
    		for (int i = 0; i < activeCards.size(); i++) {
    			String front = activeCards.get(i).getFront();
    			String back = activeCards.get(i).getBack();
    			activeCards.get(i).setFront(back);
    			activeCards.get(i).setBack(front);
    			displayCard(activeCard);
    		}
    }

    @FXML
    void cancelAction(MouseEvent event) throws IOException {
    	
    		calculateScores(Main.getSelectedDeck());
    	
    		Parent parent = FXMLLoader.load(getClass().getResource("/view/DecksView.fxml"));
		Scene scene = new Scene(parent);
		Main.getPrimaryStage().setScene(scene);
    }

    @FXML
    void markStarredAction(MouseEvent event) {
    		star_filled.setVisible(true);
    		star_white.setVisible(false);
    		activeCard.setStarred(true);
    		
    		cardService.updateCardToDB(activeCard);
    		checkCardsInfo(Main.getSelectedDeck());
    }

    @FXML
    void rightAction(MouseEvent event) {
    		lbl_typo.setVisible(false);
    		btn_right.setVisible(false);
    		btn_wrong.setVisible(false);
    		btn_next.setVisible(true);
    		btn_previous.setVisible(true);
    		lbl_answer.setText("CORRECT");
    		
    		playedCardIds.add(activeCard.getId());
    		skillPlus();
    		activeCard.setTimesPlayed(activeCard.getTimesPlayed() + 1);
    		activeCard.setTimesAnsweredCorrectly(activeCard.getTimesAnsweredCorrectly() + 1);
    		activeCard.setLastAnswerCorrect(true);
    		nextCard();
    }
    
    @FXML
    void wrongAction(MouseEvent event) {
    		lbl_typo.setVisible(false);
    		btn_right.setVisible(false);
		btn_wrong.setVisible(false);
		btn_next.setVisible(true);
		btn_previous.setVisible(true);
		lbl_answer.setText("INCORRECT");
		
		playedCardIds.add(activeCard.getId());
		skillMinus();
		activeCard.setTimesPlayed(activeCard.getTimesPlayed() + 1);
		activeCard.setLastAnswerCorrect(false);
		nextCard(); 
    }

    @FXML
    void shuffleAction(MouseEvent event) {
    		Collections.shuffle(activeCards);
    		playedCardIds.clear();
    		cardIndex = 0;
    		activeCard = activeCards.get(cardIndex);
    		displayCard(activeCard);
    		checkCardsInfo(Main.getSelectedDeck());
    }

    @FXML
    void unmarkStarred(MouseEvent event) {
    		star_filled.setVisible(false);
		star_white.setVisible(true);
		activeCard.setStarred(false);
		
		cardService.updateCardToDB(activeCard);
		checkCardsInfo(Main.getSelectedDeck());
    }
    
    @FXML
    void nextAction(MouseEvent event) {
    		nextCard();
    }


    @FXML
    void previousAction(MouseEvent event) {
    		previousCard();
    }
    
    private static List<Card> activeCards;
    private static Card activeCard;
    private static int cardIndex;
    private static CardService cardService = new CardService();
    private static DeckService deckService = new DeckService();
    private static int numberOfCards;
    private static int numberOfStarred;
    private static Set<Integer> playedCardIds = new HashSet<>();
    private static double lastScore = Main.getSelectedDeck().getLastScore();
    private static double averageScore = Main.getSelectedDeck().getAverageScore();
    
    
    public void initialize() {
    		//activeCards = cardService.getAll(Main.getSelectedDeck());
    		activeCards = CardSelectionController.selectedCards;
    		checkCardsInfo(Main.getSelectedDeck());
    		cardIndex = 0;
    		playedCardIds.clear();
    		activeCard = activeCards.get(cardIndex);
    		displayCard(activeCard);	
}
    
    public void checkCardsInfo(Deck deck) {
    		numberOfCards = CardSelectionController.selectedCards.size();
    		numberOfStarred = 0;
    		
    		for (Card card : CardSelectionController.selectedCards) {
    			if (card.isStarred()) {
    				numberOfStarred++;
    			}
    		}
    		
    		lbl_numb_cards.setText("" + numberOfCards);
    		lbl_numb_starred.setText("" + numberOfStarred);
    		lbl_deck_name.setText(deck.getName());
    }
    
    public void displayCard(Card activeCard) {
    		lbl_front.setText(activeCard.getFront());
		lbl_back.setText(activeCard.getBack());
    	
    		lbl_numb_answered.setText("" + playedCardIds.size());
    		lbl_numb_remaining.setText("" + (numberOfCards - playedCardIds.size()));
    		
    		lbl_front.setVisible(true);
    		lbl_back.setVisible(false);
    		colorSkillDots();
    		showWhiteOrFilledStar();
    }
    
    public void colorSkillDots() {
    		if (activeCard.getSkill()==1) {
    			circle1.setFill(javafx.scene.paint.Color.web("#69115E"));
    			circle2.setFill(javafx.scene.paint.Color.web("#FFFFFF"));
    			circle3.setFill(javafx.scene.paint.Color.web("#FFFFFF"));
    			circle4.setFill(javafx.scene.paint.Color.web("#FFFFFF"));
    			circle5.setFill(javafx.scene.paint.Color.web("#FFFFFF"));
    		} else if (activeCard.getSkill()==2) {
    			circle1.setFill(javafx.scene.paint.Color.web("#69115E"));
    			circle2.setFill(javafx.scene.paint.Color.web("#69115E"));
    			circle3.setFill(javafx.scene.paint.Color.web("#FFFFFF"));
    			circle4.setFill(javafx.scene.paint.Color.web("#FFFFFF"));
    			circle5.setFill(javafx.scene.paint.Color.web("#FFFFFF"));
    		} else if (activeCard.getSkill()==3) {
    			circle1.setFill(javafx.scene.paint.Color.web("#69115E"));
    			circle2.setFill(javafx.scene.paint.Color.web("#69115E"));
    			circle3.setFill(javafx.scene.paint.Color.web("#69115E"));
    			circle4.setFill(javafx.scene.paint.Color.web("#FFFFFF"));
    			circle5.setFill(javafx.scene.paint.Color.web("#FFFFFF"));
    		} else if (activeCard.getSkill()==4) {
    			circle1.setFill(javafx.scene.paint.Color.web("#69115E"));
    			circle2.setFill(javafx.scene.paint.Color.web("#69115E"));
    			circle3.setFill(javafx.scene.paint.Color.web("#69115E"));
    			circle4.setFill(javafx.scene.paint.Color.web("#69115E"));
    			circle5.setFill(javafx.scene.paint.Color.web("#FFFFFF"));
    		} else if (activeCard.getSkill()==3) {
    			circle1.setFill(javafx.scene.paint.Color.web("#69115E"));
    			circle2.setFill(javafx.scene.paint.Color.web("#69115E"));
    			circle3.setFill(javafx.scene.paint.Color.web("#69115E"));
    			circle4.setFill(javafx.scene.paint.Color.web("#69115E"));
    			circle5.setFill(javafx.scene.paint.Color.web("#69115E"));
    		}
    }
    
    public void skillPlus() {
    		if (activeCard.getSkill()>0 && activeCard.getSkill()<5) {
    			activeCard.setSkill((activeCard.getSkill())+1);
    		}
    		colorSkillDots();
    }
    
    public void skillMinus() {
    		if (activeCard.getSkill()>1 && activeCard.getSkill()<=5) {
    			activeCard.setSkill((activeCard.getSkill())-1);
    		}
    		colorSkillDots();
    }
    
    private void nextCard() {
    		cardService.updateCardToDB(activeCard);
    		
    		btn_flip.setVisible(true);
    		
		if (cardIndex>=0 && cardIndex<(activeCards.size()-1)) {
			activeCard = activeCards.get(++cardIndex);
			displayCard(activeCard);

		} else {
			displayCard(activeCard);
    			calculateScores(Main.getSelectedDeck());

			lbl_numb_answered.setText("" + playedCardIds.size());
    			lbl_numb_remaining.setText("" + (numberOfCards - playedCardIds.size()));
    			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText("You viewed all cards!");
			alert.setContentText("Deck:\t\t\t" + Main.getSelectedDeck().getName()
					+ "\nYour score:\t\t" + lastScore + "%"
					+ "\nAverage card skill:\t" + averageScore);
			alert.setTitle("Study complete.");
			alert.show();
		}
	}
    
    private void previousCard() {
		cardService.updateCardToDB(activeCard);

		if (cardIndex!=0) {
			activeCard = activeCards.get(--cardIndex);
			displayCard(activeCard);
		}
	}
    
    private void showWhiteOrFilledStar() {
    		if (activeCard.isStarred()) {
    			star_filled.setVisible(true);
    			star_white.setVisible(false);
    		} else {
    			star_filled.setVisible(false);
    			star_white.setVisible(true);
    		}
    }
    
    private void calculateScores(Deck deck) {
    	
    		averageScore = 0;
    		int correctAnswers = 0;
    		
    		for (Card card : activeCards) {
    			averageScore += card.getSkill();
    		}
		averageScore = round((averageScore / activeCards.size()) , 1);

    		
		for (Card card : activeCards) {
			if (card.isLastAnswerCorrect()) {
				correctAnswers++;
			}
		}
		
		lastScore = (round(100 * correctAnswers / activeCards.size(), 0));
    		
    		deck.setLastScore(lastScore);
    		deck.setAverageScore(averageScore);
    		
    		deckService.updateDeckToDB(deck);
    		
    }
    
    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    
    

    
    
}
