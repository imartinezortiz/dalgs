package com.example.tfg.service;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tfg.domain.Degree;
import com.example.tfg.domain.Module;
import com.example.tfg.domain.Subject;

@Service
public interface SubjectService {
	// public boolean addSubject(Subject subject);
	public List<Subject> getAll();

	// public void modifySubject(Long id, String name, String description);
	public Subject getSubject(Long id);

	public boolean deleteSubject(Long id);

	public List<Subject> getSubjectsForTopic(Long id_topic);

	public String getNextCode();

	public boolean modifySubject(Subject modify, Long id_subject);
	
	public boolean addCompetences(Subject modify, Long id_subject);

	// public List<Subject> getSubjectsForCompetence(Long id);

	public Subject getSubjectForCourse(Long id_course);

	// public boolean deleteSubjectFromCourse(Long id_course, Long id_subject);
	public Subject getSubjectByName(String string);

	public boolean deleteSubject(Long id_subject, Long id_degree);

	public boolean addSubject(Subject newSubject, Long id);

	public boolean deleteSubjectsForDegree(Degree d);

	public Subject getSubjectAndDegree(Long id_subject, Long id_degree);

}
