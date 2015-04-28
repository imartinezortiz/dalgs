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
package es.ucm.fdi.dalgs.rest.service;


import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.dalgs.acl.service.AclObjectService;
import es.ucm.fdi.dalgs.activity.repository.ActivityRepository;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.course.repository.CourseRepository;
import es.ucm.fdi.dalgs.course.service.CourseService;
import es.ucm.fdi.dalgs.domain.Activity;
import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.domain.Group;
import es.ucm.fdi.dalgs.group.repository.GroupRepository;
import es.ucm.fdi.dalgs.group.service.GroupService;
import es.ucm.fdi.dalgs.user.service.UserService;

@Service
public class WebhookService {
	@Autowired
	private ActivityRepository daoActivity;
	
	@Autowired
	private CourseRepository daoCourse;
	
	@Autowired
	private GroupRepository daoGroup;

	@Autowired
	private CourseService serviceCourse;

	@Autowired
	private GroupService serviceGroup;

	@Autowired
	private UserService serviceUser;

	@Autowired
	private AclObjectService manageAclService;


	@Transactional(readOnly = true)
	public ResultClass<Course> getCourseREST(Long id) {
		ResultClass<Course> result = new ResultClass<Course>();
		result.setSingleElement(daoCourse.getCourseFormatter(id));
		return result;
	}
	
	@Transactional(readOnly = true)
	public ResultClass<Group> getGroupREST(Long id_group) {
		ResultClass<Group> result = new ResultClass<Group>();
		result.setSingleElement(daoGroup.getGroupFormatter(id_group));
		return result;
	}

	@Transactional(readOnly = false)
	public ResultClass<Activity> addActivityCourseREST(Course course,
			Activity activity, Long id_course, Long id_academic, Locale locale) {

		boolean success = false;

		Activity activityExists = daoActivity.existByCode(activity.getInfo()
				.getCode());
		ResultClass<Activity> result = new ResultClass<>();

		if (activityExists ==null){
			activity.setCourse(getCourseREST(id_course)
					.getSingleElement());
			success = daoActivity.addActivity(activity);

			if (success) {
				activityExists = daoActivity.existByCode(activity.getInfo()
						.getCode());
				success = manageAclService.addACLToObject(activityExists
						.getId(), activityExists.getClass().getName());

				manageAclService.addPermissionToAnObject_ADMINISTRATION(
						course.getCoordinator(), activityExists.getId(),
						activityExists.getClass().getName());

				// Rest of users which belong to this course need READ
				// permission
				for (Group g : course.getGroups()) {
					manageAclService.addPermissionToAnObjectCollection_READ(
							g.getProfessors(), activityExists.getId(),
							activityExists.getClass().getName());
					manageAclService.addPermissionToAnObjectCollection_READ(
							g.getStudents(), activityExists.getId(),
							activityExists.getClass().getName());
				}

				if (success)
					result.setSingleElement(activityExists);

			} 
		}
		return result;
	}


	@Transactional(readOnly = false)
	public ResultClass<Activity> addActivitytoGroupREST(Group group,
			Activity activity, Long id_group, Long id_course, Long id_academic) {
		boolean success = false;

		Activity activityExists = daoActivity.existByCode(activity.getInfo()
				.getCode());
		ResultClass<Activity> result = new ResultClass<>();

		if (activityExists == null) {
			
			activity.setGroup(getGroupREST(id_group).getSingleElement());
			success = daoActivity.addActivity(activity);

			if (success) {
				activityExists = daoActivity.existByCode(activity.getInfo()
						.getCode());
				success = manageAclService.addACLToObject(activityExists
						.getId(), activityExists.getClass().getName());

				// Rest of users which belong to this course need READ
				// permission
				manageAclService.addPermissionToAnObjectCollection_READ(
						group.getProfessors(), activityExists.getId(),
						activityExists.getClass().getName());
				manageAclService.addPermissionToAnObjectCollection_READ(
						group.getStudents(), activityExists.getId(),
						activityExists.getClass().getName());

				if (success)
					result.setSingleElement(activityExists);
				else result.getErrorsList().add("Cannot creat ACL to Object");

			
			}
			else {
				result.getErrorsList().add("Cannot add the new activity");
			}
		}
		else result.getErrorsList().add("Code already exist");
		
		result.setHasErrors(result.getErrorsList().size()>0);
		return result;

	}

	@Transactional(readOnly = true)
	public ResultClass<Activity> getActivityREST(Long id) {
		ResultClass<Activity> result = new ResultClass<Activity>();
		result.setSingleElement(daoActivity.getActivityFormatter(id));
		return result;
	}

}
