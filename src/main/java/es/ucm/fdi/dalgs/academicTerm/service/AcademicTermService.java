package es.ucm.fdi.dalgs.academicTerm.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.dalgs.academicTerm.repository.AcademicTermRepository;
import es.ucm.fdi.dalgs.acl.service.AclObjectService;
import es.ucm.fdi.dalgs.activity.service.ActivityService;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.course.service.CourseService;
import es.ucm.fdi.dalgs.domain.AcademicTerm;
import es.ucm.fdi.dalgs.domain.Activity;
import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.domain.Degree;
import es.ucm.fdi.dalgs.domain.Group;
import es.ucm.fdi.dalgs.group.service.GroupService;

@Service
public class AcademicTermService {

	@Autowired
	private AclObjectService manageAclService;

	@Autowired
	private AcademicTermRepository daoAcademicTerm;

	@Autowired
	private ActivityService serviceActivity;

	@Autowired
	private GroupService serviceGroup;

	@Autowired
	private CourseService serviceCourse;

	@Autowired
	private MessageSource messageSource;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<AcademicTerm> addAcademicTerm(AcademicTerm academicTerm,
			Locale locale) {

		boolean success = false;

		AcademicTerm academicExists = daoAcademicTerm.exists(
				academicTerm.getTerm(), academicTerm.getDegree());
		ResultClass<AcademicTerm> result = new ResultClass<>();

		if (academicExists != null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add(messageSource.getMessage("error.Code", null, locale));
			// errors.add(messageSource.getMessage("academicTermExists", null,
			// "default Error", locale));
			// errors.add(messageSource.getMessage("academicTermExists",
			// null,"Default Error", Locale.US));

			if (academicExists.getIsDeleted()) {
				result.setElementDeleted(true);
				errors.add(messageSource.getMessage("error.deleted", null,
						locale));
				result.setSingleElement(academicExists);

			} else
				result.setSingleElement(academicTerm);
			result.setErrorsList(errors);

		} else {
			success = daoAcademicTerm.addAcademicTerm(academicTerm);

			if (success) {
				academicExists = daoAcademicTerm.exists(academicTerm.getTerm(),
						academicTerm.getDegree());
				success = manageAclService.addACLToObject(academicExists
						.getId(), academicExists.getClass().getName());
				if (success)
					result.setSingleElement(academicTerm);

			} else {
				throw new IllegalArgumentException(
						"Cannot create ACL. Object not set.");
			}

		}
		return result;

	}

	@PreAuthorize("hasPermission(#academicTerm, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> modifyAcademicTerm(AcademicTerm academicTerm,
			Long id_academic, Locale locale) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		AcademicTerm modifyAcademic = daoAcademicTerm
				.getAcademicTermById(id_academic);

		AcademicTerm academicExists = daoAcademicTerm.exists(
				academicTerm.getTerm(), modifyAcademic.getDegree());

