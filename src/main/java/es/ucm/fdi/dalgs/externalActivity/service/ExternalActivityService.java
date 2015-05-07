package es.ucm.fdi.dalgs.externalActivity.service;

import java.util.ArrayList;
import java.util.Collection;
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
import es.ucm.fdi.dalgs.domain.ExternalActivity;
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

	@Transactional(readOnly=true)
	public ResultClass<ExternalActivity> getExternalActivitiesForGroup(
			long id_group) {
		ResultClass<ExternalActivity> result = new ResultClass<>();
			result.addAll(daoExternalActivity.getExternalActivitiesForGroup(id_group));
		return result;
	}
	
	@Transactional(readOnly=false)
	public ResultClass<Boolean> deleteExternalActivity(Course course,
			Group group, Long id_externalEexternalActivity) {

		ResultClass<Boolean> result = new ResultClass<Boolean>();
		result.setSingleElement(daoExternalActivity.deleteExternalActivity(id_externalEexternalActivity));
		return result;
	}
	
	@Transactional(readOnly=true)
	public ResultClass<ExternalActivity> getExternalActivity(Long id_externalActivity,
			Long id_course, Long id_group, Long id_academic) {
		
		ResultClass<ExternalActivity> result = new ResultClass<>();
		ExternalActivity externalActivity = daoExternalActivity.getExternalActivity(id_externalActivity, id_course, id_group,
				id_academic);

		if (externalActivity == null)
			result.setHasErrors(true);
		else
			result.setSingleElement(externalActivity);

		return result;
	
	}
	
	@Transactional(readOnly=false)
	public ResultClass<ExternalActivity> unDeleteExternalActivity(
			Course course, Group group, ExternalActivity externalActivity,
			Locale locale) {
		
		ExternalActivity ea = daoExternalActivity.existByCode(externalActivity.getInfo().getCode());
		ResultClass<ExternalActivity> result = new ResultClass<>();
		
		if (ea == null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add(messageSource.getMessage("error.ElementNoExists", null,
					locale));
			result.setErrorsList(errors);

		} else {
			if (!ea.getIsDeleted()) {
				Collection<String> errors = new ArrayList<String>();
				errors.add(messageSource.getMessage("error.CodeNoDeleted",
						null, locale));
				result.setErrorsList(errors);
			}

			ea.setIsDeleted(false);
			ea.setInfo(externalActivity.getInfo());
			boolean r = daoExternalActivity.saveExternalActivity(ea);
			if (r)
				result.setSingleElement(ea);

		}
		return result;
	}
	@Transactional(readOnly=false)
	public boolean move(Long id_externalActivity, Long id_academic,
			Long id_course, Long id_group) {
		
		Group group = serviceGroup.getGroup(id_group, id_course, id_academic).getSingleElement();
		Course course= serviceCourse.getCourse(id_course, id_academic).getSingleElement();
		ExternalActivity externalActivity = daoExternalActivity.getExternalActivity(id_externalActivity, id_course, id_group, id_academic);
		
		group.getExternal_activities().remove(externalActivity);
		Activity activity =new Activity();
		activity.setCourse(course);
		activity.setGroup(externalActivity.getGroup());
		activity.setInfo(externalActivity.getInfo());
		group.getActivities().add(activity);
		if(!serviceActivity.addActivitytoGroup(group, activity, id_group, id_course, id_academic).isEmpty()) 
			if (serviceGroup.modifyGroupActivities(group).isEmpty()) return true;
		
		return false;
	}
	
}
