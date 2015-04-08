package es.ucm.fdi.dalgs.learningGoal.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.dalgs.acl.service.AclObjectService;
import es.ucm.fdi.dalgs.activity.service.ActivityService;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.competence.service.CompetenceService;
import es.ucm.fdi.dalgs.domain.Activity;
import es.ucm.fdi.dalgs.domain.Competence;
import es.ucm.fdi.dalgs.domain.LearningGoal;
import es.ucm.fdi.dalgs.learningGoal.repository.LearningGoalRepository;

@Service
public class LearningGoalService {

	@Autowired
	CompetenceService serviceCompetence;

	@Autowired
	LearningGoalRepository daoLearningGoal;

	@Autowired
	ActivityService serviceActivity;

	@Autowired
	private AclObjectService manageAclService;

	@Autowired
	private MessageSource messageSource;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<LearningGoal> addLearningGoal(LearningGoal learningGoal,
			Long id_competence, Long id_degree, Locale locale) {

		boolean success = false;

		LearningGoal learningExists = daoLearningGoal.existByCode(learningGoal
				.getInfo().getCode());
		ResultClass<LearningGoal> result = new ResultClass<>();

		if (learningExists != null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<>();
			errors.add(messageSource.getMessage("error.Code", null, locale));

			if (learningExists.getIsDeleted()) {
				result.setElementDeleted(true);
				errors.add(messageSource.getMessage("error.deleted", null,
						locale));
				result.setSingleElement(learningExists);
			} else
				result.setSingleElement(learningGoal);
			result.setErrorsList(errors);
		} else {
			learningGoal.setCompetence(serviceCompetence.getCompetence(
					id_competence, id_degree).getSingleElement());
			success = daoLearningGoal.addLearningGoal(learningGoal);
			if (success) {
				learningExists = daoLearningGoal.existByCode(learningGoal
						.getInfo().getCode());
				success = manageAclService.addACLToObject(learningExists
						.getId(), learningExists.getClass().getName());
				if (success)
					result.setSingleElement(learningGoal);

			} else {
				throw new IllegalArgumentException(
						"Cannot create ACL. Object not set.");

			}
		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<LearningGoal> getLearningGoal(Long id_learningGoal,
			Long id_competence, Long id_degree) {
		ResultClass<LearningGoal> result = new ResultClass<>();
		if (id_competence == null && id_degree == null)
			result.setSingleElement(daoLearningGoal
					.getLearningGoalFormatter(id_learningGoal));
		else
			result.setSingleElement(daoLearningGoal.getLearningGoal(
					id_learningGoal, id_competence, id_degree));
		return result;
	}

	@PreAuthorize("hasPermission(#learningGoal, 'WRITE') or hasPermission(#learningGoal, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> modifyLearningGoal(LearningGoal learningGoal,
			Long id_learningGoal, Long id_competence, Long id_degree,
			Locale locale) {
		ResultClass<Boolean> result = new ResultClass<>();

		LearningGoal modifyLearning = daoLearningGoal.getLearningGoal(
				id_learningGoal, id_competence, id_degree);

		LearningGoal learningExists = daoLearningGoal.existByCode(learningGoal
				.getInfo().getCode());

		if (!learningGoal.getInfo().getCode()
				.equalsIgnoreCase(modifyLearning.getInfo().getCode())
				&& learningExists != null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<>();
			errors.add(messageSource.getMessage("error.newCode", null, locale));

			if (learningExists.getIsDeleted()) {
				result.setElementDeleted(true);
				errors.add(messageSource.getMessage("error.deleted", null,
						locale));

			}
			result.setErrorsList(errors);
			result.setSingleElement(false);
		} else {
			modifyLearning.setInfo(learningGoal.getInfo());
			boolean r = daoLearningGoal.saveLearningGoal(modifyLearning);
			if (r)
				result.setSingleElement(true);
		}
		return result;

	}

	@PreAuthorize("hasPermission(#learningGoal, 'DELETE') or hasPermission(#learningGoal, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> deleteLearningGoal(LearningGoal learningGoal) {
		ResultClass<Boolean> result = new ResultClass<>();
		result.setSingleElement(daoLearningGoal
				.deleteLearningGoal(learningGoal));
		return result;

	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResultClass<Boolean> deleteLearningGoalForCompetence(
			Competence competence) {
		ResultClass<Boolean> result = new ResultClass<>();
		result.setSingleElement(daoLearningGoal
				.deleteLearningGoalForCompetence(competence));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<LearningGoal> getLearningGoalsFromCourse(Long id_course,
			Activity activity) {
		ResultClass<LearningGoal> result = new ResultClass<>();
		Collection<LearningGoal> learningGoals = daoLearningGoal
				.getLearningGoalsFromActivity(activity);
		if (!learningGoals.isEmpty()) {
			result.addAll(daoLearningGoal.getLearningGoalsFromCourse(id_course,
					learningGoals));
			return result;
		}

		else {
			result.addAll(daoLearningGoal.getLearningGoalsFromCourse(id_course));
			return result;
		}
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<LearningGoal> getLearningGoalByName(String name) {
		ResultClass<LearningGoal> result = new ResultClass<>();
		result.setSingleElement(daoLearningGoal.getLearningGoalByName(name));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	public ResultClass<LearningGoal> getLearningGoalsFromCompetence(
			Competence competence, Boolean show) {
		ResultClass<LearningGoal> result = new ResultClass<>();
		result.addAll(daoLearningGoal.getLearningGoalsFromCompetence(
				competence, show));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResultClass<Boolean> deleteLearningGoalForCompetences(
			Collection<Competence> competences) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		result.setSingleElement(daoLearningGoal
				.deleteLearningGoalsForCompetences(competences));
		return result;
	}

	@PreAuthorize("hasPermission(#learningGoal, 'WRITE') or hasPermission(#learningGoal, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<LearningGoal> unDeleteLearningGoal(
			LearningGoal learningGoal, Locale locale) {
		LearningGoal l = daoLearningGoal.existByCode(learningGoal.getInfo()
				.getCode());
		ResultClass<LearningGoal> result = new ResultClass<>();
		if (l == null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<>();
			errors.add(messageSource.getMessage("error.ElementNoExists", null,
					locale));
			result.setErrorsList(errors);

		} else {
			if (!l.getIsDeleted()) {
				Collection<String> errors = new ArrayList<>();
				errors.add(messageSource.getMessage("error.CodeNoDeleted",
						null, locale));
				result.setErrorsList(errors);
			}

			l.setDeleted(false);
			l.setInfo(learningGoal.getInfo());
			boolean r = daoLearningGoal.saveLearningGoal(l);
			if (r)
				result.setSingleElement(l);

		}
		return result;
	}

}
