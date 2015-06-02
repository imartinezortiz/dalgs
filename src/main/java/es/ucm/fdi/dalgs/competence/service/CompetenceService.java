/**
 * This file is part of D.A.L.G.S.
 *
 * D.A.L.G.S is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * D.A.L.G.S is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with D.A.L.G.S.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.dalgs.competence.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

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
	private CompetenceRepository repositoryCompetence;

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

		Competence competenceExists = repositoryCompetence.existByCode(competence
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

			success = repositoryCompetence.addCompetence(competence);

			if (success) {
				competenceExists = repositoryCompetence.existByCode(competence
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

	@PreAuthorize("isAuthenticated()")
	@Transactional(readOnly = true)
	public ResultClass<Competence> getAll() {
		ResultClass<Competence> result = new ResultClass<Competence>();
		result.addAll(repositoryCompetence.getAll());
		return result;
	}

	@PreAuthorize("isAuthenticated()")
	@Transactional(readOnly = false)
	public ResultClass<Competence> getCompetence(Long id, Long id_degree) {
		ResultClass<Competence> result = new ResultClass<Competence>();
		result.setSingleElement(repositoryCompetence.getCompetence(id, id_degree));
		return result;
	}

	@PreAuthorize("isAuthenticated()")
	@Transactional(readOnly = false)
	public ResultClass<Competence> getCompetenceByName(String name) {
		ResultClass<Competence> result = new ResultClass<>();
		result.setSingleElement(repositoryCompetence.getCompetenceByName(name));
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
			result.setSingleElement(repositoryCompetence.deleteCompetence(competence));
		return result;
	}

	@PreAuthorize("isAuthenticated()")
	@Transactional(readOnly = false)
	public ResultClass<Competence> getCompetencesForSubject(Long id_subject) {
		ResultClass<Competence> result = new ResultClass<>();
		result.addAll(repositoryCompetence.getCompetencesForSubject(id_subject));
		return result;
	}

	@PreAuthorize("isAuthenticated()")
	@Transactional(readOnly = true)
	public ResultClass<Competence> getCompetencesForDegree(Long id_degree,
			Boolean show) {
		ResultClass<Competence> result = new ResultClass<>();

		result.addAll(repositoryCompetence.getCompetencesForDegree(id_degree, show));
		return result;
	}

	@PreAuthorize("hasPermission(#competence, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> modifyCompetence(Competence competence,
			Long id_competence, Long id_degree, Locale locale) {
		ResultClass<Boolean> result = new ResultClass<>();

		competence.setDegree(serviceDegree.getDegree(id_degree)
				.getSingleElement());
		Competence modifyCompetence = repositoryCompetence.getCompetence(
				id_competence, id_degree);

		Competence competenceExists = repositoryCompetence.existByCode(competence
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
			boolean r = repositoryCompetence.saveCompetence(modifyCompetence);
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
			c.remove(repositoryCompetence.getCompetence(id_competence, id_degree));
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
			result.setSingleElement(repositoryCompetence
					.deleteCompetencesForDegree(degree));
		}

		else
			result.setSingleElement(false);

		return result;
	}

	@PreAuthorize("isAuthenticated()")
	@Transactional(readOnly = true)
	public ResultClass<Competence> getCompetenceAll(Long id_competence,
			Long id_degree, Boolean show) {
		ResultClass<Competence> result = new ResultClass<>();
		Competence competence = repositoryCompetence.getCompetence(id_competence,
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
		Competence c = repositoryCompetence.existByCode(
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
			boolean r = repositoryCompetence.saveCompetence(c);
			if (r)
				result.setSingleElement(c);

		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> uploadCSV(UploadForm upload, Long id_degree, Locale locale) {
		ResultClass<Boolean> result = new ResultClass<>();

		if(!upload.getFileData().isEmpty()){
			CsvPreference prefers = new CsvPreference.Builder(upload.getQuoteChar()
					.charAt(0), upload.getDelimiterChar().charAt(0),
					upload.getEndOfLineSymbols()).build();

			List<Competence> list = null;
			try {
				FileItem fileItem = upload.getFileData().getFileItem();
				CompetenceCSV competenceUpload = new CompetenceCSV();

				Degree d = serviceDegree.getDegree(id_degree).getSingleElement();
				list = competenceUpload.readCSVCompetenceToBean(
						fileItem.getInputStream(), upload.getCharset(), prefers, d);

				if (list == null){
					result.setHasErrors(true);
					result.getErrorsList().add(messageSource.getMessage("error.params", null, locale));
				}
				else{
					result.setSingleElement(repositoryCompetence.persistListCompetences(list));
					if (result.getSingleElement()) {
						for (Competence c : list) {
							Competence aux = repositoryCompetence.existByCode(c.getInfo()
									.getCode(), d);
							result.setSingleElement(result.getSingleElement()
									&& manageAclService.addACLToObject(aux.getId(), aux
											.getClass().getName()));

						}

					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				result.setSingleElement(false);
			}
		}
		else {
			result.setHasErrors(true);
			result.getErrorsList().add(messageSource.getMessage("error.fileEmpty", null, locale));
		}
		return result;

	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public void dowloadCSV(HttpServletResponse response) throws IOException {
		Collection<Competence> competences = new ArrayList<Competence>();
		competences =  repositoryCompetence.getAll();

		if(!competences.isEmpty()){
			CompetenceCSV competenceCSV = new CompetenceCSV();
			competenceCSV.downloadCSV(response, competences);
		}
	}
}
