package com.example.tfg.service.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
	public boolean addCourse(Course course, Long id_academic) {
		
		AcademicTerm academic = serviceAcademicTerm
				.getAcademicTerm(id_academic);
		course.setAcademicTerm(academic);
		Course existCourse = daoCourse.exist(course);
		if(existCourse == null){
//			course.setAcademicTerm(academic);
			academic.getCourses().add(course);
			return daoCourse.addCourse(course);				
		}
		else if(existCourse.isDeleted()){
			existCourse.setAcademicTerm(academic);
			academic.getCourses().add(existCourse);
			existCourse.setDeleted(false);
			existCourse.setSubject(course.getSubject());
			return daoCourse.saveCourse(existCourse);
		}
		else return false;
		
//		AcademicTerm academic = serviceAcademicTerm
//				.getAcademicTerm(id_academic);
//
//		course.setAcademicTerm(academic);
//		Long aux = daoCourse.isDisabled(course.getAcademicTerm().getId(),
//				course.getSubject().getId());
//		if (aux != null) {
//			course.setId(aux);
//			course.setDeleted(false);
//			return daoCourse.saveCourse(course);
//		} else if (!daoCourse.exist(course))
//			return daoCourse.addCourse(course);
//
//		return false;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public List<Course> getAll() {
		return daoCourse.getAll();
	}


	//yo lo borraria no tiene sentido en este caso el modify
	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = false)
	public boolean modifyCourse(Course course, Long id_academic, Long id_course) {
		Course courseModify = daoCourse.getCourse(id_course);
		
		courseModify.setSubject(course.getSubject());
		
		return daoCourse.saveCourse(courseModify);

			

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


}
