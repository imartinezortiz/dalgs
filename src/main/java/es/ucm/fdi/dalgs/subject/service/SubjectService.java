package es.ucm.fdi.dalgs.subject.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.dalgs.acl.service.AclObjectService;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.competence.service.CompetenceService;
import es.ucm.fdi.dalgs.course.service.CourseService;
import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.domain.Degree;
import es.ucm.fdi.dalgs.domain.Subject;
import es.ucm.fdi.dalgs.domain.Topic;
import es.ucm.fdi.dalgs.subject.repository.SubjectRepository;
import es.ucm.fdi.dalgs.topic.service.TopicService;

@Service
public class SubjectService {

	@Autowired
	private SubjectRepository daoSubject;
	
	@Autowired
	private TopicService serviceTopic;

	@Autowired
	private CompetenceService serviceCompetence;
	
	@Autowired
	private CourseService serviceCourse;
	
	@Autowired
	private AclObjectService manageAclService;


	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Subject> addSubject(Subject subject, Long id_topic) {
		
		boolean success = false;
		Subject subjectExists = daoSubject.existByCode(subject.getInfo().getCode());
		ResultClass<Subject> result = new ResultClass<>();

		if( subjectExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<>();
			errors.add("Code already exists");

			if (subjectExists.getIsDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");
				result.setSingleElement(subjectExists);
			}
			else result.setSingleElement(subject);
			result.setErrorsList(errors);
		}
		else{
			subject.setTopic(serviceTopic.getTopic(id_topic).getSingleElement());
			success = daoSubject.addSubject(subject);
			
			
			if(success){
				subjectExists = daoSubject.existByCode(subject.getInfo().getCode());
				success = manageAclService.addACLToObject(subjectExists.getId(), subjectExists.getClass().getName());
				if (success) result.setSingleElement(subject);
			
			}else{
				throw new IllegalArgumentException(	"Cannot create ACL. Object not set.");

			}
		}
		return result;		
	}

	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	@Transactional(readOnly = true)
	public ResultClass<Subject> getAll() {
		
		
		ResultClass<Subject> result = new ResultClass<>();
		result.addAll(daoSubject.getAll());
		return result;
	}

	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Subject> getSubject(Long id) {
		ResultClass<Subject> result = new ResultClass<>();
		result.setSingleElement(daoSubject.getSubject(id));
		return result;
	}

	@PreAuthorize("hasPermission(#subject, 'DELETE') or hasPermission(#subject, 'ADMINISTRATION')" )
	@Transactional(propagation = Propagation.REQUIRED)
	public ResultClass<Boolean>deleteSubject(Subject subject) {
		ResultClass<Boolean> result = new ResultClass<>();

		ResultClass<Course> courses = serviceCourse.getCoursesBySubject(subject);
		if (!courses.isEmpty()){
				Collection<Subject> subjects = new ArrayList<>();
				subjects.add(subject);
					ResultClass<Boolean> deleteCourses= serviceCourse.deleteCoursesForSubject(subjects); 
					if (deleteCourses.getSingleElement()){
						subject.getCompetences().clear();
						result.setSingleElement(daoSubject.deleteSubject(subject));
					}
		}
		else result.setSingleElement(daoSubject.deleteSubject(subject));
		return result;
	}

	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	@Transactional(readOnly = true)
	public ResultClass<Subject> getSubjectsForTopic(Long id_topic, Boolean show) {
		ResultClass<Subject> result = new ResultClass<>();
		result.addAll(daoSubject.getSubjectsForTopic(id_topic, show));
		return result;
	}

	@PreAuthorize("hasPermission(#subject, 'WRITE') or hasPermission(#subject, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> modifySubject(Subject subject, Long id_subject) {
		ResultClass<Boolean> result = new ResultClass<>();

		Subject modifySubject = daoSubject.getSubject(id_subject);
		
		Subject subjectExists = daoSubject.existByCode(subject.getInfo().getCode());
		
		if(!subject.getInfo().getCode().equalsIgnoreCase(modifySubject.getInfo().getCode()) && 
				subjectExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("New code already exists");

			if (subjectExists.getIsDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");

			}
			result.setErrorsList(errors);
			result.setSingleElement(false);
		}
		else{
			modifySubject.setInfo(subject.getInfo());
			boolean r = daoSubject.saveSubject(modifySubject);
			if (r) 
				result.setSingleElement(true);
		}
		return result;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> addCompetences(Subject modify, Long id_subject) {
		ResultClass<Boolean> result = new ResultClass<>();
		Subject subject = daoSubject.getSubject(id_subject);
		subject.setInfo(modify.getInfo());
		subject.setCompetences(modify.getCompetences());		
		result.setSingleElement(daoSubject.saveSubject(subject));
		return result;
	}
	

	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	@Transactional(readOnly = true)
	public ResultClass<Subject> getSubjectForCourse(Long id) {
		ResultClass<Subject> result = new ResultClass<Subject>();
		result.setSingleElement(daoSubject.getSubjectForCourse(id));
		return result;
	}

	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	@Transactional(readOnly = true)
	public ResultClass<Subject> getSubjectByName(String string) {
		ResultClass<Subject> result = new ResultClass<Subject>();
		result.setSingleElement(daoSubject.getSubjectByName(string));
		return result;
	}
	
	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	@Transactional(readOnly = true)
	public ResultClass<Subject> getSubjectAll(Long id_subject) {
		ResultClass<Subject> result = new ResultClass<Subject>();
		Subject p = daoSubject.getSubject(id_subject);;
		p.setCompetences(serviceCompetence.getCompetencesForSubject(id_subject));
		result.setSingleElement(p);
		return result;
	}


	@PostFilter("hasPermission(filterObject, 'READ') or hasPermission(filterObject, 'ADMINISTRATION')")
	@Transactional(readOnly = true)
	public ResultClass<Subject> getSubjectForDegree(Degree degree) {
		ResultClass<Subject> result = new ResultClass<>();
		result.addAll(daoSubject.getSubjectForDegree(degree));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> deleteSubjectsForTopic(Collection<Topic> topics) {	
		
		ResultClass<Boolean> result = new ResultClass<>();
		Collection<Subject> subjects = daoSubject.getSubjectsForTopics(topics);
		
		if (!subjects.isEmpty()) {
			ResultClass<Boolean> deleteCourses= serviceCourse.deleteCoursesForSubject(subjects);
			if(deleteCourses.getSingleElement())
				result.setSingleElement(daoSubject.deleteSubjectsForTopics(topics));
			else result.setSingleElement(false);
		}
		else result.setSingleElement(daoSubject.deleteSubjectsForTopics(topics));
		return result;
	}
	
	@PreAuthorize("hasPermission(#subect, 'WRITE') or hasPermission(#subject, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Subject> unDeleteSubject(Subject subject){
		Subject s = daoSubject.existByCode(subject.getInfo().getCode());
		ResultClass<Subject> result = new ResultClass<>();
		if(s == null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<>();
			errors.add("Code doesn't exist");
			result.setErrorsList(errors);

		}
		else{
			if(!s.getIsDeleted()){
				Collection<String> errors = new ArrayList<>();
				errors.add("Code is not deleted");
				result.setErrorsList(errors);
			}

			s.setDeleted(false);
			s.setInfo(subject.getInfo());
			boolean r = daoSubject.saveSubject(s);
			if(r) 
				result.setSingleElement(s);	

		}
		return result;
		
	}


}
