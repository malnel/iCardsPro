package pl.flashcards.service;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import pl.flashcards.model.User;
import pl.flashcards.util.HibernateUtil;

public class SigninService {
	
	/**
	 * Open Hibernate session and transaction.
	 * Save user to DB. Return generated id.
	 * 
	 * @param user
	 * @return
	 */
	public int save (User user) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction transaction = session.beginTransaction();
		
		int id = (int) session.save(user);
		transaction.commit();
		session.close();
		return id;
	}
	
	/**
	 * Open Hibernate session and transaction.
	 * Query DB to see if login already exists.
	 * 
	 * @param user
	 * @return
	 */
	public boolean isExistingLogin(String login) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction trx = session.beginTransaction();
		
		Query query = session.createQuery("FROM User WHERE login=:login");
		query.setString("login", login);
		
		List<User> list = query.list();
		trx.commit();
		session.close();
		
		if (list.isEmpty()) {
			return false;
		}
		User user = list.get(0);
		return true;
	}

}
