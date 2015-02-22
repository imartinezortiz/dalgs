package com.example.tfg.service;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tfg.domain.Activity;
import com.example.tfg.domain.LearningGoal;
import com.example.tfg.domain.LearningGoalStatus;
import com.example.tfg.domain.Course;

@Service
public interface ActivityService {
	public boolean addActivity(Activity activity, Long id_course);

	public List<Activity> getAll();

	public boolean modifyActivity(Activity activity, Long id_activity,
			Long id_course);

	public Activity getActivity(Long id);

	public boolean deleteActivity(Long id);

	public List<Activity> getActivitiesForCourse(Long id_course);

	public String getNextCode();

	public Activity getActivityByName(String string);

	public boolean deleteLearningActivity(Long id_learningGoalStatus,
			Long id_Activity);

	public boolean addLearningGoals(Long id, LearningGoalStatus learningGoalStatus);

	public boolean deleteActivitiesFromCourses(Collection<Course> courses);

	public boolean deleteActivitiesFromCourse(Course course);

//	public boolean deleteLearningActivities(LearningGoal learningGoal);

	

}
