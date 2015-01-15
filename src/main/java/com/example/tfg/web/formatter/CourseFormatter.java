package com.example.tfg.web.formatter;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

import com.example.tfg.domain.Course;
import com.example.tfg.repository.CourseDao;

@Component
public class CourseFormatter implements Formatter<Course> {

	     @Autowired
	     private CourseDao courseService;
	     //Some service class which can give the Actor after
	     //fetching from Database
	 
	     @Override
	     public String print(Course course, Locale arg1) {
	          
	    	 return course.getSubject().getName();
	     }
	 
	     @Override
	      public Course parse(String courseId, Locale arg1) throws ParseException {
	           return courseService.getCourse(Long.parseLong(courseId));
	           //Else you can just return a new object by setting some values
	           //which you deem fit.
	      }
}
