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

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Id;

/**
 * ACL_ENTRY stores the individual permissions assigned to each recipient.
 * Columns include a foreign key to the ACL_OBJECT_IDENTITY, the recipient (ie a
 * foreign key to ACL_SID), whether we'll be auditing or not, and the integer
 * bit mask that represents the actual permission being granted or denied. We
 * have a single row for every recipient that receives a permission to work with
 * a domain object.
 */
@Entity
@Table(name = "acl_entry", uniqueConstraints = @UniqueConstraint(columnNames = {
		"acl_object_identity", "ace_order" }))
public class AclEntry implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private AclSid aclSid;
	private AclObjectIdentity aclObjectIdentity;
	private int aceOrder;
	private int mask;
	private boolean granting;
	private boolean auditSuccess;
	private boolean auditFailure;

	public AclEntry() {
	}

	public AclEntry(AclSid aclSid, AclObjectIdentity aclObjectIdentity,
			int aceOrder, int mask, boolean granting, boolean auditSuccess,
			boolean auditFailure) {
		this.aclSid = aclSid;
		this.aclObjectIdentity = aclObjectIdentity;
		this.aceOrder = aceOrder;
		this.mask = mask;
		this.granting = granting;
		this.auditSuccess = auditSuccess;
		this.auditFailure = auditFailure;
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

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SID", nullable = false)
	public AclSid getAclSid() {
		return this.aclSid;
	}

	public void setAclSid(AclSid aclSid) {
		this.aclSid = aclSid;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ACL_OBJECT_IDENTITY", nullable = false)
	public AclObjectIdentity getAclObjectIdentity() {
		return this.aclObjectIdentity;
	}

	public void setAclObjectIdentity(AclObjectIdentity aclObjectIdentity) {
		this.aclObjectIdentity = aclObjectIdentity;
	}

	@Column(name = "ACE_ORDER", nullable = false)
	public int getAceOrder() {
		return this.aceOrder;
	}

	public void setAceOrder(int aceOrder) {
		this.aceOrder = aceOrder;
	}

	@Column(name = "MASK", nullable = false)
	public int getMask() {
		return this.mask;
	}

	public void setMask(int mask) {
		this.mask = mask;
	}

	@Column(name = "GRANTING", nullable = false)
	public boolean isGranting() {
		return this.granting;
	}

	public void setGranting(boolean granting) {
		this.granting = granting;
	}

	@Column(name = "AUDIT_SUCCESS", nullable = false)
	public boolean isAuditSuccess() {
		return this.auditSuccess;
	}

	public void setAuditSuccess(boolean auditSuccess) {
		this.auditSuccess = auditSuccess;
	}

	@Column(name = "AUDIT_FAILURE", nullable = false)
	public boolean isAuditFailure() {
		return this.auditFailure;
	}

	public void setAuditFailure(boolean auditFailure) {
		this.auditFailure = auditFailure;
	}

}