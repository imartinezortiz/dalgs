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
		
		Module moduleExists = daoModule.existByCode(module.getInfo().getCode(), id_degree);
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
			module.setDegree(serviceDegree.getDegree(id_degree).getE());
			boolean r = daoModule.addModule(module);
			if (r) 
				result.setE(true);
		}
		return result;		
	}
	

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly=true)
	public ResultClass<List<Module>> getAll() {
		ResultClass<List<Module>> result = new ResultClass<List<Module>>();
		result.setE(daoModule.getAll());
		return result;
	}

//	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly=false)
	public ResultClass<Boolean> modifyModule(Module module, Long id_module, Long id_degree) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		Module modifyModule = daoModule.getModule(id_degree);
		
		Module moduleExists = daoModule.existByCode(module.getInfo().getCode(), id_degree);
		
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
	public ResultClass<Module> getModule(Long id) {
		ResultClass<Module> result = new ResultClass<Module>();
		result.setE(daoModule.getModule(id));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly=false)
	public ResultClass<Boolean> deleteModule(Long id) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		Module module = daoModule.getModule(id);
		if(serviceTopic.deleteTopicsForModule(module).getE()){
			result.setE(daoModule.deleteModule(module));
			return result;
		}
		result.setE(false);
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly=true)
	public ResultClass<Module> getModuleAll(Long id_module) {
		ResultClass<Module> result = new ResultClass<Module>();
		Module p = daoModule.getModule(id_module);
		p.setTopics(serviceTopic.getTopicsForModule(id_module).getE());
		result.setE(p);
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly=true)
	public ResultClass<Collection<Module>> getModulesForDegree(Long id) {
		ResultClass<Collection<Module>> result = new ResultClass<Collection<Module>>();
		result.setE(daoModule.getModulesForDegree(id));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly=false)
	public ResultClass<Boolean> modifyModule(Module module) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		result.setE(daoModule.saveModule(module));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly=false)
	public ResultClass<Boolean> deleteModulesForDegree(Degree d) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		if(serviceTopic.deleteTopicsForModules(d.getModules()).getE()){
			result.setE(daoModule.deleteModulesForDegree(d));
		}
		result.setE(false);	
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = false)
	public ResultClass<Boolean> unDeleteModule(Module module, Long id_degree) {
		Module m = daoModule.existByCode(module.getInfo().getCode(), id_degree);
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
