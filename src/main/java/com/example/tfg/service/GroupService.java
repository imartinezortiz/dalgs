package com.example.tfg.service;

import org.springframework.stereotype.Service;

import com.example.tfg.domain.Group;

@Service
public interface GroupService {

	boolean addGroup(Group newgroup, Long id_course);

	Group getGroup(Long id_group);

	boolean modifyGroup(Group group, Long id_group, Long id_course);

	boolean deleteGroup(Long id_group);

}
