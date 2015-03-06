package es.ucm.fdi.dalgs.domain;

import java.util.Collection;
import java.util.Collections;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.JoinColumn;
import javax.persistence.UniqueConstraint;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Entity
@Table(name = "user")
public class User implements UserDetails, CredentialsContainer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id_user")
	private Long id;

	@Column(name = "firstname")
	private String firstName;
	
	@Column(name = "lastname")
	private String lastName;

	@Column(name="username", unique = true)
	private String username;
	
	@Column(name = "password")
	private String password;

	@Column(name="email", unique = true)
	private String email;
	

	

/*
	@ManyToMany(mappedBy = "users", fetch = FetchType.LAZY)
	private Collection<Group> groups = new ArrayList<Group>();
	*/
	
	//  User Credentials
	
	private String salt;

	private boolean enabled;

	private boolean accountNonExpired;

	private boolean accountNonLocked;

	private boolean credentialsNonExpired;
	
	
	// User Roles

	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="user_roles", joinColumns=@JoinColumn(name="user"),  uniqueConstraints=@UniqueConstraint(columnNames={"user", "role"}))
	private Collection<UserRole> roles;
	
	//Constructor
	public User() {
		this.accountNonExpired = true;
		this.credentialsNonExpired = true;
		this.enabled = false;
		this.accountNonLocked = true;
	}
	

	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}


/*
	public Collection<Group> getGroups() {
		return groups;
	}

	public void setGroups(Collection<Group> groups) {
		this.groups = groups;
	}
	*/

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		User other = (User) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + "]";
	}

	@Override
	public void eraseCredentials() {
		this.password = null;
		this.setSalt(null);
	}
	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	@Override
	public boolean isEnabled() {
		return this.enabled;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Collections.unmodifiableCollection(this.roles);
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}



	public Collection<UserRole> getRoles() {
		return roles;
	}



	public void setRoles(Collection<UserRole> roles) {
		this.roles = roles;
	}



	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}



	public void setAccountNonExpired(boolean accountNonExpired) {
		this.accountNonExpired = accountNonExpired;
	}



	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}



	public void setCredentialsNonExpired(boolean credentialsNonExpired) {
		this.credentialsNonExpired = credentialsNonExpired;
	}

}