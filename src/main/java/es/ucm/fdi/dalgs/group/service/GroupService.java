package es.ucm.fdi.dalgs.group.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.dalgs.acl.service.AclObjectService;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.course.service.CourseService;
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
	private UserService serviceUser;

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = false)
	public ResultClass<Group> addGroup(Group group, Long id_course) {

		boolean success = false;

		Group groupExists = daoGroup.existByName(group.getName());
		ResultClass<Group> result = new ResultClass<>();

		if( groupExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code already exists");

			if (groupExists.getIsDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");
				result.setSingleElement(groupExists);
			}
			else result.setSingleElement(group);
			result.setErrorsList(errors);
		}
		else{
			group.setCourse(serviceCourse.getCourse(id_course).getSingleElement());
			success = daoGroup.addGroup(group);


			if(success){
				groupExists = daoGroup.existByName(group.getName());
				success = manageAclService.addACLToObject(groupExists.getId(), groupExists.getClass().getName());
				if (success) result.setSingleElement(group);


			}
			else{
				throw new IllegalArgumentException(	"Cannot create ACL. Object not set.");

			}
		}
		return result;		
	}

	@PostFilter("hasPermission(filterObject, 'READ') or hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = true)
	public ResultClass<Group> getGroup(Long id_group) {
		ResultClass<Group> result = new ResultClass<Group>();
		result.setSingleElement(daoGroup.getGroup(id_group));
		return result;
	}

	@PreAuthorize("hasPermission(#group, 'WRITE') or hasPermission(#group, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> modifyGroup(Group group, Long id_group) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		Group modifyGroup = daoGroup.getGroup(id_group);

		Group groupExists = daoGroup.existByName(group.getName());

		if (!group.getName().equalsIgnoreCase(modifyGroup.getName()) && groupExists != null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("New code already exists");

			if (groupExists.getIsDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");

			}
			result.setErrorsList(errors);
			result.setSingleElement(false);
		}
		else{
			modifyGroup.setName(group.getName());
			boolean r = daoGroup.saveGroup(modifyGroup);
			if (r) {
				result.setSingleElement(true);

			}
		}
		return result;
	}

	//-----
	@PreAuthorize("hasPermission(#group, 'WRITE') or hasPermission(#group, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> modifyGroupActivities(Group group) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		boolean r = daoGroup.saveGroup(group);
		if (r) 	result.setSingleElement(true);

		return result;
	}

	//----

	@PreAuthorize("hasPermission(#group, 'DELETE') or hasPermission(#group, 'ADMINISTRATION')" )
	@Transactional(readOnly = false)
	public ResultClass<Boolean> deleteGroup(Group group) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		result.setSingleElement(daoGroup.deleteGroup(group));
		return result;
	}

	@PostFilter("hasPermission(filterObject, 'READ') or hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = true)
	public ResultClass<Group> getGroupsForCourse(Long id, Boolean showAll) {
		ResultClass<Group> result = new ResultClass<>();
		result.addAll(daoGroup.getGroupsForCourse(id,showAll));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = false)
	public ResultClass<Boolean> deleteGroupsFromCourses(Collection<Course> coursesList) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		result.setSingleElement(daoGroup.deleteGroupsFromCourses(coursesList));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = false)
	public ResultClass<Group> unDeleteGroup(Group group) {

		Group g = daoGroup.existByName(group.getName());

		ResultClass<Group> result = new ResultClass<>();
		if(g == null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code doesn't exist");
			result.setErrorsList(errors);

		}
		else{
			if(!g.getIsDeleted()){
				Collection<String> errors = new ArrayList<String>();
				errors.add("Code is not deleted");
				result.setErrorsList(errors);
			}

			g.setDeleted(false);
			g.setName(group.getName());
			boolean r = daoGroup.saveGroup(g);
			if(r) 
				result.setSingleElement(g);	

		}
		return result;
	}

	@PostFilter("hasPermission(filterObject, 'READ') or hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = true)
	public ResultClass<Group> getGroupsForStudent(Long id_student){
		ResultClass<Group> result = new ResultClass<>();
		Collection<Group> groups = daoGroup.getGroupsForStudent(id_student);

		if(groups!=null)
			result.addAll(groups);
		result.addAll(groups);
		return result;
	}

	@PostFilter("hasPermission(filterObject, 'READ') or hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = true)
	public ResultClass<Group> getGroupsForProfessor(Long id_professor){
		ResultClass<Group> result = new ResultClass<>();
		
		Collection<Group> groups = daoGroup.getGroupsForProfessor(id_professor);
		if(groups!=null)
		result.addAll(groups);
		return result;
	}

	@PreAuthorize("hasPermission(#group, 'WRITE') or hasPermission(#group, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> setProfessors(Group group, Long id_group) {
		ResultClass<Boolean> result = new ResultClass<>();
		Group modifyGroup = daoGroup.getGroup(id_group);
		
		Collection<User> old_professors = modifyGroup.getProfessors();  //To delete old ACL permissions
		
		result.setHasErrors(!daoGroup.saveGroup(modifyGroup));
		modifyGroup.setProfessors(group.getProfessors());
		result.setSingleElement(result.hasErrors());

		
		if (!result.hasErrors()) {
			result.setSingleElement(true);
			
			// Deleting the authorities to the old professor list
			if(!old_professors.isEmpty()) 
				manageAclService.removePermissionToAnObjectCollection_ADMINISTRATION(old_professors, modifyGroup.getId(), modifyGroup.getClass().getName());
			// Adding the authorities to the professor list
			manageAclService.addPermissionToAnObjectCollection_ADMINISTRATION(modifyGroup.getProfessors(), modifyGroup.getId(), modifyGroup.getClass().getName());
			
		}
		

		return result;

	}
	
	@PreAuthorize("hasPermission(#group, 'WRITE') or hasPermission(#group, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> setStudents(Group group, Long id_group) {
		ResultClass<Boolean> result = new ResultClass<>();
		Group modifyGroup = daoGroup.getGroup(id_group);
		Collection<User> old_students = modifyGroup.getStudents();
		
		result.setHasErrors(!daoGroup.saveGroup(modifyGroup));
		modifyGroup.setStudents(group.getStudents());
		result.setSingleElement(result.hasErrors());

		
		if (!result.hasErrors()) {
			result.setSingleElement(true);
			
			// Deleting the authorities to the old students list
			if(!old_students.isEmpty()) 
				manageAclService.removePermissionToAnObjectCollection_READ(old_students, modifyGroup.getId(), modifyGroup.getClass().getName());
			// Adding the authorities to the students list
			manageAclService.addPermissionToAnObjectCollection_READ(modifyGroup.getStudents(), modifyGroup.getId(), modifyGroup.getClass().getName());
			//TODO Añadir permisos necesarios para ver 
		}
		
		return result;	
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> deleteUserGroup(Long id_group,Long id_user) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		Group g = daoGroup.getGroup(id_group);
		
		//TODO
		g.getProfessors().remove(serviceUser.getUser(id_user));
		
		result = this.modifyGroup(g, id_group);
		
		if(!result.hasErrors()){
			User u = serviceUser.getUser(id_user);
			if(serviceUser.hasRole(u, "ROLE_PROFESSOR") || serviceUser.hasRole(u, "ROLE_COORDINATOR") ){
				// Adding the authorities to the professor list
				manageAclService.removePermissionToAnObject_ADMINISTRATION(u, id_group, Group.class.getName());
			}
			else if(serviceUser.hasRole(u, "ROLE_STUDENT")){
				manageAclService.removePermissionToAnObject_READ(u, id_group, Group.class.getName());

			}
		}
		return result;
	}


}



