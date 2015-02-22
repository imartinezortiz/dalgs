package com.example.tfg.repository;

import org.springframework.stereotype.Repository;

import com.example.tfg.domain.Group;

@Repository
public interface GroupDao {

	Group getGroup(Long id_group);

	Group existByName(String name);

	boolean addGroup(Group newgroup);

	boolean saveGroup(Group existGroup);

	boolean deleteGroup(Long id_group);

}
