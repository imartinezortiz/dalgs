package com.example.tfg.service;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tfg.domain.Degree;
import com.example.tfg.domain.Module;

@Service
public interface ModuleService {

	public boolean addModule(Module newModule, Long id_degree);

	public List<Module> getAll();

	public boolean modifyModule(Module modify, Long id);

	public Module getModule(Long id);

	public boolean deleteModule(Long id);

	public Module getModuleAll(Long id_module, Long id_degree);

	public Collection<Module> getModulesForDegree(Long id);

	public boolean modifyModule(Module module);

	public boolean deleteModulesForDegree(Degree d);

	

}
