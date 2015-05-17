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

package es.ucm.fdi.dalgs.externalActivity.service;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.dalgs.activity.service.ActivityService;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.course.service.CourseService;
import es.ucm.fdi.dalgs.domain.Activity;
import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.domain.Group;
import es.ucm.fdi.dalgs.externalActivity.repository.ExternalActivityRepository;
import es.ucm.fdi.dalgs.group.service.GroupService;

@Service
public class ExternalActivityService {
	@Autowired
	private MessageSource messageSource;

	@Autowired 
	private ExternalActivityRepository daoExternalActivity;
	@Autowired
	private ActivityService serviceActivity;
	
	@Autowired
	private CourseService serviceCourse;
	
	@Autowired
	private GroupService serviceGroup;


	
	@Transactional(readOnly=false)
	public ResultClass<Boolean> deleteExternalActivity(Long id_academic, Long id_course,
			Long id_group, Long id_externalEexternalActivity) {
		boolean success;
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		Group group = serviceGroup.getGroup(id_group, id_course, id_academic, false).getSingleElement();
		Activity externalActivity = serviceActivity.getActivity(id_externalEexternalActivity, id_course, id_group, id_academic).getSingleElement(); 
		externalActivity.setGroup(null);
		success = daoExternalActivity.deleteExternalActivity(externalActivity);
		group.getExternal_activities().remove(externalActivity);
		if (success)result.setSingleElement(serviceGroup.updateGroup(group).getSingleElement());
	
		return result;
	}
	
	@Transactional(readOnly=false)
	public ResultClass<Boolean> deleteExternalActivity(Long id_academic, Long id_course,
			Long id_externalEexternalActivity) {
		boolean success;
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		Course course = serviceCourse.getCourseAll(id_course, id_academic, false).getSingleElement();
		Activity externalActivity = serviceActivity.getActivity(id_externalEexternalActivity, id_course, null,id_academic).getSingleElement(); 
		externalActivity.setCourse(null);
		success = daoExternalActivity.deleteExternalActivity(externalActivity);
		course.getExternal_activities().remove(externalActivity);
		if (success) result.setSingleElement(serviceCourse.updateCourse(course).getSingleElement());
	
		return result;
	}
	
	@Transactional(readOnly=true)
	public ResultClass<Activity> getExternalActivity(Long id_externalActivity,
			Long id_course, Long id_group, Long id_academic) {
		
		ResultClass<Activity> result = new ResultClass<>();
		
		
		Activity externalActivity = serviceActivity.getActivity(id_externalActivity, id_course, id_group, id_academic).getSingleElement();
		
		
		if (externalActivity == null)
			result.setHasErrors(true);
		else
			result.setSingleElement(externalActivity);

		return result;
	
	}
	
	

	@Transactional(readOnly=false)
	public ResultClass<Boolean> moveGroup(Long id_externalActivity, Long id_academic,
			Long id_course, Long id_group) {
		
		Group group = serviceGroup.getGroup(id_group, id_course, id_academic, false).getSingleElement();
		Activity externalActivity = serviceActivity.getActivity(id_externalActivity, id_course, id_group, id_academic).getSingleElement();
		ResultClass<Boolean> result = new ResultClass<>();

			group.getActivities().add(externalActivity);
			group.getExternal_activities().remove(externalActivity);

			result = serviceGroup.updateGroup(group);



		return result;
	}



	
	@Transactional(readOnly=false)
	public ResultClass<Boolean> moveCourse(Long id_externalActivity,
			Long id_academic, Long id_course, Locale locale) {
		
		Course course = serviceCourse.getCourse(id_course, id_academic).getSingleElement();

		Activity externalActivity = serviceActivity.getActivity(id_externalActivity, id_course, null, id_academic).getSingleElement();

		ResultClass<Boolean> result = new ResultClass<>();
	
		
		course.getActivities().add(externalActivity);
		course.getExternal_activities().remove(externalActivity);
		result = serviceCourse.updateCourse(course);
		
		return result;
	}

	public ResultClass<Activity> getExternalActivitiesAll() {
		ResultClass<Activity> result = new ResultClass<>();
		result.addAll(daoExternalActivity.getExternalActivitiesAll());

		return result;
	}
	
	

}
