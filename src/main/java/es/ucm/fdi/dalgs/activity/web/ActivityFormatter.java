package es.ucm.fdi.dalgs.activity.web;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import es.ucm.fdi.dalgs.activity.repository.ActivityRepository;
import es.ucm.fdi.dalgs.domain.Activity;

@Component
public class ActivityFormatter implements Formatter<Activity> {

	@Autowired
	private ActivityRepository activityDao;

	// Some service class which can give the Actor after
	// fetching from Database

	public String print(Activity activity, Locale arg1) {
		return activity.getInfo().getName();
	}

	public Activity parse(String activityId, Locale arg1) throws ParseException {

		return activityDao.getActivity(Long.parseLong(activityId));
		// Else you can just return a new object by setting some values
		// which you deem fit.
	}
}
