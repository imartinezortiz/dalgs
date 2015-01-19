package com.example.tfg.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tfg.domain.AcademicTerm;

@Service
public interface AcademicTermService {
	public boolean addAcademicTerm(AcademicTerm academicTerm);
	//public List<AcademicTerm> getAll();
	public boolean modifyAcademicTerm(AcademicTerm academicTerm);
	public  List<AcademicTerm> getAcademicsTerm(String term);
	public boolean deleteTerm(String term);
	public boolean deleteAcademicTerm(String term, Long id_degree);

	public List<AcademicTerm> getAcademicTermsForDegree(Long id_degree);
	public List<String> getAllTerms();
	public AcademicTerm getAcademicTermDegree(String term, Long id_degree);

	
	public boolean modifyTerm(String term, String newTerm) ;

}
