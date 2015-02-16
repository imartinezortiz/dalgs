package com.example.tfg.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.tfg.domain.Degree;
import com.example.tfg.domain.Subject;

@Repository
public interface SubjectDao {

	public boolean addSubject(Subject subject);

	public List<Subject> getAll();

	public boolean saveSubject(Subject subject);

	public Subject getSubject(Long id);

	public boolean deleteSubject(Long id);

	public List<Subject> getSubjectsForTopic(Long id_topic);

	// public List<Subject> getSubjectsForCompetence(Long id);
	public String getNextCode();

	public Subject existByCode(String code);

	public Subject getSubjectForCourse(Long id);

//	public Subject getSubjectByName(String string);

	public boolean deleteSubjectsForDegree(Degree degree);

	public boolean addSubjects(List<Subject> s);

}
