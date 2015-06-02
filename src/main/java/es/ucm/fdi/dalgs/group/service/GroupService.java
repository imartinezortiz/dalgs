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
package es.ucm.fdi.dalgs.group.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.apache.commons.fileupload.FileItem;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.prefs.CsvPreference;

import es.ucm.fdi.dalgs.acl.service.AclObjectService;
import es.ucm.fdi.dalgs.activity.service.ActivityService;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.classes.UploadForm;
import es.ucm.fdi.dalgs.course.service.CourseService;
import es.ucm.fdi.dalgs.domain.Activity;
import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.domain.Group;
import es.ucm.fdi.dalgs.domain.User;
import es.ucm.fdi.dalgs.group.repository.GroupRepository;
import es.ucm.fdi.dalgs.mailbox.service.MailBoxService;
import es.ucm.fdi.dalgs.user.service.UserCSV;
import es.ucm.fdi.dalgs.user.service.UserService;

@Service
public class GroupService {

	@Autowired
	private AclObjectService manageAclService;

	@Autowired
	private GroupRepository repositoryGroup;

	@Autowired
	private CourseService serviceCourse;

	@Autowired
	private ActivityService serviceActivity;

	@Autowired
	private UserService serviceUser;
	
	@Autowired 
	private MailBoxService serviceMailBox;

