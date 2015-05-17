/**
 * This file is part of D.A.L.G.S.
 *
 * D.A.L.G.S is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * D.A.L.G.S is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with D.A.L.G.S.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.dalgs.user.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.prefs.CsvPreference;

import es.ucm.fdi.dalgs.acl.service.AclObjectService;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.classes.StringSHA;
import es.ucm.fdi.dalgs.classes.UploadForm;
import es.ucm.fdi.dalgs.domain.Group;
import es.ucm.fdi.dalgs.domain.User;
import es.ucm.fdi.dalgs.user.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository daoUser;

	@Autowired
	private AclObjectService manageAclService;

	@Autowired
	private MessageSource messageSource;

	@PreAuthorize("permitAll")
	@Transactional(readOnly = true)
	public ResultClass<User> findByEmail(String email) {
		ResultClass<User> result = new ResultClass<>();
		result.setSingleElement(daoUser.findByEmail(email));
		return result;
	}

	@PreAuthorize("permitAll")
	@Transactional(readOnly = true)
	public ResultClass<User> findByUsername(String username) {
		ResultClass<User> result = new ResultClass<>();
		result.setSingleElement(daoUser.findByUsername(username));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<User> getAll(Integer pageIndex, Boolean showAll,
			String typeOfUser) {
		ResultClass<User> result = new ResultClass<>();
		result.addAll(daoUser.getAll(pageIndex, showAll, typeOfUser));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultClass<Boolean>  deleteUser(Long id) {
		ResultClass<Boolean> result = new ResultClass<>();
		result.setSingleElement(daoUser.deleteUser(id));
		return result;
	}

	@PreAuthorize("#user.username == principal.username or hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean>  saveUser(User user) {
		ResultClass<Boolean> result = new ResultClass<>();
		result.setSingleElement(daoUser.saveUser(user));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = false)
	public ResultClass<Integer> numberOfPages() {
		ResultClass<Integer> result = new ResultClass<>();
		result.setSingleElement(daoUser.numberOfPages());
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<User> getUser(Long id_user) {
		ResultClass<User> result = new ResultClass<>();
		result.setSingleElement(daoUser.getUser(id_user));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> addUser(User user) {
		ResultClass<Boolean> result = new ResultClass<>();
		StringSHA sha = new StringSHA();
		String pass = sha.getStringMessageDigest(user.getPassword());
		user.setPassword(pass);
		if(daoUser.addUser(user)){
			User u = findByUsername(user.getUsername()).getSingleElement();
			manageAclService.addACLToObject(u.getId(), u.getClass().getName());

			manageAclService.addPermissionToAnObject_WRITE(u, u.getId(), u.getClass().getName());
			result.setSingleElement(true);
		}
		else result.setSingleElement(false);

		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> uploadCVS(UploadForm upload, String typeOfUser, Locale locale) {
		ResultClass<Boolean> result = new ResultClass<>();

		CsvPreference prefers = new CsvPreference.Builder(upload.getQuoteChar()
				.charAt(0), upload.getDelimiterChar().charAt(0),
				upload.getEndOfLineSymbols()).build();

		List<User> list = null;
		try {
			FileItem fileItem = upload.getFileData().getFileItem();
			UserCSV userUpload = new UserCSV();
			list = userUpload.readCSVUserToBean(fileItem.getInputStream(),
					upload.getCharset(), prefers, typeOfUser);
			if (list == null){
				result.setHasErrors(true);
				result.getErrorsList().add(messageSource.getMessage("error.params", null, locale));
			}
			else{
				if( daoUser.persistListUsers(list)){
					for(User u : list){
						User aux = findByUsername(u.getUsername()).getSingleElement();
						manageAclService.addACLToObject(u.getId(), u.getClass().getName());
						manageAclService.addPermissionToAnObject_WRITE(aux, aux.getId(), aux.getClass().getName());
					}
					result.setSingleElement(true);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			result.setSingleElement(false);
		}

		return result;
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
	public ResultClass<String> getAllByRole(String user_role) {
		ResultClass<String> result = new ResultClass<>();
		result.addAll(daoUser.getAllByRole(user_role));
		return result;

	}

	@PreAuthorize("hasRole('ROLE_USER')")
	public ResultClass<User> findByFullName(String fullname) {
		String fullnamesplit[] = fullname.split(" - ");		
		return findByUsername(fullnamesplit[0]);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = true)
	public ResultClass<User> getAll() {
		ResultClass<User> result = new ResultClass<>();
		result.addAll(daoUser.getAllUsers());
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = true)
	public void downloadCSV(HttpServletResponse response) throws IOException {


		Collection<User> users = new ArrayList<User>();
		users =  daoUser.getAllUsers();

		if(!users.isEmpty()){
			UserCSV userCSV = new UserCSV();
			userCSV.downloadCSV(response, users);
		}


	}

	@PreAuthorize("hasPermission(#group, 'WRITE') or hasPermission(#group, 'ADMINISTRATION')")
	public ResultClass<Boolean> persistListUsers(Group group, List<User> list) {
		ResultClass<Boolean> result = new ResultClass<>();
		result.setSingleElement(daoUser.persistListUsers(list));
		return result ;
	}

	@PreAuthorize("hasPermission(#group, 'WRITE') or hasPermission(#group, 'ADMINISTRATION')")
	public Collection<User> getListUsersWithId(Group group, List<User> list) {
		// TODO Auto-generated method stub
		Collection<User> usersId = new ArrayList<User>();
		for (User u: list){
			usersId.add(daoUser.findByUsername(u.getUsername()));
		}
		return usersId;
	}




}
