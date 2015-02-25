package com.example.tfg.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tfg.classes.ResultClass;
import com.example.tfg.domain.Competence;
import com.example.tfg.domain.Degree;

@Service
public interface CompetenceService {
	public ResultClass<Boolean> addCompetence(Competence competence, Long id_degree);

	public List<Competence> getAll();

	public Competence getCompetence(Long id);

	public boolean deleteCompetence(Long id);

	public List<Competence> getCompetencesForSubject(Long id_subject);

	public List<Competence> getCompetencesForDegree(Long id_degree);

	public ResultClass<Boolean> modifyCompetence(Competence competence, Long id_competence, Long id_degree);

	public Competence getCompetenceByName(String name);

	public boolean deleteCompetencesForDegree(Degree d);

	public boolean modifyCompetence(Competence competence);

	public Competence getCompetenceAll(Long id_competence);

	public boolean deleteCompetenceFromSubject(Long id_competence,
			Long id_subject);
	public ResultClass<Boolean> unDeleteCompetence(Competence competence, Long id_degree);
}
