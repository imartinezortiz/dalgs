/**
 * This file is part of D.A.L.G.S.
 *
 * D.A.L.G.S is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * D.A.L.G.S is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with D.A.L.G.S.  If not, see <http://www.gnu.org/licenses/>.
 */
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
import es.ucm.fdi.dalgs.domain.Group;
import es.ucm.fdi.dalgs.domain.Subject;
import es.ucm.fdi.dalgs.domain.User;
import es.ucm.fdi.dalgs.externalActivity.service.ExternalActivityService;
import es.ucm.fdi.dalgs.group.service.GroupService;
import es.ucm.fdi.dalgs.mailbox.service.MailBoxService;

@Service
public class CourseService {
	@Autowired
	private CourseRepository repositoryCourse;

	@Autowired
	private AclObjectService manageAclService;

	@Autowired
	ActivityService serviceActivity;
	
	@Autowired
	ExternalActivityService serviceExternalActivity;

	@Autowired
	GroupService serviceGroup;

	@Autowired
	private AcademicTermService serviceAcademicTerm;

	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private MailBoxService serviceMailBox;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Course> addCourse(Course course, Long id_academic,
			Locale locale) {

		boolean success = false;

		course.setAcademicTerm(serviceAcademicTerm.getAcademicTerm(id_academic,
				false).getSingleElement());
		Course courseExists = repositoryCourse.exist(course);
		ResultClass<Course> result = new ResultClass<Course>();

		if (courseExists != null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add(messageSource.getMessage("error.Code", null, locale));

			if (courseExists.getIsDeleted()) {
				result.setElementDeleted(true);
				errors.add(messageSource.getMessage("error.deleted", null,
						locale));
				result.setSingleElement(courseExists);

			}
			result.setSingleElement(course);
			result.setErrorsList(errors);
		} else {

			success = repositoryCourse.addCourse(course);

			if (success) {
				courseExists = repositoryCourse.exist(course);
				success = manageAclService.addACLToObject(courseExists.getId(),
						courseExists.getClass().getName());

				// Adding the authorities to the new coordinator
				// Adding the READ permissions in cascade to see through the
				// general view
				manageAclService.addPermissionCASCADE(course.getCoordinator(),
						course, id_academic, course.getId(), null);

				if (success)
					result.setSingleElement(course);

			} else {
				throw new IllegalArgumentException(
						"Cannot create ACL. Object not set.");
			}
		}

		return result;

	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	@Transactional(readOnly = true)
	public List<Course> getAll() {
		return repositoryCourse.getAll();
	}

	@PreAuthorize("hasPermission(#course, 'WRITE') or hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> modifyCourse(Course course, Long id_academic,
			Long id_course, Locale locale) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		course.setAcademicTerm(serviceAcademicTerm.getAcademicTerm(id_academic,
				false).getSingleElement());

		Course modifyCourse = repositoryCourse.getCourse(id_course, id_academic);

		Course courseExists = repositoryCourse.exist(course);

		User old_coordinator = modifyCourse.getCoordinator();

		if (!course.getSubject().equals(modifyCourse.getSubject())
				&& courseExists != null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add(messageSource.getMessage("error.newCode", null, locale));

			if (courseExists.getIsDeleted()) {
				result.setElementDeleted(true);
				errors.add(messageSource.getMessage("error.deleted", null,
						locale));

			}
			result.setErrorsList(errors);
			result.setSingleElement(false);
		} else {
			modifyCourse.setSubject(course.getSubject());
			modifyCourse.setCoordinator(course.getCoordinator());
			boolean r = repositoryCourse.saveCourse(modifyCourse);
			if (r) {
				result.setSingleElement(true);
				// Deleting the authorities to the old coordinator
				if (old_coordinator != null) {
					manageAclService.removePermissionCASCADE(
							old_coordinator, modifyCourse,
							id_academic, id_course, null);
					
					/*for (Group g : modifyCourse.getGroups()){
						manageAclService.removePermissionCASCADE(
								old_coordinator, g,
								id_academic, id_course, g.getId());
					}*/
					
				}

				// Adding the authorities to the new coordinator
				// Adding the READ permissions in cascade to see through the
				// general view
				manageAclService.addPermissionCASCADE(
						modifyCourse.getCoordinator(), modifyCourse,
						id_academic, id_course, null);
				
				/*for (Group g : modifyCourse.getGroups()){
					manageAclService.addPermissionCASCADE(
							modifyCourse.getCoordinator(), g,
							id_academic, id_course, g.getId());
				}*/
			}
		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	@Transactional(readOnly = true)
	public ResultClass<Course> getCourse(Long id, Long id_academic) {
		ResultClass<Course> result = new ResultClass<Course>();
		result.setSingleElement(repositoryCourse.getCourse(id, id_academic));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultClass<Boolean> deleteCourse(Long id, Long id_academic) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		Course course = repositoryCourse.getCourse(id, id_academic);
		if (serviceActivity.deleteActivitiesFromCourse(course)
				.getSingleElement()) {
			
			if(course.getCoordinator() !=null)
				manageAclService.removePermissionCASCADE(course.getCoordinator(), course, id_academic, course.getId(), null);
			
			result.setSingleElement(repositoryCourse.deleteCourse(course));
			return result;
		}
		
		
		result.setSingleElement(false);
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	public ResultClass<Course> getCoursesByAcademicTerm(Long id_academic,
			Boolean showAll) {
		ResultClass<Course> result = new ResultClass<>();

		Collection<Course> courses = new ArrayList<Course>();
		courses.addAll(repositoryCourse.getCoursesByAcademicTerm(id_academic, showAll));
		result.addAll(courses);
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResultClass<Boolean> deleteCoursesFromAcademic(AcademicTerm academic) {
		ResultClass<Boolean> result = new ResultClass<>();
		boolean deleteActivities = serviceActivity.deleteActivitiesFromCourses(
				academic.getCourses()).getSingleElement();
		boolean deleteGroups = serviceGroup.deleteGroupsFromCourses(
				academic.getCourses()).getSingleElement();
		if (deleteActivities && deleteGroups) {
			result.setSingleElement(repositoryCourse
					.deleteCoursesFromAcademic(academic));
		} else
			result.setSingleElement(false);

		return result;

	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	@Transactional(readOnly = true)
	public ResultClass<Course> getCourseAll(Long id, Long id_academic,
			Boolean showAll) {
		ResultClass<Course> result = new ResultClass<>();
		Course c = repositoryCourse.getCourse(id, id_academic);
		if (c != null) {
//			c.setActivities(new ArrayList<Activity>());
			c.setGroups(new ArrayList<Group>());
			c.setActivities(
					serviceActivity.getActivitiesForCourse(id, showAll));
			c.getGroups().addAll(serviceGroup.getGroupsForCourse(id, showAll));
		
//			c.getExternal_activities().addAll(serviceExternalActivity.getExternalActivitiesForCourse(c));
		} else
			result.setHasErrors(true);
		
		result.setSingleElement(c);

		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	public ResultClass<Course> getCoursesfromListAcademic(
			Collection<AcademicTerm> academicList) {

		ResultClass<Course> result = new ResultClass<>();
		result.addAll(repositoryCourse.getCoursesFromListAcademic(academicList));

		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResultClass<Boolean> deleteCourses(
			Collection<AcademicTerm> academicList) {

		ResultClass<Boolean> result = new ResultClass<>();

		Collection<Course> coursesList = repositoryCourse
				.getCoursesFromListAcademic(academicList);
		boolean deleteActivities = serviceActivity.deleteActivitiesFromCourses(
				coursesList).getSingleElement();
		boolean deleteGroups = serviceGroup
				.deleteGroupsFromCourses(coursesList).getSingleElement();
		if (deleteActivities && deleteGroups) {
			result.setSingleElement(repositoryCourse.deleteCourses(academicList));
		} else
			result.setSingleElement(false);

		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Course> unDeleteCourse(Course course, Long id_academic,
			Locale locale) {
		course.setAcademicTerm(serviceAcademicTerm.getAcademicTerm(id_academic,
				false).getSingleElement());
		Course c = repositoryCourse.exist(course);
		ResultClass<Course> result = new ResultClass<>();
		if (c == null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add(messageSource.getMessage("error.ElementNoExists", null,
					locale));
			result.setErrorsList(errors);

		} else {
			if (!c.getIsDeleted()) {
				Collection<String> errors = new ArrayList<String>();
				errors.add(messageSource.getMessage("error.CodeNoDeleted",
						null, locale));
				result.setErrorsList(errors);
			}

			c.setDeleted(false);
			c.setSubject(course.getSubject());	
			
			if(course.getCoordinator() !=null){
				manageAclService.addPermissionCASCADE(course.getCoordinator(), course, id_academic, course.getId(), null);
				c.setCoordinator(course.getCoordinator());
			}
			
			boolean r = repositoryCourse.saveCourse(c);
			if (r)
				result.setSingleElement(c);

		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	public ResultClass<Course> getCoursesBySubject(Subject subject) {
		ResultClass<Course> result = new ResultClass<>();
		result.addAll(repositoryCourse.getCoursesBySubject(subject));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResultClass<Boolean> deleteCoursesForSubject(
			Collection<Subject> subjects) {
		ResultClass<Boolean> result = new ResultClass<>();
		result.setSingleElement(repositoryCourse.deleteCoursesForSubject(subjects));

		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	public ResultClass<Course> getCourseForCoordinator(Long id_user) {
		ResultClass<Course> result = new ResultClass<>();
		result.addAll(repositoryCourse.getCoursesByUser(id_user));
		return result;
	}

	public ResultClass<Boolean> updateCourse(Course course) {
		ResultClass<Boolean> result = new ResultClass<>();
		result.setSingleElement(repositoryCourse.saveCourse(course));
		return result;
	}

	public Course getCourseFormatter(Long id) {
		
		return repositoryCourse.getCourseFormatter(id);
	}
	

}
