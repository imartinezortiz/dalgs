package es.ucm.fdi.dalgs.academicTerm.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.dalgs.academicTerm.repository.AcademicTermRepository;
import es.ucm.fdi.dalgs.acl.service.AclObjectService;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.course.service.CourseService;
import es.ucm.fdi.dalgs.domain.AcademicTerm;
import es.ucm.fdi.dalgs.domain.Activity;
import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.domain.Degree;
import es.ucm.fdi.dalgs.domain.Group;

@Service
public class AcademicTermService {

	@Autowired
	private AclObjectService manageAclService;

	@Autowired
	private AcademicTermRepository daoAcademicTerm;

	@Autowired
	private CourseService serviceCourse;
	

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<AcademicTerm> addAcademicTerm(AcademicTerm academicTerm) {

		boolean success  =false;

		AcademicTerm academicExists = daoAcademicTerm.exists(academicTerm.getTerm(), academicTerm.getDegree());
		ResultClass<AcademicTerm> result = new ResultClass<>();

		if( academicExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code already exists");

			if (academicExists.getIsDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");
				result.setSingleElement(academicExists);
				
			}
			else result.setSingleElement(academicTerm);
			result.setErrorsList(errors);

		}
		else{
			success = daoAcademicTerm.addAcademicTerm(academicTerm);


		if(success){ 
			academicExists = daoAcademicTerm.exists(academicTerm.getTerm(), academicTerm.getDegree());
			success = manageAclService.addACLToObject(academicExists.getId(), academicExists.getClass().getName());
			if (success) result.setSingleElement(academicTerm);
			
		} 
		else {
			throw new IllegalArgumentException(	"Cannot create ACL. Object not set.");
		}
		
		}
		return result;
	
	}


	
	@PreAuthorize("hasPermission(#academicTerm, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> modifyAcademicTerm(AcademicTerm academicTerm, Long id_academic) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		AcademicTerm modifyAcademic = daoAcademicTerm.getAcademicTermById(id_academic);


		AcademicTerm academicExists = daoAcademicTerm.exists(academicTerm.getTerm(), modifyAcademic.getDegree());

		if(!academicTerm.getTerm().equalsIgnoreCase(modifyAcademic.getTerm())  && 
				academicExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("New code already exists");

			if (academicExists.getIsDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");

			}
			result.setErrorsList(errors);
			result.setSingleElement(false);
		}
		else{
			modifyAcademic.setTerm(academicTerm.getTerm());
			boolean r = daoAcademicTerm.saveAcademicTerm(modifyAcademic);
			if (r) 
				result.setSingleElement(true);
		}
		return result;


	}

	/**
	 *  Retrieves all academic terms.
	 *  <p>
	 *  Access-control will be evaluated after this method is invoked.
	 *  filterObject refers to the returned object list.
	 */
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	@Transactional(readOnly = true)
	public ResultClass<AcademicTerm> getAcademicTerms(Integer pageIndex, Boolean showAll) {
		ResultClass<AcademicTerm> result = new ResultClass<>();
		result.addAll(daoAcademicTerm.getAcademicsTerm(pageIndex, showAll));

		return result;
	}

	@PreAuthorize("hasPermission(#academicTerm, 'ADMINISTRATION')" )
	@Transactional(readOnly = false)// propagation = Propagation.REQUIRED)
	public ResultClass<Boolean> deleteAcademicTerm(AcademicTerm academicTerm) {
		boolean success = false;
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		if (academicTerm.getCourses().isEmpty() || serviceCourse.deleteCoursesFromAcademic(academicTerm).getSingleElement()){

			/* COMENTADO PARA LA PAPELERA DE LA VISTA
			 success = ( manageAclService.removeAclFromObject(academicTerm.getId(), academicTerm.getClass().getName()) &&
						daoAcademicTerm.deleteAcademicTerm(academicTerm.getId()) );
			 */
			success = 	daoAcademicTerm.deleteAcademicTerm(academicTerm.getId());
		}
		result.setSingleElement(success);
		return result;
	}

