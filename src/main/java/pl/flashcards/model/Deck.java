package pl.flashcards.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Deck {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column
	private String name;
	
	@Column
	private double averageScore;
	
	@Column
	private double lastScore;
	
	@Column
	private int setting;
	
	@Column(name = "user_id")
	private int userId;

	public Deck(int id, String name, double averageScore, double lastScore, int setting, int userId) {
		super();
		this.id = id;
		this.name = name;
		this.averageScore = averageScore;
		this.lastScore = lastScore;
		this.setting = setting;
		this.userId = userId;
	}

	public Deck(String name, double averageScore, double lastScore, int setting, int userId) {
		super();
		this.name = name;
		this.averageScore = averageScore;
		this.lastScore = lastScore;
		this.setting = setting;
		this.userId = userId;
	}
	
	public Deck(int id) {
		super();
		this.id = id;
	}
	
	public Deck() {
		super();
	}
	
	public Deck(String name) {
		super();
		this.name = name;
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

	@Override
	public String toString() {
		return "Deck [id=" + id + ", name=" + name + ", averageScore=" + averageScore + ", lastScore=" + lastScore
				+ ", setting=" + setting + ", userId=" + userId + "]";
	}
	
	

}
