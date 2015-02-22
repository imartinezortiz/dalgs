package com.example.tfg.service;

import java.util.Collection;

import org.springframework.stereotype.Service;

import com.example.tfg.domain.Course;
import com.example.tfg.domain.Group;

@Service
public interface GroupService {

	boolean addGroup(Group newgroup, Long id_course);

	Group getGroup(Long id_group);

	boolean modifyGroup(Group group, Long id_group);

	boolean deleteGroup(Long id_group);

	Collection<Group> getGroupsForCourse(Long id);

	boolean deleteGroupsFromCourses(Collection<Course> coursesList);

}
