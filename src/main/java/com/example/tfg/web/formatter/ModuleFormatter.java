package com.example.tfg.web.formatter;

import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Component;

import com.example.tfg.domain.Module;
import com.example.tfg.repository.ModuleDao;

@Component
public class ModuleFormatter implements Formatter<Module>{


	@Autowired
	private ModuleDao moduleDao;

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
