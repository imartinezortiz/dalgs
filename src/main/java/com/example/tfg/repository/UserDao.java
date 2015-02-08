package com.example.tfg.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.tfg.domain.User;

@Repository
public interface UserDao {// extends JpaRepository<User, Long> {

	public User findByUsername(String username);
	public List<User> getAll(Integer pageIndex);
	public boolean deleteUser(Long id);
	public boolean saveUser(User user);
	public List<User> getAllByCourse(Long id_course, Integer pageIndex);

	public boolean addUser(User user);
	public boolean existInCourse(Long id, Long id_course);
	public User getUser(Long id_user);

	public boolean persistALotUsers(List<User> users);
	public Integer numberOfPages();
}