package com.example.tfg.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tfg.classes.ResultClass;
import com.example.tfg.domain.Degree;
import com.example.tfg.domain.Subject;

@Service
public interface DegreeService {
	public ResultClass<Boolean> addDegree(Degree degree);

	public List<Degree> getAll();

//	public boolean modifyDegree(Degree degree);

	public ResultClass<Boolean> modifyDegree(Degree degree, Long id);

	public Degree getDegree(Long id);

	public boolean deleteDegree(Long id);

	public Degree getDegreeSubject(Subject p);

	public String getNextCode();

	public Degree getDegreeAll(Long id);

	public ResultClass<Boolean> unDeleteDegree(Degree degree);

}
