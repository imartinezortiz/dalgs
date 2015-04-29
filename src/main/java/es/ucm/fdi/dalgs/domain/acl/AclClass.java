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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

//Generated Feb 5, 2009 9:48:08 AM by Hibernate Tools 3.2.4.CR1

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.UniqueConstraint;

/**
 * ACL_CLASS allows us to uniquely identify any domain object class in the
 * system. The only columns are the ID and the Java class name. Thus, there is a
 * single row for each unique Class we wish to store ACL permissions for.
 */

@Entity
@Table(name = "acl_class", uniqueConstraints = @UniqueConstraint(columnNames = "CLASS"))
public class AclClass implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	private Long id;
	private String class_;
	private Set<AclObjectIdentity> aclObjectIdentities = new HashSet<AclObjectIdentity>(
			0);

	public AclClass() {
	}

	public AclClass(String class_) {
		this.class_ = class_;
	}

	public AclClass(String class_, Set<AclObjectIdentity> aclObjectIdentities) {
		this.class_ = class_;
		this.aclObjectIdentities = aclObjectIdentities;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ID", unique = true, nullable = false)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "CLASS", unique = true, nullable = false, length = 100)
	public String getClass_() {
		return this.class_;
	}

	public void setClass_(String class_) {
		this.class_ = class_;
	}

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "aclClass")
	public Set<AclObjectIdentity> getAclObjectIdentities() {
		return this.aclObjectIdentities;
	}

	public void setAclObjectIdentities(
			Set<AclObjectIdentity> aclObjectIdentities) {
		this.aclObjectIdentities = aclObjectIdentities;
	}

}