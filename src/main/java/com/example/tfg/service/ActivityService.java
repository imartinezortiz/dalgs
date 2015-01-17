package com.example.tfg.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tfg.domain.Activity;


@Service
public interface ActivityService {
	public boolean addActivity(Activity activity);
	public List<Activity> getAll();
	public boolean modifyActivity(Activity activity);
	public  Activity getActivity(Long id);
	public boolean deleteActivity(Long id);
	public List<Activity> getActivitiesForCourse(Long id_course);
	public String getNextCode();
	//public boolean deleteActivityFromCourse(Long id_course, Long id_activity);
	public Activity getActivityByName(String string);
	public boolean deleteCompetenceActivity(Long id_competenceStatus,
			Long id_Activity);

}
