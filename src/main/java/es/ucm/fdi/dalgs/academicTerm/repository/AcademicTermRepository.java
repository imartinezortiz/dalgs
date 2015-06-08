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
package es.ucm.fdi.dalgs.academicTerm.repository;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import es.ucm.fdi.dalgs.domain.AcademicTerm;
import es.ucm.fdi.dalgs.domain.Degree;

@Repository
public class AcademicTermRepository {

	protected EntityManager em;

	private static final Integer noOfRecords = 5;

	protected static final Logger logger = LoggerFactory
			.getLogger(AcademicTermRepository.class);

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

	public boolean addAcademicTerm(AcademicTerm academicTerm) {

		try {
			em.persist(academicTerm);
			return true;
		} catch (PersistenceException e) {
			logger.error(e.getMessage());
			return false;
		} catch (DataIntegrityViolationException e) {
			logger.error(e.getMessage());
			return false;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	public boolean saveAcademicTerm(AcademicTerm academicTerm) {
		try {
			em.merge(academicTerm);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());

			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public List<AcademicTerm> getAcademicsTerm(Integer pageIndex,
			Boolean showAll) {// String
		// term) {
		Query query = null;

		if (showAll)
			query = em
					.createQuery("select a from AcademicTerm a  order by a.term DESC");
		else
			query = em
					.createQuery("select a from AcademicTerm a  where a.isDeleted='false' order by a.term DESC");

		if (query.getResultList().isEmpty())
			return null;

		return query.setMaxResults(noOfRecords)
				.setFirstResult(pageIndex * noOfRecords).getResultList();

	}

	public boolean deleteAcademicTerm(Long id_academic) {

		AcademicTerm academic = em
				.getReference(AcademicTerm.class, id_academic);

		academic.setDeleted(true);
		try {

			em.merge(academic);
			return true;

		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}

	}


	public AcademicTerm getAcademicTermById(Long id) {
		return em.find(AcademicTerm.class, id);
	}

	public boolean modifyTerm(String term, String newTerm) {
		Query query = em
				.createQuery("UPDATE AcademicTerm SET term =?1 WHERE term=?2");
		query.setParameter(1, newTerm);
		query.setParameter(2, term);

		query.executeUpdate();
		if (query.executeUpdate() >= 0)
			return true;
		return false;
	}

	public Integer numberOfPages(Boolean showAll) {

		Query query = null;
		if (showAll)
			query = em.createNativeQuery("select count(*) from academicterm");
		else
			query = em
					.createNativeQuery("select count(*) from academicterm where isDeleted='false'");

		logger.info(query.getSingleResult().toString());
		double dou = Double.parseDouble(query.getSingleResult().toString())
				/ ((double) noOfRecords);
		return (int) Math.ceil(dou);

	}

	@SuppressWarnings("unchecked")
	public List<AcademicTerm> getAcademicTermsByDegree(Degree degree) {
		Query query = em
				.createQuery("select a from AcademicTerm a where a.isDeleted='false' and  a.degree=?1");
		query.setParameter(1, degree);

		return (List<AcademicTerm>) query.getResultList();

	}

	public AcademicTerm exists(String term, Degree degree) {
		Query query = null;
		query = em
				.createQuery("select a from AcademicTerm a where  a.term=?1 and a.degree = ?2");
		query.setParameter(1, term);
		query.setParameter(2, degree);

		if (query.getResultList().isEmpty())
			return null;
		else
			return (AcademicTerm) query.getSingleResult();

	}

	public boolean deleteAcademicTerm(Collection<AcademicTerm> academicList) {
		Query query = em
				.createQuery("UPDATE AcademicTerm a SET isDeleted = true WHERE a in ?1");
		query.setParameter(1, academicList);
		int n = query.executeUpdate();
		if (n > 0)
			return true;
		return false;
	}
}