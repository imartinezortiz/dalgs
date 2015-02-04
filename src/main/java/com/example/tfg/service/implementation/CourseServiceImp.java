package com.example.tfg.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.example.tfg.domain.AcademicTerm;
import com.example.tfg.domain.Course;
import com.example.tfg.repository.CourseDao;
import com.example.tfg.service.AcademicTermService;
import com.example.tfg.service.ActivityService;
import com.example.tfg.service.CourseService;

@Service
public class CourseServiceImp implements CourseService {

	@Autowired
	private CourseDao daoCourse;

	@Autowired
	ActivityService serviceActivity;

	// @Autowired
	// private DegreeService serviceDegree;

	@Autowired
	private AcademicTermService serviceAcademicTerm;

	@Transactional(readOnly = false)
	public boolean addCourse(Course course, Long id_academic) {
		AcademicTerm academic = serviceAcademicTerm
				.getAcademicTerm(id_academic);

		course.setAcademicTerm(academic);
		Long aux = daoCourse.isDisabled(course.getAcademicTerm().getId(),
				course.getSubject().getId());
		if (aux != null) {
			course.setId(aux);
			course.setDeleted(false);
			return daoCourse.saveCourse(course);
		} else if (!daoCourse.exist(course))
			return daoCourse.addCourse(course);

		return false;
	}

	@Transactional(readOnly = true)
	public List<Course> getAll() {
		return daoCourse.getAll();
	}

	@Transactional(readOnly = false)
	public boolean modifyCourse(Course course, Long id_academic) {
		course.setAcademicTerm(serviceAcademicTerm.getAcademicTerm(id_academic));
		return daoCourse.saveCourse(course);
	}

	@Transactional(readOnly = false)
	public Course getCourse(Long id) {
		return daoCourse.getCourse(id);
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public boolean deleteCourse(Long id) {
		Course course = daoCourse.getCourse(id);
		if (serviceActivity.deleteActivitiesFromCourse(course))
			return daoCourse.deleteCourse(id);
		return false;
	}

	/*
	 * @Transactional(readOnly = true) public List<Course>
	 * getCoursesByAcademicTerm(String term) { return
	 * daoCourse.getCoursesByAcademicTerm(term); }
	 */

	@Transactional(readOnly = false)
	public boolean exists(Course course) {
		return daoCourse.exist(course);
	}

	@Transactional(readOnly = true)
	public List<Course> getCoursesByAcademicTerm(Long id_academic) {

		return daoCourse.getCoursesByAcademicTerm(id_academic);
	}

	public boolean deleteCoursesFromAcademic(AcademicTerm academic) {
		boolean deleted = false;
		for (Course course : academic.getCourses()) {
			deleted = serviceActivity.deleteActivitiesFromCourse(course);
			if (!deleted)
				break;
		}
		if (deleted)
			if (daoCourse.deleteCoursesFromAcademic(academic))
				return true;
		return deleted;
	}

}
