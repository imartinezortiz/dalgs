package com.example.tfg.repository;

import java.util.List;

import com.example.tfg.domain.AcademicTerm;

public interface AcademicTermDao {

	public boolean addAcademicTerm(AcademicTerm academicTerm);
	//public List<AcademicTerm> getAll();
    public boolean saveAcademicTerm(AcademicTerm academicTerm);
    public List<AcademicTerm> getAcademicsTerm(String term);
    public AcademicTerm getAcademicTermById(Long id);

	public boolean deleteAcademicTerm(String term);
	public List<AcademicTerm> getAcademicTermsForDegree(Long id_degree);
	public List<String> getAllTerms();
	public AcademicTerm getAcademicTermDegree(String term, Long id_degree);

}
