package com.example.tfg.service.implementation;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public boolean addAcademicTerm(AcademicTerm academicTerm) {

		AcademicTerm existAcademic = daoAcademicTerm.exists(academicTerm.getTerm(), academicTerm.getDegree());
		if (existAcademic == null){
			return daoAcademicTerm.addAcademicTerm(academicTerm);			
		}
		else if(existAcademic.isDeleted()){
			existAcademic.setDeleted(false);
			return daoAcademicTerm.saveAcademicTerm(existAcademic);			
		}
		else return false;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public boolean modifyAcademicTerm(AcademicTerm academicTerm,
			Long id_academic) {
//		academicTerm.setId(id_academic);
		AcademicTerm ac = daoAcademicTerm.getAcademicTermById(id_academic);
		ac.setTerm(academicTerm.getTerm());
//		academicTerm.setDegree(ac.getDegree());
//		if (!daoAcademicTerm.exists(academicTerm))
		if(daoAcademicTerm.exists(academicTerm.getTerm(), academicTerm.getDegree()) == null)
			return daoAcademicTerm.saveAcademicTerm(academicTerm);
		return false;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = false)
	public List<AcademicTerm> getAcademicsTerm(Integer pageIndex) {
		return daoAcademicTerm.getAcademicsTerm(pageIndex);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
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

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = false)
	public Integer numberOfPages() {
		return daoAcademicTerm.numberOfPages();
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public AcademicTerm getAcademicTerm(Long id_academic) {
		return daoAcademicTerm.getAcademicTermById(id_academic);
	}

//	@Transactional(readOnly = false)
//	public boolean modifyTerm(String term, String newTerm) {
//		if (!daoAcademicTerm.existTerm(newTerm))
//			return daoAcademicTerm.modifyTerm(term, newTerm);
//		return false;
//	}
	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = false)
	public List<AcademicTerm> getAcademicTermsByDegree(Long id_degree) {
		return daoAcademicTerm.getAcademicTermsByDegree(id_degree);
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly = true)
	public AcademicTerm getAcademicTermAll(Long id_academic) {
		AcademicTerm aT= daoAcademicTerm.getAcademicTermById(id_academic);
		aT.setCourses(serviceCourse.getCoursesByAcademicTerm(id_academic));
		return aT;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	// propagation = Propagation.REQUIRED)
	public boolean deleteAcademicTerm(Collection<AcademicTerm> academicList) {
		
		boolean deleteCourses = serviceCourse.deleteCourses(academicList);
		if (deleteCourses)
		return daoAcademicTerm.deleteAcademicTerm(academicList);
		else return false;
	}

}
