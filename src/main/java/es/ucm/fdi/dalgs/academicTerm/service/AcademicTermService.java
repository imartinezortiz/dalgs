package es.ucm.fdi.dalgs.academicTerm.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.dalgs.academicTerm.repository.AcademicTermRepository;
import es.ucm.fdi.dalgs.acl.service.AclObjectService;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.course.service.CourseService;
import es.ucm.fdi.dalgs.domain.AcademicTerm;

@Service
public class AcademicTermService {

	@Autowired
	private MutableAclService mutableAclService;

	@Autowired
	private AclObjectService manageAclService;

	@Autowired
	private AcademicTermRepository daoAcademicTerm;

	@Autowired
	private CourseService serviceCourse;

	public void setMutableAclService(MutableAclService mutableAclService) {
		this.mutableAclService = mutableAclService;
	}


	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> addAcademicTerm(AcademicTerm academicTerm) {

//		boolean success  =false;

		
		
		AcademicTerm academicExists = daoAcademicTerm.exists(academicTerm.getTerm(), academicTerm.getDegree());
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		if( academicExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code already exists");

			if (academicExists.getIsDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");
				result.setE(false);
			}
			result.setErrorsList(errors);
		}
		else{
			boolean r = daoAcademicTerm.addAcademicTerm(academicTerm);
			if (r) 
				result.setE(true);
		}
		
		
		
		
//		
//		AcademicTerm academicAux = daoAcademicTerm.exists(academicTerm.getTerm(), academicTerm.getDegree());
//		if (academicAux == null){
//			success = daoAcademicTerm.addAcademicTerm(academicTerm);			
//		}
//		else if(academicAux.getIsDeleted()){
//			academicAux.setDeleted(false);
//			success =  daoAcademicTerm.saveAcademicTerm(academicAux);			
//		}
//		academicAux =  daoAcademicTerm.exists(academicTerm.getTerm(), academicTerm.getDegree());


		if(result.getE()){ //&& academicAux != null){
			result.setE(manageAclService.addAclToObject(academicTerm.getId(), academicTerm.getClass().getName()));
		} else {
			throw new IllegalArgumentException(	"Cannot create ACL. Object not set.");
		}
		return result;
	}

	@PreAuthorize("hasPermission(#academicTerm, 'WRITE') or hasPermission(#academicTerm, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public boolean modifyAcademicTerm(AcademicTerm academicTerm) {
		return daoAcademicTerm.saveAcademicTerm(academicTerm);
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
	public ResultClass<List<AcademicTerm>> getAcademicsTerm(Integer pageIndex, Boolean showAll) {
		ResultClass<List<AcademicTerm>> result = new ResultClass<List<AcademicTerm>>();
		List<AcademicTerm> a = daoAcademicTerm.getAcademicsTerm(pageIndex, showAll);
		result.setE(a);
		return result;
	}

	@PreAuthorize("hasPermission(#academicTerm, 'DELETE') or hasPermission(#academicTerm, 'ADMINISTRATION')" )
	@Transactional(readOnly = false)// propagation = Propagation.REQUIRED)
	public ResultClass<Boolean> deleteAcademicTerm(AcademicTerm academicTerm) {
		boolean success = false;
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		if (academicTerm.getCourses() == null || serviceCourse.deleteCoursesFromAcademic(academicTerm)){

			/* COMENTADO PARA LA PAPELERA DE LA VISTA
			 success = ( manageAclService.removeAclFromObject(academicTerm.getId(), academicTerm.getClass().getName()) &&
						daoAcademicTerm.deleteAcademicTerm(academicTerm.getId()) );
			 */
			success = 	daoAcademicTerm.deleteAcademicTerm(academicTerm.getId());
		}
		result.setE(success);
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = false)
	public ResultClass<Integer> numberOfPages(Boolean showAll) {
		ResultClass<Integer> result = new ResultClass<Integer>();
		result.setE(daoAcademicTerm.numberOfPages(showAll));
		return result;
	}


	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ')") // Collection: filterObject
	@Transactional(readOnly = false)
	public ResultClass<List<AcademicTerm>> getAcademicTermsByDegree(Long id_degree) {
		ResultClass<List<AcademicTerm>> result = new ResultClass<List<AcademicTerm>>();
		result.setE(daoAcademicTerm.getAcademicTermsByDegree(id_degree));
		return result;
	}


	// WORKING 
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostAuthorize("hasPermission(returnObject, 'READ')")
	@Transactional(readOnly = true)
	public ResultClass<AcademicTerm> getAcademicTerm(Long id_academic) {
		ResultClass<AcademicTerm> result = new ResultClass<AcademicTerm>();
		AcademicTerm aT= daoAcademicTerm.getAcademicTermById(id_academic);
		aT.setCourses(serviceCourse.getCoursesByAcademicTerm(id_academic).getE());
		result.setE(aT);
		return result;
	}


	@PreAuthorize("hasPermission(returnObject, 'DELETE') or hasPermission(returnObject, 'ADMINISTRATION')" )
	@Transactional(readOnly = false)
	public ResultClass<Boolean> deleteAcademicTermCollection(Collection<AcademicTerm> academicList) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		boolean deleteCourses = serviceCourse.deleteCourses(academicList);
		if (deleteCourses)
			result.setE(daoAcademicTerm.deleteAcademicTerm(academicList));
		else result.setE(false);

		return result;
	}
}
