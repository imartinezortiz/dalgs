package com.example.tfg.service.implementation;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tfg.domain.Degree;
import com.example.tfg.domain.Module;
import com.example.tfg.repository.ModuleDao;
import com.example.tfg.service.DegreeService;
import com.example.tfg.service.ModuleService;
import com.example.tfg.service.TopicService;

@Service
public class ModuleServiceImp implements ModuleService {

	@Autowired
	private ModuleDao daoModule;

	@Autowired
	private DegreeService serviceDegree;

	@Autowired
	private TopicService serviceTopic; 

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly=false)
	public boolean addModule(Module module, Long id_degree) {
		Module existModule = daoModule.existByCode(module.getInfo().getCode());
		Degree degree = serviceDegree.getDegree(id_degree);
		if(existModule == null){
			module.setDegree(degree);
			degree.getModules().add(module);
			if(daoModule.addModule(module))
				return serviceDegree.modifyDegree(degree);


		}else if(existModule.isDeleted()==true){
			existModule.setInfo(module.getInfo());
			existModule.setDeleted(false);
			degree.getModules().add(existModule);
			if(daoModule.saveModule(existModule))
				return serviceDegree.modifyDegree(degree);

		}
		return false;		
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly=true)
	public List<Module> getAll() {
		return daoModule.getAll();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly=false)
	public boolean modifyModule(Module modify, Long id) {
		Module module = daoModule.getModule(id);
		module.setInfo(modify.getInfo());
		return daoModule.saveModule(module);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly=true)
	public Module getModule(Long id) {
		return daoModule.getModule(id);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly=false)
	public boolean deleteModule(Long id) {
		Module module = daoModule.getModule(id);
		if(serviceTopic.deleteTopicsForModule(module))
			return daoModule.deleteModule(module);
		return false;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly=true)
	public Module getModuleAll(Long id_module, Long id_degree) {

		Module p = daoModule.getModule(id_module);
		p.setTopics(serviceTopic.getTopicsForModule(id_module));
		return p;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly=true)
	public Collection<Module> getModulesForDegree(Long id) {

		return daoModule.getModulesForDegree(id);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly=false)
	public boolean modifyModule(Module module) {
		return daoModule.saveModule(module);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly=false)
	public boolean deleteModulesForDegree(Degree d) {
		if(serviceTopic.deleteTopicsForModules(d.getModules()))
			return daoModule.deleteModulesForDegree(d);
		return false;
	}



}
