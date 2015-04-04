package es.ucm.fdi.dalgs.user.service;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.prefs.CsvPreference;

import es.ucm.fdi.dalgs.classes.UploadForm;
import es.ucm.fdi.dalgs.domain.User;
import es.ucm.fdi.dalgs.user.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository daoUser;

	@PreAuthorize("permitAll")
	@Transactional(readOnly = true)
	public User findByEmail(String email) {
		return daoUser.findByEmail(email);
	}

	@PreAuthorize("permitAll")
	@Transactional(readOnly = true)
	public User findByUsername(String username) {
		return daoUser.findByUsername(username);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public List<User> getAll(Integer pageIndex, Boolean showAll, String typeOfUser) {
		return daoUser.getAll(pageIndex,showAll,typeOfUser);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteUser(Long id) {
		return daoUser.deleteUser(id);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public boolean saveUser(User user) {
		return daoUser.saveUser(user);
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
	public boolean addUser(User user) {
		// StringSHA sha = new StringSHA();
		// String pass = sha.getStringMessageDigest(user.getPassword());
		// user.setPassword(pass);
		return (daoUser.addUser(user));
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public boolean uploadCVS(UploadForm upload, String typeOfUser) {
		CsvPreference prefers =
		new CsvPreference.Builder(upload.getQuoteChar().charAt(0), upload
				.getDelimiterChar().charAt(0), upload.getEndOfLineSymbols())
				.build();

		List<User> list = null;
		try {
			FileItem fileItem = upload.getFileData().getFileItem();
			UserUpload userUpload = new UserUpload();
			list = userUpload.readCSVUserToBean(fileItem.getInputStream(),
					upload.getCharset(), prefers , typeOfUser);

			return daoUser.persistListUsers(list);

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public final boolean hasRole(User user, String role) {
		boolean hasRole = false;
		try {

			Collection<GrantedAuthority> authorities = (Collection<GrantedAuthority>) user
					.getAuthorities();
			for (GrantedAuthority grantedAuthority : authorities) {
				hasRole = grantedAuthority.getAuthority().equals(role);
				if (hasRole) {
					break;
				}
			}

		} catch (NotFoundException nfe) {
		}

		return hasRole;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	public List<String> getAllByRole(String user_role) {
		return daoUser.getAllByRole(user_role);

	}

	@PreAuthorize("hasRole('ROLE_USER')")
	public User findByFullName(String fullname) {
		String fullnamesplit[] = fullname.split(" - ");
		return findByUsername(fullnamesplit[0]);
	}

}
