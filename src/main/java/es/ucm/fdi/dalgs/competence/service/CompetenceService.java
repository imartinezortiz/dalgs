package es.ucm.fdi.dalgs.competence.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.competence.repository.CompetenceRepository;
import es.ucm.fdi.dalgs.degree.service.DegreeService;
import es.ucm.fdi.dalgs.domain.Competence;
import es.ucm.fdi.dalgs.domain.Degree;
import es.ucm.fdi.dalgs.learningGoal.service.LearningGoalService;
import es.ucm.fdi.dalgs.subject.service.SubjectService;

@Service
public class CompetenceService {
	@Autowired
	private CompetenceRepository daoCompetence;

	@Autowired
	private SubjectService serviceSubject;

	@Autowired
	private DegreeService serviceDegree;

	@Autowired 
	private LearningGoalService serviceLearning;
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = false)
	public ResultClass<Boolean> addCompetence(Competence competence, Long id_degree) {
		competence.setDegree(serviceDegree.getDegree(id_degree).getE());
		
		Competence competenceExists = daoCompetence.existByCode(competence.getInfo().getCode(), competence.getDegree());
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		if( competenceExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code already exists");

			if (competenceExists.getIsDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");

			}
			result.setE(false);
			result.setErrorsList(errors);
		}
		else{
			
			boolean r = daoCompetence.addCompetence(competence);
			if (r) 
				result.setE(true);
		}
		return result;	
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<List<Competence>> getAll() {
		ResultClass<List<Competence>> result = new ResultClass<List<Competence>>();
		result.setE(daoCompetence.getAll());
		return result;
	}
	
	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = false)
	public ResultClass<Competence> getCompetence(Long id) {
		ResultClass<Competence> result = new ResultClass<Competence>();
		result.setE(daoCompetence.getCompetence(id));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = false)
	public ResultClass<Competence> getCompetenceByName(String name) {
		ResultClass<Competence> result = new ResultClass<Competence>();
		result.setE(daoCompetence.getCompetenceByName(name));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteCompetence(Long id) {
		Competence competence = daoCompetence.getCompetence(id);
		Collection <Competence> competences= new ArrayList<Competence>();
		competences.add(competence);
		if(serviceLearning.deleteLearningGoalForCompetences(competences).getE())
			return daoCompetence.deleteCompetence(id);
		return false;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = false)
	public List<Competence> getCompetencesForSubject(Long id_subject) {

		return daoCompetence.getCompetencesForSubject(id_subject);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public List<Competence> getCompetencesForDegree(Long id_degree) {
		return daoCompetence.getCompetencesForDegree(id_degree);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = false)
	public ResultClass<Boolean> modifyCompetence(Competence competence, Long id_competence, Long id_degree) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		
		competence.setDegree(serviceDegree.getDegree(id_degree).getE());
		Competence modifyCompetence = daoCompetence.getCompetence(id_competence);
		
		Competence competenceExists = daoCompetence.existByCode(competence.getInfo().getCode(),competence.getDegree() );
		
		if(!competence.getInfo().getCode().equalsIgnoreCase(modifyCompetence.getInfo().getCode()) && 
				competenceExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("New code already exists");

			if (competenceExists.getIsDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");

			}
			result.setErrorsList(errors);
			result.setE(false);
		}
		else{
			modifyCompetence.setInfo(competence.getInfo());
			boolean r = daoCompetence.saveCompetence(modifyCompetence);
			if (r) 
				result.setE(true);
		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultClass<Boolean> deleteCompetenceFromSubject(Long id_competence,
			Long id_subject) {
		// Subject subject = daoSubject.getSubject(id);
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		Collection<Competence> c = serviceSubject.getSubject(id_subject).getE()
				.getCompetences();
		try {
			c.remove(daoCompetence.getCompetence(id_competence));
			result.setE(true);
			

		} catch (Exception e) {
			result.setE(false);;
		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	public ResultClass<Boolean> deleteCompetencesForDegree(Degree degree) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		if(serviceLearning.deleteLearningGoalForCompetences(degree.getCompetences()).getE()){
			result.setE(daoCompetence.deleteCompetencesForDegree(degree));
		}
			
		else result.setE(false);
		
		return result;
	}

//	@PreAuthorize("hasRole('ROLE_ADMIN')")	
//	public boolean modifyCompetence(Competence competence) {
//		return daoCompetence.saveCompetence(competence);
//	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<Competence> getCompetenceAll(Long id_competence) {
		ResultClass<Competence> result = new ResultClass<Competence>();
		Competence competence = daoCompetence.getCompetence(id_competence);
		//		Competence c = daoCompetence.getCompetenceAll(id_competence);
		competence.setLearningGoals(serviceLearning.getLearningGoalsFromCompetence(competence).getE());
		result.setE(competence);
		return result;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = false)
	public ResultClass<Boolean> unDeleteCompetence(Competence competence, Long id_degree) {
		
		competence.setDegree(serviceDegree.getDegree(id_degree).getE());
		Competence c = daoCompetence.existByCode(competence.getInfo().getCode(), competence.getDegree());
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		if(c == null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code doesn't exist");
			result.setErrorsList(errors);

		}
		else{
			if(!c.getIsDeleted()){
				Collection<String> errors = new ArrayList<String>();
				errors.add("Code is not deleted");
				result.setErrorsList(errors);
			}

			c.setDeleted(false);
			c.setInfo(competence.getInfo());
			boolean r = daoCompetence.saveCompetence(c);
			if(r) 
				result.setE(true);	

		}
		return result;
	}
}
