/**
 * This file is part of D.A.L.G.S.
 *
 * D.A.L.G.S is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * D.A.L.G.S is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with D.A.L.G.S.  If not, see <http://www.gnu.org/licenses/>.
 */
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

import es.ucm.fdi.dalgs.activity.service.ActivityService;
import es.ucm.fdi.dalgs.domain.AcademicTerm;
import es.ucm.fdi.dalgs.domain.Activity;
import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.domain.Group;
import es.ucm.fdi.dalgs.domain.User;
import es.ucm.fdi.dalgs.group.service.GroupService;
import es.ucm.fdi.dalgs.user.service.UserService;

@Service
public class AclObjectService {

	private SidRetrievalStrategy sidRetrievalStrategy = new SidRetrievalStrategyImpl();

	@Autowired
	private MutableAclService mutableAclService;

	@Autowired
	private UserService userService;

	@Autowired
	private ActivityService activityService;

	@Autowired
	private GroupService groupService;

	public void setMutableAclService(MutableAclService mutableAclService) {
		this.mutableAclService = mutableAclService;
	}

	public boolean addACLToObject(Long id_object, String name_class) {

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

		
		
		if(authentication.getPrincipal() !="anonymousUser"){
			User user = (User) authentication.getPrincipal();

			acl.insertAce(0, BasePermission.ADMINISTRATION,
					new PrincipalSid(user.getUsername()), true);
			acl.insertAce(1, BasePermission.DELETE, new GrantedAuthoritySid(
					"ROLE_ADMIN"), true);

		}
		acl.insertAce(0, BasePermission.ADMINISTRATION, new GrantedAuthoritySid(
				"ROLE_ADMIN"), true);
		acl.insertAce(1, BasePermission.DELETE, new GrantedAuthoritySid(
				"ROLE_ADMIN"), true);

		/*
		 * // READ access for users with ROLE_USER acl.insertAce(2,
		 * BasePermission.READ, new GrantedAuthoritySid( "ROLE_USER"), true);
		 */
		return true;
	}

	public boolean removeACLFromObject(Long id_object, String name_class) {
		// Delete the ACL information as well
		ObjectIdentity oid = new ObjectIdentityImpl(name_class, id_object);
		mutableAclService.deleteAcl(oid, false);
		return true;
	}

