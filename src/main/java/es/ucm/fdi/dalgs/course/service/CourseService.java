package es.ucm.fdi.dalgs.course.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.dalgs.academicTerm.service.AcademicTermService;
import es.ucm.fdi.dalgs.activity.service.ActivityService;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.course.repository.CourseRepository;
import es.ucm.fdi.dalgs.domain.AcademicTerm;
import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.group.service.GroupService;

@Service
public class CourseService {
	@Autowired
	private CourseRepository daoCourse;

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
		
		course.setAcademicTerm(serviceAcademicTerm.getAcademicTerm(id_academic).getE());
		Course courseExists = daoCourse.exist(course);
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		if( courseExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code already exists");

			if (courseExists.getIsDeleted()){
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
		
		course.setAcademicTerm(serviceAcademicTerm.getAcademicTerm(id_academic).getE());
		
		Course modifyCourse = daoCourse.getCourse(id_course);
		
		Course courseExists = daoCourse.exist(course);
		
		if(!course.getSubject().equals(modifyCourse.getSubject()) && 
				courseExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("New code already exists");

			if (courseExists.getIsDeleted()){
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
	public ResultClass<Course> getCourse(Long id) {
		ResultClass<Course> result = new ResultClass<Course>();
		result.setE(daoCourse.getCourse(id));
		return result;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultClass<Boolean> deleteCourse(Long id) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		Course course = daoCourse.getCourse(id);
		if (serviceActivity.deleteActivitiesFromCourse(course).getE()){
			result.setE(daoCourse.deleteCourse(id));
			return result;
		}
		result.setE(false);
		return result;
	}

	
	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<List<Course>> getCoursesByAcademicTerm(Long id_academic) {
		ResultClass<List<Course>> result = new ResultClass<List<Course>>();
		result.setE(daoCourse.getCoursesByAcademicTerm(id_academic));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	public boolean deleteCoursesFromAcademic(AcademicTerm academic) {
		boolean deleteActivities = serviceActivity.deleteActivitiesFromCourses(academic.getCourses()).getE();
		boolean deleteGroups = serviceGroup.deleteGroupsFromCourses(academic.getCourses()).getE();
		if (deleteActivities && deleteGroups)
			return daoCourse.deleteCoursesFromAcademic(academic);
		else return false;

	}
	


	@Transactional(readOnly = true)
	@PreAuthorize("hasRole('ROLE_USER')")	
	public Course getCourseAll(Long id) {
		Course c = daoCourse.getCourse(id);
		c.setActivities(serviceActivity.getActivitiesForCourse(id).getE());
		c.setGroups(serviceGroup.getGroupsForCourse(id).getE());
		
	
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
		boolean deleteActivities = serviceActivity.deleteActivitiesFromCourses(coursesList).getE();
		boolean deleteGroups = serviceGroup.deleteGroupsFromCourses(coursesList).getE();
		if (deleteActivities && deleteGroups)
		return daoCourse.deleteCourses(academicList);
		else return false;
	}


	public ResultClass<Boolean> modifyCourse(Course course) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		result.setE(daoCourse.saveCourse(course));
		return result;
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
			if(!c.getIsDeleted()){
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
