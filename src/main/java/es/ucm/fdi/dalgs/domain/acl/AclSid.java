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
package es.ucm.fdi.dalgs.domain.acl;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 * ACL_SID allows us to uniquely identify any principal or authority in the
 * system ("SID" stands for "security identity"). The only columns are the ID, a
 * textual representation of the SID, and a flag to indicate whether the textual
 * representation refers to a principal name or a GrantedAuthority. Thus, there
 * is a single row for each unique principal or GrantedAuthority. When used in
 * the context of receiving a permission, a SID is generally called a
 * "recipient".
 */

@Entity
@Table(name = "acl_sid", uniqueConstraints = @UniqueConstraint(columnNames = {
		"principal", "sid" }))
public class AclSid implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long id;
	private boolean principal;
	private String sid;
	private Set<AclEntry> aclEntries = new HashSet<AclEntry>(0);
	private Set<AclObjectIdentity> aclObjectIdentities = new HashSet<AclObjectIdentity>(
			0);

	public AclSid() {
	}

	public AclSid(long id, boolean principal, String sid) {
		this.id = id;
		this.principal = principal;
		this.sid = sid;
	}

	public AclSid(long id, boolean principal, String sid,
			Set<AclEntry> aclEntries, Set<AclObjectIdentity> aclObjectIdentities) {
		this.id = id;
		this.principal = principal;
		this.sid = sid;
		this.aclEntries = aclEntries;
		this.aclObjectIdentities = aclObjectIdentities;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "id", unique = true, nullable = false)
	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Column(name = "principal", nullable = false)
	public boolean isPrincipal() {
		return this.principal;
	}

	public void setPrincipal(boolean principal) {
		this.principal = principal;
	}

	@Column(name = "sid", nullable = false, length = 100)
	public String getSid() {
		return this.sid;
	}

	public void setSid(String sid) {
		this.sid = sid;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "aclSid")
	public Set<AclEntry> getAclEntries() {
		return this.aclEntries;
	}

	public void setAclEntries(Set<AclEntry> aclEntries) {
		this.aclEntries = aclEntries;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "aclSid")
	public Set<AclObjectIdentity> getAclObjectIdentities() {
		return this.aclObjectIdentities;
	}

	public void setAclObjectIdentities(
			Set<AclObjectIdentity> aclObjectIdentities) {
		this.aclObjectIdentities = aclObjectIdentities;
	}

}