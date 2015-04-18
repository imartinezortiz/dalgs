package es.ucm.fdi.dalgs.rest;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;

public class ActivityREST implements Serializable{
	private long id_course;
	private long id_group;
	private String name;
	private String description;
	
    private Date createdDate;

	public ActivityREST() {
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
	
	 
    @JsonSerialize(using=DateSerializer.class)
    public Date getCreatedDate() {
        return createdDate;
    }
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
	

}
