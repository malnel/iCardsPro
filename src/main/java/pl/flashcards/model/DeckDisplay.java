package pl.flashcards.model;

public class DeckDisplay {
	
	private int id;

	private String name;
	
	private double averageScore;
	
	private double lastScore;
	
	private int setting;
	
	private int userId;
	
	private int numberOfCards;
	
	private int numberOfStarred;
	
	private int number;

	public DeckDisplay(Deck deck, int numberOfCards,
			int numberOfStarred, int number) {
		super();
		this.id = deck.getId();
		this.name = deck.getName();
		this.averageScore = deck.getAverageScore();
		this.lastScore = deck.getLastScore();
		this.setting = deck.getSetting();
		this.userId = deck.getUserId();
		this.numberOfCards = numberOfCards;
		this.numberOfStarred = numberOfStarred;
		this.number = number;
	}
	
	public DeckDisplay(Deck deck) {
		super();
		this.id = deck.getId();
		this.name = deck.getName();
		this.averageScore = deck.getAverageScore();
		this.lastScore = deck.getLastScore();
		this.setting = deck.getSetting();
		this.userId = deck.getUserId();
		this.numberOfCards = 0;
		this.numberOfStarred = 0;
		this.number = 0;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAverageScore() {
		return averageScore;
	}

	public void setAverageScore(double averageScore) {
		this.averageScore = averageScore;
	}

	public double getLastScore() {
		return lastScore;
	}

	public void setLastScore(double lastScore) {
		this.lastScore = lastScore;
	}

	public int getSetting() {
		return setting;
	}

	public void setSetting(int setting) {
		this.setting = setting;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getNumberOfCards() {
		return numberOfCards;
	}

	public void setNumberOfCards(int numberOfCards) {
		this.numberOfCards = numberOfCards;
	}

	public int getNumberOfStarred() {
		return numberOfStarred;
	}

	public void setNumberOfStarred(int numberOfStarred) {
		this.numberOfStarred = numberOfStarred;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

}


