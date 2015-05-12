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
package es.ucm.fdi.dalgs.group.repository;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import es.ucm.fdi.dalgs.domain.AcademicTerm;
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

	public Group getGroup(Long id_group, Long id_course, Long id_academic) {
		Course course = em.getReference(Course.class, id_course);
		AcademicTerm academic = em
				.getReference(AcademicTerm.class, id_academic);

		
		Query query = null;
		
	
		query = em
				.createQuery("select g from Group g  left join g.activities a where g.id = ?2 and g.course = ?1 and g.id = ?2 and g.course.academicTerm=?3"); //and (a.id is null or a.isDeleted='false')");
		query.setParameter(1, course);
		query.setParameter(2, id_group);
		query.setParameter(3, academic);
		
				
		if (query.getResultList().isEmpty())
			return null;
		else
			return (Group) query.getSingleResult();
	}

	public Group getGroupFormatter(Long id_group) {
		return em.find(Group.class, id_group);
	}

	public Group existInCourse(Long id_course, String name) {// , Long id) {
		Course course = em.getReference(Course.class, id_course);

		Query query = null;

		query = em
				.createQuery("select g from Group g  where g.course = ?1 and g.name = ?2");
		query.setParameter(1, course);
		query.setParameter(2, name);

		if (query.getResultList().isEmpty())
			return null;
		else
			return (Group) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public Collection<Group> getGroupsForCourse(Long id, Boolean showAll) {
		Course course = em.getReference(Course.class, id);

		if (!showAll) {
			Query query = em
					.createQuery("select g from Group g where g.course=?1 and g.isDeleted='false' ");
			query.setParameter(1, course);
			return query.getResultList();
		} else {
			Query query = em
					.createQuery("select g from Group g where g.course=?1");
			query.setParameter(1, course);
			return query.getResultList();

		}

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

	public boolean deleteGroup(Group group) {
		try {
			group.getProfessors().clear();
			group.getStudents().clear();
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
	public Collection<Group> getGroupsForStudent(Long id_student) {
		User user = em.getReference(User.class, id_student);

		Query query = em
				.createQuery("select g from Group g join g.course c join c.subject join c.academicTerm join g.students s where s=?1 and g.isDeleted='false' ");
		query.setParameter(1, user);

		if (query.getResultList().isEmpty())
			return null;

		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Collection<Group> getGroupsForProfessor(Long id_professor) {
		User user = em.getReference(User.class, id_professor);

		Query query = em
				.createQuery("select g from Group g join g.course c join c.subject join c.academicTerm join g.professors s where s=?1 and g.isDeleted='false' ");
		query.setParameter(1, user);

		if (query.getResultList().isEmpty())
			return null;
		return query.getResultList();
	}

}
