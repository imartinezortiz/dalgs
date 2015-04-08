package es.ucm.fdi.dalgs.group.web;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import es.ucm.fdi.dalgs.domain.Group;
import es.ucm.fdi.dalgs.group.repository.GroupRepository;

@Component
public class GroupFormatter implements Formatter<Group> {

	@Autowired
	private GroupRepository daoGroup;

	// Some service class which can give the Actor after
	// fetching from Database

	public String print(Group group, Locale arg1) {
		return group.getName();
	}

	public Group parse(String groupId, Locale arg1) throws ParseException {

		return daoGroup.getGroupFormatter(Long.parseLong(groupId));
		// Else you can just return a new object by setting some values
		// which you deem fit.
	}

}
