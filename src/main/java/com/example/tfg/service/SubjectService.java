package com.example.tfg.service;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tfg.classes.ResultClass;
import com.example.tfg.domain.Degree;
import com.example.tfg.domain.Subject;
import com.example.tfg.domain.Topic;

@Service
public interface SubjectService {

	public List<Subject> getAll();

	public Subject getSubject(Long id);

	public boolean deleteSubject(Long id);

	public List<Subject> getSubjectsForTopic(Long id_topic);

	public String getNextCode();

	public ResultClass<Boolean> modifySubject(Subject modify, Long id_subject);
	
	public boolean addCompetences(Subject modify, Long id_subject);

	public Subject getSubjectForCourse(Long id_course);

	public Subject getSubjectByName(String string);

	public ResultClass<Boolean> addSubject(Subject newSubject, Long id_topic);

	public Subject getSubjectAll(Long id_subject);

	public Collection<Subject> getSubjectForDegree(Degree degree);

	public boolean deleteSubjectsForTopic(Collection<Topic> topics);
	
	public ResultClass<Boolean> unDeleteSubject(Subject subject);

}
