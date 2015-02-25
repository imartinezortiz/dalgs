package com.example.tfg.service.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.tfg.classes.ResultClass;
import com.example.tfg.domain.Activity;
import com.example.tfg.domain.Course;
import com.example.tfg.domain.LearningGoal;
import com.example.tfg.domain.LearningGoalStatus;
import com.example.tfg.repository.ActivityDao;
import com.example.tfg.service.ActivityService;
import com.example.tfg.service.CourseService;
import com.example.tfg.service.LearningGoalService;
import com.example.tfg.service.UserService;

@Service
public class ActivityServiceImp implements ActivityService {

	@Autowired
	private ActivityDao daoActivity;

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

			if (activityExists.isDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");

			}
			result.setE(false);
			result.setErrorsList(errors);
		}
		else{
			activity.setCourse(serviceCourse.getCourse(id_course));
			boolean r = daoActivity.addActivity(activity);
			if (r) 
				result.setE(true);
		}
		return result;		
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public List<Activity> getAll() {
		return daoActivity.getAll();
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

			if (activityExists.isDeleted()){
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
	public Activity getActivity(Long id) {
		return daoActivity.getActivity(id);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PROFESSOR')")
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteActivity(Long id) {

		return daoActivity.deleteActivity(id);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public List<Activity> getActivitiesForCourse(Long id_course) {
		return daoActivity.getActivitiesForCourse(id_course);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = true)
	public String getNextCode() {
		return daoActivity.getNextCode();

	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public Activity getActivityByName(String string) {
		// TODO Auto-generated method stub
		return daoActivity.getActivityByName(string);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PROFESSOR')")
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteLearningActivity(Long id_learningGoalStatus,
			Long id_Activity) {
		Activity a = daoActivity.getActivity(id_Activity);



		Collection<LearningGoalStatus> c = a.getLearningGoalStatus();
		LearningGoal learningGoal = serviceLearningGoal.getLearningGoal(id_learningGoalStatus);
		try {
			for (LearningGoalStatus aux : c) {
				if (aux.getLearningGoal().equals(learningGoal)) {
					a.getLearningGoalStatus().remove(aux);
					break;

				}

			}
			return daoActivity.saveActivity(a);

		} catch (Exception e) {
			return false;
		}

	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PROFESSOR')")
	@Transactional(readOnly = false)
	public boolean addLearningGoals(Long id, LearningGoalStatus learningGoalStatus) {
		Activity p = daoActivity.getActivity(id);

		if (learningGoalStatus.getPercentage() <= 0.0
				|| learningGoalStatus.getPercentage() > 100.0)
			return false;
		p.getLearningGoalStatus().add(learningGoalStatus);
		
		return daoActivity.saveActivity(p);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PROFESSOR')")
	public boolean deleteActivitiesFromCourses(Collection<Course> courses) {
		return daoActivity.deleteActivitiesFromCourses(courses);
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PROFESSOR')")
	public boolean deleteActivitiesFromCourse(Course course) {

		return daoActivity.deleteActivitiesFromCourse(course);
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
			if(!a.isDeleted()){
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

//	@Override
//	public boolean deleteLearningActivities(LearningGoal learningGoal) {
//		Collection <Activity> activities = daoActivity.getActivitiesForLearningGoal(learningGoal);
//		try{
//			for (Activity a : activities)
//			{
//				for(LearningGoalStatus lg : a.getLearningGoalStatus())
//				{
//					if (lg.getLearningGoal().equals(learningGoal)) {
//						a.getLearningGoalStatus().remove(lg);
//						break;
//					}
//				}
//				daoActivity.saveActivity(a);
//			}
//			return true;
//		}
//		catch(Exception e){
//			return false;
//		}
//		
//	}

//	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_PROFESSOR')")
//	public boolean deleteLearningActivities(LearningGoal learningGoal) {
//		Collection <Activity> activities = daoActivity.getActivitiesForLearningGoal(learningGoal);
//		try{
//			for (Activity a : activities)
//			{
//				for(LearningGoalStatus lg : a.getLearningGoalStatus())
//				{
//					if (lg.getLearningGoal().equals(learningGoal)) {
//						a.getLearningGoalStatus().remove(lg);
//						break;
//					}
//				}
//				daoActivity.saveActivity(a);
//			}
//			return true;
//		}
//		catch(Exception e){
//			return false;
//		}
//		
//	}




}
