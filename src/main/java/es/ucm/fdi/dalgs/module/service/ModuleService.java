package es.ucm.fdi.dalgs.module.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.degree.service.DegreeService;
import es.ucm.fdi.dalgs.domain.Degree;
import es.ucm.fdi.dalgs.domain.Module;
import es.ucm.fdi.dalgs.module.repository.ModuleRepository;
import es.ucm.fdi.dalgs.topic.service.TopicService;

@Service
public class ModuleService {

	@Autowired
	private ModuleRepository daoModule;

	@Autowired
	private DegreeService serviceDegree;

	@Autowired
	private TopicService serviceTopic; 

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly=false)
	public ResultClass<Boolean> addModule(Module module, Long id_degree) {
		
		Module moduleExists = daoModule.existByCode(module.getInfo().getCode());
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		if( moduleExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code already exists");

			if (moduleExists.getIsDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");

			}
			result.setE(false);
			result.setErrorsList(errors);
		}
		else{
			module.setDegree(serviceDegree.getDegree(id_degree));
			boolean r = daoModule.addModule(module);
			if (r) 
				result.setE(true);
		}
		return result;		
	}
	

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly=true)
	public List<Module> getAll() {
		return daoModule.getAll();
	}

//	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly=false)
	public ResultClass<Boolean> modifyModule(Module module, Long id) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		Module modifyModule = daoModule.getModule(id);
		
		Module moduleExists = daoModule.existByCode(module.getInfo().getCode());
		
		if(!module.getInfo().getCode().equalsIgnoreCase(modifyModule.getInfo().getCode()) && 
				moduleExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("New code already exists");

			if (moduleExists.getIsDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");

			}
			result.setErrorsList(errors);
			result.setE(false);
		}
		else{
			modifyModule.setInfo(module.getInfo());
			boolean r = daoModule.saveModule(modifyModule);
			if (r) 
				result.setE(true);
		}
		return result;
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

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = false)
	public ResultClass<Boolean> unDeleteModule(Module module) {
		Module m = daoModule.existByCode(module.getInfo().getCode());
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		if(m == null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code doesn't exist");
			result.setErrorsList(errors);

		}
		else{
			if(!m.getIsDeleted()){
				Collection<String> errors = new ArrayList<String>();
				errors.add("Code is not deleted");
				result.setErrorsList(errors);
			}

			m.setDeleted(false);
			m.setInfo(module.getInfo());
			boolean r = daoModule.saveModule(m);
			if(r) 
				result.setE(true);	

		}
		return result;
	}
}
