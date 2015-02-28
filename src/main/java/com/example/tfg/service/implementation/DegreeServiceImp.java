package com.example.tfg.service.implementation;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tfg.domain.AcademicTerm;
import com.example.tfg.domain.Degree;
import com.example.tfg.domain.Subject;
import com.example.tfg.repository.DegreeDao;
import com.example.tfg.service.AcademicTermService;
import com.example.tfg.service.CompetenceService;
import com.example.tfg.service.DegreeService;
import com.example.tfg.service.ModuleService;

@Service
public class DegreeServiceImp implements DegreeService {

	@Autowired
	private DegreeDao daoDegree;

	
	@Autowired
	private ModuleService serviceModule;

	@Autowired
	private CompetenceService serviceCompetence;

	@Autowired
	private AcademicTermService serviceAcademicTerm;

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = false)
	public boolean addDegree(Degree degree) {
		
		Degree existDegree = daoDegree.existByCode(degree.getInfo().getCode());
		if (existDegree == null)
			return daoDegree.addDegree(degree);
		else if(existDegree.isDeleted()==true) {
			existDegree.setInfo(degree.getInfo());
			existDegree.setDeleted(false);
			return daoDegree.saveDegree(existDegree);
			
		}
		else return false;

	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public List<Degree> getAll() {
		return daoDegree.getAll();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	public boolean modifyDegree(Degree degree) {
		return daoDegree.saveDegree(degree);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = false)
	public boolean modifyDegree(Degree degree, Long id_degree) {
		Degree modifydegree = daoDegree.getDegree(id_degree);
		modifydegree.setInfo(degree.getInfo());
		return daoDegree.saveDegree(modifydegree);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = false)
	public Degree getDegree(Long id) {
		return daoDegree.getDegree(id);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = false)
	public boolean deleteDegree(Long id) {
		Degree d = daoDegree.getDegree(id);
		boolean deleteModules = serviceModule.deleteModulesForDegree(d);
		boolean deleteCompetences = serviceCompetence
				.deleteCompetencesForDegree(d);
		Collection<AcademicTerm> academicList = serviceAcademicTerm.getAcademicTermsByDegree(id);
		
		boolean deleteAcademic = serviceAcademicTerm.deleteAcademicTermCollection(academicList);
		if (deleteModules && deleteCompetences && deleteAcademic) {
			return daoDegree.deleteDegree(d);
		} else	return false;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public Degree getDegreeSubject(Subject p) {

		return daoDegree.getDegreeSubject(p);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public String getNextCode() {
		return daoDegree.getNextCode();

	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public Degree getDegreeAll(Long id) {
	

		Degree d = daoDegree.getDegree(id);
		d.setModules(serviceModule.getModulesForDegree(id));
		d.setCompetences(serviceCompetence.getCompetencesForDegree(id));
		return d;
	}

}
