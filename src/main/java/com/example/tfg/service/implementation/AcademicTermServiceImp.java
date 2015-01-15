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

	@Transactional(readOnly = true)
	public List<AcademicTerm> getAll() {
		return daoAcademicTerm.getAll();
	}

	@Transactional(readOnly = false)
	public boolean modifyAcademicTerm(AcademicTerm academicTerm) {
		return daoAcademicTerm.saveAcademicTerm(academicTerm);
	
	}

	@Transactional(readOnly = false)
	public AcademicTerm getAcademicTerm(Long id) {
		return daoAcademicTerm.getAcademicTerm(id);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteAcademicTerm(Long id) {
		return daoAcademicTerm.deleteAcademicTerm(id);
	}

	@Transactional(readOnly = false)
	public List<AcademicTerm> getAcademicTermsForDegree(Long id_degree) {
		return daoAcademicTerm.getAcademicTermsForDegree(id_degree);
	}
	


}
