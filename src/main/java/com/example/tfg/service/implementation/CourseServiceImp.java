package com.example.tfg.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.tfg.domain.AcademicTerm;
import com.example.tfg.domain.Competence;
import com.example.tfg.domain.Course;
import com.example.tfg.repository.CourseDao;
import com.example.tfg.repository.DegreeDao;
import com.example.tfg.service.CourseService;

@Service
public class CourseServiceImp implements CourseService {

	@Autowired
	private CourseDao daoCourse;
	
	@Autowired
	private DegreeDao daoDegree;
	
	@Transactional(readOnly = false)
	public boolean addCourse(Course course) {
		Long aux = daoCourse.isDisabled(course.getAcademicTerm().getId(), course.getSubject().getId());
		 if(aux!=null){
			 course.setId(aux);
			 course.setDeleted(false);
		 	return daoCourse.saveCourse(course);
		 }
	 	 else if (!daoCourse.exist(course))
				return daoCourse.addCourse(course);
		
		return false;
	}
	
	@Transactional(readOnly = true)
	public List<Course> getAll(){
		return daoCourse.getAll();
	}

	@Transactional(readOnly = false)
	public boolean modifyCourse(Course course){
		return daoCourse.saveCourse(course);
	}
	
	@Transactional(readOnly = false)
	public  Course getCourse(Long id){
		return daoCourse.getCourse(id);
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteCourse(Long id){
		return daoCourse.deleteCourse(id);
	}

	/*@Transactional(readOnly = true)
	public List<Course> getCoursesByAcademicTerm(String term) {
		return daoCourse.getCoursesByAcademicTerm(term);
	}
	*/

	@Transactional(readOnly=false)
	public  boolean exists(Course course){
		return daoCourse.exist(course);
	}

	@Transactional(readOnly = true)
	public List<Course> getCoursesByAcademicTerm(Long id_academic) {
		
		return daoCourse.getCoursesByAcademicTerm(id_academic);
	}


}
