package com.example.tfg.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tfg.domain.Competence;

@Service
public interface CompetenceService {
	public boolean addCompetence(Competence competence);
	public List<Competence> getAll();
	public  Competence getCompetence(Long id);
	public boolean deleteCompetence(Long id);
	
	public List<Competence> getCompetencesForSubject(Long id_subject);
	public List<Competence> getCompetencesForDegree(Long id_degree);

	
	public boolean modifyCompetence(Competence modify);
	//public List<Competence> getCompetencesDontExistOnSubject(Long id_subject);
	public boolean deleteCompetenceFromSubject(Long id_competence, Long id_subject);
	
	public  Competence getCompetenceByName(String name);
	public String getNextCode();

	
	//public boolean existsInSubject(Long id_subject, Competence c);

}
