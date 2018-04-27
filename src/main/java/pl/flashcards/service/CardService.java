package pl.flashcards.service;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import pl.flashcards.model.Card;
import pl.flashcards.model.Deck;
import pl.flashcards.util.HibernateUtil;

public class CardService {
	
	public List<Card> getAll(Deck deck) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction trx = session.beginTransaction();

		Query query = session.createQuery("SELECT c FROM Card c WHERE c.deckId=:deckId");
		query.setInteger("deckId", deck.getId());
		List<Card> cards = query.list();
		trx.commit();
		session.close();
		return cards;
	}
	
	public int saveCardToDB (Card card) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();

		int id = (int) session.save(card);
		transaction.commit();
		session.close();
		return id;
	}
	
	public void updateCardToDB (Card card) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction trx = session.beginTransaction();

		Query query = session.createQuery("UPDATE Card SET "
				+ "front=:front, "
				+ "back=:back, "
				+ "isStarred=:isStarred, "
				+ "timesPlayed=:timesPlayed, "
				+ "timesAnsweredCorrectly=:timesAnsweredCorrectly, "
				+ "lastAnswerCorrect=:lastAnswerCorrect, "
				+ "skill=:skill WHERE id=:id");
		query.setString("front", card.getFront());
		query.setString("back", card.getBack());
		query.setBoolean("isStarred", card.isStarred());
		query.setInteger("timesPlayed", card.getTimesPlayed());
		query.setInteger("timesAnsweredCorrectly", card.getTimesAnsweredCorrectly());
		query.setBoolean("lastAnswerCorrect", card.isLastAnswerCorrect());
		query.setInteger("skill", card.getSkill());
		query.setInteger("id", card.getId());
		query.executeUpdate();

		trx.commit();
		session.close();
	}
	
	public void deleteCard (Card card) {
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction trx = session.beginTransaction();

		Query query = session.createQuery("DELETE FROM Card WHERE id=:id");
		query.setInteger("id", card.getId());
		query.executeUpdate();
		
		trx.commit();
		session.close();
	}

}
