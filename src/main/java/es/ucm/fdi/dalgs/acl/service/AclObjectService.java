package es.ucm.fdi.dalgs.acl.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import es.ucm.fdi.dalgs.domain.User;

@Service
public class AclObjectService {
	

	private SidRetrievalStrategy sidRetrievalStrategy = new SidRetrievalStrategyImpl();

	@Autowired
	private MutableAclService mutableAclService;

	public void setMutableAclService(MutableAclService mutableAclService) {
		this.mutableAclService = mutableAclService;
	}
	
	public boolean addAclToObject(Long id_object, String name_class){
		
		Authentication authentication = null;
		ObjectIdentity objectIdentity = new ObjectIdentityImpl(	name_class, id_object);
		 
		// Create or update the relevant ACL 
		MutableAcl acl = null; 
		try { 
			acl =(MutableAcl) mutableAclService.readAclById(objectIdentity); 
		} 
		catch (NotFoundException nfe) {
			acl = mutableAclService.createAcl(objectIdentity); 
		}
		
		try {
			authentication = SecurityContextHolder.getContext().getAuthentication();
			List<Sid> sids = sidRetrievalStrategy.getSids(authentication);
			acl = (MutableAcl) this.mutableAclService.readAclById(objectIdentity, sids);
		} catch (NotFoundException nfe) {
			acl = mutableAclService.createAcl(objectIdentity);
			return false;
		}
		
		User user = (User) authentication.getPrincipal();
		acl.insertAce(0, BasePermission.ADMINISTRATION, new PrincipalSid(user.getUsername()), true);
		acl.insertAce(1, BasePermission.DELETE, new GrantedAuthoritySid("ROLE_ADMIN"), true);

		
		//Acceso de lectura a todos los usuarios con ROLE_USER
		acl.insertAce(2, BasePermission.READ, new GrantedAuthoritySid("ROLE_USER"), true);
		
		return true;
	}


	public boolean removeAclFromObject(Long id_object, String name_class) {
		// Delete the ACL information as well
        ObjectIdentity oid = new ObjectIdentityImpl(name_class, id_object);
		mutableAclService.deleteAcl(oid, false);

		return true;
	}

	
}
