package com.example.tfg.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.tfg.domain.User;

@Repository
public interface UserDao {

	public List<User> getAll(Integer pageIndex);
	public boolean deleteUser(Long id);
	public boolean saveUser(User user);

	public boolean addUser(User user);
	public User getUser(Long id_user);

	public boolean persistALotUsers(List<User> users);
	public Integer numberOfPages();
	
	public User findByEmail(String username);
	public User findByUsername(String username);

}