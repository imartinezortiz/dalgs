package com.example.tfg.service.implementation;

import java.util.Collection;
import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.domain.SidRetrievalStrategyImpl;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.acls.model.SidRetrievalStrategy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tfg.domain.AcademicTerm;
import com.example.tfg.domain.User;
import com.example.tfg.repository.AcademicTermDao;
import com.example.tfg.service.AcademicTermService;
import com.example.tfg.service.CourseService;

@Service
public class AcademicTermServiceImp implements AcademicTermService {

	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(AcademicTermServiceImp.class);
	
	private SidRetrievalStrategy sidRetrievalStrategy = new SidRetrievalStrategyImpl();

	@Autowired
	private MutableAclService mutableAclService;

//	private Map<Integer, AcademicTerm> academicStore = new HashMap<Integer, AcademicTerm>();
	
	@Autowired
	private AcademicTermDao daoAcademicTerm;

	@Autowired
	private CourseService serviceCourse;
	
	public void setMutableAclService(MutableAclService mutableAclService) {
		this.mutableAclService = mutableAclService;
	}


	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public boolean addAcademicTerm(AcademicTerm academicTerm) {

		boolean success  =false;
		Authentication authentication = null;
		Integer id = new Integer(Math.abs(academicTerm.hashCode()));
		ObjectIdentity objectIdentity = new ObjectIdentityImpl(
				AcademicTerm.class, id);
		MutableAcl acl = mutableAclService.createAcl(objectIdentity);
		try {
			authentication = SecurityContextHolder.getContext().getAuthentication();
			List<Sid> sids = sidRetrievalStrategy.getSids(authentication);
			acl = (MutableAcl) this.mutableAclService.readAclById(objectIdentity, sids);
		} catch (NotFoundException nfe) {
			acl = mutableAclService.createAcl(objectIdentity);
		}
		
		
		AcademicTerm existAcademic = daoAcademicTerm.exists(academicTerm.getTerm(), academicTerm.getDegree());
		if (existAcademic == null){
			academicTerm.setId(id.longValue());
			success= daoAcademicTerm.addAcademicTerm(academicTerm);			
		}
		else if(existAcademic.isDeleted()){
			existAcademic.setDeleted(false);
			success =  daoAcademicTerm.saveAcademicTerm(existAcademic);			
		}
		if(success){
			User user =(User) authentication.getPrincipal();
			acl.insertAce(0, BasePermission.ADMINISTRATION, new PrincipalSid(user.getUsername()), true);
			acl.insertAce(1, BasePermission.DELETE, new GrantedAuthoritySid("ROLE_ADMIN"), true);

			
			//Acceso de lectura a todos los usuarios con ROLE_USER
			acl.insertAce(2, BasePermission.READ, new GrantedAuthoritySid("ROLE_USER"), true);
			
			success = (mutableAclService.updateAcl(acl) != null);
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

	//@PreAuthorize("hasRole('ROLE_ADMIN')")
	//@PreAuthorize("hasPermission(#academicTerm, 'WRITE') or hasPermission(#academicTerm, admin)" )

	@PreAuthorize("hasPermission(#academicTerm, 'DELETE') or hasPermission(#academicTerm, 'ADMINISTRATION')" )
	@Transactional(readOnly = false)// propagation = Propagation.REQUIRED)
	public boolean deleteAcademicTerm(AcademicTerm academicTerm) {
		boolean success = false;
		//AcademicTerm academic = daoAcademicTerm.getAcademicTermById(id_academic);
		if (academicTerm.getCourses()==null	|| serviceCourse.deleteCoursesFromAcademic(academicTerm)){
			success =  daoAcademicTerm.deleteAcademicTerm(academicTerm.getId());

			// Delete the ACL information as well
	        ObjectIdentity oid = new ObjectIdentityImpl(AcademicTerm.class, academicTerm.getId());
			mutableAclService.deleteAcl(oid, false);
			if (logger.isDebugEnabled()) {
	            logger.debug("Deleted academicTerm " + academicTerm.getId() + " including ACL permissions");
	        }
			
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