		if (!academicTerm.getTerm().equalsIgnoreCase(modifyAcademic.getTerm())
				&& academicExists != null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add(messageSource.getMessage("error.newCode", null, locale));

			if (academicExists.getIsDeleted()) {
				result.setElementDeleted(true);
				errors.add(messageSource.getMessage("error.deleted", null,
						locale));

			}
			result.setErrorsList(errors);
			result.setSingleElement(false);
		} else {
			modifyAcademic.setTerm(academicTerm.getTerm());
			boolean r = daoAcademicTerm.saveAcademicTerm(modifyAcademic);
			if (r)
				result.setSingleElement(true);
		}
		return result;

	}

	/**
	 * Retrieves all academic terms.
	 * <p>
	 * Access-control will be evaluated after this method is invoked.
	 * filterObject refers to the returned object list.
	 */
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	@Transactional(readOnly = true)
	public ResultClass<AcademicTerm> getAcademicTerms(Integer pageIndex,
			Boolean showAll) {
		ResultClass<AcademicTerm> result = new ResultClass<>();
		result.addAll(daoAcademicTerm.getAcademicsTerm(pageIndex, showAll));

		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	// propagation = Propagation.REQUIRED)
	public ResultClass<Boolean> deleteAcademicTerm(AcademicTerm academicTerm) {
		boolean success = false;
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		if (academicTerm.getCourses().isEmpty()
				|| serviceCourse.deleteCoursesFromAcademic(academicTerm)
						.getSingleElement()) {

			success = daoAcademicTerm.deleteAcademicTerm(academicTerm.getId());
		}
		result.setSingleElement(success);
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = false)
	public ResultClass<Integer> numberOfPages(Boolean showAll) {
		ResultClass<Integer> result = new ResultClass<Integer>();
		result.setSingleElement(daoAcademicTerm.numberOfPages(showAll));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<AcademicTerm> getAcademicTermsByDegree(Degree degree) {
		ResultClass<AcademicTerm> result = new ResultClass<>();
		result.addAll(daoAcademicTerm.getAcademicTermsByDegree(degree));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	@Transactional(readOnly = true)
	public ResultClass<AcademicTerm> getAcademicTerm(Long id_academic,
			Boolean showAll) {
		ResultClass<AcademicTerm> result = new ResultClass<AcademicTerm>();
		AcademicTerm aT = daoAcademicTerm.getAcademicTermById(id_academic);
		if (aT != null) {
			Collection<Course> courses = new ArrayList<Course>();
			courses.addAll(serviceCourse.getCoursesByAcademicTerm(id_academic,
					showAll));

			aT.setCourses(courses);
			result.setSingleElement(aT);
		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> deleteAcademicTermCollection(
			Collection<AcademicTerm> academicList) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		boolean deleteCourses = serviceCourse.deleteCourses(academicList)
				.getSingleElement();
		if (deleteCourses)
			result.setSingleElement(daoAcademicTerm
					.deleteAcademicTerm(academicList));
		else
			result.setSingleElement(false);

		return result;
	}

	@PreAuthorize("hasPermission(#academicTerm, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<AcademicTerm> restoreAcademic(AcademicTerm academicTerm,
			Locale locale) {
		AcademicTerm a = daoAcademicTerm.exists(academicTerm.getTerm(),
				academicTerm.getDegree());
		ResultClass<AcademicTerm> result = new ResultClass<>();

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
			a.setTerm(academicTerm.getTerm());
			boolean r = daoAcademicTerm.saveAcademicTerm(a);
			if (r)
				result.setSingleElement(a);

		}
		return result;

	}

	@PreAuthorize("hasPermission(#academicTerm, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<AcademicTerm> copyAcademicTerm(
			AcademicTerm academicTerm, Locale locale) {
		AcademicTerm copy = academicTerm.depth_copy();

		ResultClass<AcademicTerm> result = new ResultClass<>();

		if (copy == null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Copy doesn't work");
			result.setErrorsList(errors);

		} else {
			copy.setTerm(copy.getTerm() + " (copy)");

			for (Course c : copy.getCourses()) {

				// c.setActivities(null);

				for (Activity a : c.getActivities()) {

					// a.setGroup(null);
					a.getInfo().setCode(a.getInfo().getCode() + " (copy)");
				}

				for (Group g : c.getGroups()) {
					g.setName(g.getName() + " (copy)");
					for (Activity a : g.getActivities()) {
;
						a.getInfo().setCode(a.getInfo().getCode() + " (copy)");
					}
				}

			}

			boolean success = daoAcademicTerm.addAcademicTerm(copy);
			if (success) {
				AcademicTerm exists = daoAcademicTerm.exists(copy.getTerm(),
						copy.getDegree());
				if (exists != null) {
					result.setSingleElement(exists);
					manageAclService.addACLToObject(exists.getId(), exists
							.getClass().getName());

					for (Course c : exists.getCourses()) {
						success = success
								&& manageAclService.addACLToObject(c.getId(), c
										.getClass().getName());

						for (Activity a : c.getActivities()) {
							success = success
									&& manageAclService.addACLToObject(
											a.getId(), a.getClass().getName());
						}

						for (Group g : c.getGroups()) {
							success = success
									&& manageAclService.addACLToObject(
											g.getId(), g.getClass().getName());

							for (Activity a : g.getActivities()) {
								success = success
										&& manageAclService.addACLToObject(a
												.getId(), a.getClass()
												.getName());
							}
						}
					}
				}
			}

			result.setHasErrors(!success);

		}
		return result;
	}

}
