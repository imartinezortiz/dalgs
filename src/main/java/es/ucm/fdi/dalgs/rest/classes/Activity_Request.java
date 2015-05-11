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
package es.ucm.fdi.dalgs.rest.classes;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;

public class Activity_Request implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private Long id_course;
	private Long id_group;
	private String name;
	private String description;
	private String code;
	
    private Date createdDate;

	public Activity_Request() {
		super();
	}
	
	public Long getId_course() {
		return id_course;
	}
	public void setId_course(long id_course) {
		this.id_course = id_course;
	}
	public Long getId_group() {
		return id_group;
	}
	public void setId_group(long id_group) {
		this.id_group = id_group;
	}
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

	@JsonSerialize(using=DateSerializer.class)
    public Date getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
	

}
