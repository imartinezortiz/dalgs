package com.example.tfg.service;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tfg.domain.AcademicTerm;

@Service
public interface AcademicTermService {

	public boolean addAcademicTerm(AcademicTerm academicTerm);

	public boolean modifyAcademicTerm(AcademicTerm academicTerm);
	
	public List<AcademicTerm> getAcademicsTerm(Integer pageIndex);

	public boolean deleteAcademicTermCollection(Collection<AcademicTerm> academicList);
	
	public boolean deleteAcademicTerm(AcademicTerm academicTerm);

	public Integer numberOfPages();

	public List<AcademicTerm> getAcademicTermsByDegree(Long id_degree);

	public AcademicTerm getAcademicTerm(Long id_academic);

}
