package es.ucm.fdi.dalgs.group.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.course.service.CourseService;
import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.domain.Group;
import es.ucm.fdi.dalgs.group.repository.GroupRepository;

@Service
public class GroupService {

	@Autowired
	private GroupRepository daoGroup;
	
	@Autowired
	private CourseService serviceCourse;
	
	
	@Transactional(readOnly = false)
	public ResultClass<Boolean> addGroup(Group group, Long id_course) {
		
		Group groupExists = daoGroup.existByName(group.getName());
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		if( groupExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code already exists");

			if (groupExists.getIsDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");

			}
			result.setE(false);
			result.setErrorsList(errors);
		}
		else{
			group.setCourse(serviceCourse.getCourse(id_course).getE());
			boolean r = daoGroup.addGroup(group);
			if (r) 
				result.setE(true);
		}
		return result;		
	}

	@Transactional(readOnly = true)
	public ResultClass<Group> getGroup(Long id_group) {
		ResultClass<Group> result = new ResultClass<Group>();
		result.setE(daoGroup.getGroup(id_group));
		return result;
	}

	@Transactional(readOnly = false)
	public ResultClass<Boolean> modifyGroup(Group group, Long id_group) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		Group modifyGroup = daoGroup.getGroup(id_group);
		
		Group groupExists = daoGroup.existByName(group.getName());
		
		if(!group.getName().equalsIgnoreCase(modifyGroup.getName()) && 
				groupExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("New code already exists");

			if (groupExists.getIsDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");

			}
			result.setErrorsList(errors);
			result.setE(false);
		}
		else{
			modifyGroup.setName(group.getName());;
			boolean r = daoGroup.saveGroup(modifyGroup);
			if (r) 
				result.setE(true);
		}
		return result;
	}

	@Transactional(readOnly = false)
	public ResultClass<Boolean> deleteGroup(Long id_group) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		result.setE(daoGroup.deleteGroup(id_group));
		return result;
	}


	public ResultClass<Collection<Group>> getGroupsForCourse(Long id) {
		ResultClass<Collection<Group>> result = new ResultClass<Collection<Group>>();
		result.setE(daoGroup.getGroupsForCourse(id));
		return result;
	}

	
	public ResultClass<Boolean> deleteGroupsFromCourses(Collection<Course> coursesList) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		result.setE(daoGroup.deleteGroupsFromCourses(coursesList));
		return result;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = false)
	public ResultClass<Boolean> unDeleteGroup(Group group) {
		Group g = daoGroup.existByName(group.getName());
		ResultClass<Boolean> result = new ResultClass<Boolean>();
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
				result.setE(true);	

		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostAuthorize("hasPermission(returnObject, 'READ')")
	public ResultClass<Collection<Group>> getGroupsForStudent(Long id_student){
		ResultClass<Collection<Group>> result = new ResultClass<Collection<Group>>();
		result.setE(daoGroup.getGroupsForStudent(id_student));
		return result;
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostAuthorize("hasPermission(returnObject, 'READ')")
	public ResultClass<Collection<Group>> getGroupsForProfessor(Long id_professor){
		ResultClass<Collection<Group>> result = new ResultClass<Collection<Group>>();
		result.setE(daoGroup.getGroupsForProfessor(id_professor));
		return result;
	}

}
