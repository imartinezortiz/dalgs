package com.example.tfg.service.implementation;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.tfg.domain.Activity;
import com.example.tfg.domain.CompetenceStatus;
import com.example.tfg.repository.ActivityDao;
import com.example.tfg.repository.CourseDao;
import com.example.tfg.service.ActivityService;

@Service
public class ActivityServiceImp implements ActivityService {

	@Autowired
	private ActivityDao daoActivity;

	@Autowired
	private CourseDao daoCourse;

	@Transactional(readOnly = false)
	public boolean addActivity(Activity activity) {
		if (!daoActivity.existByCode(activity.getCode()))
			return daoActivity.addActivity(activity);
		else
			return false;

	}

	@Transactional(readOnly = true)
	public List<Activity> getAll() {
		return daoActivity.getAll();
	}

	@Transactional(readOnly = false)
	public boolean modifyActivity(Activity activity) {
		return daoActivity.saveActivity(activity);

	}

	@Transactional(readOnly = false)
	public Activity getActivity(long id) {
		return daoActivity.getActivity(id);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteActivity(long id) {
		return daoActivity.deleteActivity(id);
	}

	@Transactional(readOnly = false)
	public List<Activity> getActivitiesForCourse(long id_course) {
		return daoActivity.getActivitiesForCourse(id_course);
	}

	@Transactional(readOnly = true)
	public String getNextCode() {
		return daoActivity.getNextCode();

	}

/*	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteActivityFromCourse(long id_course, long id_activity) {

		Collection<Activity> c = daoCourse.getCourse(id_course).getActivities();
		try {
			c.remove(daoActivity.getActivity(id_activity));
			return true;

		} catch (Exception e) {
			return false;
		}
		
		return daoActivity.deleteActivity(id_activity); 
	}*/

	@Transactional(readOnly = true)
	public Activity getActivityByName(String string) {
		// TODO Auto-generated method stub
		return daoActivity.getActivityByName(string);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteCompetenceActivity(long id_competenceStatus,
			long id_Activity) {
		Activity a = daoActivity.getActivity(id_Activity);
		Collection<CompetenceStatus> c = a.getCompetenceStatus();
		try {
			for (CompetenceStatus aux: c){
				if(aux.getId_competence() == id_competenceStatus){
					a.getCompetenceStatus().remove(aux);
					
					//daoActivity.saveActivity(c);
					return true;
				}
			}
			return false;

		} catch (Exception e) {
			return false;
		}
	
	}

}
