package es.ucm.fdi.dalgs.academicTerm.service;

import java.util.ArrayList;
import java.util.Collection;

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
import es.ucm.fdi.dalgs.domain.Course;

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
			
			

		

		if(success){ //&& academicAux != null){
			academicExists = daoAcademicTerm.exists(academicTerm.getTerm(), academicTerm.getDegree());
			success = manageAclService.addAclToObject(academicExists.getId(), academicExists.getClass().getName());
			if (success) result.setSingleElement(academicTerm);
		} else {
			throw new IllegalArgumentException(	"Cannot create ACL. Object not set.");
		}
		
		}
		return result;
	
	}


	
	@PreAuthorize("hasPermission(#academicTerm, 'WRITE') or hasPermission(#academicTerm, 'ADMINISTRATION')")
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
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ')")
	@Transactional(readOnly = true)
	public ResultClass<AcademicTerm> getAcademicsTerm(Integer pageIndex, Boolean showAll) {
		ResultClass<AcademicTerm> result = new ResultClass<>();
		result.addAll(daoAcademicTerm.getAcademicsTerm(pageIndex, showAll));

		return result;
	}

//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PreAuthorize("hasPermission(#academicTerm, 'DELETE') or hasPermission(#academicTerm, 'ADMINISTRATION')" )
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

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = false)
	public ResultClass<Integer> numberOfPages(Boolean showAll) {
		ResultClass<Integer> result = new ResultClass<Integer>();
		result.setSingleElement(daoAcademicTerm.numberOfPages(showAll));
		return result;
	}


	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ')") // Collection: filterObject
	@Transactional(readOnly = false)
	public ResultClass<AcademicTerm> getAcademicTermsByDegree(Long id_degree) {
		ResultClass<AcademicTerm> result = new ResultClass<>();
		result.addAll(daoAcademicTerm.getAcademicTermsByDegree(id_degree));
		return result;
	}


	// WORKING 
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ')")
	@Transactional(readOnly = true)
	public ResultClass<AcademicTerm> getAcademicTerm(Long id_academic) {
		ResultClass<AcademicTerm> result = new ResultClass<AcademicTerm>();
		AcademicTerm aT= daoAcademicTerm.getAcademicTermById(id_academic);
	
		aT.setCourses(serviceCourse.getCoursesByAcademicTerm(id_academic));
		result.setSingleElement(aT);
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	//	@PreAuthorize("hasPermission(returnObject, 'DELETE') or hasPermission(returnObject, 'ADMINISTRATION')" )
	@Transactional(readOnly = false)
	public ResultClass<Boolean> deleteAcademicTermCollection(Collection<AcademicTerm> academicList) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		boolean deleteCourses = serviceCourse.deleteCourses(academicList).getSingleElement();
		if (deleteCourses)
			result.setSingleElement(daoAcademicTerm.deleteAcademicTerm(academicList));
		else result.setSingleElement(false);

		return result;
	}

	
	@PreAuthorize("hasPermission(#academicTerm, 'WRITE') or hasPermission(#academicTerm, 'ADMINISTRATION')")	@Transactional(readOnly = false)
	public ResultClass<AcademicTerm> undeleteAcademic(AcademicTerm academicTerm) {
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
	
//	@PreAuthorize("hasRole('ROLE_USER')")
//	//@PreAuthorize("hasPermission(returnObject, 'DELETE') or hasPermission(returnObject, 'ADMINISTRATION')" )
//	@Transactional(propagation=Propagation.REQUIRED)
//	public boolean cloneAcademicTerm(AcademicTerm academic) {
//
//		AcademicTerm cloneAcademic = academic.clone();
//		if (cloneAcademic!=null)
//			return this.addAcademicTerm(cloneAcademic);
//
//		else return false;
//	}

}
