package com.example.tfg.service.implementation;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.tfg.domain.Competence;
import com.example.tfg.repository.CompetenceDao;
import com.example.tfg.repository.SubjectDao;
import com.example.tfg.service.CompetenceService;
@Service
public class CompetenceServiceImp implements CompetenceService {
	@Autowired
	private CompetenceDao daoCompetence;

	@Autowired SubjectDao daoSubject;
	
	@Transactional(readOnly = false)
	public boolean addCompetence(Competence competence) {
		if(!daoCompetence.existByCode(competence.getCode()))
			return daoCompetence.addCompetence(competence);
		return false;

	}

	@Transactional(readOnly = true)
	public List<Competence> getAll(){
		return daoCompetence.getAll();
	}

	/*
	 * @Transactional(readOnly = false)
	public void modifyCompetence(long id, String name, String description,Subject subject){
		Competence aux = daoCompetence.getCompetence(id);
		aux.setName(name);
		aux.setDescription(description);
		//		aux.setSubject(subject);
		daoCompetence.saveCompetence(aux);
	}
	*/


	@Transactional(readOnly=false)
	public  Competence getCompetence(long id){
		return daoCompetence.getCompetence(id);
	}

	@Transactional(readOnly=false)
	public  Competence getCompetenceByName(String name){
		return daoCompetence.getCompetenceByName(name);
	}


	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteCompetence(long id){
		//	Subject subject = daoSubject.getSubject(id);
		return daoCompetence.deleteCompetence(id);
	}

	@Transactional(readOnly = false)
	public List<Competence> getCompetencesForSubject(long id_subject) {
		
		return daoCompetence.getCompetencesForSubject(id_subject);
	}

	@Transactional(readOnly = true)
	public List<Competence> getCompetencesForDegree(long id_degree) {
		// TODO Auto-generated method stub
		return daoCompetence.getCompetencesForDegree(id_degree);
	}



	@Transactional(readOnly = false)
	public boolean modifyCompetence(Competence modify){
		return daoCompetence.saveCompetence(modify);
	}
	//----
	/*
	@Transactional(readOnly = true)
	public boolean existsInSubject(long id_subject, Competence c) {
		// TODO Auto-generated method stub
		return daoCompetence.existsInSubject(id_subject, c);
	}
	*/


	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteCompetenceFromSubject(long id_competence, long id_subject){
		//Subject subject = daoSubject.getSubject(id);
		
		Collection<Competence> c = daoSubject.getSubject(id_subject).getCompetences();
		try{
			c.remove(daoCompetence.getCompetence(id_competence));
			return true;

		}
		catch(Exception e){
			return false;
		}
	}
	
	@Transactional(readOnly = true)
	public String getNextCode(){
		return daoCompetence.getNextCode();
	
	}
}
