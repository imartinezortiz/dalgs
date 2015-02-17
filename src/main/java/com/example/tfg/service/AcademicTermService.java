package com.example.tfg.service;

import java.util.Collection;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.tfg.domain.AcademicTerm;

@Service
public interface AcademicTermService {

	public boolean addAcademicTerm(AcademicTerm academicTerm);

	public boolean modifyAcademicTerm(AcademicTerm academicTerm,
			Long id_academic);
//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public List<AcademicTerm> getAcademicsTerm(Integer pageIndex);// String
																	// term);

	public boolean deleteAcademicTerm(Collection<AcademicTerm> academicList);
	
	public boolean deleteAcademicTerm(Long id_academic);

	public AcademicTerm getAcademicTerm(Long id_academic);

//	public boolean modifyTerm(String term, String newTerm);

	public Integer numberOfPages();

	public List<AcademicTerm> getAcademicTermsByDegree(Long id_degree);

	public AcademicTerm getAcademicTermAll(Long id_academic);

}
