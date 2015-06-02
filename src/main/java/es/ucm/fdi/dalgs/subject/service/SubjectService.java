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
package es.ucm.fdi.dalgs.subject.service;

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
import es.ucm.fdi.dalgs.competence.service.CompetenceService;
import es.ucm.fdi.dalgs.course.service.CourseService;
import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.domain.Degree;
import es.ucm.fdi.dalgs.domain.Subject;
import es.ucm.fdi.dalgs.domain.Topic;
import es.ucm.fdi.dalgs.subject.repository.SubjectRepository;
import es.ucm.fdi.dalgs.topic.service.TopicService;

@Service
public class SubjectService {

	@Autowired
	private SubjectRepository repositorySubject;

	@Autowired
	private TopicService serviceTopic;

	@Autowired
	private CompetenceService serviceCompetence;

	@Autowired
	private CourseService serviceCourse;

	@Autowired
	private AclObjectService manageAclService;

	@Autowired
	private MessageSource messageSource;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Subject> addSubject(Subject subject, Long id_topic,
			Long id_module, Long id_degree, Locale locale) {

		boolean success = false;
		Subject subjectExists = repositorySubject.existByCode(subject.getInfo()
				.getCode());
		ResultClass<Subject> result = new ResultClass<>();

		if (subjectExists != null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<>();
			errors.add(messageSource.getMessage("error.Code", null, locale));

			if (subjectExists.getIsDeleted()) {
				result.setElementDeleted(true);
				errors.add(messageSource.getMessage("error.deleted", null,
						locale));
				result.setSingleElement(subjectExists);
			} else
				result.setSingleElement(subject);
			result.setErrorsList(errors);
		} else {
			subject.setTopic(serviceTopic.getTopic(id_topic, id_module,
					id_degree).getSingleElement());
			success = repositorySubject.addSubject(subject);

			if (success) {
				subjectExists = repositorySubject.existByCode(subject.getInfo()
						.getCode());
				success = manageAclService.addACLToObject(
						subjectExists.getId(), subjectExists.getClass()
						.getName());
				if (success)
					result.setSingleElement(subject);

			} else {
				throw new IllegalArgumentException(
						"Cannot create ACL. Object not set.");

			}
		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<Subject> getAll() {

		ResultClass<Subject> result = new ResultClass<>();
		result.addAll(repositorySubject.getAll());
		return result;
	}

	@PreAuthorize("isAuthenticated()")
	@Transactional(readOnly = false)
	public ResultClass<Subject> getSubject(Long id, Long id_topic,
			Long id_module, Long id_degree) {
		ResultClass<Subject> result = new ResultClass<>();
		result.setSingleElement(repositorySubject.getSubject(id, id_topic, id_module,
				id_degree));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultClass<Boolean> deleteSubject(Subject subject) {
		ResultClass<Boolean> result = new ResultClass<>();

		ResultClass<Course> courses = serviceCourse
				.getCoursesBySubject(subject);
		if (!courses.isEmpty()) {
			Collection<Subject> subjects = new ArrayList<>();
			subjects.add(subject);
			ResultClass<Boolean> deleteCourses = serviceCourse
					.deleteCoursesForSubject(subjects);
			if (deleteCourses.getSingleElement()) {
				subject.getCompetences().clear();
				result.setSingleElement(repositorySubject.deleteSubject(subject));
			}
		} else
			result.setSingleElement(repositorySubject.deleteSubject(subject));
		return result;
	}

	@PreAuthorize("isAuthenticated()")
	@Transactional(readOnly = true)
	public ResultClass<Subject> getSubjectsForTopic(Long id_topic, Boolean show) {
		ResultClass<Subject> result = new ResultClass<>();
		result.addAll(repositorySubject.getSubjectsForTopic(id_topic, show));
		return result;
	}

	@PreAuthorize("hasPermission(#subject, 'WRITE') or hasPermission(#subject, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> modifySubject(Subject subject, Long id_subject,
			Long id_topic, Long id_module, Long id_degree, Locale locale) {
		ResultClass<Boolean> result = new ResultClass<>();

		Subject modifySubject = repositorySubject.getSubject(id_subject, id_topic,
				id_module, id_degree);

		Subject subjectExists = repositorySubject.existByCode(subject.getInfo()
				.getCode());

		if (!subject.getInfo().getCode()
				.equalsIgnoreCase(modifySubject.getInfo().getCode())
				&& subjectExists != null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add(messageSource.getMessage("error.newCode", null, locale));

			if (subjectExists.getIsDeleted()) {
				result.setElementDeleted(true);
				errors.add(messageSource.getMessage("error.deleted", null,
						locale));

			}
			result.setErrorsList(errors);
			result.setSingleElement(false);
		} else {
			modifySubject.setInfo(subject.getInfo());
			boolean r = repositorySubject.saveSubject(modifySubject);
			if (r)
				result.setSingleElement(true);
		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> addCompetences(Subject modify, Long id_subject,
			Long id_topic, Long id_module, Long id_degree) {
		ResultClass<Boolean> result = new ResultClass<>();
		Subject subject = repositorySubject.getSubject(id_subject, id_topic,
				id_module, id_degree);
		subject.setInfo(modify.getInfo());
		subject.setCompetences(modify.getCompetences());
		result.setSingleElement(repositorySubject.saveSubject(subject));
		return result;
	}

	@PreAuthorize("isAuthenticated()")
	@Transactional(readOnly = true)
	public ResultClass<Subject> getSubjectForCourse(Long id) {
		ResultClass<Subject> result = new ResultClass<Subject>();
		result.setSingleElement(repositorySubject.getSubjectForCourse(id));
		return result;
	}

	@PreAuthorize("isAuthenticated()")
	@Transactional(readOnly = true)
	public ResultClass<Subject> getSubjectByName(String string) {
		ResultClass<Subject> result = new ResultClass<Subject>();
		result.setSingleElement(repositorySubject.getSubjectByName(string));
		return result;
	}

	@PreAuthorize("isAuthenticated()")
	@Transactional(readOnly = true)
	public ResultClass<Subject> getSubjectAll(Long id_subject, Long id_topic,
			Long id_module, Long id_degree) {
		ResultClass<Subject> result = new ResultClass<Subject>();
		Subject p = repositorySubject.getSubject(id_subject, id_topic, id_module,
				id_degree);
		if (p != null) {
			p.setCompetences(serviceCompetence
					.getCompetencesForSubject(id_subject));

			result.setSingleElement(p);
		}
		return result;
	}

	@PreAuthorize("isAuthenticated()")
	@Transactional(readOnly = true)
	public ResultClass<Subject> getSubjectForDegree(Degree degree) {
		ResultClass<Subject> result = new ResultClass<>();
		result.addAll(repositorySubject.getSubjectForDegree(degree));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> deleteSubjectsForTopic(Collection<Topic> topics) {

		ResultClass<Boolean> result = new ResultClass<>();
		Collection<Subject> subjects = repositorySubject.getSubjectsForTopics(topics);

		if (!subjects.isEmpty()) {
			ResultClass<Boolean> deleteCourses = serviceCourse
					.deleteCoursesForSubject(subjects);
			if (deleteCourses.getSingleElement())
				result.setSingleElement(repositorySubject
						.deleteSubjectsForTopics(topics));
			else
				result.setSingleElement(false);
		} else
			result.setSingleElement(repositorySubject.deleteSubjectsForTopics(topics));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Subject> unDeleteSubject(Subject subject, Locale locale) {
		Subject s = repositorySubject.existByCode(subject.getInfo().getCode());
		ResultClass<Subject> result = new ResultClass<>();
		if (s == null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<>();
			errors.add(messageSource.getMessage("error.ElementNoExists", null,
					locale));
			result.setErrorsList(errors);

		} else {
			if (!s.getIsDeleted()) {
				Collection<String> errors = new ArrayList<>();
				errors.add(messageSource.getMessage("error.CodeNoDeleted",
						null, locale));
				result.setErrorsList(errors);
			}

			s.setDeleted(false);
			s.setInfo(subject.getInfo());
			boolean r = repositorySubject.saveSubject(s);
			if (r)
				result.setSingleElement(s);

		}
		return result;

	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> uploadCSV(UploadForm upload, Long id_topic, Long id_module,
			Long id_degree, Locale locale) {
		ResultClass<Boolean> result = new ResultClass<>();
		if (!upload.getFileData().isEmpty()){
			CsvPreference prefers = new CsvPreference.Builder(upload.getQuoteChar()
					.charAt(0), upload.getDelimiterChar().charAt(0),
					upload.getEndOfLineSymbols()).build();

		List<Subject> list = null;
		try {
			FileItem fileItem = upload.getFileData().getFileItem();
			SubjectCSV subjectUpload = new SubjectCSV();

			Topic t = serviceTopic.getTopic(id_topic, id_module, id_degree)
					.getSingleElement();
			list = subjectUpload.readCSVSubjectToBean(
					fileItem.getInputStream(), upload.getCharset(), prefers, t);
			if (list == null){
				result.setHasErrors(true);
				result.getErrorsList().add(messageSource.getMessage("error.params", null, locale));
			}
			else{
				result.setSingleElement(repositorySubject.persistListSubjects(list));
				if (result.getSingleElement()) {
					for (Subject s : list) {
						Subject aux = repositorySubject.existByCode(s.getInfo().getCode());
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
public void downloadCSV(HttpServletResponse response) throws IOException {

	Collection<Subject> subjects = new ArrayList<Subject>();
	subjects =  repositorySubject.getAll();

	if(!subjects.isEmpty()){
		SubjectCSV subjectCSV = new SubjectCSV();
		subjectCSV.downloadCSV(response, subjects);
	}

}


}
