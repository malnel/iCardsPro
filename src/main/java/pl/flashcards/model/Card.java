package pl.flashcards.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Card {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false)
	private String front;
	
	@Column(nullable = false)
	private String back;
	
	@Column(nullable = false, columnDefinition = "boolean default false")
	private boolean isStarred;
	
	@Column(nullable = false, columnDefinition = "int default 0")
	private int timesPlayed;
	
	@Column(nullable = false, columnDefinition = "int default 0")
	private int timesAnsweredCorrectly;
	
	@Column(nullable = false, columnDefinition = "boolean default false")
	private boolean lastAnswerCorrect;
	
	@Column(nullable = false, columnDefinition = "int default 3")
	private int skill;
	
	@Column(name = "deck_id")
	private int deckId;

	private transient Integer number;
	
	private transient Boolean isSelected;
	
	public Card(int id, String front, String back, boolean isStarred, int timesPlayed, int timesAnsweredCorrectly,
			boolean lastAnswerCorrect, int skill, int deckId) {
		super();
		this.id = id;
		this.front = front;
		this.back = back;
		this.isStarred = isStarred;
		this.timesPlayed = timesPlayed;
		this.timesAnsweredCorrectly = timesAnsweredCorrectly;
		this.lastAnswerCorrect = lastAnswerCorrect;
		this.skill = skill;
		this.deckId = deckId;
		this.isSelected = false;
	}
	
	public Card(int id, String front, String back, boolean isStarred, int timesPlayed, int timesAnsweredCorrectly,
			boolean lastAnswerCorrect, int skill, int deckId, boolean isSelected) {
		super();
		this.id = id;
		this.front = front;
		this.back = back;
		this.isStarred = isStarred;
		this.timesPlayed = timesPlayed;
		this.timesAnsweredCorrectly = timesAnsweredCorrectly;
		this.lastAnswerCorrect = lastAnswerCorrect;
		this.skill = skill;
		this.deckId = deckId;
		this.isSelected = isSelected;
	}

	public Card(String front, String back, boolean isStarred, int timesPlayed, int timesAnsweredCorrectly,
			boolean lastAnswerCorrect, int skill, int deckId) {
		super();
		this.front = front;
		this.back = back;
		this.isStarred = isStarred;
		this.timesPlayed = timesPlayed;
		this.timesAnsweredCorrectly = timesAnsweredCorrectly;
		this.lastAnswerCorrect = lastAnswerCorrect;
		this.skill = skill;
		this.deckId = deckId;
	}

	public Card(String front, String back, int deckId, int skill) {
		super();
		this.front = front;
		this.back = back;
		this.deckId = deckId;
		this.skill = skill;
		this.number = 0;
	}
	
	public Card(int id) {
		super();
		this.id = id;
	}

	public Card() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFront() {
		return front;
	}

	public void setFront(String front) {
		this.front = front;
	}

	public String getBack() {
		return back;
	}

	public void setBack(String back) {
		this.back = back;
	}

	public boolean isStarred() {
		return isStarred;
	}

	public void setStarred(boolean isStarred) {
		this.isStarred = isStarred;
	}

	public int getTimesPlayed() {
		return timesPlayed;
	}

	public void setTimesPlayed(int timesPlayed) {
		this.timesPlayed = timesPlayed;
	}

	public int getTimesAnsweredCorrectly() {
		return timesAnsweredCorrectly;
	}

	public void setTimesAnsweredCorrectly(int timesAnsweredCorrectly) {
		this.timesAnsweredCorrectly = timesAnsweredCorrectly;
	}

	public boolean isLastAnswerCorrect() {
		return lastAnswerCorrect;
	}

	public void setLastAnswerCorrect(boolean lastAnswerCorrect) {
		this.lastAnswerCorrect = lastAnswerCorrect;
	}

	public int getSkill() {
		return skill;
	}

	public void setSkill(int skill) {
		this.skill = skill;
	}

	public int getDeckId() {
		return deckId;
	}

	public void setDeckId(int deckId) {
		this.deckId = deckId;
	}
	
	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	@Override
	public String toString() {
		return "Card [id=" + id + ", front=" + front + ", back=" + back + ", isStarred=" + isStarred + ", timesPlayed="
				+ timesPlayed + ", timesAnsweredCorrectly=" + timesAnsweredCorrectly + ", lastAnswerCorrect="
				+ lastAnswerCorrect + ", skill=" + skill + ", deckId=" + deckId +", isSelected=" + isSelected + "]";
	}

	public Boolean isSelected() {
		return isSelected;
	}

	public void setSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	

}
