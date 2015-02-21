package com.example.tfg.service.implementation;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.tfg.domain.Degree;
import com.example.tfg.domain.Subject;
import com.example.tfg.domain.Topic;
import com.example.tfg.repository.SubjectDao;
import com.example.tfg.service.CompetenceService;
import com.example.tfg.service.CourseService;
import com.example.tfg.service.SubjectService;
import com.example.tfg.service.TopicService;

@Service
public class SubjectServiceImp implements SubjectService {

	@Autowired
	private SubjectDao daoSubject;
	
	@Autowired
	private TopicService serviceTopic;

	@Autowired
	private CompetenceService serviceCompetence;
	
	@Autowired
	private CourseService serviceCourse;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public boolean addSubject(Subject subject, Long id_topic) {
		
		Subject existSubject = daoSubject.existByCode(subject.getInfo().getCode());
		Topic topic = serviceTopic.getTopic(id_topic);
		if(existSubject == null){
			subject.setTopic(topic);
			topic.getSubjects().add(subject);
			if(daoSubject.addSubject(subject))
				return serviceTopic.modifyTopic(topic);
				
		}else if(existSubject.isDeleted()==true){
			existSubject.setInfo(subject.getInfo());
			existSubject.setDeleted(false);
			topic.getSubjects().add(existSubject);
			if (daoSubject.saveSubject(existSubject))
				return serviceTopic.modifyTopic(topic);
		}
		return false;		
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public List<Subject> getAll() {
		return daoSubject.getAll();
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = false)
	public Subject getSubject(Long id) {
		return daoSubject.getSubject(id);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteSubject(Long id) {
		daoSubject.getSubject(id).getCompetences().clear();
		return daoSubject.deleteSubject(id);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public List<Subject> getSubjectsForTopic(Long id_topic) {
		return daoSubject.getSubjectsForTopic(id_topic);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public boolean modifySubject(Subject modify, Long id_subject) {
		Subject subject = daoSubject.getSubject(id_subject);
		subject.setInfo(modify.getInfo());
		return daoSubject.saveSubject(subject);
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public boolean addCompetences(Subject modify, Long id_subject) {

		Subject subject = daoSubject.getSubject(id_subject);
		subject.setInfo(modify.getInfo());
		subject.setCompetences(modify.getCompetences());		

		return daoSubject.saveSubject(subject);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public String getNextCode() {
		return daoSubject.getNextCode();

	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public Subject getSubjectForCourse(Long id) {
		return daoSubject.getSubjectForCourse(id);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public Subject getSubjectByName(String string) {
		return daoSubject.getSubjectByName(string);
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public Subject getSubjectAll(Long id_subject) {
		Subject p = daoSubject.getSubject(id_subject);;
		p.setCompetences(serviceCompetence.getCompetencesForSubject(id_subject));
		return p;
	}


	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public Collection<Subject> getSubjectForDegree(Degree degree) {
		return daoSubject.getSubjectForDegree(degree);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public boolean deleteSubjectsForTopic(Collection<Topic> topics) {	
		return daoSubject.deleteSubjectsForTopics(topics);
	}

	
}
