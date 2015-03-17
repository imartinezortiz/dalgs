package es.ucm.fdi.dalgs.activity.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.dalgs.acl.service.AclObjectService;
import es.ucm.fdi.dalgs.activity.repository.ActivityRepository;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.course.service.CourseService;
import es.ucm.fdi.dalgs.domain.Activity;
import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.domain.LearningGoal;
import es.ucm.fdi.dalgs.domain.LearningGoalStatus;
import es.ucm.fdi.dalgs.learningGoal.service.LearningGoalService;
import es.ucm.fdi.dalgs.user.service.UserService;

@Service
public class ActivityService {
	@Autowired
	private ActivityRepository daoActivity;

	@Autowired
	private CourseService serviceCourse;
	
	@Autowired
	private UserService serviceUser;

	@Autowired
	private AclObjectService manageAclService;

	@Autowired
	private LearningGoalService serviceLearningGoal;

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PROFESSOR')")
	@Transactional(readOnly = false)
	public ResultClass<Activity> addActivity(Activity activity, Long id_course) {

		boolean success = false;
		
		Activity activityExists = daoActivity.existByCode(activity.getInfo().getCode());
		ResultClass<Activity> result = new ResultClass<>();

		if( activityExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code already exists");

			if (activityExists.getIsDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");
				result.setSingleElement(activityExists);

			}
			else result.setSingleElement(activity);
			result.setErrorsList(errors);
		}
		else{
			activity.setCourse(serviceCourse.getCourse(id_course).getSingleElement());
			success = daoActivity.addActivity(activity);
			
			if(success){
				activityExists = daoActivity.existByCode(activity.getInfo().getCode());
				success = manageAclService.addAclToObject(activityExists.getId(), activityExists.getClass().getName());
				if (success) result.setSingleElement(activity);
			
			}else{
				throw new IllegalArgumentException(	"Cannot create ACL. Object not set.");

			}
			
		
		}
		return result;		
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<Activity> getAll() {
		ResultClass<Activity> result = new ResultClass<>();
		result.addAll(daoActivity.getAll());
		return result;
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PROFESSOR')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> modifyActivity(Activity activity, Long id_activity,
			Long id_course) {

		ResultClass<Boolean> result = new ResultClass<Boolean>();

		Activity modifyActivity = daoActivity.getActivity(id_activity);
		
		Activity activityExists = daoActivity.existByCode(activity.getInfo().getCode());
		
		if(!activity.getInfo().getCode().equalsIgnoreCase(modifyActivity.getInfo().getCode()) && 
				activityExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("New code already exists");

			if (activityExists.getIsDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");

			}
			result.setErrorsList(errors);
			result.setSingleElement(false);
		}
		else{
			modifyActivity.setInfo(activity.getInfo());
			boolean r = daoActivity.saveActivity(modifyActivity);
			if (r) 
				result.setSingleElement(true);
		}
		return result;

	}
	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = false)
	public ResultClass<Activity> getActivity(Long id) {
		ResultClass<Activity> result = new ResultClass<Activity>();
		result.setSingleElement(daoActivity.getActivity(id));
		return result;
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PROFESSOR')")
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultClass<Boolean> deleteActivity(Long id) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		result.setSingleElement(daoActivity.deleteActivity(id));
		return result;
	}

//	@PreAuthorize("hasRole('ROLE_USER')")
//	@PostFilter("hasPermission(filterObject, 'READ')")	
	public ResultClass<Activity> getActivitiesForCourse(Long id_course, Boolean showAll) {
		ResultClass<Activity> result = new ResultClass<>();
		result.addAll(daoActivity.getActivitiesForCourse(id_course, showAll));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = true)
	public ResultClass<String> getNextCode() {
		ResultClass<String> result = new ResultClass<String>();
		result.setSingleElement(daoActivity.getNextCode());
		return result;

	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<Activity> getActivityByName(String string) {
		ResultClass<Activity> result = new ResultClass<Activity>();
		result.setSingleElement(daoActivity.getActivityByName(string));
		return result;
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PROFESSOR')")
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultClass<Boolean> deleteLearningActivity(Long id_learningGoalStatus,
			Long id_Activity) {
		
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		Activity a = daoActivity.getActivity(id_Activity);



		Collection<LearningGoalStatus> c = a.getLearningGoalStatus();
		LearningGoal learningGoal = serviceLearningGoal.getLearningGoal(id_learningGoalStatus).getSingleElement();
		try {
			for (LearningGoalStatus aux : c) {
				if (aux.getLearningGoal().equals(learningGoal)) {
					a.getLearningGoalStatus().remove(aux);
					break;

				}

			}
			result.setSingleElement(daoActivity.saveActivity(a));
			

		} catch (Exception e) {
			result.setSingleElement(false);
		}
		return result;
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PROFESSOR')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> addLearningGoals(Long id, LearningGoalStatus learningGoalStatus) {
		Activity p = daoActivity.getActivity(id);
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		if (learningGoalStatus.getPercentage() <= 0.0
				|| learningGoalStatus.getPercentage() > 100.0){
			result.setSingleElement(false);
			
		}
		else {
			p.getLearningGoalStatus().add(learningGoalStatus);
			result.setSingleElement(daoActivity.saveActivity(p));
		}
		
		
		return result;
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PROFESSOR')")
	public ResultClass<Boolean> deleteActivitiesFromCourses(Collection<Course> courses) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		result.setSingleElement(daoActivity.deleteActivitiesFromCourses(courses));
		return result;
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PROFESSOR')")
	public ResultClass<Boolean> deleteActivitiesFromCourse(Course course) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		result.setSingleElement(daoActivity.deleteActivitiesFromCourse(course));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = false)
	public ResultClass<Activity> unDeleteActivity(Activity activity) {
		Activity a = daoActivity.existByCode(activity.getInfo().getCode());
		ResultClass<Activity> result = new ResultClass<>();
		if(a == null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code doesn't exist");
			result.setErrorsList(errors);

		}
		else{
			if(!a.getIsDeleted()){
				Collection<String> errors = new ArrayList<String>();
				errors.add("Code is not deleted");
				result.setErrorsList(errors);
			}

			a.setDeleted(false);
			a.setInfo(activity.getInfo());
			boolean r = daoActivity.saveActivity(a);
			if(r) 
				result.setSingleElement(a);	

		}
		return result;
	}
	

}
