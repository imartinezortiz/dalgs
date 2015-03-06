package es.ucm.fdi.dalgs.course.web;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import es.ucm.fdi.dalgs.course.repository.CourseRepository;
import es.ucm.fdi.dalgs.domain.Course;

@Component
public class CourseFormatter implements Formatter<Course> {

	@Autowired
	private CourseRepository courseService;

	// Some service class which can give the Actor after
	// fetching from Database

	@Override
	public String print(Course course, Locale arg1) {

		return course.getSubject().getInfo().getName();
	}

	@Override
	public Course parse(String courseId, Locale arg1) throws ParseException {
		return courseService.getCourse(Long.parseLong(courseId));
		// Else you can just return a new object by setting some values
		// which you deem fit.
	}
}
