package com.example.tfg.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tfg.classes.UploadForm;
import com.example.tfg.domain.User;

@Service
public interface UserService {
	public User findByUsername(String username);
	
	public List<User> getAll(Integer pageIndex);
	public boolean deleteUser(Long id);
	public boolean saveUser(User user);
	public List<User> getAllByCourse(Long id_course,Integer pageIndex);

	public boolean addUser(User user);
	public boolean uploadCVS(UploadForm upload);
	public Integer numberOfPages() ;
	public User getUser(Long id_user);
	
	public User findByEmail(String email);

	
}
