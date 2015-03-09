package es.ucm.fdi.dalgs.domain;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Embeddable;


import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.security.core.GrantedAuthority;

@Embeddable
public class UserRole implements GrantedAuthority, Serializable {

	/**
	 * @see java.io.Serializable
	 */
	private static final long serialVersionUID = 1L;
	
	@NotEmpty @NotNull @NotBlank
	@Basic
	private String role;
	
	UserRole() {
		
	}
	
	public UserRole(String role) {
		this.role = role;
	}
	
	@Override
	public String getAuthority() {
		return this.role;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserRole other = (UserRole) obj;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserRole [role=" + role + "]";
	}


}