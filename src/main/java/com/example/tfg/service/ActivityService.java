package com.example.tfg.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tfg.domain.Activity;


@Service
public interface ActivityService {
	public boolean addActivity(Activity activity);
	public List<Activity> getAll();
	public boolean modifyActivity(Activity activity);
	public  Activity getActivity(long id);
	public boolean deleteActivity(long id);
	public List<Activity> getActivitiesForCourse(long id_course);
	public String getNextCode();
	//public boolean deleteActivityFromCourse(long id_course, long id_activity);
	public Activity getActivityByName(String string);
	public boolean deleteCompetenceActivity(long id_competenceStatus,
			long id_Activity);

}
