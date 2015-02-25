package com.example.tfg.service.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.tfg.classes.ResultClass;
import com.example.tfg.domain.AcademicTerm;
import com.example.tfg.domain.Course;
import com.example.tfg.repository.CourseDao;
import com.example.tfg.service.AcademicTermService;
import com.example.tfg.service.ActivityService;
import com.example.tfg.service.CourseService;
import com.example.tfg.service.GroupService;

@Service
public class CourseServiceImp implements CourseService {

	@Autowired
	private CourseDao daoCourse;

	@Autowired
	ActivityService serviceActivity;
	
	@Autowired
	GroupService serviceGroup;

	// @Autowired
	// private DegreeService serviceDegree;

	@Autowired
	private AcademicTermService serviceAcademicTerm;

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = false)
	public ResultClass<Boolean> addCourse(Course course, Long id_academic) {
		
		course.setAcademicTerm(serviceAcademicTerm.getAcademicTerm(id_academic));
		Course courseExists = daoCourse.exist(course);
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		if( courseExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code already exists");

			if (courseExists.isDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");

			}
			result.setE(false);
			result.setErrorsList(errors);
		}
		else{
		
			boolean r = daoCourse.addCourse(course);
			if (r) 
				result.setE(true);
		}
		return result;		
	
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public List<Course> getAll() {
		return daoCourse.getAll();
	}


	//yo lo borraria no tiene sentido en este caso el modify
	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = false)
	public ResultClass<Boolean> modifyCourse(Course course, Long id_academic, Long id_course) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		
		course.setAcademicTerm(serviceAcademicTerm.getAcademicTerm(id_academic));
		
		Course modifyCourse = daoCourse.getCourse(id_course);
		
		Course courseExists = daoCourse.exist(course);
		
		if(!course.getSubject().equals(modifyCourse.getSubject()) && 
				courseExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("New code already exists");

			if (courseExists.isDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");

			}
			result.setErrorsList(errors);
			result.setE(false);
		}
		else{
			modifyCourse.setSubject(course.getSubject());
			boolean r = daoCourse.saveCourse(modifyCourse);
			if (r) 
				result.setE(true);
		}
		return result;

			

	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = false)
	public Course getCourse(Long id) {
		return daoCourse.getCourse(id);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteCourse(Long id) {
		Course course = daoCourse.getCourse(id);
		if (serviceActivity.deleteActivitiesFromCourse(course))
			return daoCourse.deleteCourse(id);
		return false;
	}

	
	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public List<Course> getCoursesByAcademicTerm(Long id_academic) {

		return daoCourse.getCoursesByAcademicTerm(id_academic);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	public boolean deleteCoursesFromAcademic(AcademicTerm academic) {
		boolean deleteActivities = serviceActivity.deleteActivitiesFromCourses(academic.getCourses());
		boolean deleteGroups = serviceGroup.deleteGroupsFromCourses(academic.getCourses());
		if (deleteActivities && deleteGroups)
			return daoCourse.deleteCoursesFromAcademic(academic);
		else return false;

	}
	


	@Transactional(readOnly = true)
	@PreAuthorize("hasRole('ROLE_USER')")	
	public Course getCourseAll(Long id) {
		Course c = daoCourse.getCourse(id);
		c.setActivities(serviceActivity.getActivitiesForCourse(id));
		c.setGroups(serviceGroup.getGroupsForCourse(id));
		
	
		return c;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	public Collection<Course> getCoursesfromListAcademic(
			Collection<AcademicTerm> academicList) {
			
		return daoCourse.getCoursesFromListAcademic(academicList);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	public boolean deleteCourses(Collection<AcademicTerm> academicList) {
		Collection<Course> coursesList = daoCourse.getCoursesFromListAcademic(academicList);
		boolean deleteActivities = serviceActivity.deleteActivitiesFromCourses(coursesList);
		boolean deleteGroups = serviceGroup.deleteGroupsFromCourses(coursesList);
		if (deleteActivities && deleteGroups)
		return daoCourse.deleteCourses(academicList);
		else return false;
	}


	public boolean modifyCourse(Course course) {

		return daoCourse.saveCourse(course);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = false)
	public ResultClass<Boolean> unDeleteCourse(Course course) {
		Course c = daoCourse.exist(course);
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		if(c == null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code doesn't exist");
			result.setErrorsList(errors);

		}
		else{
			if(!c.isDeleted()){
				Collection<String> errors = new ArrayList<String>();
				errors.add("Code is not deleted");
				result.setErrorsList(errors);
			}

			c.setDeleted(false);
			c.setSubject(course.getSubject());
			boolean r = daoCourse.saveCourse(c);
			if(r) 
				result.setE(true);	

		}
		return result;
	}
}
