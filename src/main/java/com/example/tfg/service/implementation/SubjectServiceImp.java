package com.example.tfg.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.tfg.domain.Subject;
import com.example.tfg.repository.CourseDao;
import com.example.tfg.repository.DegreeDao;
import com.example.tfg.repository.SubjectDao;
import com.example.tfg.service.SubjectService;


@Service
public class SubjectServiceImp implements SubjectService{

	@Autowired
	private SubjectDao daoSubject;
	
	@Autowired
	private DegreeDao daoDegree;
	
	@Autowired
	private CourseDao daoCourse;
	
	@Transactional(readOnly = false)
	public boolean addSubject(Subject subject) {
		if (!daoSubject.existByCode(subject.getCode()))
			return daoSubject.addSubject(subject);
		else return false;
	}
	
	@Transactional(readOnly = true)
	public List<Subject> getAll(){
		return daoSubject.getAll();
	}

	@Transactional(readOnly = false)
	public void modifySubject(Long id, String name, String description){
		Subject aux = daoSubject.getSubject(id);
		aux.setName(name);
		aux.setDescription(description);
		daoSubject.saveSubject(aux);
	}
	
	@Transactional(readOnly = false)
	public  Subject getSubject(Long id){
		return daoSubject.getSubject(id);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteSubject(Long id){
		
		daoSubject.getSubject(id).getCompetences().clear();
		daoDegree.getDegreeSubject(daoSubject.getSubject(id));
		return daoSubject.deleteSubject(id);
	}

	@Transactional(readOnly = true)
	public List<Subject> getSubjectsForDegree(Long id_degree) {
		return daoSubject.getSubjectsForDegree(id_degree);
	}

	@Transactional(readOnly = false)
	public boolean modifySubject(Subject modify) {
		
		return daoSubject.saveSubject(modify);
	}

	@Transactional(readOnly = true)
	public List<Subject> getSubjectsForCompetence(long id) {
		return daoSubject.getSubjectsForCompetence(id);
	}
	@Transactional(readOnly = true)
	public String getNextCode(){
		return daoSubject.getNextCode();
	
	}
	@Transactional(readOnly = true)
	public Subject getSubjectForCourse(long id) {
		return daoSubject.getSubjectForCourse(id);
	}

	/*
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteSubjectFromCourse(long id_course, long id_subject) {
		Subject c = daoCourse.getCourse(id_course).getSubject();
		try {
			return daoSubject.deleteSubject(c.getId());
		} catch (Exception e) {
			return false;
		}
	}
*/

	@Transactional(readOnly = true)
	public Subject getSubjectByName(String string) {
		return daoSubject.getSubjectByName(string);
	}
}
