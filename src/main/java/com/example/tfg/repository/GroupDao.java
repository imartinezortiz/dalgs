package com.example.tfg.repository;

import java.util.Collection;

import org.springframework.stereotype.Repository;

import com.example.tfg.domain.Course;
import com.example.tfg.domain.Group;

@Repository
public interface GroupDao {

	Group getGroup(Long id_group);

	Group existByName(String name);

	boolean addGroup(Group newgroup);

	boolean saveGroup(Group existGroup);

	boolean deleteGroup(Long id_group);

	Collection<Group> getGroupsForCourse(Long id);

	boolean deleteGroupsFromCourses(Collection<Course> coursesList);

}
