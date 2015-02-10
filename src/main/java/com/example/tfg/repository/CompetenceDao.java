package com.example.tfg.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.tfg.domain.Competence;
import com.example.tfg.domain.Degree;

@Repository
public interface CompetenceDao {
	public boolean addCompetence(Competence competence);

	public List<Competence> getAll();

	public boolean saveCompetence(Competence competence);

	public Competence getCompetence(Long id);

	public boolean deleteCompetence(Long id);

	public List<Competence> getCompetencesForSubject(Long id_subject);

	public List<Competence> getCompetencesForDegree(Long id_degree);

	public Competence existByCode(String code, Degree degree);

	public Competence getCompetenceByName(String name);

	// public String getNextCode();
	public boolean deleteCompetencesForDegree(Degree degree);

}
