package es.ucm.fdi.dalgs.activity.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.dalgs.acl.service.AclObjectService;
import es.ucm.fdi.dalgs.activity.repository.ActivityRepository;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.course.service.CourseService;
import es.ucm.fdi.dalgs.domain.Activity;
import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.domain.Group;
import es.ucm.fdi.dalgs.domain.LearningGoal;
import es.ucm.fdi.dalgs.domain.LearningGoalStatus;
import es.ucm.fdi.dalgs.group.service.GroupService;
import es.ucm.fdi.dalgs.learningGoal.service.LearningGoalService;
import es.ucm.fdi.dalgs.user.service.UserService;

@Service
public class ActivityService {
	@Autowired
	private ActivityRepository daoActivity;

	@Autowired
	private CourseService serviceCourse;

	@Autowired
	private GroupService serviceGroup;

	@Autowired
	private UserService serviceUser;

	@Autowired
	private AclObjectService manageAclService;

	@Autowired
	private LearningGoalService serviceLearningGoal;

	@PreAuthorize("hasPermission(#course, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Activity> addActivityCourse(Course course, Activity activity,
			Long id_course) {

		boolean success = false;

		Activity activityExists = daoActivity.existByCode(activity.getInfo()
				.getCode());
		ResultClass<Activity> result = new ResultClass<>();

		if (activityExists != null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code already exists");

			if (activityExists.getIsDeleted()) {
				result.setElementDeleted(true);
				errors.add("Element is deleted");
				result.setSingleElement(activityExists);

			} else
				result.setSingleElement(activity);
			result.setErrorsList(errors);
		} else {
			activity.setCourse(serviceCourse.getCourse(id_course)
					.getSingleElement());
			success = daoActivity.addActivity(activity);

			if (success) {
				activityExists = daoActivity.existByCode(activity.getInfo()
						.getCode());
				success = manageAclService.addAclToObject(activityExists
						.getId(), activityExists.getClass().getName());
				if (success)
					result.setSingleElement(activity);

			} else {
				throw new IllegalArgumentException(
						"Cannot create ACL. Object not set.");

			}
		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<Activity> getAll() {
		ResultClass<Activity> result = new ResultClass<>();
		result.addAll(daoActivity.getAll());
		return result;
	}

	@PreAuthorize("hasPermission(#course, 'ADMINISTRATION') or hasPermission(#group, 'ADMINISTRATION') ")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> modifyActivity(Course course, Group group, Activity activity,
			Long id_activity) {

		ResultClass<Boolean> result = new ResultClass<Boolean>();

		Activity modifyActivity = daoActivity.getActivity(id_activity);

		Activity activityExists = daoActivity.existByCode(activity.getInfo()
				.getCode());

		if (!activity.getInfo().getCode()
				.equalsIgnoreCase(modifyActivity.getInfo().getCode())
				&& activityExists != null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("New code already exists");

			if (activityExists.getIsDeleted()) {
				result.setElementDeleted(true);
				errors.add("Element is deleted");

			}
			result.setErrorsList(errors);
			result.setSingleElement(false);
		} else {
			modifyActivity.setInfo(activity.getInfo());
			boolean r = daoActivity.saveActivity(modifyActivity);
			if (r)
				result.setSingleElement(true);
		}
		return result;

	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = false)
	public ResultClass<Activity> getActivity(Long id) {
		ResultClass<Activity> result = new ResultClass<Activity>();
		result.setSingleElement(daoActivity.getActivity(id));
		return result;
	}

	@PreAuthorize("hasPermission(#course, 'ADMINISTRATION') or hasPermission(#group, 'ADMINISTRATION') ")
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultClass<Boolean> deleteActivity(Course course,Group group, Long id) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		result.setSingleElement(daoActivity.deleteActivity(id));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	public ResultClass<Activity> getActivitiesForCourse(Long id_course,
			Boolean showAll) {
		ResultClass<Activity> result = new ResultClass<>();
		result.addAll(daoActivity.getActivitiesForCourse(id_course, showAll));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<String> getNextCode() {
		ResultClass<String> result = new ResultClass<String>();
		result.setSingleElement(daoActivity.getNextCode());
		return result;

	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<Activity> getActivityByName(String string) {
		ResultClass<Activity> result = new ResultClass<Activity>();
		result.setSingleElement(daoActivity.getActivityByName(string));
		return result;
	}

	@PreAuthorize("hasPermission(#course, 'ADMINISTRATION') or hasPermission(#group, 'ADMINISTRATION') ")
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultClass<Boolean> deleteLearningActivity(Course course, Group group,
			Long id_learningGoalStatus, Long id_Activity) {

		ResultClass<Boolean> result = new ResultClass<Boolean>();
		Activity a = daoActivity.getActivity(id_Activity);

		Collection<LearningGoalStatus> c = a.getLearningGoalStatus();
		LearningGoal learningGoal = serviceLearningGoal.getLearningGoal(
				id_learningGoalStatus).getSingleElement();
		try {
			for (LearningGoalStatus aux : c) {
				if (aux.getLearningGoal().equals(learningGoal)) {
					a.getLearningGoalStatus().remove(aux);
					break;

				}

			}
			result.setSingleElement(daoActivity.saveActivity(a));

		} catch (Exception e) {
			result.setSingleElement(false);
		}
		return result;
	}

	@PreAuthorize("hasPermission(#course, 'ADMINISTRATION') or hasPermission(#group, 'ADMINISTRATION') ")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> addLearningGoals(Course course, Group group, Long id,
			LearningGoalStatus learningGoalStatus) {
		Activity p = daoActivity.getActivity(id);
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		if (learningGoalStatus.getPercentage() <= 0.0
				|| learningGoalStatus.getPercentage() > 100.0) {
			result.setSingleElement(false);

		} else {
			p.getLearningGoalStatus().add(learningGoalStatus);
			result.setSingleElement(daoActivity.saveActivity(p));
		}

		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResultClass<Boolean> deleteActivitiesFromCourses(
			Collection<Course> courses) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		result.setSingleElement(daoActivity.deleteActivitiesFromCourses(courses));
		return result;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResultClass<Boolean> deleteActivitiesFromGroups(
			Collection<Group> groups) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		result.setSingleElement(daoActivity.deleteActivitiesFromGroups(groups));
		return result;
	}

	@PreAuthorize("hasPermission(#course, 'ADMINISTRATION')")
	public ResultClass<Boolean> deleteActivitiesFromCourse(Course course) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		result.setSingleElement(daoActivity.deleteActivitiesFromCourse(course));
		return result;
	}

	@PreAuthorize("hasPermission(#course, 'ADMINISTRATION') or hasPermission(#group, 'ADMINISTRATION') ")
	@Transactional(readOnly = false)
	public ResultClass<Activity> unDeleteActivity(Course course, Group group, Activity activity) {
		Activity a = daoActivity.existByCode(activity.getInfo().getCode());
		ResultClass<Activity> result = new ResultClass<>();
		if (a == null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code doesn't exist");
			result.setErrorsList(errors);

		} else {
			if (!a.getIsDeleted()) {
				Collection<String> errors = new ArrayList<String>();
				errors.add("Code is not deleted");
				result.setErrorsList(errors);
			}

			a.setDeleted(false);
			a.setInfo(activity.getInfo());
			boolean r = daoActivity.saveActivity(a);
			if (r)
				result.setSingleElement(a);

		}
		return result;
	}
	
	@PreAuthorize("hasPermission(#group, 'ADMINISTRATION')")
	public ResultClass<Boolean> deleteActivitiesFromGroup(Group group) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		result.setSingleElement(daoActivity.deleteActivitiesFromGroup(group));
		return result;
	}

	@PreAuthorize("hasPermission(#group, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Activity> addActivitytoGroup(Group group,
			Activity activity, Long id_group) {
		boolean success = false;

		Activity activityExists = daoActivity.existByCode(activity.getInfo()
				.getCode());
		ResultClass<Activity> result = new ResultClass<>();

		if (activityExists != null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code already exists");

			if (activityExists.getIsDeleted()) {
				result.setElementDeleted(true);
				errors.add("Element is deleted");
				result.setSingleElement(activityExists);

			} else
				result.setSingleElement(activity);
			result.setErrorsList(errors);
		} else {
			activity.setGroup(serviceGroup.getGroup(id_group)
					.getSingleElement());
			success = daoActivity.addActivity(activity);

			if (success) {
				activityExists = daoActivity.existByCode(activity.getInfo()
						.getCode());
				success = manageAclService.addAclToObject(activityExists
						.getId(), activityExists.getClass().getName());
				if (success)
					result.setSingleElement(activity);

			} else {
				throw new IllegalArgumentException(
						"Cannot create ACL. Object not set.");

			}
		}
		return result;
	}
	
}
