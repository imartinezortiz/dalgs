package com.example.tfg.service.implementation;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.tfg.domain.Activity;
import com.example.tfg.domain.CompetenceStatus;
import com.example.tfg.domain.Course;
import com.example.tfg.repository.ActivityDao;
import com.example.tfg.service.ActivityService;
import com.example.tfg.service.CourseService;

@Service
public class ActivityServiceImp implements ActivityService {

	@Autowired
	private ActivityDao daoActivity;

	@Autowired
	private CourseService serviceCourse;

	@Transactional(readOnly = false)
	public boolean addActivity(Activity activity, Long id_course) {
		activity.setCourse(serviceCourse.getCourse(id_course));
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
	public boolean modifyActivity(Activity activity, Long id_activity,
			Long id_course) {
		activity.setId(id_activity);
		activity.setCourse(serviceCourse.getCourse(id_course));
		activity.setCompetenceStatus(daoActivity.getActivity(id_activity)
				.getCompetenceStatus());
		return daoActivity.saveActivity(activity);

	}

	@Transactional(readOnly = false)
	public Activity getActivity(Long id) {
		return daoActivity.getActivity(id);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteActivity(Long id) {

		return daoActivity.deleteActivity(id);
	}

	@Transactional(readOnly = false)
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
	public boolean deleteCompetenceActivity(Long id_competenceStatus,
			Long id_Activity) {
		Activity a = daoActivity.getActivity(id_Activity);
		Collection<CompetenceStatus> c = a.getCompetenceStatus();
		try {
			for (CompetenceStatus aux : c) {
				if (aux.getCompetence().getId() == id_competenceStatus) {
					a.getCompetenceStatus().remove(aux);
					break;

				}

			}
			return daoActivity.saveActivity(a);

		} catch (Exception e) {
			return false;
		}

	}

	// @Transactional(readOnly = true)
	// public boolean existsCompetenceStatus(Long id_activity, Long
	// id_competence) {
	// return daoActivity.existsCompetenceStatus(id_activity, id_competence);
	// }

	@Transactional(readOnly = false)
	public boolean addCompetences(Long id, CompetenceStatus competencestatus) {
		Activity p = daoActivity.getActivity(id);
		if (daoActivity.existsCompetenceStatus(id, competencestatus
				.getCompetence().getId()))
			return false;
		if (competencestatus.getPercentage() <= 0.0
				|| competencestatus.getPercentage() > 100.0)
			return false;
		p.getCompetenceStatus().add(competencestatus);
		return daoActivity.saveActivity(p);
	}

	public boolean deleteActivitiesFromCourse(Course course) {
		return daoActivity.deleteActivitiesFromCourse(course);
	}

}
