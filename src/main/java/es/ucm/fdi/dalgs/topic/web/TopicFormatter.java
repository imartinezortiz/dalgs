package es.ucm.fdi.dalgs.topic.web;

import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Component;

import es.ucm.fdi.dalgs.domain.Topic;
import es.ucm.fdi.dalgs.topic.repository.TopicDao;

@Component
public class TopicFormatter implements Formatter<Topic>{

	@Autowired
	private TopicDao topicDao;

	// Some service class which can give the Actor after
	// fetching from Database

	
	public String print(Topic topic, Locale arg1) {
		return topic.getInfo().getName();
	}

	
	public Topic parse(String topicId, Locale arg1) throws ParseException {

		return topicDao.getTopic(Long.parseLong(topicId));
		// Else you can just return a new object by setting some values
		// which you deem fit.
	}
}
