package es.ucm.fdi.dalgs.group.repository;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.domain.Group;
import es.ucm.fdi.dalgs.domain.User;

@Repository
public class GroupRepository {

	protected EntityManager em;
	protected static final Logger logger = LoggerFactory
			.getLogger(GroupRepository.class);

	public EntityManager getEntityManager() {
		return em;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		try {
			this.em = entityManager;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
	
	public Group getGroup(Long id_group) {
		return em.find(Group.class, id_group);
	}

	
	public Group existByName(String name, Long id) {
		Query query = null;
		if (id!=null){
			query =em.createQuery("select g from Group g  where g.name = ?1 and g.id !=?2");
			query.setParameter(1, name);		
			query.setParameter(2, id);	
		}
		else{
			query =em.createQuery("select g from Group g  where g.name = ?1");
			query.setParameter(1, name);		
		}

		if (query.getResultList().isEmpty())
			return null;
		else
			return (Group)query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public Collection<Group> getGroupsForCourse(Long id) {
		Course course = em.getReference(Course.class, id);

		Query query = em
				.createQuery("select g from Group g where g.course=?1 and g.isDeleted='false' ");
		query.setParameter(1, course);

		if (query.getResultList().isEmpty())
			return null;

		return query.getResultList();
	}

	public boolean addGroup(Group newgroup) {
		try {
			em.persist(newgroup);
		} catch (ConstraintViolationException e) {
			logger.error(e.getMessage());
			return false;
		}

		return true;
	}

	
	public boolean saveGroup(Group existGroup) {
		try {
			em.merge(existGroup);
		} catch (ConstraintViolationException e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}

	
	public boolean deleteGroup(Long id_group) {
		Group group = em.getReference(Group.class, id_group);
		try {
			group.setDeleted(true);
			em.merge(group);

			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}


	public boolean deleteGroupsFromCourses(Collection<Course> coursesList) {
		try {
			Query query = em
					.createQuery("UPDATE Group g SET g.isDeleted = true where g.course in ?1");

			query.setParameter(1, coursesList);
			query.executeUpdate();

		} catch (Exception e) {
			logger.error(e.getMessage());

			return false;
		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public Collection<Group> getGroupsForStudent(Long id_student){
		User user = em.getReference(User.class, id_student);

		Query query = em.createQuery("select g from Group g join g.students s where a=?1 and g.isDeleted='false' ");
		query.setParameter(1, user);

		if (query.getResultList().isEmpty())
			return null;

		return query.getResultList();
	}
	@SuppressWarnings("unchecked")
	public Collection<Group> getGroupsForProfessor(Long id_professor){
		User user = em.getReference(User.class, id_professor);

		Query query = em.createQuery("select g from Group g join g.professors s where a=?1 and g.isDeleted='false' ");
		query.setParameter(1, user);

		if (query.getResultList().isEmpty())
			return null;

		return query.getResultList();
	}
}
