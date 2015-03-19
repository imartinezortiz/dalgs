package es.ucm.fdi.dalgs.module.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.dalgs.acl.service.AclObjectService;
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
	
	@Autowired
	private AclObjectService manageAclService;


	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly=false)
	public ResultClass<Module> addModule(Module module, Long id_degree) {
		
		boolean success = false;
		
		Module moduleExists = daoModule.existByCode(module.getInfo().getCode(), id_degree);
		ResultClass<Module> result = new ResultClass<Module>();

		if( moduleExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code already exists");
				
			if (moduleExists.getIsDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");
				result.setSingleElement(moduleExists);
			}
			else result.setSingleElement(module);
			result.setErrorsList(errors);
		}
		else{
			module.setDegree(serviceDegree.getDegree(id_degree).getSingleElement());
			success = daoModule.addModule(module);
			
			if(success){
				moduleExists = daoModule.existByCode(module.getInfo().getCode(), id_degree);
				success = manageAclService.addAclToObject(moduleExists.getId(), moduleExists.getClass().getName());
				if (success) result.setSingleElement(module);
			
			}else{
				throw new IllegalArgumentException(	"Cannot create ACL. Object not set.");

			}
		}
		
		return result;		
	}
	

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly=true)
	public ResultClass<Module> getAll() {
		ResultClass<Module> result = new ResultClass<Module>();
		result.addAll(daoModule.getAll());
		return result;
	}

//	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@PreAuthorize("hasPermission(#module, 'WRITE') or hasPermission(#module, 'ADMINISTRATION')")
	@Transactional(readOnly=false)
	public ResultClass<Boolean> modifyModule(Module module, Long id_module, Long id_degree) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		Module modifyModule = daoModule.getModule(id_module);
		
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
			result.setSingleElement(false);
		}
		else{
			modifyModule.setInfo(module.getInfo());
			boolean r = daoModule.saveModule(modifyModule);
			if (r) 
				result.setSingleElement(true);
		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly=true)
	public ResultClass<Module> getModule(Long id) {
		ResultClass<Module> result = new ResultClass<Module>();
		result.setSingleElement(daoModule.getModule(id));
		return result;
	}

//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PreAuthorize("hasPermission(#module, 'DELETE') or hasPermission(#module, 'ADMINISTRATION')" )
	@Transactional(readOnly=false)
	public ResultClass<Boolean> deleteModule(Module module) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
//		Module module = daoModule.getModule(id);
		if(serviceTopic.deleteTopicsForModule(module).getSingleElement()){
			result.setSingleElement(daoModule.deleteModule(module));
			return result;
		}
		result.setSingleElement(false);
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly=true)
	public ResultClass<Module> getModuleAll(Long id_module, Boolean show) {
		ResultClass<Module> result = new ResultClass<Module>();
		Module p = daoModule.getModule(id_module);
		p.setTopics(serviceTopic.getTopicsForModule(id_module, show));
		result.setSingleElement(p);
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly=true)
	public ResultClass<Module> getModulesForDegree(Long id, Boolean show) {
		ResultClass<Module> result = new ResultClass<>();
		result.addAll(daoModule.getModulesForDegree(id, show));
		return result;
	}

//	@PreAuthorize("hasRole('ROLE_ADMIN')")
//	@Transactional(readOnly=false)
//	public ResultClass<Boolean> modifyModule(Module module) {
//		ResultClass<Boolean> result = new ResultClass<Boolean>();
//		result.setSingleElement(daoModule.saveModule(module));
//		return result;
//	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly=false)
	public ResultClass<Boolean> deleteModulesForDegree(Degree d) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		if(serviceTopic.deleteTopicsForModules(d.getModules()).getSingleElement()){
			result.setSingleElement(daoModule.deleteModulesForDegree(d));
		}
		else result.setSingleElement(false);	
		return result;
	}

//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PreAuthorize("hasPermission(#module, 'WRITE') or hasPermission(#module, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Module> unDeleteModule(Module module, Long id_degree) {
		Module m = daoModule.existByCode(module.getInfo().getCode(), id_degree);
		ResultClass<Module> result = new ResultClass<Module>();
		if(m == null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code doesn't exist");
			result.setErrorsList(errors);
			result.setSingleElement(module);
		}
		else{
			if(!m.getIsDeleted()){
				Collection<String> errors = new ArrayList<String>();
				errors.add("Code is not deleted");
				result.setErrorsList(errors);
				result.setSingleElement(module);
			}

			m.setDeleted(false);
			m.setInfo(module.getInfo());
			boolean r = daoModule.saveModule(m);
			if(r) 
				result.setSingleElement(m);	

		}
		return result;
	}
}
