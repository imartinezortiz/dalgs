package es.ucm.fdi.dalgs.user.web;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import es.ucm.fdi.dalgs.domain.User;
import es.ucm.fdi.dalgs.user.repository.UserRepository;

@Component
public class UserFormatter implements Formatter<User> {

	@Autowired
	private UserRepository daoUser;

	// Some service class which can give the Actor after
	// fetching from Database

	public String print(User user, Locale arg1) {
		return user.getUsername();
	}

	public User parse(String userId, Locale arg1) throws ParseException {

		return daoUser.getUser(Long.parseLong(userId));
		// Else you can just return a new object by setting some values
		// which you deem fit.
	}

}
