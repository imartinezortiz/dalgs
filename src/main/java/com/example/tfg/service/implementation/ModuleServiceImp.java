package com.example.tfg.service.implementation;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tfg.domain.Degree;
import com.example.tfg.domain.Module;
import com.example.tfg.domain.Module;
import com.example.tfg.repository.ModuleDao;
import com.example.tfg.service.DegreeService;
import com.example.tfg.service.ModuleService;

@Service
public class ModuleServiceImp implements ModuleService {

	@Autowired
	private ModuleDao daoModule;
	
	@Autowired
	private DegreeService serviceDegree;
	
	@Transactional(readOnly=false)
	public boolean addModule(Module module, Long id_degree) {
		Module existModule = daoModule.existByCode(module.getInfo().getCode());
		Degree degree = serviceDegree.getDegree(id_degree);
		if(existModule == null){
			module.setDegree(degree);
			degree.getModules().add(module);
			return daoModule.addModule(module);
				
		}else if(existModule.isDeleted()==true){
			existModule.setInfo(module.getInfo());
			existModule.setDeleted(false);
			degree.getModules().add(existModule);
			return daoModule.saveModule(existModule);				
		}
		else return false;		
	}

	@Transactional(readOnly=true)
	public List<Module> getAll() {
		return daoModule.getAll();
	}

	@Transactional(readOnly=false)
	public boolean modifyModule(Module modify, Long id) {
		Module module = daoModule.getModule(id);
		module.setInfo(modify.getInfo());

		

		return daoModule.saveModule(module);
	}

	@Transactional(readOnly=true)
	public Module getModule(Long id) {
		return daoModule.getModule(id);
	}

	@Transactional(readOnly=false)
	public boolean deleteModule(Long id) {
		return daoModule.deleteModule(id);
	}

	@Transactional(readOnly=true)
	public Module getModuleAll(Long id_module, Long id_degree) {
		
		
		Module p = daoModule.getModule(id_module);
		Degree d = serviceDegree.getDegree(id_degree);
		p.setDegree(d);
		return p;
	}

	@Override
	public Collection<Module> getModulesForDegree(Long id) {
		// TODO Auto-generated method stub
		return daoModule.getModulesForDegree(id);
	}



}
