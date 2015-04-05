package es.ucm.fdi.dalgs.group.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.dalgs.acl.service.AclObjectService;
import es.ucm.fdi.dalgs.activity.service.ActivityService;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.course.service.CourseService;
import es.ucm.fdi.dalgs.domain.Activity;
import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.domain.Group;
import es.ucm.fdi.dalgs.domain.User;
import es.ucm.fdi.dalgs.group.repository.GroupRepository;
import es.ucm.fdi.dalgs.user.service.UserService;

@Service
public class GroupService {

	@Autowired
	private AclObjectService manageAclService;

	@Autowired
	private GroupRepository daoGroup;

	@Autowired
	private CourseService serviceCourse;

	@Autowired
	private ActivityService serviceActivity;

	@Autowired
	private UserService serviceUser;

	@Autowired
	private MessageSource messageSource;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Group> addGroup(Group group, Long id_course, Long id_academic,
			Locale locale) {

		boolean success = false;

		Group groupExists = daoGroup.existInCourse(id_course,group.getName());
		ResultClass<Group> result = new ResultClass<>();

		if (groupExists != null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add(messageSource.getMessage("error.Code", null, locale));

			if (groupExists.getIsDeleted()) {
				result.setElementDeleted(true);
				errors.add(messageSource.getMessage("error.deleted", null,
						locale));
				result.setSingleElement(groupExists);
			} else
				result.setSingleElement(group);
			result.setErrorsList(errors);
		} else {
			group.setCourse(serviceCourse.getCourse(id_course, id_academic)
					.getSingleElement());
			success = daoGroup.addGroup(group);

			if (success) {
				groupExists = daoGroup.existInCourse(id_course,group.getName());
				success = manageAclService.addACLToObject(groupExists.getId(),
						groupExists.getClass().getName());
				if (success)
					result.setSingleElement(group);

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
	public ResultClass<Group> getGroup(Long id_group, Long id_course, Long id_academic) {
		ResultClass<Group> result = new ResultClass<Group>();
		result.setSingleElement(daoGroup.getGroup(id_group, id_course, id_academic));
		return result;
	}

	@PreAuthorize("hasPermission(#group, 'WRITE') or hasPermission(#group, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> modifyGroup(Group group, Long id_group,Long id_course, Long id_academic,
			Locale locale) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		Group modifyGroup = daoGroup.getGroup(id_group,  id_course, id_academic);

		Group groupExists = daoGroup.existInCourse(id_course, group.getName());

		if (!group.getName().equalsIgnoreCase(modifyGroup.getName())
				&& groupExists != null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add(messageSource.getMessage("error.newCode", null, locale));

			if (groupExists.getIsDeleted()) {
				result.setElementDeleted(true);
				errors.add(messageSource.getMessage("error.deleted", null,
						locale));

			}
			result.setErrorsList(errors);
			result.setSingleElement(false);
		} else {
			modifyGroup.setName(group.getName());
			boolean r = daoGroup.saveGroup(modifyGroup);
			if (r) {
				result.setSingleElement(true);

			}
		}
		return result;
	}

	// -----
	@PreAuthorize("hasPermission(#group, 'WRITE') or hasPermission(#group, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> modifyGroupActivities(Group group) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		boolean r = daoGroup.saveGroup(group);
		if (r)
			result.setSingleElement(true);

		return result;
	}

	// ----

	@PreAuthorize("hasPermission(#group, 'DELETE') or hasPermission(#group, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> deleteGroup(Group group) {

		ResultClass<Boolean> result = new ResultClass<Boolean>();

		if (serviceActivity.deleteActivitiesFromGroup(group).getSingleElement()) {
			manageAclService.removePermissionCollectionCASCADE(group
					.getStudents(), group, group.getCourse().getAcademicTerm()
					.getId(), group.getCourse().getId(), group.getId());
			manageAclService.removePermissionCollectionCASCADE(group
					.getProfessors(), group, group.getCourse()
					.getAcademicTerm().getId(), group.getCourse().getId(),
					group.getId());
			result.setSingleElement(daoGroup.deleteGroup(group));

			return result;
		}
		result.setSingleElement(false);
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	@Transactional(readOnly = true)
	public ResultClass<Group> getGroupsForCourse(Long id, Boolean showAll) {
		ResultClass<Group> result = new ResultClass<>();
		result.addAll(daoGroup.getGroupsForCourse(id, showAll));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> deleteGroupsFromCourses(
			Collection<Course> coursesList) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		for (Course course : coursesList)
			for (Group group : course.getGroups()) {
				manageAclService.removePermissionCollectionCASCADE(group
						.getStudents(), group,
						course.getAcademicTerm().getId(), course.getId(), group
								.getId());
				manageAclService.removePermissionCollectionCASCADE(group
						.getProfessors(), group, course.getAcademicTerm()
						.getId(), course.getId(), group.getId());

			}
		result.setSingleElement(daoGroup.deleteGroupsFromCourses(coursesList));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Group> unDeleteGroup(Group group, Long id_course, Locale locale) {

		Group g = daoGroup.existInCourse(id_course,group.getName());

		ResultClass<Group> result = new ResultClass<>();
		if (g == null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add(messageSource.getMessage("error.ElementNoExists", null,
					locale));
			result.setErrorsList(errors);

		} else {
			if (!g.getIsDeleted()) {
				Collection<String> errors = new ArrayList<String>();
				errors.add(messageSource.getMessage("error.CodeNoDeleted",
						null, locale));
				result.setErrorsList(errors);
			}

			g.setDeleted(false);
			g.setName(group.getName());
			boolean r = daoGroup.saveGroup(g);

			if (r)
				result.setSingleElement(g);

		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	@Transactional(readOnly = true)
	public ResultClass<Group> getGroupsForStudent(Long id_student) {
		ResultClass<Group> result = new ResultClass<>();
		Collection<Group> groups = daoGroup.getGroupsForStudent(id_student);

		if (groups != null)
			result.addAll(groups);
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	@Transactional(readOnly = true)
	public ResultClass<Group> getGroupsForProfessor(Long id_professor) {
		ResultClass<Group> result = new ResultClass<>();

		Collection<Group> groups = daoGroup.getGroupsForProfessor(id_professor);
		if (groups != null)
			result.addAll(groups);
		return result;
	}

	@PreAuthorize("hasPermission(#group, 'WRITE') or hasPermission(#group, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> setProfessors(Group group, Long id_group,
			Long id_course, Long id_academic) {
		ResultClass<Boolean> result = new ResultClass<>();

		Group modifyGroup = daoGroup.getGroup(id_group, id_course, id_academic);

		Collection<User> old_professors = modifyGroup.getProfessors(); // To
																		// delete
																		// old
																		// ACL
																		// permissions
		modifyGroup.setProfessors(group.getProfessors());

		result.setHasErrors(!daoGroup.saveGroup(modifyGroup));

		result.setSingleElement(result.hasErrors());

		if (!result.hasErrors()) {
			result.setSingleElement(true);

			// Deleting the authorities to the old professor list
			if (!old_professors.isEmpty()) {
				manageAclService.removePermissionCollectionCASCADE(
						old_professors, modifyGroup, id_academic, id_course,
						id_group);
			}
			// Adding the authorities to the professor list
			// Adding the READ permissions in cascade to see through the general
			// view
			manageAclService.addPermissionCollectionCASCADE(
					modifyGroup.getProfessors(), modifyGroup, id_academic,
					id_course, id_group);
		}
		return result;
	}

	@PreAuthorize("hasPermission(#group, 'WRITE') or hasPermission(#group, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> setStudents(Group group, Long id_group,
			Long id_course, Long id_academic) {
		ResultClass<Boolean> result = new ResultClass<>();
		Group modifyGroup = daoGroup.getGroup(id_group, id_course, id_academic);
		Collection<User> old_students = modifyGroup.getStudents();

		modifyGroup.setStudents(group.getStudents());

		result.setHasErrors(!daoGroup.saveGroup(modifyGroup));
		result.setSingleElement(result.hasErrors());

		if (!result.hasErrors()) {
			result.setSingleElement(true);

			// Deleting the authorities to the old students list
			if (!old_students.isEmpty()) {
				manageAclService.removePermissionCollectionCASCADE(
						old_students, modifyGroup, id_academic, id_course,
						id_group);
			}
			manageAclService.addPermissionCollectionCASCADE(
					modifyGroup.getStudents(), modifyGroup, id_academic,
					id_course, id_group);
		}

		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> deleteUserGroup(Long id_group, Long id_user,
			Long id_course, Long id_academic, Locale locale) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		Group g = daoGroup.getGroup(id_group, id_course, id_academic);

		User u = serviceUser.getUser(id_user);
		if (serviceUser.hasRole(u, "ROLE_PROFESSOR")
				&& !serviceUser.hasRole(u, "ROLE_COORDINATOR")) {

			g.getProfessors().remove(serviceUser.getUser(id_user));
			result = this.modifyGroup(g, id_group, id_course,id_academic, locale);
		} else if (serviceUser.hasRole(u, "ROLE_STUDENT")) {
			g.getStudents().remove(serviceUser.getUser(id_user));
			result = this.modifyGroup(g, id_group,id_course, id_academic,locale);
		}
		if (!result.hasErrors()) {
			// Removing the authorities to the student
			manageAclService.removePermissionCASCADE(u, g, id_academic,id_course, id_group);
		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public ResultClass<Group> copyGroup(Group group, Long id_course,
			Locale locale) {

		ResultClass<Group> result = new ResultClass<Group>();
		Group copy = group.depth_copy();

		if (copy == null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Copy doesn't work");
			result.setErrorsList(errors);

		} else {

			copy.setName(copy.getName() + " (copy)");

			for (Activity a : copy.getActivities()) {
				a.getInfo().setCode(a.getInfo().getCode() + " (copy)");
			}

			boolean success = daoGroup.addGroup(copy);
			if (success) {
				Group exists = daoGroup.existInCourse(id_course, copy.getName());

				if (exists != null) {
					result.setSingleElement(exists);
					manageAclService.addACLToObject(exists.getId(), exists.getClass().getName());

					for (Activity a : exists.getActivities()) {
						success = success && manageAclService.addACLToObject(a.getId(), a.getClass().getName());

					}
				}
			}

			result.setHasErrors(!success);

		}
		return result;

	}
}
