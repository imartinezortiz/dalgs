package com.example.tfg.repository;

import org.springframework.stereotype.Repository;

import com.example.tfg.domain.User;
@Repository
public interface UserDao {//extends JpaRepository<User, Long> {
	
	public User findByUsername(String username);
}