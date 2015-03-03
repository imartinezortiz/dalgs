package com.example.tfg.classes;

import java.util.List;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.example.tfg.domain.AcademicTerm;
import com.example.tfg.domain.User;
import com.example.tfg.service.implementation.AcademicTermServiceImp;

public class ManageAcl {
	private MutableAcl acl = null;
	
	private static final org.slf4j.Logger logger = LoggerFactory
			.getLogger(ManageAcl.class);
	private SidRetrievalStrategy sidRetrievalStrategy = new SidRetrievalStrategyImpl();

	@Autowired
	private MutableAclService mutableAclService;
	
	public void setMutableAclService(MutableAclService mutableAclService) {
		this.mutableAclService = mutableAclService;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	private MutableAcl createACLToEntity(Integer id, Object obj){
		Authentication authentication = null;
		
		ObjectIdentity objectIdentity = null;
		if(obj instanceof AcademicTerm)
			objectIdentity = new ObjectIdentityImpl(AcademicTerm.class, id);
		else logger.info("NO implementado");
			
		MutableAcl acl = null;
		try {
			acl = mutableAclService.createAcl(objectIdentity);

			authentication = SecurityContextHolder.getContext().getAuthentication();
			List<Sid> sids = sidRetrievalStrategy.getSids(authentication);
			acl = (MutableAcl) this.mutableAclService.readAclById(objectIdentity, sids);
			return acl;
			
		} catch (NotFoundException nfe) {
			acl = mutableAclService.createAcl(objectIdentity);
			acl = mutableAclService.updateAcl(acl);
			return null;
		}
	
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")

	private boolean addAuthoritesToEntity(MutableAcl acl){
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		acl.insertAce(0, BasePermission.ADMINISTRATION, new PrincipalSid(user.getUsername()), true);
		acl.insertAce(1, BasePermission.DELETE, new GrantedAuthoritySid("ROLE_ADMIN"), true);
		//Acceso de lectura a todos los usuarios con ROLE_USER
		acl.insertAce(2, BasePermission.READ, new GrantedAuthoritySid("ROLE_USER"), true);
		return (mutableAclService.updateAcl(acl) != null);
	}
}