	//TODO Contemplar el filtrado de objectos
	@Transactional(readOnly = false)
	public ResultClass<Integer> numberOfPages(Boolean showAll) {
		ResultClass<Integer> result = new ResultClass<Integer>();
		result.setSingleElement(daoAcademicTerm.numberOfPages(showAll));
		return result;
	}


	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<AcademicTerm> getAcademicTermsByDegree(Degree degree) {
		ResultClass<AcademicTerm> result = new ResultClass<>();
		result.addAll(daoAcademicTerm.getAcademicTermsByDegree(degree));
		return result;
	}


	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	@Transactional(readOnly = true)
	public ResultClass<AcademicTerm> getAcademicTerm(Long id_academic, Boolean showAll) {
		ResultClass<AcademicTerm> result = new ResultClass<AcademicTerm>();
		AcademicTerm aT= daoAcademicTerm.getAcademicTermById(id_academic);
		aT.setCourses(serviceCourse.getCoursesByAcademicTerm(id_academic, showAll));
		result.setSingleElement(aT);
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> deleteAcademicTermCollection(Collection<AcademicTerm> academicList) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		boolean deleteCourses = serviceCourse.deleteCourses(academicList).getSingleElement();
		if (deleteCourses)
			result.setSingleElement(daoAcademicTerm.deleteAcademicTerm(academicList));
		else result.setSingleElement(false);

		return result;
	}

	
	@PreAuthorize("hasPermission(#academicTerm, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<AcademicTerm> restoreAcademic(AcademicTerm academicTerm) {
		AcademicTerm a = daoAcademicTerm.exists(academicTerm.getTerm(), academicTerm.getDegree());
		ResultClass<AcademicTerm> result = new ResultClass<>();
		
		if(a == null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("element doesn't exist");
			result.setErrorsList(errors);
			
		}
		else{
			if(!a.getIsDeleted()){
				Collection<String> errors = new ArrayList<String>();
				errors.add("Code is not deleted");
				result.setErrorsList(errors);
			}

			a.setDeleted(false);
			a.setTerm(academicTerm.getTerm());
			boolean r = daoAcademicTerm.saveAcademicTerm(a);
			if (r)
				result.setSingleElement(a);	

		}
		return result;

	}


	@PreAuthorize("hasPermission(#academicTerm, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<AcademicTerm> copyAcademicTerm(AcademicTerm academicTerm) {
		AcademicTerm copy = academicTerm.copy();
		
		ResultClass<AcademicTerm> result = new ResultClass<>();
		
		if(copy == null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Copy doesn't work");
			result.setErrorsList(errors);
			
		}
		else{
	
			copy.setDeleted(false);
			copy.setTerm(academicTerm.getTerm() + " (copy)");
			
			//TODO Cambiar el resto de codes que tengan que ser unicos
			List<Activity> activities_aux = new ArrayList<Activity>();
			for(Course c: copy.getCourses()){
				for(Activity a: c.getActivities()){
					a.getInfo().setCode(a.getInfo().getCode()+ "(copy)");
					activities_aux.add(a);
				}
				c.setActivities(activities_aux);
				activities_aux.clear();
				
				for(Group g : c.getGroups()){
					for(Activity a: g.getActivities()){
						a.getInfo().setCode(a.getInfo().getCode()+ "(copy)");
						activities_aux.add(a);
					}
					g.setActivities(activities_aux);
					activities_aux.clear();
				}
			}
			boolean r = daoAcademicTerm.addAcademicTerm(copy);
			if (r){
				//result.setSingleElement(copy);	
				AcademicTerm academicExists = daoAcademicTerm.exists(academicTerm.getTerm(), academicTerm.getDegree());
				boolean	success = manageAclService.addACLToObject(academicExists.getId(), academicExists.getClass().getName());
				if (success) result.setSingleElement(academicTerm);
				
			} 
				else {
					throw new IllegalArgumentException(	"Cannot create ACL. Object not set.");
				}
			}
		
		return result;

	}

}
