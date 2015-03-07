package es.ucm.fdi.dalgs.activity.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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
	private LearningGoalService serviceLearningGoal;

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PROFESSOR')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> addActivity(Activity activity, Long id_course) {

		Activity activityExists = daoActivity.existByCode(activity.getInfo().getCode());
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		if( activityExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code already exists");

			if (activityExists.getIsDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");

			}
			result.setE(false);
			result.setErrorsList(errors);
		}
		else{
			activity.setCourse(serviceCourse.getCourse(id_course).getE());
			boolean r = daoActivity.addActivity(activity);
			if (r) 
				result.setE(true);
		}
		return result;		
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<List<Activity>> getAll() {
		ResultClass<List<Activity>> result = new ResultClass<List<Activity>>();
		result.setE(daoActivity.getAll());
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
			result.setE(false);
		}
		else{
			modifyActivity.setInfo(activity.getInfo());
			boolean r = daoActivity.saveActivity(modifyActivity);
			if (r) 
				result.setE(true);
		}
		return result;

	}
	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = false)
	public ResultClass<Activity> getActivity(Long id) {
		ResultClass<Activity> result = new ResultClass<Activity>();
		result.setE(daoActivity.getActivity(id));
		return result;
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PROFESSOR')")
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultClass<Boolean> deleteActivity(Long id) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		result.setE(daoActivity.deleteActivity(id));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<List<Activity>> getActivitiesForCourse(Long id_course) {
		ResultClass<List<Activity>> result = new ResultClass<List<Activity>>();
		result.setE(daoActivity.getActivitiesForCourse(id_course));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = true)
	public ResultClass<String> getNextCode() {
		ResultClass<String> result = new ResultClass<String>();
		result.setE(daoActivity.getNextCode());
		return result;

	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<Activity> getActivityByName(String string) {
		ResultClass<Activity> result = new ResultClass<Activity>();
		result.setE(daoActivity.getActivityByName(string));
		return result;
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PROFESSOR')")
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultClass<Boolean> deleteLearningActivity(Long id_learningGoalStatus,
			Long id_Activity) {
		
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		Activity a = daoActivity.getActivity(id_Activity);



		Collection<LearningGoalStatus> c = a.getLearningGoalStatus();
		LearningGoal learningGoal = serviceLearningGoal.getLearningGoal(id_learningGoalStatus).getE();
		try {
			for (LearningGoalStatus aux : c) {
				if (aux.getLearningGoal().equals(learningGoal)) {
					a.getLearningGoalStatus().remove(aux);
					break;

				}

			}
			result.setE(daoActivity.saveActivity(a));
			

		} catch (Exception e) {
			result.setE(false);
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
			result.setE(false);
			
		}
		else {
			p.getLearningGoalStatus().add(learningGoalStatus);
			result.setE(daoActivity.saveActivity(p));
		}
		
		
		return result;
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PROFESSOR')")
	public ResultClass<Boolean> deleteActivitiesFromCourses(Collection<Course> courses) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		result.setE(daoActivity.deleteActivitiesFromCourses(courses));
		return result;
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PROFESSOR')")
	public ResultClass<Boolean> deleteActivitiesFromCourse(Course course) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		result.setE(daoActivity.deleteActivitiesFromCourse(course));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = false)
	public ResultClass<Boolean> unDeleteActivity(Activity activity) {
		Activity a = daoActivity.existByCode(activity.getInfo().getCode());
		ResultClass<Boolean> result = new ResultClass<Boolean>();
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
				result.setE(true);	

		}
		return result;
	}
	

}
