package com.example.tfg.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.example.tfg.classes.ResultClass;
import com.example.tfg.domain.Course;
import com.example.tfg.domain.Group;

@Service
public interface GroupService {

	ResultClass<Boolean> addGroup(Group newgroup, Long id_course);

	Group getGroup(Long id_group);

	ResultClass<Boolean> modifyGroup(Group group, Long id_group);

	boolean deleteGroup(Long id_group);

	Collection<Group> getGroupsForCourse(Long id);

	boolean deleteGroupsFromCourses(Collection<Course> coursesList);
	
	public ResultClass<Boolean> unDeleteGroup(Group group);

	Collection<Group> getGroupsForStudent(Long id_student);
	Collection<Group> getGroupsForProfessor(Long id_professor);
}
