package com.example.tfg.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tfg.domain.AcademicTerm;
import com.example.tfg.domain.Course;

@Service
public interface CourseService {
	public boolean addCourse(Course course);
	public List<Course> getAll();
	public boolean modifyCourse(Course course);
	public  Course getCourse(Long id);
	public boolean deleteCourse(Long id);
   
	//public List<Course> getCoursesByAcademicTerm(String term);
	
	public List<Course> getCoursesByAcademicTerm(Long id_academic);


}
