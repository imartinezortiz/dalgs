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
import es.ucm.fdi.dalgs.domain.ExternalActivity;
import es.ucm.fdi.dalgs.domain.Group;
import es.ucm.fdi.dalgs.externalActivity.repository.ExternalActivityRepository;
import es.ucm.fdi.dalgs.group.repository.GroupRepository;
import es.ucm.fdi.dalgs.group.service.GroupService;
import es.ucm.fdi.dalgs.user.service.UserService;

@Service
public class WebhookService {
	@Autowired
	private ActivityRepository daoActivity;
	
	@Autowired
	private ExternalActivityRepository daoExternalActivity;
	
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
	public ResultClass<ExternalActivity> addActivityCourseREST(Course course,
			ExternalActivity act, Long id_course, Long id_academic) {

		boolean success = false;


		ResultClass<ExternalActivity> result = new ResultClass<>();


			act.setCourse(getCourseREST(id_course).getSingleElement());
			success = daoExternalActivity.addActivity(act);
			
			
			if (success) {
				ExternalActivity externalActivityExists = daoExternalActivity.existByCode(act.getInfo()
						.getCode());
				success = manageAclService.addACLToObject(externalActivityExists
						.getId(), externalActivityExists.getClass().getName());

				manageAclService.addPermissionToAnObject_ADMINISTRATION(
						course.getCoordinator(), externalActivityExists.getId(),
						externalActivityExists.getClass().getName());

				// Rest of users which belong to this course need READ
				// permission
				for (Group g : course.getGroups()) {
					manageAclService.addPermissionToAnObjectCollection_READ(
							g.getProfessors(), externalActivityExists.getId(),
							externalActivityExists.getClass().getName());
					manageAclService.addPermissionToAnObjectCollection_READ(
							g.getStudents(), externalActivityExists.getId(),
							externalActivityExists.getClass().getName());
				}

				if (success)
					result.setSingleElement(externalActivityExists);

			} 

		return result;
	}


	@Transactional(readOnly = false)
	public ResultClass<ExternalActivity> addActivitytoGroupREST(Group group,
			ExternalActivity act, Long id_group, Long id_course, Long id_academic) {
		
		
		boolean success = false;

	
//		Activity activityExists = daoActivity.existByCode(act.getInfo()
//				.getCode());
		ResultClass<ExternalActivity> result = new ResultClass<>();

//		if (activityExists == null) {
			
			act.setGroup(getGroupREST(id_group).getSingleElement());
			success = daoExternalActivity.addActivity(act);

//			if (success) {
//				ExternalActivity ExternalActivityExists = daoExternalActivity.existByCode(act.getInfo()
//						.getCode());
//				success = manageAclService.addACLToObject(ExternalActivityExists
//						.getId(), ExternalActivityExists.getClass().getName());
//
//				// Rest of users which belong to this course need READ
//				// permission
//				manageAclService.addPermissionToAnObjectCollection_READ(
//						group.getProfessors(), ExternalActivityExists.getId(),
//						ExternalActivityExists.getClass().getName());
//				manageAclService.addPermissionToAnObjectCollection_READ(
//						group.getStudents(), ExternalActivityExists.getId(),
//						ExternalActivityExists.getClass().getName());
//
//				if (success)
//					result.setSingleElement(ExternalActivityExists);
//				else result.getErrorsList().add("Cannot creat ACL to Object");
//
//			
//			}
//			else {
//				result.getErrorsList().add("Cannot add the new activity");
//			}
//		}
//		else result.getErrorsList().add("Code already exist");
//		
//		result.setHasErrors(result.getErrorsList().size()>0);
		return result;

	}

	@Transactional(readOnly = true)
	public ResultClass<Activity> getActivityREST(Long id) {
		ResultClass<Activity> result = new ResultClass<Activity>();
		result.setSingleElement(daoActivity.getActivityFormatter(id));
		return result;
	}

}
