package com.example.tfg.service;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tfg.classes.ResultClass;
import com.example.tfg.domain.AcademicTerm;
import com.example.tfg.domain.Course;

@Service
public interface CourseService {
	public ResultClass<Boolean> addCourse(Course course, Long id_academic);

	public List<Course> getAll();

	public ResultClass<Boolean> modifyCourse(Course course, Long id_academic, Long id_course);

	public Course getCourse(Long id);

	public boolean deleteCourse(Long id);

	public List<Course> getCoursesByAcademicTerm(Long id_academic);

	public boolean deleteCoursesFromAcademic(AcademicTerm academic);

	public Course getCourseAll(Long id);

	public Collection<Course> getCoursesfromListAcademic(
			Collection<AcademicTerm> academicList);

	public boolean deleteCourses(Collection<AcademicTerm> academicList);

	public boolean modifyCourse(Course course);
	
	public ResultClass<Boolean> unDeleteCourse(Course course);

	

}
