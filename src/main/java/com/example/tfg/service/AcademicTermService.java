package com.example.tfg.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tfg.domain.AcademicTerm;

@Service
public interface AcademicTermService {
	public boolean addAcademicTerm(AcademicTerm academicTerm);
	public List<AcademicTerm> getAll();
	public boolean modifyAcademicTerm(AcademicTerm academicTerm);
	public  AcademicTerm getAcademicTerm(Long id);
	public boolean deleteAcademicTerm(Long id);
	public List<AcademicTerm> getAcademicTermsForDegree(Long id_degree);
}
