package es.ucm.fdi.dalgs.competence.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.prefs.CsvPreference;

import es.ucm.fdi.dalgs.acl.service.AclObjectService;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.classes.UploadForm;
import es.ucm.fdi.dalgs.competence.repository.CompetenceRepository;
import es.ucm.fdi.dalgs.degree.service.DegreeService;
import es.ucm.fdi.dalgs.domain.Competence;
import es.ucm.fdi.dalgs.domain.Degree;
import es.ucm.fdi.dalgs.learningGoal.service.LearningGoalService;
import es.ucm.fdi.dalgs.subject.service.SubjectService;

@Service
public class CompetenceService {
	@Autowired
	private CompetenceRepository daoCompetence;

	@Autowired
	private SubjectService serviceSubject;

	@Autowired
	private DegreeService serviceDegree;

	@Autowired
	private LearningGoalService serviceLearning;

	@Autowired
	private AclObjectService manageAclService;

	@Autowired
	private MessageSource messageSource;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Competence> addCompetence(Competence competence,
			Long id_degree, Locale locale) {

		boolean success = false;

		competence.setDegree(serviceDegree.getDegree(id_degree)
				.getSingleElement());

		Competence competenceExists = daoCompetence.existByCode(competence
				.getInfo().getCode(), competence.getDegree());
		ResultClass<Competence> result = new ResultClass<>();

		if (competenceExists != null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add(messageSource.getMessage("error.Code", null, locale));

			if (competenceExists.getIsDeleted()) {
				result.setElementDeleted(true);
				errors.add(messageSource.getMessage("error.deleted", null,
						locale));
				result.setSingleElement(competenceExists);

			} else
				result.setSingleElement(competence);
			result.setErrorsList(errors);
		} else {

			success = daoCompetence.addCompetence(competence);

			if (success) {
				competenceExists = daoCompetence.existByCode(competence
						.getInfo().getCode(), competence.getDegree());
				success = manageAclService.addACLToObject(competenceExists
						.getId(), competenceExists.getClass().getName());
				if (success)
					result.setSingleElement(competence);

			} else {
				throw new IllegalArgumentException(
						"Cannot create ACL. Object not set.");

			}

		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<Competence> getAll() {
		ResultClass<Competence> result = new ResultClass<Competence>();
		result.addAll(daoCompetence.getAll());
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = false)
	public ResultClass<Competence> getCompetence(Long id, Long id_degree) {
		ResultClass<Competence> result = new ResultClass<Competence>();
		result.setSingleElement(daoCompetence.getCompetence(id, id_degree));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = false)
	public ResultClass<Competence> getCompetenceByName(String name) {
		ResultClass<Competence> result = new ResultClass<>();
		result.setSingleElement(daoCompetence.getCompetenceByName(name));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultClass<Boolean> deleteCompetence(Competence competence) {
		ResultClass<Boolean> result = new ResultClass<>();

		Collection<Competence> competences = new ArrayList<>();
		competences.add(competence);
		if (!serviceLearning.deleteLearningGoalForCompetences(competences)
				.getSingleElement())
			result.setSingleElement(false);
		else
			result.setSingleElement(daoCompetence.deleteCompetence(competence));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = false)
	public ResultClass<Competence> getCompetencesForSubject(Long id_subject) {
		ResultClass<Competence> result = new ResultClass<>();
		result.addAll(daoCompetence.getCompetencesForSubject(id_subject));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<Competence> getCompetencesForDegree(Long id_degree,
			Boolean show) {
		ResultClass<Competence> result = new ResultClass<>();

		result.addAll(daoCompetence.getCompetencesForDegree(id_degree, show));
		return result;
	}

	@PreAuthorize("hasPermission(#competence, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> modifyCompetence(Competence competence,
			Long id_competence, Long id_degree, Locale locale) {
		ResultClass<Boolean> result = new ResultClass<>();

		competence.setDegree(serviceDegree.getDegree(id_degree)
				.getSingleElement());
		Competence modifyCompetence = daoCompetence.getCompetence(
				id_competence, id_degree);

		Competence competenceExists = daoCompetence.existByCode(competence
				.getInfo().getCode(), competence.getDegree());

		if (!competence.getInfo().getCode()
				.equalsIgnoreCase(modifyCompetence.getInfo().getCode())
				&& competenceExists != null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<>();
			errors.add(messageSource.getMessage("error.newCode", null, locale));

			if (competenceExists.getIsDeleted()) {
				result.setElementDeleted(true);
				errors.add(messageSource.getMessage("error.deleted", null,
						locale));

			}
			result.setErrorsList(errors);
			result.setSingleElement(false);
		} else {
			modifyCompetence.setInfo(competence.getInfo());
			boolean r = daoCompetence.saveCompetence(modifyCompetence);
			if (r)
				result.setSingleElement(true);
		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultClass<Boolean> deleteCompetenceFromSubject(Long id_competence,
			Long id_subject, Long id_degree, Long id_topic, Long id_module) {
		// Subject subject = daoSubject.getSubject(id);
		ResultClass<Boolean> result = new ResultClass<>();
		Collection<Competence> c = serviceSubject
				.getSubject(id_subject, id_topic, id_module, id_degree)
				.getSingleElement().getCompetences();
		try {
			c.remove(daoCompetence.getCompetence(id_competence, id_degree));
			result.setSingleElement(true);

		} catch (Exception e) {
			result.setSingleElement(false);
			;
		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResultClass<Boolean> deleteCompetencesForDegree(Degree degree) {
		ResultClass<Boolean> result = new ResultClass<>();

		if (serviceLearning.deleteLearningGoalForCompetences(
				degree.getCompetences()).getSingleElement()) {
			result.setSingleElement(daoCompetence
					.deleteCompetencesForDegree(degree));
		}

		else
			result.setSingleElement(false);

		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<Competence> getCompetenceAll(Long id_competence,
			Long id_degree, Boolean show) {
		ResultClass<Competence> result = new ResultClass<>();
		Competence competence = daoCompetence.getCompetence(id_competence,
				id_degree);
		competence.setLearningGoals(serviceLearning
				.getLearningGoalsFromCompetence(competence, show));
		result.setSingleElement(competence);
		return result;
	}

	@PreAuthorize("hasPermission(#competence, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Competence> unDeleteCompetence(Competence competence,
			Long id_degree, Locale locale) {

		competence.setDegree(serviceDegree.getDegree(id_degree)
				.getSingleElement());
		Competence c = daoCompetence.existByCode(
				competence.getInfo().getCode(), competence.getDegree());
		ResultClass<Competence> result = new ResultClass<>();
		if (c == null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<>();
			errors.add(messageSource.getMessage("error.ElementNoExists", null,
					locale));
			result.setErrorsList(errors);

		} else {
			if (!c.getIsDeleted()) {
				Collection<String> errors = new ArrayList<>();
				errors.add(messageSource.getMessage("error.CodeNoDeleted",
						null, locale));
				result.setErrorsList(errors);
			}

			c.setDeleted(false);
			c.setInfo(competence.getInfo());
			boolean r = daoCompetence.saveCompetence(c);
			if (r)
				result.setSingleElement(c);

		}
		return result;
	}

	@Transactional(readOnly = false)
	public boolean uploadCSV(UploadForm upload, Long id_degree) {
		boolean success = false;
		CsvPreference prefers = new CsvPreference.Builder(upload.getQuoteChar()
				.charAt(0), upload.getDelimiterChar().charAt(0),
				upload.getEndOfLineSymbols()).build();

		List<Competence> list = null;
		try {
			FileItem fileItem = upload.getFileData().getFileItem();
			CompetenceUpload competenceUpload = new CompetenceUpload();

			Degree d = serviceDegree.getDegree(id_degree).getSingleElement();
			list = competenceUpload.readCSVCompetenceToBean(
					fileItem.getInputStream(), upload.getCharset(), prefers, d);

			success = daoCompetence.persistListCompetences(list);
			if (success) {
				for (Competence c : list) {
					Competence aux = daoCompetence.existByCode(c.getInfo()
							.getCode(), d);
					success = success
							&& manageAclService.addACLToObject(aux.getId(), aux
									.getClass().getName());

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return success;

	}
}
