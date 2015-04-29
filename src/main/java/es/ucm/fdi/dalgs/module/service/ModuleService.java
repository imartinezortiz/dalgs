/**
 * This file is part of D.A.L.G.S.
 *
 * D.A.L.G.S is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * D.A.L.G.S is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with D.A.L.G.S.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.dalgs.module.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.prefs.CsvPreference;

import es.ucm.fdi.dalgs.acl.service.AclObjectService;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.classes.UploadForm;
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

	@Autowired
	private MessageSource messageSource;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Module> addModule(Module module, Long id_degree,
			Locale locale) {

		boolean success = false;

		Module moduleExists = daoModule.existByCode(module.getInfo().getCode(),
				id_degree);
		ResultClass<Module> result = new ResultClass<Module>();

		if (moduleExists != null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add(messageSource.getMessage("error.Code", null, locale));

			if (moduleExists.getIsDeleted()) {
				result.setElementDeleted(true);
				errors.add(messageSource.getMessage("error.deleted", null,
						locale));
				result.setSingleElement(moduleExists);
			} else
				result.setSingleElement(module);
			result.setErrorsList(errors);
		} else {
			module.setDegree(serviceDegree.getDegree(id_degree)
					.getSingleElement());
			success = daoModule.addModule(module);

			if (success) {
				moduleExists = daoModule.existByCode(
						module.getInfo().getCode(), id_degree);
				success = manageAclService.addACLToObject(moduleExists.getId(),
						moduleExists.getClass().getName());
				if (success)
					result.setSingleElement(module);

			} else {
				throw new IllegalArgumentException(
						"Cannot create ACL. Object not set.");

			}
		}

		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<Module> getAll() {
		ResultClass<Module> result = new ResultClass<Module>();
		result.addAll(daoModule.getAll());
		return result;
	}

	@PreAuthorize("hasPermission(#module, 'WRITE') or hasPermission(#module, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> modifyModule(Module module, Long id_module,
			Long id_degree, Locale locale) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		Module modifyModule = daoModule.getModule(id_module, id_degree);

		Module moduleExists = daoModule.existByCode(module.getInfo().getCode(),
				id_degree);

		if (!module.getInfo().getCode()
				.equalsIgnoreCase(modifyModule.getInfo().getCode())
				&& moduleExists != null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add(messageSource.getMessage("error.newCode", null, locale));

			if (moduleExists.getIsDeleted()) {
				result.setElementDeleted(true);
				errors.add(messageSource.getMessage("error.deleted", null,
						locale));

			}
			result.setErrorsList(errors);
			result.setSingleElement(false);
		} else {
			modifyModule.setInfo(module.getInfo());
			boolean r = daoModule.saveModule(modifyModule);
			if (r)
				result.setSingleElement(true);
		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<Module> getModule(Long id, Long id_degree) {
		ResultClass<Module> result = new ResultClass<Module>();
		result.setSingleElement(daoModule.getModule(id, id_degree));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> deleteModule(Module module) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		if (serviceTopic.deleteTopicsForModule(module).getSingleElement()) {
			result.setSingleElement(daoModule.deleteModule(module));
			return result;
		}
		result.setSingleElement(false);
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<Module> getModuleAll(Long id_module, Long id_degree,
			Boolean show) {
		ResultClass<Module> result = new ResultClass<Module>();
		Module p = daoModule.getModule(id_module, id_degree);
		if (p != null) {
			p.setTopics(serviceTopic.getTopicsForModule(id_module, show));
			result.setSingleElement(p);
		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<Module> getModulesForDegree(Long id, Boolean show) {
		ResultClass<Module> result = new ResultClass<>();
		result.addAll(daoModule.getModulesForDegree(id, show));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> deleteModulesForDegree(Degree d) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		if (serviceTopic.deleteTopicsForModules(d.getModules())
				.getSingleElement()) {
			result.setSingleElement(daoModule.deleteModulesForDegree(d));
		} else
			result.setSingleElement(false);
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Module> unDeleteModule(Module module, Long id_degree,
			Locale locale) {
		Module m = daoModule.existByCode(module.getInfo().getCode(), id_degree);
		ResultClass<Module> result = new ResultClass<Module>();
		if (m == null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add(messageSource.getMessage("error.ElementNoExists", null,
					locale));
			result.setErrorsList(errors);
			result.setSingleElement(module);
		} else {
			if (!m.getIsDeleted()) {
				Collection<String> errors = new ArrayList<String>();
				errors.add(messageSource.getMessage("error.CodeNoDeleted",
						null, locale));
				result.setErrorsList(errors);
				result.setSingleElement(module);
			}

			m.setDeleted(false);
			m.setInfo(module.getInfo());
			boolean r = daoModule.saveModule(m);
			if (r)
				result.setSingleElement(m);

		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public boolean uploadCSV(UploadForm upload, Long id_degree) {
		boolean success = false;
		CsvPreference prefers = new CsvPreference.Builder(upload.getQuoteChar()
				.charAt(0), upload.getDelimiterChar().charAt(0),
				upload.getEndOfLineSymbols()).build();

		List<Module> list = null;
		try {
			FileItem fileItem = upload.getFileData().getFileItem();
			ModuleCSV moduleUpload = new ModuleCSV();

			Degree d = serviceDegree.getDegree(id_degree).getSingleElement();
			list = moduleUpload.readCSVModuleToBean(fileItem.getInputStream(),
					upload.getCharset(), prefers, d);

			success = daoModule.persistListModules(list);
			if (success) {
				for (Module c : list) {
					Module aux = daoModule.existByCode(c.getInfo().getCode(),
							id_degree);
					success = success
							&& manageAclService.addACLToObject(aux.getId(), aux
									.getClass().getName());

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return success;
	}

	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public void downloadCSV(HttpServletResponse response) throws IOException {

        Collection<Module> modules = new ArrayList<Module>();
        modules =  daoModule.getAll();
        
        if(!modules.isEmpty()){
        	ModuleCSV moduleCSV = new ModuleCSV();
        	moduleCSV.downloadCSV(response, modules);
        }
      
	}
}
