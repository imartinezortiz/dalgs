package com.example.tfg.service.implementation;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

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

	@Transactional(readOnly = false)
	public boolean addActivity(Activity activity, Long id_course) {

		//		Course course = serviceCourse.getCourse(id_course);
		//		Activity existActivity = daoActivity.existByCode(activity.getInfo().getCode());
		//		if(existActivity == null){
		//			course.getActivities().add(activity);
		//			activity.setCourse(course);
		//			return daoActivity.addActivity(activity);			
		//		}
		//		else if (existActivity.isDeleted()){
		//			existActivity.setCourse(course);
		//			existActivity.setDeleted(false);
		//			course.getActivities().add(existActivity);
		//			boolean r = daoActivity.saveActivity(existActivity);	
		//			return daoActivity.saveActivity(existActivity);	
		//			
		//		}
		//		else return false;



		activity.setCourse(serviceCourse.getCourse(id_course));

		//		try{
		//			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		//		
		//		User u =  serviceUser.findByUsername(((User) auth.getPrincipal()).getUsername()); //domain tfg
		//		
		//		if  ((u.getRole().getRole()==1) ||//ROLE_ADMIN
		//				((u.getRole().getRole()==3) && serviceCourse.isPermitted(u.getId(), id_course)))
		if (!daoActivity.existByCode(activity.getInfo().getCode()))
			return daoActivity.addActivity(activity);



		//		}
		//		catch (NotFoundException nfe){
		//		//logger
		return false;
	}



	@Transactional(readOnly = true)
	public List<Activity> getAll() {
		return daoActivity.getAll();
	}

	@Transactional(readOnly = false)
	public boolean modifyActivity(Activity activity, Long id_activity,
			Long id_course) {

		Activity activityModify =  daoActivity.getActivity(id_activity);
		activityModify.setInfo(activity.getInfo());
		//	activityModify.setLearningGoalStatus(activity.getLearningGoalStatus());
		return daoActivity.saveActivity(activityModify);

		//		activity.setId(id_activity);
		//		activity.setCourse(serviceCourse.getCourse(id_course));
		//		activity.setLearningGoalStatus(daoActivity.getActivity(id_activity)
		//				.getLearningGoalStatus());
		//		return daoActivity.saveActivity(activity);

	}

	@Transactional(readOnly = false)
	public Activity getActivity(Long id) {
		return daoActivity.getActivity(id);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteActivity(Long id) {

		return daoActivity.deleteActivity(id);
	}

	@Transactional(readOnly = true)
	public List<Activity> getActivitiesForCourse(Long id_course) {
		return daoActivity.getActivitiesForCourse(id_course);
	}

	@Transactional(readOnly = true)
	public String getNextCode() {
		return daoActivity.getNextCode();

	}

	/*
	 * @Transactional(propagation = Propagation.REQUIRED) public boolean
	 * deleteActivityFromCourse(Long id_course, Long id_activity) {
	 * 
	 * Collection<Activity> c = daoCourse.getCourse(id_course).getActivities();
	 * try { c.remove(daoActivity.getActivity(id_activity)); return true;
	 * 
	 * } catch (Exception e) { return false; }
	 * 
	 * return daoActivity.deleteActivity(id_activity); }
	 */

	@Transactional(readOnly = true)
	public Activity getActivityByName(String string) {
		// TODO Auto-generated method stub
		return daoActivity.getActivityByName(string);
	}

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

	// @Transactional(readOnly = true)
	// public boolean existsLearningGoalStatus(Long id_activity, Long
	// id_competence) {
	// return daoActivity.existsLearningGoalStatus(id_activity, id_competence);
	// }

	@Transactional(readOnly = false)
	public boolean addLearningGoals(Long id, LearningGoalStatus learningGoalStatus) {
		Activity p = daoActivity.getActivity(id);
		//		if (daoActivity.existsLearningGoalStatus(id, learningGoalStatus.getLearningGoal()))
		//			return false;
		if (learningGoalStatus.getPercentage() <= 0.0
				|| learningGoalStatus.getPercentage() > 100.0)
			return false;
		p.getLearningGoalStatus().add(learningGoalStatus);
		//		if (daoActivity.existsLearningGoalStatus(id, learningGoalStatus
		//				.getLearningGoal().getId()))
		//			return false;
		//		if (learningGoalStatus.getPercentage() <= 0.0
		//				|| learningGoalStatus.getPercentage() > 100.0)
		//			return false;
		//		p.getLearningGoalStatus().add(learningGoalStatus);
		return daoActivity.saveActivity(p);
	}

	public boolean deleteActivitiesFromCourses(Collection<Course> courses) {
		return daoActivity.deleteActivitiesFromCourses(courses);
	}


	public boolean deleteActivitiesFromCourse(Course course) {

		return daoActivity.deleteActivitiesFromCourse(course);
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




}
