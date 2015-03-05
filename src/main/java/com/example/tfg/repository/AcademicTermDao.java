package com.example.tfg.repository;

import java.util.Collection;
import java.util.List;

import com.example.tfg.domain.AcademicTerm;
import com.example.tfg.domain.Degree;

public interface AcademicTermDao {

	public boolean addAcademicTerm(AcademicTerm academicTerm);

	public boolean saveAcademicTerm(AcademicTerm academicTerm);

	public List<AcademicTerm> getAcademicsTerm(Integer pageIndex);

	public AcademicTerm getAcademicTermById(Long id);

	public boolean deleteAcademicTerm(Long id_academic);

	public List<AcademicTerm> getAcademicTermsByDegree(Long id_degree);

	public boolean modifyTerm(String term, String newTerm);

	public Integer numberOfPages();

	public AcademicTerm exists(String term, Degree degree);

	public boolean deleteAcademicTerm(Collection<AcademicTerm> academicList);

}
