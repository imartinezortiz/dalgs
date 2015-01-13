package com.example.tfg.repository;

import java.util.List;

import com.example.tfg.domain.AcademicTerm;

public interface AcademicTermDao {

	public boolean addAcademicTerm(AcademicTerm academicTerm);
	public List<AcademicTerm> getAll();
    public boolean saveAcademicTerm(AcademicTerm academicTerm);
    public AcademicTerm getAcademicTerm(long id);
	public boolean deleteAcademicTerm(long id);
	public List<AcademicTerm> getAcademicTermsForDegree(long id_degree);

	public boolean existByCode(String term);
}
