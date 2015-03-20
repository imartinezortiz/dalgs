package es.ucm.fdi.dalgs.acl.service;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.GrantedAuthoritySid;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.domain.SidRetrievalStrategyImpl;
import org.springframework.security.acls.model.AccessControlEntry;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.NotFoundException;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Permission;
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

	public boolean addAclToObject(Long id_object, String name_class) {

		Authentication authentication = null;
		ObjectIdentity objectIdentity = new ObjectIdentityImpl(name_class,
				id_object);

		// Create or update the relevant ACL
		MutableAcl acl = null;
		try {
			acl = (MutableAcl) mutableAclService.readAclById(objectIdentity);
		} catch (NotFoundException nfe) {
			acl = mutableAclService.createAcl(objectIdentity);
		}

		try {
			authentication = SecurityContextHolder.getContext()
					.getAuthentication();
			List<Sid> sids = sidRetrievalStrategy.getSids(authentication);
			acl = (MutableAcl) this.mutableAclService.readAclById(
					objectIdentity, sids);
		} catch (NotFoundException nfe) {
			acl = mutableAclService.createAcl(objectIdentity);
			return false;
		}

		User user = (User) authentication.getPrincipal();
		acl.insertAce(0, BasePermission.ADMINISTRATION,
				new PrincipalSid(user.getUsername()), true);
		acl.insertAce(1, BasePermission.DELETE, new GrantedAuthoritySid(
				"ROLE_ADMIN"), true);

		// Acceso de lectura a todos los usuarios con ROLE_USER
		acl.insertAce(2, BasePermission.READ, new GrantedAuthoritySid(
				"ROLE_USER"), true);

		return true;
	}

	public boolean removeAclFromObject(Long id_object, String name_class) {
		// Delete the ACL information as well
		ObjectIdentity oid = new ObjectIdentityImpl(name_class, id_object);
		mutableAclService.deleteAcl(oid, false);

		return true;
	}

	// Authorize professors to manage his course
	public void addPermissionToAnObjectCollection(Collection<User> professors,
			Long id_object, String name_class) {

		// Create or update the relevant ACL
		MutableAcl acl = null;
		// Prepare the information we'd like in our access control entry (ACE)
		ObjectIdentity oi = new ObjectIdentityImpl(name_class, id_object);

		Sid sid = null;
		for (User u : professors) {
			sid = new PrincipalSid(u.getUsername());
			Permission p = BasePermission.ADMINISTRATION;

			try {
				acl = (MutableAcl) mutableAclService.readAclById(oi);
			} catch (NotFoundException nfe) {
				acl = mutableAclService.createAcl(oi);
			}

			// Now grant some permissions via an access control entry (ACE)
			acl.insertAce(acl.getEntries().size(), p, sid, true);
			mutableAclService.updateAcl(acl);

		}

	}

	// Remove ACL Permissions
	public void removePermissionToAnObject(User user, Long id_object,
			String name_class) {

		// Create or update the relevant ACL
		MutableAcl acl = null;
		// Prepare the information we'd like in our access control entry (ACE)
		ObjectIdentity oi = new ObjectIdentityImpl(name_class, id_object);

		Sid sid = null;

		sid = new PrincipalSid(user.getUsername());
		Permission p = BasePermission.ADMINISTRATION;

		try {
			acl = (MutableAcl) mutableAclService.readAclById(oi);
		} catch (NotFoundException nfe) {
			acl = mutableAclService.createAcl(oi);
		}

		Integer aceIndex = 0;
		for (AccessControlEntry ace : acl.getEntries()) {
			if ((ace.getSid().equals(sid))
					&& (ace.getPermission().equals(p))) { 
				acl.deleteAce(aceIndex);
				break;
			}
			else aceIndex++;
		}

		// acl.insertAce(acl.getEntries().size(), p, sid, true);

		// Now grant some permissions via an access control entry (ACE)
		if(acl !=null)mutableAclService.updateAcl(acl);
	}

	// Remove ACL Permissions
	public void removePermissionToAnObjectCollection(Collection<User> users,
			Long id_object, String name_class) {

		// Create or update the relevant ACL
		MutableAcl acl = null;
		// Prepare the information we'd like in our access control entry (ACE)
		ObjectIdentity oi = new ObjectIdentityImpl(name_class, id_object);

		Sid sid = null;

		for (User u : users) {
			sid = new PrincipalSid(u.getUsername());
			Permission p = BasePermission.ADMINISTRATION;

			try {
				acl = (MutableAcl) mutableAclService.readAclById(oi);
			} catch (NotFoundException nfe) {
				acl = mutableAclService.createAcl(oi);
			}

	        int aceIndex = 0;
			for (AccessControlEntry ace : acl.getEntries()) {
				if ((ace.getSid().equals(sid))
						&& (ace.getPermission().equals(p))) {
					acl.deleteAce(aceIndex);
					break;
				}
				else  aceIndex++;
			}
		}

		// acl.insertAce(acl.getEntries().size(), p, sid, true);

		// Now grant some permissions via an access control entry (ACE)
		if(acl !=null)	mutableAclService.updateAcl(acl);

	}

	// Authorize professors to manage his course
	public void addPermissionToAnObjectCoordinator(User coordinator,
			Long id_object, String name_class) {

		// Create or update the relevant ACL
		MutableAcl acl = null;
		// Prepare the information we'd like in our access control entry (ACE)
		ObjectIdentity oi = new ObjectIdentityImpl(name_class, id_object);

		Sid sid = null;

		sid = new PrincipalSid(coordinator.getUsername());
		Permission p = BasePermission.ADMINISTRATION;

		try {
			acl = (MutableAcl) mutableAclService.readAclById(oi);
		} catch (NotFoundException nfe) {
			acl = mutableAclService.createAcl(oi);
		}

		// Now grant some permissions via an access control entry (ACE)
		acl.insertAce(acl.getEntries().size(), p, sid, true);
		mutableAclService.updateAcl(acl);

	}

}
