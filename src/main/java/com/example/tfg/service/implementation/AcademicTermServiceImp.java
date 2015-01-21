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
	 
		Long a = daoAcademicTerm.isDisabled(academicTerm.getTerm(), academicTerm.getDegree().getId());
	 	 if( a !=null){
	 		 academicTerm.setId(a);
		 	return daoAcademicTerm.saveAcademicTerm(academicTerm);
	 	 }
	 	 else if (!daoAcademicTerm.exists(academicTerm.getTerm(), academicTerm.getDegree().getId()))
				return daoAcademicTerm.addAcademicTerm(academicTerm);
	 	 
		 	return daoAcademicTerm.saveAcademicTerm(academicTerm);
//return false;

	}


	@Transactional(readOnly = false)
	public boolean modifyAcademicTerm(AcademicTerm academicTerm) {
	 	if (!daoAcademicTerm.exists(academicTerm.getTerm(), academicTerm.getDegree().getId()))
	 		return daoAcademicTerm.saveAcademicTerm(academicTerm);
	 	return false;
	}

	@Transactional(readOnly = false)
	public List<AcademicTerm> getAcademicsTerm(){//String term) {
		return daoAcademicTerm.getAcademicsTerm();//term);
	}
	
	/*@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteTerm(String term) {
		return daoAcademicTerm.deleteTerm(term);
	}*/
	
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteAcademicTerm(Long id_academic) {
		return daoAcademicTerm.deleteAcademicTerm(id_academic);
	}

	/*@Transactional(readOnly = false)
	public List<AcademicTerm> getAcademicTermsForDegree(Long id_degree) {
		return daoAcademicTerm.getAcademicTermsForDegree(id_degree);
	}
	*/
	/*@Transactional(readOnly = true)
	public List<String> getAllTerms() {
		return daoAcademicTerm.getAllTerms();
	}
	*/
	

	@Transactional(readOnly = true)
	public AcademicTerm getAcademicTerm( Long id_academic) {
		return daoAcademicTerm.getAcademicTerm(id_academic);
	}

	@Transactional(readOnly = false)
	public boolean modifyTerm(String term, String newTerm) {
		if(!daoAcademicTerm.existTerm(newTerm))
		return daoAcademicTerm.modifyTerm(term, newTerm);
		return false;
	}




}
