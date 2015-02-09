package com.example.tfg.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tfg.domain.AcademicTerm;
import com.example.tfg.domain.Degree;
import com.example.tfg.domain.Subject;
import com.example.tfg.repository.DegreeDao;
import com.example.tfg.service.AcademicTermService;
import com.example.tfg.service.CompetenceService;
import com.example.tfg.service.DegreeService;
import com.example.tfg.service.SubjectService;

@Service
public class DegreeServiceImp implements DegreeService {

	@Autowired
	private DegreeDao daoDegree;

	@Autowired
	private SubjectService serviceSubject;

	@Autowired
	private CompetenceService serviceCompetence;

	@Autowired
	private AcademicTermService serviceAcademicTerm;

	@Transactional(readOnly = false)
	public boolean addDegree(Degree degree) {
		
		Degree existDegree = daoDegree.existByCode(degree.getInfoDegree().getCode());
		if (existDegree == null)
			return daoDegree.addDegree(degree);
		else if(existDegree.isDeleted()==true) {
			existDegree.setInfoDegree(degree.getInfoDegree());
			existDegree.setDeleted(false);
			return daoDegree.saveSubject(existDegree);
			
		}
		else return false;
//		if (!daoDegree.existByCode(degree.getInfoDegree().getCode()))
//			return daoDegree.addDegree(degree);
//		else
//			return false;

	}

	@Transactional(readOnly = true)
	public List<Degree> getAll() {
		return daoDegree.getAll();
	}

	public boolean modifyDegree(Degree degree) {

		return daoDegree.saveSubject(degree);
	}

	@Transactional(readOnly = false)
	public boolean modifyDegree(Degree degree, Long id_degree) {

		Degree modifydegree = daoDegree.getDegree(id_degree);
		modifydegree.setInfoDegree(degree.getInfoDegree());
//		if (degree.getCode() != null)
//			Modifydegree.setCode(degree.getCode());
//		if (degree.getName() != null)
//			Modifydegree.setName(degree.getName());
//		if (degree.getDescription() != null)
//			Modifydegree.setDescription(degree.getDescription());
		return daoDegree.saveSubject(modifydegree);
	}

	@Transactional(readOnly = false)
	public Degree getDegree(Long id) {
		return daoDegree.getDegree(id);
	}

	@Transactional(readOnly = false)
	public boolean deleteDegree(Long id) {
		Degree d = daoDegree.getDegree(id);
		boolean deleteSubjects = serviceSubject.deleteSubjectsForDegree(d);
		boolean deleteCompetences = serviceCompetence
				.deleteCompetencesForDegree(d);
		if (deleteSubjects && deleteCompetences) {
			for (AcademicTerm a : serviceAcademicTerm
					.getAcademicTermsByDegree(id)) {
				serviceAcademicTerm.deleteAcademicTerm(a.getId());
			}
			return daoDegree.deleteDegree(d);
		} else
			return false;
	}

	@Transactional(readOnly = true)
	public Degree getDegreeSubject(Subject p) {

		return daoDegree.getDegreeSubject(p);
	}

	@Transactional(readOnly = true)
	public String getNextCode() {
		return daoDegree.getNextCode();

	}

	@Transactional(readOnly = true)
	public Degree getDegreeAll(Long id) {

		Degree d = daoDegree.getDegree(id);
		d.setSubjects(serviceSubject.getSubjectsForDegree(id));
		d.setCompetences(serviceCompetence.getCompetencesForDegree(id));
		return d;
	}

}
