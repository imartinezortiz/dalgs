package es.ucm.fdi.dalgs.degree.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.dalgs.academicTerm.service.AcademicTermService;
import es.ucm.fdi.dalgs.acl.service.AclObjectService;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.competence.service.CompetenceService;
import es.ucm.fdi.dalgs.degree.repository.DegreeRepository;
import es.ucm.fdi.dalgs.domain.AcademicTerm;
import es.ucm.fdi.dalgs.domain.Degree;
import es.ucm.fdi.dalgs.domain.Subject;
import es.ucm.fdi.dalgs.module.service.ModuleService;

@Service
public class DegreeService {

	@Autowired
	private AclObjectService manageAclService;

	@Autowired
	private DegreeRepository daoDegree;

	@Autowired
	private ModuleService serviceModule;

	@Autowired
	private CompetenceService serviceCompetence;

	@Autowired
	private AcademicTermService serviceAcademicTerm;
	
	@Autowired
	private MessageSource messageSource;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Degree> addDegree(Degree degree, Locale locale) {

		boolean success = false;

		Degree degreeExists = daoDegree.existByCode(degree.getInfo().getCode());
		ResultClass<Degree> result = new ResultClass<Degree>();
		Collection<String> errors = new ArrayList<String>();
		if (degreeExists != null) {
			result.setHasErrors(true);

			errors.add(messageSource.getMessage("error.Code", null, locale));

			if (degreeExists.getIsDeleted()) {
				result.setElementDeleted(true);
				errors.add(messageSource.getMessage("error.deleted", null, locale));
				result.setSingleElement(degreeExists);
			}
			result.setErrorsList(errors);
		} else if (degree.getInfo().getCode() == null) {
			errors.add("Code necessary");

		} else {

			success = daoDegree.addDegree(degree);

			if (success) {
				degreeExists = daoDegree
						.existByCode(degree.getInfo().getCode());
				success = manageAclService.addACLToObject(degreeExists.getId(),
						degreeExists.getClass().getName());
				if (success)
					result.setSingleElement(degreeExists);

			} else {
				errors.add("Cannot create ACL. Object not set");
			}

		}

		return result;

	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<Degree> getDegrees(Integer pageIndex, Boolean showAll) {
		ResultClass<Degree> result = new ResultClass<>();
		result.addAll(daoDegree.getDegrees(pageIndex, showAll));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<Degree> getAll() {
		ResultClass<Degree> result = new ResultClass<>();
		result.addAll(daoDegree.getAll());
		return result;

	}

	@PreAuthorize("hasPermission(#degree, 'WRITE') or hasPermission(#degree, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> modifyDegree(Degree degree, Long id_degree, Locale locale) {
		ResultClass<Boolean> result = new ResultClass<>();

		Degree modifydegree = daoDegree.getDegree(id_degree);

		Degree degreeExists = daoDegree.existByCode(degree.getInfo().getCode());

		if (!degree.getInfo().getCode()
				.equalsIgnoreCase(modifydegree.getInfo().getCode())
				&& degreeExists != null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<>();
			errors.add(messageSource.getMessage("error.newCode", null, locale));

			if (degreeExists.getIsDeleted()) {
				result.setElementDeleted(true);
				errors.add(messageSource.getMessage("error.deleted", null, locale));

			}
			result.setErrorsList(errors);
		} else {
			modifydegree.setInfo(degree.getInfo());
			boolean r = daoDegree.saveDegree(modifydegree);
			if (r)
				result.setSingleElement(true);
		}
		return result;

	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> deleteDegree(Degree degree) {
		ResultClass<Boolean> result = new ResultClass<>();
		boolean deleteModules = false;
		boolean deleteCompetences = false;
		boolean deleteAcademic = false;

		if (!degree.getModules().isEmpty())
			deleteModules = serviceModule.deleteModulesForDegree(degree)
					.getSingleElement();
		if (!degree.getCompetences().isEmpty())
			deleteCompetences = serviceCompetence.deleteCompetencesForDegree(
					degree).getSingleElement();
		ResultClass<AcademicTerm> academicList = serviceAcademicTerm
				.getAcademicTermsByDegree(degree);

		if (!academicList.isEmpty())
			deleteAcademic = serviceAcademicTerm.deleteAcademicTermCollection(
					academicList).getSingleElement();
		if ((deleteModules || degree.getModules().isEmpty())
				&& (deleteCompetences || degree.getCompetences().isEmpty())
				&& (deleteAcademic || academicList.isEmpty())) {
			result.setSingleElement(daoDegree.deleteDegree(degree));
			return result;
		} else {
			result.setSingleElement(false);
			return result;
		}
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<Degree> getDegreeSubject(Subject p) {
		ResultClass<Degree> result = new ResultClass<>();
		result.setSingleElement(daoDegree.getDegreeSubject(p));
		return result;
	}


	
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResultClass<Degree> getDegree(Long id) {
		ResultClass<Degree> result = new ResultClass<>();
		result.setSingleElement(daoDegree.getDegree(id));
		return result;

	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public ResultClass<Degree> getDegreeAll(Long id, Boolean show) {
		ResultClass<Degree> result = new ResultClass<Degree>();

		Degree d = daoDegree.getDegree(id);
		d.setModules(serviceModule.getModulesForDegree(id, show));
		d.setCompetences(serviceCompetence.getCompetencesForDegree(id, show));
		result.setSingleElement(d);
		return result;
	}

	@PreAuthorize("hasPermission(#degree, 'WRITE') or hasPermission(#degree, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Degree> unDeleteDegree(Degree degree, Locale locale) {
		Degree d = daoDegree.existByCode(degree.getInfo().getCode());
		ResultClass<Degree> result = new ResultClass<Degree>();
		if (d == null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add(messageSource.getMessage("error.ElementNoExists", null, locale));
			result.setErrorsList(errors);

		} else {
			if (!d.getIsDeleted()) {
				Collection<String> errors = new ArrayList<String>();
				errors.add(messageSource.getMessage("error.CodeNoDeleted", null, locale));
				result.setErrorsList(errors);
			}

			d.setDeleted(false);
			d.setInfo(degree.getInfo());
			boolean r = daoDegree.saveDegree(d);
			if (r)
				result.setSingleElement(d);

		}
		return result;
	}

	@Transactional(readOnly = true)
	public ResultClass<Integer> numberOfPages(Boolean showAll) {
		ResultClass<Integer> result = new ResultClass<>();
		result.setSingleElement(daoDegree.numberOfPages(showAll));
		return result;
	}

}
