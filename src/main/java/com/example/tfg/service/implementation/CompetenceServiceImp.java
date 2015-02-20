package com.example.tfg.service.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.tfg.domain.Competence;
import com.example.tfg.domain.Degree;
import com.example.tfg.repository.CompetenceDao;
import com.example.tfg.service.CompetenceService;
import com.example.tfg.service.DegreeService;
import com.example.tfg.service.LearningGoalService;
import com.example.tfg.service.SubjectService;

@Service
public class CompetenceServiceImp implements CompetenceService {
	@Autowired
	private CompetenceDao daoCompetence;

	@Autowired
	private SubjectService serviceSubject;

	@Autowired
	private DegreeService serviceDegree;

	@Autowired 
	private LearningGoalService serviceLearning;

	@Transactional(readOnly = false)
	public boolean addCompetence(Competence competence, Long id_degree) {
		Degree degree = serviceDegree.getDegree(id_degree);
		Competence existCompetence = daoCompetence.existByCode(competence.getInfo().getCode(), degree);
		if(existCompetence == null){
			competence.setDegree(degree);
			degree.getCompetences().add(competence);
			return daoCompetence.addCompetence(competence);			
		}
		else if (existCompetence.isDeleted() == true) {
			existCompetence.setDegree(degree);
			existCompetence.setDeleted(false);
			degree.getCompetences().add(existCompetence);
			return daoCompetence.saveCompetence(existCompetence);

		}
		else return false;

		//		if (!daoCompetence.existByCode(competence.getInfo().getCode(), degree)) {
		//
		//			competence.setDegree(degree);
		//			return daoCompetence.addCompetence(competence);
		//		}


	}

	@Transactional(readOnly = true)
	public List<Competence> getAll() {
		return daoCompetence.getAll();
	}

	@Transactional(readOnly = false)
	public Competence getCompetence(Long id) {
		return daoCompetence.getCompetence(id);
	}

	@Transactional(readOnly = false)
	public Competence getCompetenceByName(String name) {
		return daoCompetence.getCompetenceByName(name);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteCompetence(Long id) {
		Competence competence = daoCompetence.getCompetence(id);
		Collection <Competence> competences= new ArrayList<Competence>();
		competences.add(competence);
		if(serviceLearning.deleteLearningGoalForCompetences(competences))
			return daoCompetence.deleteCompetence(id);
		return false;
	}

	@Transactional(readOnly = false)
	public List<Competence> getCompetencesForSubject(Long id_subject) {

		return daoCompetence.getCompetencesForSubject(id_subject);
	}

	@Transactional(readOnly = true)
	public List<Competence> getCompetencesForDegree(Long id_degree) {
		// TODO Auto-generated method stub
		return daoCompetence.getCompetencesForDegree(id_degree);
	}

	@Transactional(readOnly = false)
	public boolean modifyCompetence(Competence competence, Long id_competence) {
		Competence modifyCompetence = daoCompetence.getCompetence(id_competence);
		modifyCompetence.setInfo(competence.getInfo());
		return daoCompetence.saveCompetence(modifyCompetence);
	}

	// ----
	/*
	 * @Transactional(readOnly = true) public boolean existsInSubject(Long
	 * id_subject, Competence c) { // TODO Auto-generated method stub return
	 * daoCompetence.existsInSubject(id_subject, c); }
	 */

	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteCompetenceFromSubject(Long id_competence,
			Long id_subject) {
		// Subject subject = daoSubject.getSubject(id);

		Collection<Competence> c = serviceSubject.getSubject(id_subject)
				.getCompetences();
		try {
			c.remove(daoCompetence.getCompetence(id_competence));
			return true;

		} catch (Exception e) {
			return false;
		}
	}

	public boolean deleteCompetencesForDegree(Degree degree) {
		if(serviceLearning.deleteLearningGoalForCompetences(degree.getCompetences()))
			return daoCompetence.deleteCompetencesForDegree(degree);
		else return false;
	}


	public boolean modifyCompetence(Competence competence) {
		return daoCompetence.saveCompetence(competence);
	}

	@Transactional(readOnly = true)
	public Competence getCompetenceAll(Long id_competence) {
		Competence competence = daoCompetence.getCompetence(id_competence);
		//		Competence c = daoCompetence.getCompetenceAll(id_competence);
		competence.setLearningGoals(serviceLearning.getLearningGoalsFromCompetence(competence));

		return competence;
	}

	// @Transactional(readOnly = true)
	// public String getNextCode(){
	// return daoCompetence.getNextCode();
	//
	// }
}
