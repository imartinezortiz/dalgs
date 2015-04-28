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
package es.ucm.fdi.dalgs.competence.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import es.ucm.fdi.dalgs.domain.Competence;
import es.ucm.fdi.dalgs.domain.Degree;
import es.ucm.fdi.dalgs.domain.Subject;

@Repository
public class CompetenceRepository {
	protected EntityManager em;

	protected static final Logger logger = LoggerFactory
			.getLogger(CompetenceRepository.class);

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

	public Competence existByCode(String code, Degree degree) {
		Query query = em
				.createQuery("Select c from Competence c where c.info.code=?1 and c.degree=?2");
		query.setParameter(1, code);
		query.setParameter(2, degree);

		if (query.getResultList().isEmpty())
			return null;
		else
			return (Competence) query.getSingleResult();
	}

	public boolean addCompetence(Competence competence) {

		try {
			em.persist(competence);
			return true;
		} catch (ConstraintViolationException e) {
			logger.error(e.getMessage());
			return false;
		}

	}

	@SuppressWarnings("unchecked")
	public List<Competence> getAll() {
		return em
				.createQuery(
						"select c from Competence c inner join c.degree where c.isDeleted='false' order by c.id")

				.getResultList();
	}

	public boolean saveCompetence(Competence competence) {
		try {
			em.merge(competence);
			return true;
		} catch (ConstraintViolationException e) {
			return false;
		}
	}

	public Competence getCompetence(Long id, Long id_degree) {
		Query query = null;

		Degree degree = em.getReference(Degree.class, id_degree);
		query = em
				.createQuery("select c from Competence c where c.id=?1 and c.degree=?2 ");
		query.setParameter(1, id);
		query.setParameter(2, degree);

		if (query.getResultList().isEmpty())
			return null;

		return (Competence) query.getSingleResult();

	}

	public Competence getCompetenceFormatter(Long id) {
		return em.find(Competence.class, id);

	}

	public boolean deleteCompetence(Competence competence) {
		// Competence competence = em.getReference(Competence.class,
		// competence2);
		// Competence competence = this.getCompetence(id);
		competence.setDeleted(true);

		try {
			em.merge(competence);

			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}

	}

	@SuppressWarnings("unchecked")
	public List<Competence> getCompetencesForSubject(Long id_subject) {
		Subject subject = em.getReference(Subject.class, id_subject);

		Query query = em
				.createQuery("select c from Subject s join s.competences c where s = ?1 and c.isDeleted='false'");
		query.setParameter(1, subject);

		return (List<Competence>) query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Competence> getCompetencesForDegree(Long id_degree, Boolean show) {
		Degree degree = em.getReference(Degree.class, id_degree);
		if (show) {

			Query query = em
					.createQuery("select c from Competence c where c.degree=?1");
			query.setParameter(1, degree);
			return query.getResultList();
		} else {
			Query query = em
					.createQuery("select c from Competence c where c.degree=?1 and c.isDeleted='false'");
			query.setParameter(1, degree);
			return query.getResultList();
		}
	}

	public Competence getCompetenceByName(String name) {
		Query query = em
				.createQuery("select c from Competence c where c.info.name=?1");
		query.setParameter(1, name);

		return (Competence) query.getResultList().get(0);

	}

	public boolean deleteCompetencesForDegree(Degree degree) {

		try {
			Query query = em
					.createQuery("UPDATE Competence c SET c.isDeleted = true where c.degree=?1");

			query.setParameter(1, degree);
			query.executeUpdate();

		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}

	public boolean persistListCompetences(List<Competence> competences) {
		int i = 0;
		for (Competence c : competences) {
			try {

				// In this case we have to hash the password (SHA-256)
				// StringSHA sha = new StringSHA();
				// String pass = sha.getStringMessageDigest(u.getPassword());
				// u.setPassword(pass);

				c.setId(null); // If not a detached entity is passed to persist
				em.persist(c);
				// em.flush();

				if (++i % 20 == 0) {
					em.flush();
				}
			} catch (Exception e) {
				logger.error(e.getMessage());
				return false;
			}
		}

		return true;

	}

}
