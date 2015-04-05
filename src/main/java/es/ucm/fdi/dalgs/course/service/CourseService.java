package es.ucm.fdi.dalgs.course.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.dalgs.academicTerm.service.AcademicTermService;
import es.ucm.fdi.dalgs.acl.service.AclObjectService;
import es.ucm.fdi.dalgs.activity.service.ActivityService;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.course.repository.CourseRepository;
import es.ucm.fdi.dalgs.domain.AcademicTerm;
import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.domain.Subject;
import es.ucm.fdi.dalgs.domain.User;
import es.ucm.fdi.dalgs.group.service.GroupService;

@Service
public class CourseService {
	@Autowired
	private CourseRepository daoCourse;

	@Autowired
	private AclObjectService manageAclService;
	
	@Autowired
	ActivityService serviceActivity;
	
	@Autowired
	GroupService serviceGroup;

	@Autowired
	private AcademicTermService serviceAcademicTerm;
	
	@Autowired
	private MessageSource messageSource;

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = false)
	public ResultClass<Course> addCourse(Course course, Long id_academic, Locale locale) {
		
		boolean success = false;
		
		course.setAcademicTerm(serviceAcademicTerm.getAcademicTerm(id_academic,false).getSingleElement());
		Course courseExists = daoCourse.exist(course);
		ResultClass<Course> result = new ResultClass<Course>();

		if( courseExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add(messageSource.getMessage("error.Code", null, locale));

			if (courseExists.getIsDeleted()){
				result.setElementDeleted(true);
				errors.add(messageSource.getMessage("error.deleted", null, locale));
				result.setSingleElement(courseExists);

			}
			result.setSingleElement(course);
			result.setErrorsList(errors);
		}
		else{
		
			success = daoCourse.addCourse(course);
				

				if(success){
					courseExists = daoCourse.exist(course);
					success = manageAclService.addACLToObject(courseExists.getId(), courseExists.getClass().getName());
				
					// 	Adding the authorities to the new coordinator 
					//	Adding the READ permissions in cascade to see through the general view
					manageAclService.addPermissionCASCADE(course.getCoordinator(), course, id_academic, course.getId(),null);

					if (success)result.setSingleElement(course);

				} else {
					throw new IllegalArgumentException(	"Cannot create ACL. Object not set.");
				}
			}
					
		return result;		
	
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	@Transactional(readOnly = true)
	public List<Course> getAll() {
		return daoCourse.getAll();
	}



	@PreAuthorize("hasPermission(#course, 'WRITE') or hasPermission(#course, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> modifyCourse(Course course, Long id_academic, Long id_course, Locale locale) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		
		course.setAcademicTerm(serviceAcademicTerm.getAcademicTerm(id_academic, false).getSingleElement());
		
		Course modifyCourse = daoCourse.getCourse(id_course, id_academic);
		
		Course courseExists = daoCourse.exist(course);
		
		User old_coordinator = modifyCourse.getCoordinator();
		
		if(!course.getSubject().equals(modifyCourse.getSubject()) && 
				courseExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add(messageSource.getMessage("error.newCode", null, locale));

			if (courseExists.getIsDeleted()){
				result.setElementDeleted(true);
				errors.add(messageSource.getMessage("error.deleted", null, locale));

			}
			result.setErrorsList(errors);
			result.setSingleElement(false);
		}
		else{
			modifyCourse.setSubject(course.getSubject());
			modifyCourse.setCoordinator(course.getCoordinator());
			boolean r = daoCourse.saveCourse(modifyCourse);
			if (r) {
				result.setSingleElement(true);
				// Deleting the authorities to the old coordinator
				if(old_coordinator !=null){
					manageAclService.removePermissionCASCADE(modifyCourse.getCoordinator(), modifyCourse, id_academic,id_course, null);
				}
				
				// 	Adding the authorities to the new coordinator 
				//	Adding the READ permissions in cascade to see through the general view
				manageAclService.addPermissionCASCADE(modifyCourse.getCoordinator(), modifyCourse, id_academic,id_course, null);
			}
		}
		return result;
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	@Transactional(readOnly = true)
	public ResultClass<Course> getCourse(Long id, Long id_academic) {
		ResultClass<Course> result = new ResultClass<Course>();
		result.setSingleElement(daoCourse.getCourse(id, id_academic));
		return result;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultClass<Boolean> deleteCourse(Long id, Long id_academic) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		Course course = daoCourse.getCourse(id , id_academic);
		if (serviceActivity.deleteActivitiesFromCourse(course).getSingleElement()){
			result.setSingleElement(daoCourse.deleteCourse(course));
			return result;
		}
		result.setSingleElement(false);
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	public ResultClass<Course> getCoursesByAcademicTerm(Long id_academic, Boolean showAll) {
		ResultClass<Course> result = new ResultClass<>();

		Collection<Course> courses = new ArrayList<Course>();
		courses.addAll(daoCourse.getCoursesByAcademicTerm(id_academic, showAll)); 
		result.addAll(courses);
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")		
	public ResultClass<Boolean> deleteCoursesFromAcademic(AcademicTerm academic) {
		ResultClass<Boolean> result = new ResultClass<>();
		boolean deleteActivities = serviceActivity.deleteActivitiesFromCourses(academic.getCourses()).getSingleElement();
		boolean deleteGroups = serviceGroup.deleteGroupsFromCourses(academic.getCourses()).getSingleElement();
		if (deleteActivities && deleteGroups){
				result.setSingleElement(daoCourse.deleteCoursesFromAcademic(academic));
		}
		else result.setSingleElement(false);
			
		return result;

	}
	


	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	@Transactional(readOnly = true)
	public ResultClass<Course> getCourseAll(Long id, Long id_academic, Boolean showAll) {
		ResultClass<Course> result = new ResultClass<>();
		Course c = daoCourse.getCourse(id, id_academic);
		c.getActivities().addAll(serviceActivity.getActivitiesForCourse(id, showAll));
		c.getGroups().addAll(serviceGroup.getGroupsForCourse(id, showAll));
		
		result.setSingleElement(c);
	
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	public ResultClass<Course> getCoursesfromListAcademic(
			Collection<AcademicTerm> academicList) {
		
		ResultClass<Course> result = new ResultClass<>();
		result.addAll(daoCourse.getCoursesFromListAcademic(academicList));
		
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResultClass<Boolean> deleteCourses(Collection<AcademicTerm> academicList) {
		
		ResultClass<Boolean> result = new ResultClass<>();
		
		Collection<Course> coursesList = daoCourse.getCoursesFromListAcademic(academicList);
		boolean deleteActivities = serviceActivity.deleteActivitiesFromCourses(coursesList).getSingleElement();
		boolean deleteGroups = serviceGroup.deleteGroupsFromCourses(coursesList).getSingleElement();
		if (deleteActivities && deleteGroups){
			result.setSingleElement(daoCourse.deleteCourses(academicList));
		}
		else result.setSingleElement(false);
		
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Course> unDeleteCourse(Course course, Long id_academic, Locale locale) {
		course.setAcademicTerm(serviceAcademicTerm.getAcademicTerm(id_academic, false).getSingleElement());
		Course c = daoCourse.exist(course);
		ResultClass<Course> result = new ResultClass<>();
		if(c == null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add(messageSource.getMessage("error.ElementNoExists", null, locale));
			result.setErrorsList(errors);

		}
		else{
			if(!c.getIsDeleted()){
				Collection<String> errors = new ArrayList<String>();
				errors.add(messageSource.getMessage("error.CodeNoDeleted", null, locale));
				result.setErrorsList(errors);
			}

			c.setDeleted(false);
			c.setSubject(course.getSubject());
			boolean r = daoCourse.saveCourse(c);
			if(r) 
				result.setSingleElement(c);	

		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	public ResultClass<Course> getCoursesBySubject(Subject subject) {
		ResultClass<Course> result = new ResultClass<>();
		result.addAll(daoCourse.getCoursesBySubject(subject)); 
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	public ResultClass<Boolean> deleteCoursesForSubject(Collection<Subject> subjects) {
		ResultClass<Boolean> result = new ResultClass<>();
		result.setSingleElement(daoCourse.deleteCoursesForSubject(subjects));

		return result;
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	public ResultClass<Course> getCourseForCoordinator(Long id_user) {
		ResultClass<Course> result = new ResultClass<>();
		result.addAll(daoCourse.getCoursesByUser(id_user)); 
		return result;
	}
}
