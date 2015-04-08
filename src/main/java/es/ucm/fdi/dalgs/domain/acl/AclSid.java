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