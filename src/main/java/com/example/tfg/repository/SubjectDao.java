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
	public List<Subject> getSubjectsForDegree(Long id_degree);
	public List<Subject> getSubjectsForCompetence(Long id);
	public String getNextCode();
	public boolean existByCode(String code);
	public Subject getSubjectForCourse(Long id);
	public Subject getSubjectByName(String string);
	public boolean deleteSubjectsForDegree(Degree degree);

}
