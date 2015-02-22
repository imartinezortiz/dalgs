package com.example.tfg.service.implementation;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	public boolean addGroup(Group newgroup, Long id_course) {
		
		Group existGroup = daoGroup.existByName(newgroup.getName());
		Course course = serviceCourse.getCourse(id_course);
		if(existGroup == null){
			newgroup.setCourse(course);
			course.getGroups().add(newgroup);
			if(daoGroup.addGroup(newgroup))
				return serviceCourse.modifyCourse(course);


		}else if(existGroup.isDeleted()==true){
			existGroup.setName(newgroup.getName());
			existGroup.setDeleted(false);
			course.getGroups().add(existGroup);
			if(daoGroup.saveGroup(existGroup))
				return serviceCourse.modifyCourse(course);

		}
		return false;
	}

	@Transactional(readOnly = true)
	public Group getGroup(Long id_group) {

		return daoGroup.getGroup(id_group);
	}

	@Transactional(readOnly = false)
	public boolean modifyGroup(Group group, Long id_group) {
		Group groupModify = daoGroup.getGroup(id_group);
		groupModify.setName(group.getName());
		return daoGroup.saveGroup(groupModify);
	}

	@Transactional(readOnly = false)
	public boolean deleteGroup(Long id_group) {
	
		return daoGroup.deleteGroup(id_group);
	}

	@Override
	public Collection<Group> getGroupsForCourse(Long id) {
		// TODO Auto-generated method stub
		return daoGroup.getGroupsForCourse(id);
	}

	@Override
	public boolean deleteGroupsFromCourses(Collection<Course> coursesList) {
		// TODO Auto-generated method stub
		return daoGroup.deleteGroupsFromCourses(coursesList);
	}

}
