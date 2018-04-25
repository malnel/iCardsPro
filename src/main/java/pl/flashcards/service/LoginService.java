package pl.flashcards.service;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import pl.flashcards.Main;
import pl.flashcards.model.User;
import pl.flashcards.util.HibernateUtil;
import pl.flashcards.service.LoginService;

public class LoginService {

	/**
	 * Open Hibernate session and transaction.
	 * Make a DB query to return all users with given login and password.
	 * Save result in list of Users.
	 * If list is empty, then return false.
	 * Else create User object user and assign fields from first result in the list.
	 * Set static loggedInUser as user.
	 * Return true.
	 * @param login
	 * @param password
	 * @return
	 */
	public boolean login(String login, String password) {
		
		Session session = HibernateUtil.getSessionFactory().openSession();
		Transaction trx = session.beginTransaction();
		
		Query query = session.createQuery("FROM User WHERE login=:login and password=:password");
		query.setString("login", login);
		query.setString("password", password);
		
		List<User> list = query.list();
		trx.commit();
		session.close();
		
		if (list.isEmpty()) {
			return false;
		}
		User user = list.get(0);
		Main.setLoggedInUser(user);
		System.out.println("Zalogowano: " + user.getName());
		return true;
	}
	
	
}
