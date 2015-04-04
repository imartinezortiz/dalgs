package es.ucm.fdi.dalgs.subject.service;

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
	private SubjectRepository daoSubject;
	
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
	public ResultClass<Subject> addSubject(Subject subject, Long id_topic, Locale locale) {
		
		boolean success = false;
		Subject subjectExists = daoSubject.existByCode(subject.getInfo().getCode());
		ResultClass<Subject> result = new ResultClass<>();

		if( subjectExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<>();
			errors.add(messageSource.getMessage("error.Code", null, locale));

			if (subjectExists.getIsDeleted()){
				result.setElementDeleted(true);
				errors.add(messageSource.getMessage("error.deleted", null, locale));
				result.setSingleElement(subjectExists);
			}
			else result.setSingleElement(subject);
			result.setErrorsList(errors);
		}
		else{
			subject.setTopic(serviceTopic.getTopic(id_topic).getSingleElement());
			success = daoSubject.addSubject(subject);
			
			
			if(success){
				subjectExists = daoSubject.existByCode(subject.getInfo().getCode());
				success = manageAclService.addACLToObject(subjectExists.getId(), subjectExists.getClass().getName());
				if (success) result.setSingleElement(subject);
			
			}else{
				throw new IllegalArgumentException(	"Cannot create ACL. Object not set.");

			}
		}
		return result;		
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<Subject> getAll() {
		
		
		ResultClass<Subject> result = new ResultClass<>();
		result.addAll(daoSubject.getAll());
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = false)
	public ResultClass<Subject> getSubject(Long id) {
		ResultClass<Subject> result = new ResultClass<>();
		result.setSingleElement(daoSubject.getSubject(id));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	@Transactional(propagation = Propagation.REQUIRED)
	public ResultClass<Boolean>deleteSubject(Subject subject) {
		ResultClass<Boolean> result = new ResultClass<>();

		ResultClass<Course> courses = serviceCourse.getCoursesBySubject(subject);
		if (!courses.isEmpty()){
				Collection<Subject> subjects = new ArrayList<>();
				subjects.add(subject);
					ResultClass<Boolean> deleteCourses= serviceCourse.deleteCoursesForSubject(subjects); 
					if (deleteCourses.getSingleElement()){
						subject.getCompetences().clear();
						result.setSingleElement(daoSubject.deleteSubject(subject));
					}
		}
		else result.setSingleElement(daoSubject.deleteSubject(subject));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<Subject> getSubjectsForTopic(Long id_topic, Boolean show) {
		ResultClass<Subject> result = new ResultClass<>();
		result.addAll(daoSubject.getSubjectsForTopic(id_topic, show));
		return result;
	}

	@PreAuthorize("hasPermission(#subject, 'WRITE') or hasPermission(#subject, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> modifySubject(Subject subject, Long id_subject, Locale locale) {
		ResultClass<Boolean> result = new ResultClass<>();

		Subject modifySubject = daoSubject.getSubject(id_subject);
		
		Subject subjectExists = daoSubject.existByCode(subject.getInfo().getCode());
		
		if(!subject.getInfo().getCode().equalsIgnoreCase(modifySubject.getInfo().getCode()) && 
				subjectExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add(messageSource.getMessage("error.newCode", null, locale));

			if (subjectExists.getIsDeleted()){
				result.setElementDeleted(true);
				errors.add(messageSource.getMessage("error.deleted", null, locale));

			}
			result.setErrorsList(errors);
			result.setSingleElement(false);
		}
		else{
			modifySubject.setInfo(subject.getInfo());
			boolean r = daoSubject.saveSubject(modifySubject);
			if (r) 
				result.setSingleElement(true);
		}
		return result;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> addCompetences(Subject modify, Long id_subject) {
		ResultClass<Boolean> result = new ResultClass<>();
		Subject subject = daoSubject.getSubject(id_subject);
		subject.setInfo(modify.getInfo());
		subject.setCompetences(modify.getCompetences());		
		result.setSingleElement(daoSubject.saveSubject(subject));
		return result;
	}
	

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<Subject> getSubjectForCourse(Long id) {
		ResultClass<Subject> result = new ResultClass<Subject>();
		result.setSingleElement(daoSubject.getSubjectForCourse(id));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<Subject> getSubjectByName(String string) {
		ResultClass<Subject> result = new ResultClass<Subject>();
		result.setSingleElement(daoSubject.getSubjectByName(string));
		return result;
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<Subject> getSubjectAll(Long id_subject) {
		ResultClass<Subject> result = new ResultClass<Subject>();
		Subject p = daoSubject.getSubject(id_subject);;
		p.setCompetences(serviceCompetence.getCompetencesForSubject(id_subject));
		result.setSingleElement(p);
		return result;
	}


	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<Subject> getSubjectForDegree(Degree degree) {
		ResultClass<Subject> result = new ResultClass<>();
		result.addAll(daoSubject.getSubjectForDegree(degree));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> deleteSubjectsForTopic(Collection<Topic> topics) {	
		
		ResultClass<Boolean> result = new ResultClass<>();
		Collection<Subject> subjects = daoSubject.getSubjectsForTopics(topics);
		
		if (!subjects.isEmpty()) {
			ResultClass<Boolean> deleteCourses= serviceCourse.deleteCoursesForSubject(subjects);
			if(deleteCourses.getSingleElement())
				result.setSingleElement(daoSubject.deleteSubjectsForTopics(topics));
			else result.setSingleElement(false);
		}
		else result.setSingleElement(daoSubject.deleteSubjectsForTopics(topics));
		return result;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Subject> unDeleteSubject(Subject subject, Locale locale){
		Subject s = daoSubject.existByCode(subject.getInfo().getCode());
		ResultClass<Subject> result = new ResultClass<>();
		if(s == null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<>();
			errors.add(messageSource.getMessage("error.ElementNoExists", null, locale));
			result.setErrorsList(errors);

		}
		else{
			if(!s.getIsDeleted()){
				Collection<String> errors = new ArrayList<>();
				errors.add(messageSource.getMessage("error.CodeNoDeleted", null, locale));
				result.setErrorsList(errors);
			}

			s.setDeleted(false);
			s.setInfo(subject.getInfo());
			boolean r = daoSubject.saveSubject(s);
			if(r) 
				result.setSingleElement(s);	

		}
		return result;
		
	}
	@Transactional(readOnly = false)
	public boolean uploadCSV(UploadForm upload, Long id_topic) {
		CsvPreference prefers =
				new CsvPreference.Builder(upload.getQuoteChar().charAt(0), upload
						.getDelimiterChar().charAt(0), upload.getEndOfLineSymbols())
						.build();

				List<Subject> list = null;
				try {
					FileItem fileItem = upload.getFileData().getFileItem();
					SubjectUpload subjectUpload = new SubjectUpload();
					
					Topic t = serviceTopic.getTopic(id_topic).getSingleElement();
					list = subjectUpload.readCSVSubjectToBean(fileItem.getInputStream(),
							upload.getCharset(), prefers, t);

					return daoSubject.persistListSubjects(list);

				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
		
	
	}


}
