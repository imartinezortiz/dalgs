package es.ucm.fdi.dalgs.activity.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PostFilter;
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

	@Autowired
	private MessageSource messageSource;

	@PreAuthorize("hasPermission(#course, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Activity> addActivityCourse(Course course,
			Activity activity, Long id_course, Long id_academic, Locale locale) {

		boolean success = false;

		Activity activityExists = daoActivity.existByCode(activity.getInfo()
				.getCode());
		ResultClass<Activity> result = new ResultClass<>();

		if (activityExists != null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add(messageSource.getMessage("error.Code", null, locale));

			if (activityExists.getIsDeleted()) {
				result.setElementDeleted(true);
				errors.add(messageSource.getMessage("error.deleted", null,
						locale));
				result.setSingleElement(activityExists);

			} else
				result.setSingleElement(activity);
			result.setErrorsList(errors);
		} else {
			activity.setCourse(serviceCourse.getCourse(id_course, id_academic)
					.getSingleElement());
			success = daoActivity.addActivity(activity);

			if (success) {
				activityExists = daoActivity.existByCode(activity.getInfo()
						.getCode());
				success = manageAclService.addACLToObject(activityExists
						.getId(), activityExists.getClass().getName());

				manageAclService.addPermissionToAnObject_ADMINISTRATION(
						course.getCoordinator(), activityExists.getId(),
						activityExists.getClass().getName());

				// Rest of users which belong to this course need READ
				// permission
				for (Group g : course.getGroups()) {
					manageAclService.addPermissionToAnObjectCollection_READ(
							g.getProfessors(), activityExists.getId(),
							activityExists.getClass().getName());
					manageAclService.addPermissionToAnObjectCollection_READ(
							g.getStudents(), activityExists.getId(),
							activityExists.getClass().getName());
				}

				if (success)
					result.setSingleElement(activityExists);

			} else {
				throw new IllegalArgumentException(
						"Cannot create ACL. Object not set.");

			}
		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	@Transactional(readOnly = true)
	public ResultClass<Activity> getAll() {
		ResultClass<Activity> result = new ResultClass<>();
		result.addAll(daoActivity.getAll());
		return result;
	}

	@PreAuthorize("hasPermission(#course, 'ADMINISTRATION') or hasPermission(#group, 'ADMINISTRATION') ")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> modifyActivity(Course course, Group group,
			Activity activity, Long id_activity, Long id_course,
			Long id_academic, Locale locale) {

		ResultClass<Boolean> result = new ResultClass<Boolean>();

		Activity modifyActivity = new Activity();

		if (group != null) {
			modifyActivity = daoActivity.getActivity(id_activity, id_course,
					group.getId(), id_academic);
		} else if (course != null) {
			modifyActivity = daoActivity.getActivity(id_activity,
					course.getId(), null, id_academic);
		}

		Activity activityExists = daoActivity.existByCode(activity.getInfo()
				.getCode());

		if (!activity.getInfo().getCode()
				.equalsIgnoreCase(modifyActivity.getInfo().getCode())
				&& activityExists != null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add(messageSource.getMessage("error.newCode", null, locale));

			if (activityExists.getIsDeleted()) {
				result.setElementDeleted(true);
				errors.add(messageSource.getMessage("error.deleted", null,
						locale));

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
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Activity> getActivity(Long id, Long id_course,
			Long id_group, Long id_academic) {
		ResultClass<Activity> result = new ResultClass<Activity>();
		Activity activity = daoActivity.getActivity(id, id_course, id_group,
				id_academic);

		if (activity == null)
			result.setHasErrors(true);
		else
			result.setSingleElement(activity);

		return result;
	}
	
	

	@Transactional(readOnly = true)
	public ResultClass<Activity> getActivityREST(Long id) {
		ResultClass<Activity> result = new ResultClass<Activity>();
		result.setSingleElement(daoActivity.getActivityFormatter(id));

		return result;
	}

	@PreAuthorize("hasPermission(#course, 'ADMINISTRATION') or hasPermission(#group, 'ADMINISTRATION') ")
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultClass<Boolean> deleteActivity(Course course, Group group,
			Long id) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		result.setSingleElement(daoActivity.deleteActivity(id));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	public ResultClass<Activity> getActivitiesForCourse(Long id_course,
			Boolean showAll) {
		ResultClass<Activity> result = new ResultClass<>();
		result.addAll(daoActivity.getActivitiesForCourse(id_course, showAll));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	@Transactional(readOnly = true)
	public ResultClass<Activity> getActivityByName(String string) {
		ResultClass<Activity> result = new ResultClass<Activity>();
		result.setSingleElement(daoActivity.getActivityByName(string));
		return result;
	}

	@PreAuthorize("hasPermission(#course, 'ADMINISTRATION') or hasPermission(#group, 'ADMINISTRATION') ")
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultClass<Boolean> deleteLearningActivity(Course course,
			Group group, Long id_learningGoalStatus, Long id_activity,
			Long id_academic) {

		ResultClass<Boolean> result = new ResultClass<Boolean>();
		Activity a = new Activity();

		if (group != null) {
			a = daoActivity.getActivity(id_activity, null, group.getId(),
					id_academic);
		} else if (course != null) {
			a = daoActivity.getActivity(id_activity, course.getId(), null,
					id_academic);
		}

		Collection<LearningGoalStatus> c = a.getLearningGoalStatus();
		LearningGoal learningGoal = serviceLearningGoal.getLearningGoal(
				id_learningGoalStatus, null, null).getSingleElement();
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
	public ResultClass<Boolean> addLearningGoals(Course course, Group group,
			Long id, LearningGoalStatus learningGoalStatus, Long id_course,
			Long id_academic) {
		Activity activity = new Activity();

		if (group != null) {
			activity = daoActivity.getActivity(id, id_course, group.getId(),
					id_academic);
		} else if (course != null) {
			activity = daoActivity
					.getActivity(id, id_course, null, id_academic);
		}

		ResultClass<Boolean> result = new ResultClass<Boolean>();
		if (learningGoalStatus.getWeight() <= 0.0
				|| learningGoalStatus.getWeight() > 100.0) {
			result.setSingleElement(false);

		} else {
			activity.getLearningGoalStatus().add(learningGoalStatus);
			result.setSingleElement(daoActivity.saveActivity(activity));
		}

		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResultClass<Boolean> deleteActivitiesFromCourses(
			Collection<Course> courses) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		result.setSingleElement(daoActivity
				.deleteActivitiesFromCourses(courses));
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
	public ResultClass<Activity> unDeleteActivity(Course course, Group group,
			Activity activity, Locale locale) {
		Activity a = daoActivity.existByCode(activity.getInfo().getCode());
		ResultClass<Activity> result = new ResultClass<>();
		if (a == null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add(messageSource.getMessage("error.ElementNoExists", null,
					locale));
			result.setErrorsList(errors);

		} else {
			if (!a.getIsDeleted()) {
				Collection<String> errors = new ArrayList<String>();
				errors.add(messageSource.getMessage("error.CodeNoDeleted",
						null, locale));
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

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	public ResultClass<Activity> getActivitiesForGroup(Long id_group,
			Boolean showAll) {
		ResultClass<Activity> result = new ResultClass<>();
		result.addAll(daoActivity.getActivitiesForGroup(id_group, showAll));
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
			Activity activity, Long id_group, Long id_course, Long id_academic) {
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
			activity.setGroup(serviceGroup.getGroup(id_group, id_course,
					id_academic).getSingleElement());
			success = daoActivity.addActivity(activity);

			if (success) {
				activityExists = daoActivity.existByCode(activity.getInfo()
						.getCode());
				success = manageAclService.addACLToObject(activityExists
						.getId(), activityExists.getClass().getName());

				// Rest of users which belong to this course need READ
				// permission
				manageAclService.addPermissionToAnObjectCollection_READ(
						group.getProfessors(), activityExists.getId(),
						activityExists.getClass().getName());
				manageAclService.addPermissionToAnObjectCollection_READ(
						group.getStudents(), activityExists.getId(),
						activityExists.getClass().getName());

				if (success)
					result.setSingleElement(activityExists);

			} else {
				throw new IllegalArgumentException(
						"Cannot create ACL. Object not set.");

			}
		}
		return result;
	}

	@PreAuthorize("hasPermission(#group, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Activity> addActivitiestoGroup(Group group,
			Collection<Activity> activities, Long id_group, Long id_course,
			Long id_academic) {
		ResultClass<Activity> result = null;
		for (Activity a : activities)
			result = this.addActivitytoGroup(group, a, id_group, id_course,
					id_academic);

		result.setHasErrors(false);
		return result;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public void dowloadCSV(HttpServletResponse response) throws IOException {
		Collection<Activity> activities = new ArrayList<Activity>();
		activities = daoActivity.getAll();
		
		if(!activities.isEmpty()){
			ActivityCSV activityCSV = new ActivityCSV();
			activityCSV.dowloadCSV(response, activities);
		}
	}

}


