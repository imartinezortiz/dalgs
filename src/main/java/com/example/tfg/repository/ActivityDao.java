package com.example.tfg.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.tfg.domain.Activity;
import com.example.tfg.domain.Course;
import com.example.tfg.domain.LearningGoal;

@Repository
public interface ActivityDao {
	public boolean addActivity(Activity activity);

	public List<Activity> getAll();

	public boolean saveActivity(Activity activity);

	public Activity getActivity(Long id);

	public boolean deleteActivity(Long id);

	public List<Activity> getActivitiesForCourse(Long id_subject);

	public Activity existByCode(String code);

	public String getNextCode();

	public Activity getActivityByName(String string);

//	public boolean existsLearningGoalStatus(Long id_activity, LearningGoal learningGoal);

	public boolean deleteActivitiesFromCourses(Collection<Course> courses);
	
	public boolean deleteActivitiesFromCourse(Course course);

	public Collection<Activity> getActivitiesForLearningGoal(
			LearningGoal learningGoal);



}
