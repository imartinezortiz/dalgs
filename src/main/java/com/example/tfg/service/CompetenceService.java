package com.example.tfg.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tfg.domain.Competence;

@Service
public interface CompetenceService {
	public boolean addCompetence(Competence competence);
	public List<Competence> getAll();
	public  Competence getCompetence(long id);
	public boolean deleteCompetence(long id);
	
	public List<Competence> getCompetencesForSubject(long id_subject);
	public List<Competence> getCompetencesForDegree(long id_degree);

	
	public boolean modifyCompetence(Competence modify);
	//public List<Competence> getCompetencesDontExistOnSubject(long id_subject);
	public boolean deleteCompetenceFromSubject(long id_competence, long id_subject);
	
	public  Competence getCompetenceByName(String name);
	public String getNextCode();

	
	//public boolean existsInSubject(long id_subject, Competence c);

}