	// Authorize professors to manage his course
	public void addPermissionToAnObjectCollection_ADMINISTRATION(
			Collection<User> professors, Long id_object, String name_class) {

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
			if(!acl.getEntries().isEmpty())
				acl.insertAce(acl.getEntries().size(), p, sid, true);
			else acl.insertAce(2, p, sid, true);
			mutableAclService.updateAcl(acl);

		}

	}

	// Remove ACL Permissions
	public void removePermissionToAnObject_ADMINISTRATION(User user,
			Long id_object, String name_class) {

		if (user != null) {
			// Create or update the relevant ACL
			MutableAcl acl = null;
			// Prepare the information we'd like in our access control entry
			// (ACE)
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
				} else
					aceIndex++;
			}

			// Now grant some permissions via an access control entry (ACE)
			if (acl != null)
				mutableAclService.updateAcl(acl);
		}
	}

	// Remove ACL Permissions
	public void removePermissionToAnObjectCollection_ADMINISTRATION(
			Collection<User> users, Long id_object, String name_class) {

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
				} else
					aceIndex++;
			}
		}

		// Now grant some permissions via an access control entry (ACE)
		if (acl != null)
			mutableAclService.updateAcl(acl);

	}

	// Authorize professors to manage his course
	public void addPermissionToAnObject_ADMINISTRATION(User coordinator,
			Long id_object, String name_class) {

		if (coordinator != null) {
			// Create or update the relevant ACL
			MutableAcl acl = null;
			// Prepare the information we'd like in our access control entry
			// (ACE)
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
			if(!acl.getEntries().isEmpty())
				acl.insertAce(acl.getEntries().size(), p, sid, true);
			else acl.insertAce(2, p, sid, true);

			mutableAclService.updateAcl(acl);
		}
	}

	// Authorize professors to manage his course
	public void addPermissionToAnObject_READ(User user, Long id_object,
			String name_class) {
		if (user != null) {
			// Create or update the relevant ACL
			MutableAcl acl = null;
			// Prepare the information we'd like in our access control entry
			// (ACE)
			ObjectIdentity oi = new ObjectIdentityImpl(name_class, id_object);

			Sid sid = null;

			sid = new PrincipalSid(user.getUsername());
			Permission p = BasePermission.READ;

			try {
				acl = (MutableAcl) mutableAclService.readAclById(oi);
			} catch (NotFoundException nfe) {
				acl = mutableAclService.createAcl(oi);
			}

			
			// Now grant some permissions via an access control entry (ACE)
			//TODO AQUIIIIIIIII
			/*if(!acl.getEntries().isEmpty())
				try {	
					acl.insertAce(acl.getEntries().size(), p, sid, true);
				} catch (NotFoundException nfe) {
					acl = mutableAclService.createAcl(oi);
					acl.insertAce(0, p, sid, true);

				}*/
			if(!acl.getEntries().isEmpty())
				acl.insertAce(acl.getEntries().size(), p, sid, true);
			else acl.insertAce(2, p, sid, true);
			mutableAclService.updateAcl(acl);
		}

	}

	// Authorize professors to manage his course
	public void addPermissionToAnObjectCollection_READ(
			Collection<User> professors, Long id_object, String name_class) {

		// Create or update the relevant ACL
		MutableAcl acl = null;
		// Prepare the information we'd like in our access control entry (ACE)
		ObjectIdentity oi = new ObjectIdentityImpl(name_class, id_object);

		Sid sid = null;
		for (User u : professors) {
			sid = new PrincipalSid(u.getUsername());
			Permission p = BasePermission.READ;

			try {
				acl = (MutableAcl) mutableAclService.readAclById(oi);
			} catch (NotFoundException nfe) {
				acl = mutableAclService.createAcl(oi);
			}

			// Now grant some permissions via an access control entry (ACE)
			if(!acl.getEntries().isEmpty())
				acl.insertAce(acl.getEntries().size(), p, sid, true);
			else acl.insertAce(2, p, sid, true);

			mutableAclService.updateAcl(acl);

		}

	}

	public void removePermissionToAnObject_READ(User user, Long id_object,
			String name_class) {
		if (user != null) {

			// Create or update the relevant ACL
			MutableAcl acl = null;
			// Prepare the information we'd like in our access control entry
			// (ACE)
			ObjectIdentity oi = new ObjectIdentityImpl(name_class, id_object);

			Sid sid = null;

			sid = new PrincipalSid(user.getUsername());
			Permission p = BasePermission.READ;

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
				} else
					aceIndex++;
			}

			// Now grant some permissions via an access control entry (ACE)
			if (acl != null)
				mutableAclService.updateAcl(acl);
		}
	}

	// Remove ACL Permissions
	public void removePermissionToAnObjectCollection_READ(
			Collection<User> users, Long id_object, String name_class) {

		// Create or update the relevant ACL
		MutableAcl acl = null;
		// Prepare the information we'd like in our access control entry (ACE)
		ObjectIdentity oi = new ObjectIdentityImpl(name_class, id_object);

		Sid sid = null;

		for (User u : users) {
			sid = new PrincipalSid(u.getUsername());
			Permission p = BasePermission.READ;

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
				} else
					aceIndex++;
			}
		}

		// Now grant some permissions via an access control entry (ACE)
		if (acl != null)
			mutableAclService.updateAcl(acl);

	}

	public void addPermissionCASCADE(User user, Object object,
			Long id_academic, Long id_course, Long id_group) {
		if (user != null) {
			if (object instanceof Course) { // Coordinator

				this.addPermissionToAnObject_READ(user, id_academic,
						AcademicTerm.class.getName());
				this.addPermissionToAnObject_ADMINISTRATION(user, id_course,
						Course.class.getName());

				// Course Activities
				Collection<Activity> activities_aux = activityService
						.getActivitiesForCourse(id_course, true);
				for (Activity a : activities_aux) {
					this.addPermissionToAnObject_ADMINISTRATION(user,
							a.getId(), a.getClass().getName());
				}

				// Groups
				Collection<Group> groups_aux = groupService.getGroupsForCourse(
						id_course, true);
				for (Group g : groups_aux) {
					this.addPermissionToAnObject_READ(user, g.getId(), g
							.getClass().getName());
					this.addPermissionToAnObject_ADMINISTRATION(user, g.getId(), g
							.getClass().getName());
					activities_aux.clear();

					// Group activities
					activities_aux = activityService.getActivitiesForGroup(
							g.getId(), true);
					for (Activity a : activities_aux) {
						this.addPermissionToAnObject_ADMINISTRATION(user,
								a.getId(), a.getClass().getName());
					}
				}
			}

			else if (object instanceof Group) {
				
				this.addPermissionToAnObject_READ(user, id_academic,
						AcademicTerm.class.getName());
				
				this.addPermissionToAnObject_READ(user, id_course,
						Course.class.getName());

				
				if (userService.hasRole(user, "ROLE_STUDENT"))
					this.addPermissionToAnObject_READ(user, id_group,
							Group.class.getName());
				else if (userService.hasRole(user, "ROLE_PROFESSOR"))
					this.addPermissionToAnObject_ADMINISTRATION(user, id_group,
							Group.class.getName());



				// Group activities
				Collection<Activity> activities_aux = activityService
						.getActivitiesForGroup(id_group, true);
				for (Activity a : activities_aux) {
					if (userService.hasRole(user, "ROLE_STUDENT"))
						this.addPermissionToAnObject_READ(user, a.getId(), a
								.getClass().getName());
					else if (userService.hasRole(user, "ROLE_PROFESSOR"))
						this.addPermissionToAnObject_ADMINISTRATION(user,
								a.getId(), a.getClass().getName());
				}
				activities_aux.clear();

				// Course activities
				activities_aux = activityService.getActivitiesForCourse(
						id_course, true);
				for (Activity a : activities_aux)
					this.addPermissionToAnObject_READ(user, a.getId(), a
							.getClass().getName());
			}
		}
	}

	public void removePermissionCASCADE(User user, Object object,
			Long id_academic, Long id_course, Long id_group) {

		if (user != null) {
			if (object instanceof Course) { // Coordinator

				this.removePermissionToAnObject_READ(user, id_academic,
						AcademicTerm.class.getName());
				this.removePermissionToAnObject_ADMINISTRATION(user, id_course,
						Course.class.getName());

				// Course Activities
				Collection<Activity> activities_aux = activityService
						.getActivitiesForCourse(id_course, true);
				for (Activity a : activities_aux) {
					this.removePermissionToAnObject_ADMINISTRATION(user,
							a.getId(), a.getClass().getName());
				}

				// Groups
				Collection<Group> groups_aux = groupService.getGroupsForCourse(
						id_course, true);
				for (Group g : groups_aux) {
					this.removePermissionToAnObject_READ(user, g.getId(), g
							.getClass().getName());

					this.removePermissionToAnObject_ADMINISTRATION(user, g.getId(), g
							.getClass().getName());
					
					activities_aux.clear();

					// Group activities
					activities_aux = activityService.getActivitiesForGroup(
							g.getId(), true);
					for (Activity a : activities_aux) {
						this.removePermissionToAnObject_ADMINISTRATION(user,
								a.getId(), a.getClass().getName());
					}
				}
			}

			else if (object instanceof Group) {

				this.removePermissionToAnObject_READ(user, id_course,
						Course.class.getName());
				this.removePermissionToAnObject_READ(user, id_academic,
						AcademicTerm.class.getName());
				if (userService.hasRole(user, "ROLE_STUDENT"))
					this.removePermissionToAnObject_READ(user, id_group,
							Group.class.getName());
				else if (userService.hasRole(user, "ROLE_PROFESSOR"))
					this.removePermissionToAnObject_ADMINISTRATION(user,
							id_group, Group.class.getName());

				// Group activities
				Collection<Activity> activities_aux = activityService
						.getActivitiesForGroup(id_group, true);
				for (Activity a : activities_aux) {
					if (userService.hasRole(user, "ROLE_STUDENT"))
						this.removePermissionToAnObject_READ(user, a.getId(), a
								.getClass().getName());
					else if (userService.hasRole(user, "ROLE_PROFESSOR"))
						this.removePermissionToAnObject_ADMINISTRATION(user,
								a.getId(), a.getClass().getName());
				}
				activities_aux.clear();

				// Course activities
				activities_aux = activityService.getActivitiesForCourse(
						id_course, true);
				for (Activity a : activities_aux)
					this.removePermissionToAnObject_READ(user, a.getId(), a
							.getClass().getName());
			}
		}
	}
	
	public void removePermissionGroupCoordinator(User coordinator, Long id_group){
		this.removePermissionToAnObject_READ(coordinator, id_group,
				Group.class.getName());
		this.removePermissionToAnObject_ADMINISTRATION(coordinator, id_group,
				Group.class.getName());
	}
	
	public void addPermissionGroupCoordinator(User coordinator, Long id_group){
		this.addPermissionToAnObject_READ(coordinator, id_group,
				Group.class.getName());
		this.addPermissionToAnObject_ADMINISTRATION(coordinator, id_group,
				Group.class.getName());
	}

	public void removePermissionCollectionCASCADE(Collection<User> users,
			Object object, Long id_academic, Long id_course, Long id_group) {
		for (User u : users)
			this.removePermissionCASCADE(u, object, id_academic, id_course,
					id_group);
	}

	public void addPermissionCollectionCASCADE(Collection<User> users,
			Object object, Long id_academic, Long id_course, Long id_group) {
		for (User u : users)
			this.addPermissionCASCADE(u, object, id_academic, id_course,
					id_group);
	}
}