	@Autowired
	private MessageSource messageSource;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Group> addGroup(Group group, Long id_course,
			Long id_academic, Locale locale) {

		boolean success = false;

		Group groupExists = repositoryGroup.existInCourse(id_course, group.getName());
		ResultClass<Group> result = new ResultClass<>();

		if (groupExists != null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add(messageSource.getMessage("error.Code", null, locale));

			if (groupExists.getIsDeleted()) {
				result.setElementDeleted(true);
				errors.add(messageSource.getMessage("error.deleted", null,
						locale));
				result.setSingleElement(groupExists);
			} else
				result.setSingleElement(group);
			result.setErrorsList(errors);
		} else {
			group.setCourse(serviceCourse.getCourse(id_course, id_academic)
					.getSingleElement());
			success = repositoryGroup.addGroup(group);

			if (success) {
				groupExists = repositoryGroup
						.existInCourse(id_course, group.getName());
				success = manageAclService.addACLToObject(groupExists.getId(),
						groupExists.getClass().getName());

				if (success
						&& (groupExists.getCourse().getCoordinator() != null))
					manageAclService.addPermissionCASCADE(groupExists
							.getCourse().getCoordinator(), groupExists,
							id_academic, id_course, groupExists.getId());

				if (success)
					result.setSingleElement(group);

			} else {
				throw new IllegalArgumentException(
						"Cannot create ACL. Object not set.");

			}
		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	@Transactional(readOnly = true)
	public ResultClass<Group> getGroup(Long id_group, Long id_course,
			Long id_academic, Boolean show) {
		ResultClass<Group> result = new ResultClass<Group>();
		Group g = repositoryGroup.getGroup(id_group, id_course,
				id_academic);
		g.setActivities(serviceActivity.getActivitiesForGroup(id_group, show));
		result.setSingleElement(g);
		return result;
	}

	@PreAuthorize("hasPermission(#group, 'WRITE') or hasPermission(#group, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> modifyGroup(Group group, Long id_group,
			Long id_course, Long id_academic, Locale locale) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		Group modifyGroup = repositoryGroup.getGroup(id_group, id_course, id_academic);

		Group groupExists = repositoryGroup.existInCourse(id_course, group.getName());

		if (!group.getName().equalsIgnoreCase(modifyGroup.getName())
				&& groupExists != null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add(messageSource.getMessage("error.newCode", null, locale));

			if (groupExists.getIsDeleted()) {
				result.setElementDeleted(true);
				errors.add(messageSource.getMessage("error.deleted", null,
						locale));

			}
			result.setErrorsList(errors);
			result.setSingleElement(false);
		} else {
			modifyGroup.setName(group.getName());
			boolean r = repositoryGroup.saveGroup(modifyGroup);
			if (r) {
				result.setSingleElement(true);

			}
		}
		return result;
	}

	// -----
	@PreAuthorize("hasPermission(#group, 'WRITE') or hasPermission(#group, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> modifyGroupActivities(Group group) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		boolean r = repositoryGroup.saveGroup(group);
		if (r)
			result.setSingleElement(true);

		return result;
	}

	// ----

	@PreAuthorize("hasPermission(#group, 'DELETE') or hasPermission(#group, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> deleteGroup(Group group) {

		ResultClass<Boolean> result = new ResultClass<Boolean>();

		if (serviceActivity.deleteActivitiesFromGroup(group).getSingleElement()) {
			manageAclService.removePermissionCollectionCASCADE(group
					.getStudents(), group, group.getCourse().getAcademicTerm()
					.getId(), group.getCourse().getId(), group.getId());
			manageAclService.removePermissionCollectionCASCADE(group
					.getProfessors(), group, group.getCourse()
					.getAcademicTerm().getId(), group.getCourse().getId(),
					group.getId());

			if (group.getCourse().getCoordinator() != null) {
				manageAclService.removePermissionGroupCoordinator(group
						.getCourse().getCoordinator(), group.getId());
			}
			group.setProfessors(new ArrayList<User>());
			group.setStudents(new ArrayList<User>());
			result.setSingleElement(repositoryGroup.deleteGroup(group));

			return result;
		}
		result.setSingleElement(false);
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	@Transactional(readOnly = true)
	public ResultClass<Group> getGroupsForCourse(Long id, Boolean showAll) {
		ResultClass<Group> result = new ResultClass<>();
		result.addAll(repositoryGroup.getGroupsForCourse(id, showAll));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> deleteGroupsFromCourses(
			Collection<Course> coursesList) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		for (Course course : coursesList) {
			for (Group group : course.getGroups()) {
				manageAclService.removePermissionCollectionCASCADE(group
						.getStudents(), group,
						course.getAcademicTerm().getId(), course.getId(), group
						.getId());
				manageAclService.removePermissionCollectionCASCADE(group
						.getProfessors(), group, course.getAcademicTerm()
						.getId(), course.getId(), group.getId());
				group.setProfessors(new ArrayList<User>());
				group.setStudents(new ArrayList<User>());
			}
			if (course.getCoordinator() != null)
				manageAclService.removePermissionCASCADE(course
						.getCoordinator(), course, course.getAcademicTerm()
						.getId(), course.getId(), null);
		}

		result.setSingleElement(repositoryGroup.deleteGroupsFromCourses(coursesList));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Group> unDeleteGroup(Group group, Long id_course,
			Locale locale) {

		Group g = repositoryGroup.existInCourse(id_course, group.getName());

		ResultClass<Group> result = new ResultClass<>();
		if (g == null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add(messageSource.getMessage("error.ElementNoExists", null,
					locale));
			result.setErrorsList(errors);

		} else {
			if (!g.getIsDeleted()) {
				Collection<String> errors = new ArrayList<String>();
				errors.add(messageSource.getMessage("error.CodeNoDeleted",
						null, locale));
				result.setErrorsList(errors);
			} else {
				g.setDeleted(false);
				g.setName(group.getName());
				boolean r = repositoryGroup.saveGroup(g);
				if (r) {
					result.setSingleElement(g);
					if (group.getCourse().getCoordinator() != null) {
						manageAclService.addPermissionGroupCoordinator(group
								.getCourse().getCoordinator(), group.getId());
					}

				}
			}

		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	@Transactional(readOnly = true)
	public ResultClass<Group> getGroupsForStudent(Long id_student) {
		ResultClass<Group> result = new ResultClass<>();
		Collection<Group> groups = repositoryGroup.getGroupsForStudent(id_student);

		if (groups != null)
			result.addAll(groups);
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	@Transactional(readOnly = true)
	public ResultClass<Group> getGroupsForProfessor(Long id_professor) {
		ResultClass<Group> result = new ResultClass<>();

		Collection<Group> groups = repositoryGroup.getGroupsForProfessor(id_professor);
		if (groups != null)
			result.addAll(groups);
		return result;
	}

	@PreAuthorize("hasPermission(#group, 'WRITE') or hasPermission(#group, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> setProfessors(Group group, Long id_group,
			Long id_course, Long id_academic, Collection<User> users) {
		ResultClass<Boolean> result = new ResultClass<>();

		Group modifyGroup = repositoryGroup.getGroup(id_group, id_course, id_academic);

		modifyGroup.setProfessors(users);



		result.setHasErrors(!repositoryGroup.saveGroup(modifyGroup));

		if (!result.hasErrors()) {
			manageAclService.addPermissionCollectionCASCADE(users, group,
					id_academic, id_course, id_group);
			result.setSingleElement(result.hasErrors());

		} else {
			result.getErrorsList().add("Error manageAclService");
			result.setHasErrors(true);
		}
		// }
		return result;
	}

	/*
	 * // Delete the authorities to the old user list
	 */
	@PreAuthorize("hasPermission(#group, 'WRITE') or hasPermission(#group, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> removeUsersFromGroup(Group group,
			String typeOfUser, Long id_academic, Long id_course) {
		ResultClass<Boolean> result = new ResultClass<>();

		Collection<User> users = new ArrayList<User>();

		if (typeOfUser.equalsIgnoreCase("ROLE_PROFESSOR")) {
			users = group.getProfessors();
			if (!users.isEmpty()) {
				manageAclService.removePermissionCollectionCASCADE(users,
						group, id_academic, id_course, group.getId());
				group.getProfessors().clear();

			}
		} else if (typeOfUser.equalsIgnoreCase("ROLE_STUDENT")) {
			users = group.getStudents();
			if (!users.isEmpty()) {
				manageAclService.removePermissionCollectionCASCADE(users,
						group, id_academic, id_course, group.getId());
				group.getStudents().clear();

			}
		}

		result.setHasErrors(!repositoryGroup.saveGroup(group));

		return result;
	}

	@PreAuthorize("hasPermission(#group, 'WRITE') or hasPermission(#group, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> setStudents(Group group, Long id_group,
			Long id_course, Long id_academic, Collection<User> users) {

		ResultClass<Boolean> result = new ResultClass<>();
		Group modifyGroup = repositoryGroup.getGroup(id_group, id_course, id_academic);


		modifyGroup.setStudents(users);
		result.setHasErrors(!repositoryGroup.saveGroup(modifyGroup));

		if (!result.hasErrors()) {
			manageAclService.addPermissionCollectionCASCADE(
					users, modifyGroup, id_academic,
					id_course, id_group);
		} else {
			result.getErrorsList().add("Error manageAclService");
			result.setHasErrors(true);
		}
		// }

		return result;
	}

	@PreAuthorize("hasPermission(#group, 'WRITE') or hasPermission(#group, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> deleteUserGroup(Group group, Long id_group, Long id_user,
			Long id_course, Long id_academic, Locale locale) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		Group g = repositoryGroup.getGroup(id_group, id_course, id_academic);

		User u = serviceUser.getUser(id_user).getSingleElement();
		if (serviceUser.hasRole(u, "ROLE_PROFESSOR")
				&& !serviceUser.hasRole(u, "ROLE_COORDINATOR")) {

			g.getProfessors().remove(serviceUser.getUser(id_user));
			result = this.modifyGroup(g, id_group, id_course, id_academic,
					locale);
		} else if (serviceUser.hasRole(u, "ROLE_STUDENT")) {
			g.getStudents().remove(serviceUser.getUser(id_user));
			result = this.modifyGroup(g, id_group, id_course, id_academic,
					locale);
		}
		if (!result.hasErrors()) {
			// Removing the authorities to the student
			manageAclService.removePermissionCASCADE(u, g, id_academic,
					id_course, id_group);
		}
		return result;
	}

	@PreAuthorize("hasPermission(#group, 'WRITE') or hasPermission(#group, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> uploadUserCVS(Group group, UploadForm upload,
			String typeOfUser, Locale locale) {

		ResultClass<Boolean> result = new ResultClass<>();
		if(!upload.getFileData().isEmpty()){
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
					if (serviceUser.persistListUsers(group, list).getSingleElement() && list != null) { // Added
						list = (List<User>) serviceUser.getListUsersWithId(group,list);													// correctly

						//				ResultClass<Boolean> success = new ResultClass<Boolean>();
						if (typeOfUser.equalsIgnoreCase("ROLE_PROFESSOR")) {


							result = setProfessors(group, group.getId(), group
									.getCourse().getId(), group.getCourse()
									.getAcademicTerm().getId(), list);
						} else if (typeOfUser.equalsIgnoreCase("ROLE_STUDENT")) {
							// group.setStudents(list);
							result = setStudents(group, group.getId(), group
									.getCourse().getId(), group.getCourse()
									.getAcademicTerm().getId(), list);

						}
						else result.setSingleElement(false);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				result.setSingleElement(false);
			} catch (final IllegalArgumentException e) {
				e.printStackTrace();
				result.setSingleElement(false);
			}
		}
		else {
			result.setHasErrors(true);
			result.getErrorsList().add(messageSource.getMessage("error.fileEmpty", null, locale));
		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false, propagation = Propagation.REQUIRED)
	public ResultClass<Group> copyGroup(Group group, Long id_course,
			Locale locale) {

		ResultClass<Group> result = new ResultClass<Group>();
		Group copy = group.depth_copy();

		if (copy == null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Copy doesn't work");
			result.setErrorsList(errors);

		} else {
			DateTime time =  new DateTime();

			copy.setName(copy.getName() + "  " +time.getMillisOfSecond());

			for (Activity a : copy.getActivities()) {
				a.getInfo().setCode(a.getInfo().getCode() + "  "+ time.getMillisOfSecond());

			}

			boolean success = repositoryGroup.addGroup(copy);
			if (success) {
				Group exists = repositoryGroup
						.existInCourse(id_course, copy.getName());

				if (exists != null) {
					result.setSingleElement(exists);
					manageAclService.addACLToObject(exists.getId(), exists
							.getClass().getName());

					for (Activity a : exists.getActivities()) {
						success = success
								&& manageAclService.addACLToObject(a.getId(), a
										.getClass().getName());

					}
				}
			}

			result.setHasErrors(!success);

		}
		return result;

	}

	public ResultClass<Boolean> updateGroup(Group group) {
		ResultClass<Boolean> result = new ResultClass<>();
		result.setSingleElement(repositoryGroup.saveGroup(group));
		return result;
	}

	public Group getGroupFormatter(Long id_group) {

		return repositoryGroup.getGroupFormatter(id_group);
	}



}
