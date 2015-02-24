package com.example.tfg.service.implementation;

import java.io.IOException;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.prefs.CsvPreference;

import com.example.tfg.classes.StringSHA;
import com.example.tfg.classes.UploadForm;
import com.example.tfg.domain.User;
import com.example.tfg.repository.UserDao;
import com.example.tfg.service.UserService;
import com.example.tfg.service.upload.UserUpload;

@Service
public class UserServiceImp implements UserService {

	@Autowired
	private UserDao daoUser;
	
	@PreAuthorize("permitAll") 
	@Transactional(readOnly = true)
	public User findByEmail(String email){
		return daoUser.findByEmail(email);
	}
	
	@PreAuthorize("permitAll") 
	@Transactional(readOnly = true)
	public User findByUsername(String username){
		return daoUser.findByUsername(username);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public List<User> getAll(Integer pageIndex){
		return daoUser.getAll( pageIndex);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteUser(Long id){
		return daoUser.deleteUser(id);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public boolean saveUser(User user){
		return daoUser.saveUser(user);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public List<User> getAllByCourse(Long id_course,Integer pageIndex){
		return daoUser.getAllByCourse(id_course,pageIndex);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = false)
	public Integer numberOfPages() {
		return daoUser.numberOfPages();
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public User getUser(Long id_user) {
		return daoUser.getUser(id_user);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public boolean addUser(User user){
		StringSHA sha = new StringSHA();
		String pass = sha.getStringMessageDigest(user.getPassword());
		user.setPassword(pass);
		return (daoUser.addUser(user));
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = true)
	public boolean existInCourse(Long id, Long id_course){
		return daoUser.existInCourse(id, id_course);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)//, propagation = Propagation.REQUIRED)
	public boolean uploadCVS(UploadForm upload) {
		CsvPreference prefers =
				// new CsvPreference.Builder('"', ';',"\n").build();
				new CsvPreference.Builder(upload.getQuoteChar().charAt(0), upload
						.getDelimiterChar().charAt(0), upload.getEndOfLineSymbols())
						.build();

				List<User> list = null;
				try {
					FileItem fileItem = upload.getFileData().getFileItem();
					UserUpload userUpload = new UserUpload();
					list = userUpload.readCSVUserToBean(fileItem.getInputStream(),
							upload.getCharset(), prefers);// getInputStream().toString()

					return daoUser.persistALotUsers(list);
					
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}

	}
	
	
}
