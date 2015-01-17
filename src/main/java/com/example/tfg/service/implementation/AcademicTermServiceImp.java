package com.example.tfg.service.implementation;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.tfg.domain.AcademicTerm;
import com.example.tfg.repository.AcademicTermDao;
import com.example.tfg.service.AcademicTermService;

@Service
public class AcademicTermServiceImp implements AcademicTermService {

	
	@Autowired
	private AcademicTermDao daoAcademicTerm;

	@Transactional(readOnly = false)
	public boolean addAcademicTerm(AcademicTerm academicTerm) {
	//	if (!daoAcademicTerm.existByCode(academicTerm.getTerm()))
			return daoAcademicTerm.addAcademicTerm(academicTerm);
	//	else
	//		return false;

	}

/*
	@Transactional(readOnly = true)
	public List<AcademicTerm> getAll() {
		return daoAcademicTerm.getAll();
	}
*/
	@Transactional(readOnly = false)
	public boolean modifyAcademicTerm(AcademicTerm academicTerm) {
		return daoAcademicTerm.saveAcademicTerm(academicTerm);
	
	}

	@Transactional(readOnly = false)
	public List<AcademicTerm> getAcademicsTerm(String term) {
		return daoAcademicTerm.getAcademicsTerm(term);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteAcademicTerm(String term) {
		return daoAcademicTerm.deleteAcademicTerm(term);
	}

	@Transactional(readOnly = false)
	public List<AcademicTerm> getAcademicTermsForDegree(Long id_degree) {
		return daoAcademicTerm.getAcademicTermsForDegree(id_degree);
	}
	@Transactional(readOnly = true)
	public List<String> getAllTerms() {
		return daoAcademicTerm.getAllTerms();
	}

	@Transactional(readOnly = true)
	public AcademicTerm getAcademicTermDegree(String term, Long id_degree) {
		return daoAcademicTerm.getAcademicTermDegree(term,id_degree);
	}


}
