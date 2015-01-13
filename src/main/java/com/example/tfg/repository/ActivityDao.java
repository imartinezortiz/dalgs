package com.example.tfg.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.tfg.domain.Activity;
@Repository
public interface ActivityDao {
	public boolean addActivity(Activity activity);
	public List<Activity> getAll();
    public boolean saveActivity(Activity activity);
    public Activity getActivity(long id);
	public boolean deleteActivity(long id);
	public List<Activity> getActivitiesForCourse(long id_subject);

	public boolean existByCode(String code);
	public String getNextCode();
	public Activity getActivityByName(String string);


}


