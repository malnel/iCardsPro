package pl.flashcards.service;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import pl.flashcards.Main;
import pl.flashcards.model.Card;
import pl.flashcards.model.Deck;
import pl.flashcards.model.DeckDisplay;
import pl.flashcards.util.HibernateUtil;

public class DeckService {
	
	public List<Deck> getAll() {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction trx = session.beginTransaction();

		Query query = session.createQuery("SELECT d FROM Deck d WHERE userId=:userId");
		query.setInteger("userId", Main.getLoggedInUser().getId());
		List<Deck> decks = query.list();
		trx.commit();
		session.close();
		return decks;
	}
	
	public List<DeckDisplay> getDeckDisplayAll(List <Deck> decks) {
		List<DeckDisplay> deckDisplays = new ArrayList<>();
		for (int i = 0; i<decks.size(); i++) {
			deckDisplays.add(i, new DeckDisplay(decks.get(i)));
			deckDisplays.get(i).setNumberOfCards(countNumberOfCards(deckDisplays.get(i)));
			deckDisplays.get(i).setNumberOfStarred(countNumberOfStarred(deckDisplays.get(i)));
		}
		return deckDisplays;
	}
	
	public int countNumberOfCards(DeckDisplay deckDisplay) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction trx = session.beginTransaction();

		Query query = session.createQuery("FROM Card WHERE deckId=:deckId");
		query.setInteger("deckId", deckDisplay.getId());
		List<Card> cards = query.list();
		trx.commit();
		session.close();
		
		int numberOfCards = cards.size();
		return numberOfCards;
	}
	
	public int countNumberOfCards(Deck deck) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction trx = session.beginTransaction();

		Query query = session.createQuery("FROM Card WHERE deckId=:deckId");
		query.setInteger("deckId", deck.getId());
		List<Card> cards = query.list();
		trx.commit();
		session.close();
		
		int numberOfCards = cards.size();
		return numberOfCards;
	}
	
	public int countNumberOfStarred(DeckDisplay deckDisplay) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction trx = session.beginTransaction();

		Query query = session.createQuery("FROM Card WHERE deckId=:deckId AND isStarred=true");
		query.setInteger("deckId", deckDisplay.getId());
		List<Card> cards = query.list();
		trx.commit();
		session.close();
		
		int numberOfStarred = cards.size();
		return numberOfStarred;
	}
	
	public int countNumberOfStarred(Deck deck) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction trx = session.beginTransaction();

		Query query = session.createQuery("FROM Card WHERE deckId=:deckId AND isStarred=true");
		query.setInteger("deckId", deck.getId());
		List<Card> cards = query.list();
		trx.commit();
		session.close();
		
		int numberOfStarred = cards.size();
		return numberOfStarred;
	}
	
	public int save(Deck deck) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();

		int id = (int) session.save(deck);
		transaction.commit();
		session.close();
		return id;
	}
	
	public void deleteDeck(Deck deck) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction trx = session.beginTransaction();

		Query query = session.createQuery("DELETE FROM Card WHERE deck_id=:deck_id");
		query.setInteger("deck_id", deck.getId());
		query.executeUpdate();
		
		Query query2 = session.createQuery("DELETE FROM Deck WHERE id=:id");
		query2.setInteger("id", deck.getId());
		query2.executeUpdate();

		trx.commit();
		session.close();
	}
	
	public void setDeckName(Deck deck, String name) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction trx = session.beginTransaction();

		Query query = session.createQuery("UPDATE Deck SET name=:name WHERE id=:deck_id");
		query.setString("name", name);
		query.setInteger("deck_id", deck.getId());
		query.executeUpdate();

		trx.commit();
		session.close();
	}
	
	public Deck selectDeck(Deck deck) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction trx = session.beginTransaction();

		Query query = session.createQuery("FROM Deck WHERE id=:selectedDeckId");
		query.setInteger("selectedDeckId", deck.getId());
		List<Deck> selectedDecks = query.list();
		Deck selectedDeck = selectedDecks.get(0);
		trx.commit();
		session.close();
		
		return selectedDeck;
	}
	
	public void updateDeckToDB(Deck deck) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction trx = session.beginTransaction();

		Query query = session.createQuery("UPDATE Deck SET "
				+ "name=:name, "
				+ "averageScore=:averageScore, "
				+ "lastScore=:lastScore, "
				+ "setting=:setting WHERE id=:id");
		query.setString("name", deck.getName());
		query.setDouble("averageScore", deck.getAverageScore());
		query.setDouble("lastScore", deck.getLastScore());
		query.setInteger("setting", deck.getSetting());
		query.setInteger("id", deck.getId());
		query.executeUpdate();

		trx.commit();
		session.close();
	}
	
//	public void update(DeckDisplay deckDisplay) {
//		Deck deck = new Deck(deckDisplay.getId());
//	
//		Session session = HibernateUtil.getSessionFactory().openSession();
//		Transaction transaction = session.beginTransaction();
//
//		session.saveOrUpdate(deck);
//		transaction.commit();
//		session.close();
//	}
	
}
