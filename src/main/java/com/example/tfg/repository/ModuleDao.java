package com.example.tfg.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.tfg.domain.Degree;
import com.example.tfg.domain.Module;

@Repository
public interface ModuleDao {
	public boolean addModule(Module module);

	public List<Module> getAll();

	public boolean saveModule(Module module);

	public Module getModule(Long id);

	public boolean deleteModule(Module module);


	public String getNextCode();

	public Module existByCode(String code, Long id_degree);

	public Collection<Module> getModulesForDegree(Long id);

	public boolean deleteModulesForDegree(Degree d);
}
