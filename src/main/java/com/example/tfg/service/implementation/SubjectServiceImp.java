package com.example.tfg.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.tfg.domain.Degree;
import com.example.tfg.domain.Topic;
import com.example.tfg.domain.Subject;
import com.example.tfg.repository.SubjectDao;
import com.example.tfg.service.CompetenceService;
import com.example.tfg.service.CourseService;
import com.example.tfg.service.DegreeService;
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
		
//		if (!daoSubject.existByCode(subject.getInfo().getCode())) {
//
//			Degree degree = serviceDegree.getDegree(id_degree);
//			subject.setDegree(degree);
//			return daoSubject.addSubject(subject);
//		} else
//			return false;

	}

	@Transactional(readOnly = true)
	public List<Subject> getAll() {
		return daoSubject.getAll();
	}

	// @Transactional(readOnly = false)
	// public void modifySubject(Long id, String name, String description){
	// Subject aux = daoSubject.getSubject(id);
	// aux.setName(name);
	// aux.setDescription(description);
	// daoSubject.saveSubject(aux);
	// }

	@Transactional(readOnly = false)
	public Subject getSubject(Long id) {
		return daoSubject.getSubject(id);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteSubject(Long id) {

		daoSubject.getSubject(id).getCompetences().clear();
		// serviceDegree.getDegreeSubject(daoSubject.getSubject(id));
		return daoSubject.deleteSubject(id);
	}

	@Transactional(readOnly = true)
	public List<Subject> getSubjectsForTopic(Long id_topic) {
		return daoSubject.getSubjectsForTopic(id_topic);
	}

	@Transactional(readOnly = false)
	public boolean modifySubject(Subject modify, Long id_subject) {

		Subject subject = daoSubject.getSubject(id_subject);
		subject.setInfo(modify.getInfo());
//		if (modify.getCompetences() != null)
//			subject.setCompetences(modify.getCompetences());
//		if (modify.getCode() != null)
//			subject.setCode(modify.getCode());
//		if (modify.getName() != null)
//			subject.setName(modify.getName());
//		if (modify.getDescription() != null)
//			subject.setDescription(modify.getDescription());
		

		return daoSubject.saveSubject(subject);
	}
	
	@Transactional(readOnly = false)
	public boolean addCompetences(Subject modify, Long id_subject) {

		Subject subject = daoSubject.getSubject(id_subject);
		subject.setInfo(modify.getInfo());
		subject.setCompetences(modify.getCompetences());		

		return daoSubject.saveSubject(subject);
	}
	

	@Transactional(readOnly = true)
	public String getNextCode() {
		return daoSubject.getNextCode();

	}

	@Transactional(readOnly = true)
	public Subject getSubjectForCourse(Long id) {
		return daoSubject.getSubjectForCourse(id);
	}

	/*
	 * @Transactional(propagation = Propagation.REQUIRED) public boolean
	 * deleteSubjectFromCourse(Long id_course, Long id_subject) { Subject c =
	 * daoCourse.getCourse(id_course).getSubject(); try { return
	 * daoSubject.deleteSubject(c.getId()); } catch (Exception e) { return
	 * false; } }
	 */

//	@Transactional(readOnly = true)
//	public Subject getSubjectByName(String string) {
//		return daoSubject.getSubjectByName(string);
//	}

//	@Transactional(readOnly = false)
//	public boolean deleteSubject(Long id_subject, Long id_degree) {
//
//		// Degree degree = serviceDegree.getDegree(id_degree);
//
//		// Subject s = daoSubject.getSubject(id_subject);
//		
//		return daoSubject.deleteSubject(id_subject);
//
//	}

	public boolean deleteSubjectsForDegree(Topic degree) {

		return daoSubject.deleteSubjectsForDegree(degree);
	}

	@Transactional(readOnly = true)
	public Subject getSubjectAll(Long id_subject) {

		Subject p = daoSubject.getSubject(id_subject);;
		p.setCompetences(serviceCompetence.getCompetencesForSubject(id_subject));
		return p;
	}

	
}
