package com.example.tfg.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.tfg.domain.Degree;
import com.example.tfg.domain.Subject;
import com.example.tfg.repository.CompetenceDao;
import com.example.tfg.repository.DegreeDao;
import com.example.tfg.repository.SubjectDao;
import com.example.tfg.service.DegreeService;

@Service
public class DegreeServiceImp implements DegreeService {

	@Autowired
	private DegreeDao daoDegree;
	
	@Autowired
	private SubjectDao daoSubject;

	@Autowired
	private CompetenceDao daoCompetence;
	
	@Transactional(readOnly = false)
	public boolean addDegree(Degree degree) {
		if (!daoDegree.existByCode(degree.getCode()))
			return daoDegree.addDegree(degree);
		else return false;
		
	}
	
	@Transactional(readOnly = true)
	public List<Degree> getAll(){
		return daoDegree.getAll();
	}

	@Transactional(readOnly = false)
	public boolean modifyDegree(Degree degree){
		return daoDegree.saveSubject(degree);
	}
	
	@Transactional(readOnly = false)
	public  Degree getDegree(Long id){
		return daoDegree.getDegree(id);
	}
	
	@Transactional(readOnly = false)
	public boolean deleteDegree(Long id){
		Degree d = daoDegree.getDegree(id);
		boolean deleteSubjects = daoSubject.deleteSubjectsForDegree(d);
		boolean deleteCompetences = daoCompetence.deleteCompetencesForDegree(d);
		if (deleteSubjects && deleteCompetences)
			return daoDegree.deleteDegree(d);
		else return false;
	}

	@Transactional(readOnly = true)
	public Degree getDegreeSubject(Subject p) {
		
		return daoDegree.getDegreeSubject(p);
	}
	@Transactional(readOnly = true)
	public String getNextCode(){
		return daoDegree.getNextCode();
	
	}
}
