package com.example.tfg.service.implementation;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tfg.classes.ResultClass;
import com.example.tfg.domain.Course;
import com.example.tfg.domain.Group;
import com.example.tfg.repository.GroupDao;
import com.example.tfg.service.CourseService;
import com.example.tfg.service.GroupService;

@Service
public class GroupServiceImp implements GroupService {

	@Autowired
	private GroupDao daoGroup;
	
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

			if (groupExists.isDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");

			}
			result.setE(false);
			result.setErrorsList(errors);
		}
		else{
			group.setCourse(serviceCourse.getCourse(id_course));
			boolean r = daoGroup.addGroup(group);
			if (r) 
				result.setE(true);
		}
		return result;		
	}

	@Transactional(readOnly = true)
	public Group getGroup(Long id_group) {

		return daoGroup.getGroup(id_group);
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

			if (groupExists.isDeleted()){
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
	public boolean deleteGroup(Long id_group) {
	
		return daoGroup.deleteGroup(id_group);
	}


	public Collection<Group> getGroupsForCourse(Long id) {
		// TODO Auto-generated method stub
		return daoGroup.getGroupsForCourse(id);
	}

	
	public boolean deleteGroupsFromCourses(Collection<Course> coursesList) {
		// TODO Auto-generated method stub
		return daoGroup.deleteGroupsFromCourses(coursesList);
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
			if(!g.isDeleted()){
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


}
