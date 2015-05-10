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
package es.ucm.fdi.dalgs.domain.info;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;


import es.ucm.fdi.dalgs.domain.Copyable;

@Embeddable
public class ActivityInfo implements Serializable, Cloneable,
		Copyable<ActivityInfo> {

	private static final long serialVersionUID = 1L;

	@NotEmpty
	@NotNull
	@NotBlank
	@Size(min = 1, max = 100)
	@Column(name = "name")
	private String name;

	@NotEmpty
	@NotNull
	@NotBlank
	@Size(min = 1, max = 1000)
	@Column(name = "description")
	private String description;

	@NotEmpty
	@NotNull
	@NotBlank
	@Size(min = 1, max = 20)
	@Column(name = "code_activity", nullable = false, unique = true)
	private String code;
	

	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		ActivityInfo other = (ActivityInfo) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

	@Override
	public ActivityInfo depth_copy() {
		ActivityInfo copy = this.shallow_copy();
		return copy;
	}

	@Override
	public ActivityInfo shallow_copy() {
		try {
			return (ActivityInfo) super.clone();

		} catch (CloneNotSupportedException e) {
			throw new RuntimeException(e);
		}
	}

}
