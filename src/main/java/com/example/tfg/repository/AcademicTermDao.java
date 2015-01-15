package com.example.tfg.repository;

import java.util.List;

import com.example.tfg.domain.AcademicTerm;

public interface AcademicTermDao {

	public boolean addAcademicTerm(AcademicTerm academicTerm);
	public List<AcademicTerm> getAll();
    public boolean saveAcademicTerm(AcademicTerm academicTerm);
    public AcademicTerm getAcademicTerm(Long id);
	public boolean deleteAcademicTerm(Long id);
	public List<AcademicTerm> getAcademicTermsForDegree(Long id_degree);

	public boolean existByCode(String term);
}
