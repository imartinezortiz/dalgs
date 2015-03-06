package es.ucm.fdi.dalgs.academicTerm.service;

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
	public boolean addAcademicTerm(AcademicTerm academicTerm) {

		boolean success  =false;
		
		AcademicTerm academicAux = daoAcademicTerm.exists(academicTerm.getTerm(), academicTerm.getDegree());
		if (academicAux == null){
			success = daoAcademicTerm.addAcademicTerm(academicTerm);			
		}
		else if(academicAux.isDeleted()){
			academicAux.setDeleted(false);
			success =  daoAcademicTerm.saveAcademicTerm(academicAux);			
		}
		academicAux =  daoAcademicTerm.exists(academicTerm.getTerm(), academicTerm.getDegree());

		
		if(success && academicAux != null){
			success = manageAclService.addAclToObject(academicAux.getId(), academicAux.getClass().getName());
		} else {
			throw new IllegalArgumentException(	"Cannot create ACL. Object not set.");
		}
		return success;
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
	public List<AcademicTerm> getAcademicsTerm(Integer pageIndex) {
		List<AcademicTerm> a = daoAcademicTerm.getAcademicsTerm(pageIndex);
		return a;
	}

	@PreAuthorize("hasPermission(#academicTerm, 'DELETE') or hasPermission(#academicTerm, 'ADMINISTRATION')" )
	@Transactional(readOnly = false)// propagation = Propagation.REQUIRED)
	public boolean deleteAcademicTerm(AcademicTerm academicTerm) {
		boolean success = false;

		if (academicTerm.getCourses() == null || serviceCourse.deleteCoursesFromAcademic(academicTerm)){
			success = ( manageAclService.removeAclFromObject(academicTerm.getId(), academicTerm.getClass().getName()) &&
						daoAcademicTerm.deleteAcademicTerm(academicTerm.getId()) );
		}
		return success;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = false)
	public Integer numberOfPages() {
		return daoAcademicTerm.numberOfPages();
	}

 
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostFilter("hasPermission(filterObject, 'READ')") // Collection: filterObject
	@Transactional(readOnly = false)
	public List<AcademicTerm> getAcademicTermsByDegree(Long id_degree) {
		return daoAcademicTerm.getAcademicTermsByDegree(id_degree);
	}


	// WORKING 
	@PreAuthorize("hasRole('ROLE_USER')")
	@PostAuthorize("hasPermission(returnObject, 'READ')")
	@Transactional(readOnly = true)
	public AcademicTerm getAcademicTerm(Long id_academic) {
		AcademicTerm aT= daoAcademicTerm.getAcademicTermById(id_academic);
		aT.setCourses(serviceCourse.getCoursesByAcademicTerm(id_academic));
		return aT;
	}
	

	@PreAuthorize("hasPermission(returnObject, 'DELETE') or hasPermission(returnObject, 'ADMINISTRATION')" )
	@Transactional(readOnly = false)
	public boolean deleteAcademicTermCollection(Collection<AcademicTerm> academicList) {
		
		boolean deleteCourses = serviceCourse.deleteCourses(academicList);
		if (deleteCourses)
		return daoAcademicTerm.deleteAcademicTerm(academicList);
		else return false;
	}
}
