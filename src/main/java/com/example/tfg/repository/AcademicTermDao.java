package com.example.tfg.repository;

import java.util.List;

import com.example.tfg.domain.AcademicTerm;

public interface AcademicTermDao {

	public boolean addAcademicTerm(AcademicTerm academicTerm);
    public boolean saveAcademicTerm(AcademicTerm academicTerm);
    public List<AcademicTerm> getAcademicsTerm(Integer pageIndex);
    public AcademicTerm getAcademicTermById(Long id);

	public boolean deleteAcademicTerm(Long id_academic);


	public boolean exists(String term, Long id_degree);
	public boolean existTerm(String term);
	public boolean modifyTerm(String term, String newTerm);
	public Long isDisabled(String term, Long id);

	public Integer numberOfPages();
}
