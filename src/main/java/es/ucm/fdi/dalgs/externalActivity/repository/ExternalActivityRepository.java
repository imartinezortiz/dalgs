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

package es.ucm.fdi.dalgs.externalActivity.repository;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import es.ucm.fdi.dalgs.activity.repository.ActivityRepository;
import es.ucm.fdi.dalgs.domain.AcademicTerm;
import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.domain.ExternalActivity;
import es.ucm.fdi.dalgs.domain.Group;

@Repository
public class ExternalActivityRepository {
	protected EntityManager em;

	protected static final Logger logger = LoggerFactory
			.getLogger(ActivityRepository.class);

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

	@SuppressWarnings("unchecked")
	public Collection<? extends ExternalActivity> getExternalActivitiesForGroup(
			long id_group) {
		Group group = em.getReference(Group.class, id_group);
		Query query = em.createQuery("from ExternalActivity ea where ea.group=?1 and ea.isDeleted='false'");

		query.setParameter(1, group);
		return (Collection<ExternalActivity>)query.getResultList();
	}

	public boolean addActivity(ExternalActivity act) {
	
		try {
			em.persist(act);
			return true;
		} catch (ConstraintViolationException e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	public ExternalActivity existByCode(String code) {
		Query query = em
				.createQuery("Select a from ExternalActivity a where a.info.code=?1 and a.isDeleted='false'");
		query.setParameter(1, code);

		if (query.getResultList().isEmpty())
			return null;
		else
			return (ExternalActivity) query.getSingleResult();
	}

	public ExternalActivity getExternalActivity(Long id_externalActivity,
			Long id_course, Long id_group, Long id_academic) {
		
		Query query = null;
		Course course = em.getReference(Course.class, id_course);
		AcademicTerm academicterm = em.getReference(AcademicTerm.class,
				id_academic);

		if (id_group != null) {
			Group group = em.getReference(Group.class, id_group);
			query = em
					.createQuery("select a from ExternalActivity a where a.id=?1 and a.group=?2 and a.group.course=?3 and a.group.course.academicTerm=?4");
			query.setParameter(1, id_externalActivity);
			query.setParameter(2, group);
			query.setParameter(3, course);
			query.setParameter(4, academicterm);
		} else if (id_course != null) {
			query = em
					.createQuery("select a from ExternalActivity a where a.id=?1 and a.course=?2 and a.course.academicTerm=?3 ");
			query.setParameter(1, id_externalActivity);
			query.setParameter(2, course);
			query.setParameter(3, academicterm);

		}

		if (query.getResultList().isEmpty())
			return null;

		return (ExternalActivity) query.getSingleResult();
		
		
	}


	public boolean saveExternalActivity(ExternalActivity externalActivity) {
		try {
			em.merge(externalActivity);
			return true;
		} catch (ConstraintViolationException e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	public Boolean deleteExternalActivity(Long id_externalEexternalActivity) {
		ExternalActivity externalActivity = em.getReference(ExternalActivity.class, id_externalEexternalActivity);
//		externalActivity.getLearningGoalStatus().clear();
		externalActivity.setIsDeleted(true);
		try {

			em.merge(externalActivity);
			// em.remove(activity);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public Collection<? extends ExternalActivity> getExternalActivitiesForCourse(
			Course c) {
	
		Query query = em.createQuery("from ExternalActivity ea where ea.course=?1 and ea.isDeleted='false'");

		query.setParameter(1, c);
		return (Collection<ExternalActivity>)query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public Collection<? extends ExternalActivity> getExternalActivitiesAll() {
		Query query = em.createQuery("select ea from ExternalActivity ea where ea.isDeleted='false'");
		return (Collection<ExternalActivity>)query.getResultList();
	}
	
	
}
