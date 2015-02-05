package com.example.tfg.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.tfg.domain.AcademicTerm;
import com.example.tfg.domain.Course;

@Repository
public interface CourseDao {

	public boolean addCourse(Course course);

	public List<Course> getAll();

	public boolean saveCourse(Course course);

	public Course getCourse(Long id);

	// public List<Course> getCoursesByAcademicTerm(String term);
	public boolean deleteCourse(Long id);

	// public boolean existByAcademicTerm(AcademicTerm aT);
	// public Course getCourseByName(String name);
	// public boolean exist(Course course);
	public boolean exist(Course course);

	public List<Course> getCoursesByAcademicTerm(Long id_academic);

	public Long isDisabled(Long id_academic, Long id_subject);

	public boolean deleteCoursesFromAcademic(AcademicTerm academic);

}
