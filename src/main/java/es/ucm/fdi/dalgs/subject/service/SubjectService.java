package es.ucm.fdi.dalgs.subject.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.competence.service.CompetenceService;
import es.ucm.fdi.dalgs.course.service.CourseService;
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

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> addSubject(Subject subject, Long id_topic) {
		
		Subject subjectExists = daoSubject.existByCode(subject.getInfo().getCode());
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		if( subjectExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code already exists");

			if (subjectExists.getIsDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");

			}
			result.setE(false);
			result.setErrorsList(errors);
		}
		else{
			subject.setTopic(serviceTopic.getTopic(id_topic).getE());
			boolean r = daoSubject.addSubject(subject);
			if (r) 
				result.setE(true);
		}
		return result;		
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<List<Subject>> getAll() {
		
		
		ResultClass<List<Subject>> result = new ResultClass<List<Subject>>();
		result.setE(daoSubject.getAll());
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = false)
	public ResultClass<Subject> getSubject(Long id) {
		ResultClass<Subject> result = new ResultClass<Subject>();
		result.setE(daoSubject.getSubject(id));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultClass<Boolean>deleteSubject(Long id) {
		daoSubject.getSubject(id).getCompetences().clear();
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		result.setE(daoSubject.deleteSubject(id));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<List<Subject>> getSubjectsForTopic(Long id_topic) {
		ResultClass<List<Subject>> result = new ResultClass<List<Subject>>();
		result.setE(daoSubject.getSubjectsForTopic(id_topic));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> modifySubject(Subject subject, Long id_subject) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		Subject modifySubject = daoSubject.getSubject(id_subject);
		
		Subject subjectExists = daoSubject.existByCode(subject.getInfo().getCode());
		
		if(!subject.getInfo().getCode().equalsIgnoreCase(modifySubject.getInfo().getCode()) && 
				subjectExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("New code already exists");

			if (subjectExists.getIsDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");

			}
			result.setErrorsList(errors);
			result.setE(false);
		}
		else{
			modifySubject.setInfo(subject.getInfo());
			boolean r = daoSubject.saveSubject(modifySubject);
			if (r) 
				result.setE(true);
		}
		return result;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> addCompetences(Subject modify, Long id_subject) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		Subject subject = daoSubject.getSubject(id_subject);
		subject.setInfo(modify.getInfo());
		subject.setCompetences(modify.getCompetences());		
		result.setE(daoSubject.saveSubject(subject));
		return result;
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<String> getNextCode() {
		ResultClass<String> result = new ResultClass<String>();
		result.setE(daoSubject.getNextCode());
		return result;

	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<Subject> getSubjectForCourse(Long id) {
		ResultClass<Subject> result = new ResultClass<Subject>();
		result.setE(daoSubject.getSubjectForCourse(id));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<Subject> getSubjectByName(String string) {
		ResultClass<Subject> result = new ResultClass<Subject>();
		result.setE(daoSubject.getSubjectByName(string));
		return result;
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<Subject> getSubjectAll(Long id_subject) {
		ResultClass<Subject> result = new ResultClass<Subject>();
		Subject p = daoSubject.getSubject(id_subject);;
		p.setCompetences(serviceCompetence.getCompetencesForSubject(id_subject));
		result.setE(p);
		return result;
	}


	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<Collection<Subject>> getSubjectForDegree(Degree degree) {
		ResultClass<Collection<Subject>> result = new ResultClass<Collection<Subject>>();
		result.setE(daoSubject.getSubjectForDegree(degree));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> deleteSubjectsForTopic(Collection<Topic> topics) {	
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		result.setE(daoSubject.deleteSubjectsForTopics(topics));
		return result;
	}

	public ResultClass<Boolean> unDeleteSubject(Subject subject){
		Subject s = daoSubject.existByCode(subject.getInfo().getCode());
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		if(s == null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code doesn't exist");
			result.setErrorsList(errors);

		}
		else{
			if(!s.getIsDeleted()){
				Collection<String> errors = new ArrayList<String>();
				errors.add("Code is not deleted");
				result.setErrorsList(errors);
			}

			s.setDeleted(false);
			s.setInfo(subject.getInfo());
			boolean r = daoSubject.saveSubject(s);
			if(r) 
				result.setE(true);	

		}
		return result;
		
	}


}
