package com.example.tfg.service.implementation;

import java.io.IOException;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.prefs.CsvPreference;

import com.example.tfg.classes.UploadForm;
import com.example.tfg.domain.Role;
import com.example.tfg.domain.User;
import com.example.tfg.repository.UserDao;
import com.example.tfg.service.UserService;
import com.example.tfg.service.upload.UserUpload;

@Service
public class UserServiceImp implements UserService {

	@Autowired
	private UserDao daoUser;
	
	
	@Transactional(readOnly = true)
	public User findByUsername(String username){
		return daoUser.findByUsername(username);
	}
	
	@Transactional(readOnly = true)
	public List<User> getAll(Integer pageIndex){
		return daoUser.getAll( pageIndex);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteUser(Long id){
		return daoUser.deleteUser(id);
	}
	
	@Transactional(readOnly = false)
	public boolean saveUser(User user){
		return daoUser.saveUser(user);
	}
	
	@Transactional(readOnly = true)
	public List<User> getAllByCourse(Long id_course,Integer pageIndex){
		return daoUser.getAllByCourse(id_course,pageIndex);
	}

	@Transactional(readOnly = false)
	public Integer numberOfPages() {
		return daoUser.numberOfPages();
	}
	@Transactional(readOnly = true)
	public User getUser(Long id_user) {
		return daoUser.getUser(id_user);
	}
	
	@Transactional(readOnly = false)
	public boolean addUser(User user){
		return daoUser.addUser(user);
	}
	
	@Transactional(readOnly = true)
	public boolean existInCourse(Long id, Long id_course){
		return daoUser.existInCourse(id, id_course);
	}

	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public boolean uploadCVS(UploadForm upload) {
		CsvPreference prefers =
				// new CsvPreference.Builder('"', ';',"\n").build();
				new CsvPreference.Builder(upload.getQuoteChar().charAt(0), upload
						.getDelimiterChar().charAt(0), upload.getEndOfLineSymbols())
						.build();

				List<User> list = null;
				try {
					FileItem fileItem = upload.getFileData().getFileItem();
					list = UserUpload.readCSVUserToBean(fileItem.getInputStream(),
							upload.getCharset(), prefers);// getInputStream().toString()

					for (User u : list) {
						User user =  u;
						System.out.println(user.getFirstName());
						Role role = new Role();
						role.setRole(2);
						role.setUser(user);
						user.setRole(role);
						daoUser.addUser(user);
					}
					return true;

				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}

	}
	

}
