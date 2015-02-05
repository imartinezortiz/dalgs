package com.example.tfg.repository;

import java.util.List;

import com.example.tfg.domain.AcademicTerm;
import com.example.tfg.domain.Degree;

public interface AcademicTermDao {

	public boolean addAcademicTerm(AcademicTerm academicTerm);

	// public List<AcademicTerm> getAll();
	public boolean saveAcademicTerm(AcademicTerm academicTerm);

	public List<AcademicTerm> getAcademicsTerm(Integer pageIndex);

	public AcademicTerm getAcademicTermById(Long id);

	// public boolean deleteTerm(String term);
	public boolean deleteAcademicTerm(Long id_academic);

	public List<AcademicTerm> getAcademicTermsByDegree(Long id_degree);

	// public List<AcademicTerm> getAcademicTermsForDegree(Long id_degree);
	// public List<String> getAllTerms();
	// spublic AcademicTerm getAcademicTerm(Long id_academic);
	// public AcademicTerm getAcademicTermDegree(String term, Long id_degree);

	public boolean exists(AcademicTerm academicTerm);

	public boolean existTerm(String term);

	public boolean modifyTerm(String term, String newTerm);

	public Long isDisabled(String term, Degree degree);

	public Integer numberOfPages();
}
