package es.ucm.fdi.dalgs.group.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.dalgs.acl.service.AclObjectService;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.course.service.CourseService;
import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.domain.Group;
import es.ucm.fdi.dalgs.group.repository.GroupRepository;

@Service
public class GroupService {


	@Autowired
	private AclObjectService manageAclService;

	@Autowired
	private GroupRepository daoGroup;

	@Autowired
	private CourseService serviceCourse;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> addGroup(Group group, Long id_course) {

		Group groupExists = daoGroup.existByName(group.getName(), null);
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		if (groupExists != null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code already exists");

			if (groupExists.getIsDeleted()) {
				result.setElementDeleted(true);
				errors.add("Element is deleted");

			}
			result.setE(false);
			result.setErrorsList(errors);
		} else {
			group.setCourse(serviceCourse.getCourse(id_course));
			boolean r = daoGroup.addGroup(group);
			if (r)
				result.setE(true);
		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public Group getGroup(Long id_group) {

		return daoGroup.getGroup(id_group);
	}

	@PreAuthorize("hasPermission(#group, 'WRITE') or hasPermission(#group, 'ADMINISTRATION')")
	// /@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> modifyGroup(Group group) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		if (daoGroup.existByName(group.getName(), group.getId()) != null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("New code already exists");

			if (group.getIsDeleted()) {
				result.setElementDeleted(true);
				errors.add("Element is deleted");

			}
			result.setErrorsList(errors);
			result.setE(false);
		} else {
			boolean r = daoGroup.saveGroup(group);
			if (r) {
				result.setE(true);

				// Adding the authorities to the professor list
				manageAclService.addPermissionToAnObject(group.getProfessors(),group.getCourse().getId(), group.getCourse().getClass().getName());
				manageAclService.addPermissionToAnObject(group.getProfessors(),group.getId(), group.getClass().getName());
			}
		}
		return result;
	}

	// //-----
	// @PreAuthorize("hasPermission(#group, 'WRITE') or hasPermission(#group, 'ADMINISTRATION')")
	// @Transactional(readOnly = false)
	// public ResultClass<Boolean> modifyGroupActivities(Group group) {
	// ResultClass<Boolean> result = new ResultClass<Boolean>();
	//
	// boolean r = daoGroup.saveGroup(group);
	// if (r) result.setE(true);
	//
	// return result;
	// }
	//
	// //----

	@PreAuthorize("hasPermission(#group, 'DELETE') or hasPermission(#group, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public boolean deleteGroup(Long id_group) {

		return daoGroup.deleteGroup(id_group);
	}

	public Collection<Group> getGroupsForCourse(Long id) {
		return daoGroup.getGroupsForCourse(id);
	}

	public boolean deleteGroupsFromCourses(Collection<Course> coursesList) {
		return daoGroup.deleteGroupsFromCourses(coursesList);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> unDeleteGroup(Group group) {
		
		Group g = daoGroup.existByName(group.getName(),null);
		
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		if (g == null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code doesn't exist");
			result.setErrorsList(errors);

		} else {
			if (!g.getIsDeleted()) {
				Collection<String> errors = new ArrayList<String>();
				errors.add("Code is not deleted");
				result.setErrorsList(errors);
			}

			g.setDeleted(false);
			g.setName(group.getName());
			boolean r = daoGroup.saveGroup(g);
			if (r)
				result.setE(true);

		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostAuthorize("hasPermission(returnObject, 'READ')")
	public Collection<Group> getGroupsForStudent(Long id_student) {
		return daoGroup.getGroupsForStudent(id_student);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostAuthorize("hasPermission(returnObject, 'READ')")
	public Collection<Group> getGroupsForProfessor(Long id_professor) {
		return daoGroup.getGroupsForProfessor(id_professor);
	}

}
