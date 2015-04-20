package es.ucm.fdi.dalgs.rest.classes;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;

public class Activity_Request implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private long id_course;
	private long id_group;
	private String name;
	private String description;
	private String code;
	
    private Date createdDate;

	public Activity_Request() {
		super();
	}
	
	public long getId_course() {
		return id_course;
	}
	public void setId_course(long id_course) {
		this.id_course = id_course;
	}
	public long getId_group() {
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
