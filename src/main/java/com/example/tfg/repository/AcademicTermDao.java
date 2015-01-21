package com.example.tfg.repository;

import java.util.List;

import com.example.tfg.domain.AcademicTerm;

public interface AcademicTermDao {

	public boolean addAcademicTerm(AcademicTerm academicTerm);
	//public List<AcademicTerm> getAll();
    public boolean saveAcademicTerm(AcademicTerm academicTerm);
    public List<AcademicTerm> getAcademicsTerm();//String term);
    public AcademicTerm getAcademicTermById(Long id);

	//public boolean deleteTerm(String term);
	public boolean deleteAcademicTerm(Long id_academic);

	//public List<AcademicTerm> getAcademicTermsForDegree(Long id_degree);
	//public List<String> getAllTerms();
	public AcademicTerm getAcademicTerm(Long id_academic);
	//public AcademicTerm getAcademicTermDegree(String term, Long id_degree);

	public boolean exists(String term, Long id_degree);
	public boolean existTerm(String term);
	public boolean modifyTerm(String term, String newTerm);
	public Long isDisabled(String term, Long id);

}
