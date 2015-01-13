package com.example.tfg.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.tfg.domain.AcademicTerm;
import com.example.tfg.domain.Course;

@Repository
public interface CourseDao {

	public boolean addCourse(Course course);
	public List<Course> getAll();
    public boolean saveSubject(Course course);
    public Course getCourse(Long id);
    public List<Course> getCoursesByAcademicTerm(AcademicTerm at);
	public boolean deleteCourse(Long id);
	
	public boolean existByName(String name);
	//public boolean existByAcademicTerm(AcademicTerm aT);
	public Course getCourseByName(String name);

}
