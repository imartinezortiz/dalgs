package com.example.tfg.service.implementation;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tfg.domain.AcademicTerm;
import com.example.tfg.repository.AcademicTermDao;
import com.example.tfg.service.AcademicTermService;
import com.example.tfg.service.CourseService;

@Service
public class AcademicTermServiceImp implements AcademicTermService {

	@Autowired
	private AcademicTermDao daoAcademicTerm;

	@Autowired
	private CourseService serviceCourse;
	
	@Secured("ROLE_ADMIN")
	@Transactional(readOnly = false)
	public boolean addAcademicTerm(AcademicTerm academicTerm) {

		Long a = daoAcademicTerm.isDisabled(academicTerm.getTerm(),
				academicTerm.getDegree());
		if (a != null) {
			academicTerm.setId(a);
			return daoAcademicTerm.saveAcademicTerm(academicTerm);
		} else if (!daoAcademicTerm.exists(academicTerm))
			return daoAcademicTerm.addAcademicTerm(academicTerm);

		return daoAcademicTerm.saveAcademicTerm(academicTerm);
		// return false;

	}
	@Secured("ROLE_ADMIN")
	@Transactional(readOnly = false)
	public boolean modifyAcademicTerm(AcademicTerm academicTerm,
			Long id_academic) {
		academicTerm.setId(id_academic);
		AcademicTerm ac = daoAcademicTerm.getAcademicTermById(id_academic);
		academicTerm.setDegree(ac.getDegree());
		if (!daoAcademicTerm.exists(academicTerm))
			return daoAcademicTerm.saveAcademicTerm(academicTerm);
		return false;
	}

	
	@Transactional(readOnly = false)
	public List<AcademicTerm> getAcademicsTerm(Integer pageIndex) {// String
																	// term) {
		return daoAcademicTerm.getAcademicsTerm(pageIndex);// term);
	}

	/*
	 * @Transactional(propagation = Propagation.REQUIRED) public boolean
	 * deleteTerm(String term) { return daoAcademicTerm.deleteTerm(term); }
	 */

	@Transactional(readOnly = false)
	// propagation = Propagation.REQUIRED)
	public boolean deleteAcademicTerm(Long id_academic) {
		AcademicTerm academic = daoAcademicTerm
				.getAcademicTermById(id_academic);
		if (academic.getCourses().isEmpty()
				|| serviceCourse.deleteCoursesFromAcademic(academic))

			return daoAcademicTerm.deleteAcademicTerm(id_academic);
		return false;
	}

	@Transactional(readOnly = false)
	public Integer numberOfPages() {
		return daoAcademicTerm.numberOfPages();
	}

	@Transactional(readOnly = true)
	public AcademicTerm getAcademicTerm(Long id_academic) {
		return daoAcademicTerm.getAcademicTermById(id_academic);
	}

	@Transactional(readOnly = false)
	public boolean modifyTerm(String term, String newTerm) {
		if (!daoAcademicTerm.existTerm(newTerm))
			return daoAcademicTerm.modifyTerm(term, newTerm);
		return false;
	}

	@Transactional(readOnly = false)
	public List<AcademicTerm> getAcademicTermsByDegree(Long id_degree) {
		return daoAcademicTerm.getAcademicTermsByDegree(id_degree);
	}

	@Transactional(readOnly = true)
	public AcademicTerm getAcademicTermAll(Long id_academic) {
		AcademicTerm aT= daoAcademicTerm.getAcademicTermById(id_academic);
		aT.setCourses(serviceCourse.getCoursesByAcademicTerm(id_academic));
		return aT;
	}

}
