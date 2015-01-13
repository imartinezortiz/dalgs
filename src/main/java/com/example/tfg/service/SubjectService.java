package com.example.tfg.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tfg.domain.Subject;



@Service
public interface SubjectService {
	public boolean addSubject(Subject subject);
	public List<Subject> getAll();
	public void modifySubject(Long id, String name, String description);
	public  Subject getSubject(Long id);
	public boolean deleteSubject(Long id);
	public List<Subject> getSubjectsForDegree(Long id_degree);
	public String getNextCode();

	public boolean modifySubject(Subject modify);
	public List<Subject> getSubjectsForCompetence(long id);
	
	public Subject getSubjectForCourse(long id_course);
	//public boolean deleteSubjectFromCourse(long id_course, long id_subject);
	public Subject getSubjectByName(String string);
}
