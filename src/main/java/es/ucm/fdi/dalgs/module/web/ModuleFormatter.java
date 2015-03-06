package es.ucm.fdi.dalgs.module.web;

import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Component;

import es.ucm.fdi.dalgs.domain.Module;
import es.ucm.fdi.dalgs.module.repository.ModuleRepository;

@Component
public class ModuleFormatter implements Formatter<Module>{


	@Autowired
	private ModuleRepository moduleDao;

	// Some service class which can give the Actor after
	// fetching from Database

	public String print(Module module, Locale arg1) {
		return module.getInfo().getName();
	}

	public Module parse(String moduleId, Locale arg1) throws ParseException {

		return moduleDao.getModule(Long.parseLong(moduleId));
		// Else you can just return a new object by setting some values
		// which you deem fit.
	}
	
}
