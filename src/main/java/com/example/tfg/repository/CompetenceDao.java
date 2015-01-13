package com.example.tfg.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.tfg.domain.Competence;


@Repository
public interface CompetenceDao {
	public boolean addCompetence(Competence competence);
	public List<Competence> getAll();
    public boolean saveCompetence(Competence competence);
    public Competence getCompetence(long id);
	public boolean deleteCompetence(long id);
	public List<Competence> getCompetencesForSubject(long id_subject);
	public List<Competence> getCompetencesForDegree(long id_degree);
	public boolean existByCode(String code);

    public Competence getCompetenceByName(String name);
	public String getNextCode();

}
