package com.example.tfg.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tfg.domain.Degree;
import com.example.tfg.domain.Subject;

@Service
public interface DegreeService {
	public boolean addDegree(Degree degree);

	public List<Degree> getAll();

//	public boolean modifyDegree(Degree degree);

	public boolean modifyDegree(Degree degree, Long id);

	public Degree getDegree(Long id);

	public boolean deleteDegree(Long id);

	public Degree getDegreeSubject(Subject p);

	public String getNextCode();

	public Degree getDegreeAll(Long id);

}
