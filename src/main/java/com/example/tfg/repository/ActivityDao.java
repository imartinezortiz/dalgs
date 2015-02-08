package com.example.tfg.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.tfg.domain.Activity;
import com.example.tfg.domain.Course;

@Repository
public interface ActivityDao {
	public boolean addActivity(Activity activity);

	public List<Activity> getAll();

	public boolean saveActivity(Activity activity);

	public Activity getActivity(Long id);

	public boolean deleteActivity(Long id);

	public List<Activity> getActivitiesForCourse(Long id_subject);

	public boolean existByCode(String code);

	public String getNextCode();

	public Activity getActivityByName(String string);

	public boolean existsLearningGoalStatus(Long id_activity, Long id_competence);

	public boolean deleteActivitiesFromCourse(Course course);

}
