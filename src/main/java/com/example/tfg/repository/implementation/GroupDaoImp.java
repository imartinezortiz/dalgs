package com.example.tfg.repository.implementation;

import org.springframework.stereotype.Repository;

import com.example.tfg.domain.Group;
import com.example.tfg.repository.GroupDao;

@Repository
public class GroupDaoImp implements GroupDao {

	@Override
	public Group getGroup(Long id_group) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Group existByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean addGroup(Group newgroup) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean saveGroup(Group existGroup) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean deleteGroup(Long id_group) {
		// TODO Auto-generated method stub
		return false;
	}

}
