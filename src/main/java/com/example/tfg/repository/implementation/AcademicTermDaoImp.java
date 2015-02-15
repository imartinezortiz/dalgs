package com.example.tfg.repository.implementation;

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

import com.example.tfg.domain.AcademicTerm;
import com.example.tfg.domain.Degree;
import com.example.tfg.repository.AcademicTermDao;

@Repository
public class AcademicTermDaoImp implements AcademicTermDao {

	protected EntityManager em;

	private static final Integer noOfRecords = 5;

	protected static final Logger logger = LoggerFactory
			.getLogger(AcademicTermDaoImp.class);

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
			return false;
		} catch (DataIntegrityViolationException e) {
			logger.error(e.getMessage());
			return false;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	/*
	 * @SuppressWarnings("unchecked") public List<AcademicTerm> getAll() {
	 * 
	 * return em.createQuery(
	 * "select a from AcademicTerm a inner join a.degree d order by a.id")
	 * .getResultList(); }
	 */

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
	public List<AcademicTerm> getAcademicsTerm(Integer pageIndex) {// String
																	// term) {
		Query query = em
				.createQuery("select a from AcademicTerm a  where a.isDeleted='false' order by a.term DESC");

		// query.setParameter(1, term);

		if (query.getResultList().isEmpty())
			return null;

		return query.setMaxResults(noOfRecords)
				.setFirstResult(pageIndex * noOfRecords).getResultList();

	}

	public boolean deleteAcademicTerm(Long id_academic) {
		// Degree degree = em.getReference(Degree.class, id_degree);

		AcademicTerm academic = em
				.getReference(AcademicTerm.class, id_academic);

		academic.setDeleted(true);
		try {

			em.merge(academic);
			// em.remove(academicTerm);
			return true;

		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}

	}

	public boolean existByTerm(String term) {
		Query query = em.createQuery("from AcademicTerm a where a.term=?1");
		query.setParameter(1, term);

		if (query.getResultList().isEmpty())
			return false;
		else
			return true;
	}


	@Override
	public AcademicTerm getAcademicTermById(Long id) {

		return em.find(AcademicTerm.class, id);

	}

//	@Override
	/*
	 * public AcademicTerm getAcademicTerm(Long id_academic) { return
	 * em.getReference(AcademicTerm.class, id_academic); }
	 */
//	public boolean exists(AcademicTerm academicTerm) {
//		// Degree degree = em.getReference(Degree.class,
//		// academicTerm.getDegree().getId());
//
//		Query query = em
//				.createQuery("select a from AcademicTerm a where a.degree=?1 and a.term=?2");
//		query.setParameter(1, academicTerm.getDegree());
//		query.setParameter(2, academicTerm.getTerm());
//
//		if (query.getResultList().isEmpty())
//			return false;
//
//		return true;
//	}

//	public Long isDisabled(String term, Degree degree) {
//		// Degree degree = em.getReference(Degree.class, id_degree);
//
//		Query query = em
//				.createQuery("select a from AcademicTerm a where a.degree=?1 and a.term=?2 and a.isDeleted='true'");
//		query.setParameter(1, degree);
//		query.setParameter(2, term);
//
//		if (query.getResultList().isEmpty())
//			return null;
//
//		AcademicTerm aux = (AcademicTerm) query.getSingleResult();
//		return aux.getId();
//	}

//	@Override
//	public boolean existTerm(String term) {
//
//		Query query = em
//				.createQuery("select a from AcademicTerm a where  a.term=?1");
//		query.setParameter(1, term);
//
//		if (query.getResultList().isEmpty())
//			return false;
//
//		return true;
//	}

	@Override
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

	public Integer numberOfPages() {
		Query query =em.createNativeQuery(
				"select count(*) from academicterm where isDeleted='false'");
		logger.info(query.getSingleResult().toString());
		double dou = Double.parseDouble(query.getSingleResult().toString())/ ((double) noOfRecords);
		return (int) Math.ceil(dou);

	}

	@SuppressWarnings("unchecked")
	public List<AcademicTerm> getAcademicTermsByDegree(Long id_degree) {
		Degree degree = em.getReference(Degree.class, id_degree);

		Query query = em
				.createQuery("select a from AcademicTerm a where a.isDeleted='false' and  a.degree=?1");
		query.setParameter(1, degree);

		return query.getResultList();

	}

	@Override
	public AcademicTerm exists(String term, Degree degree) {
		Query query = em
				.createQuery("select a from AcademicTerm a where  a.term=?1 and a.degree = ?2");
		query.setParameter(1, term);
		query.setParameter(2, degree);

		if (query.getResultList().isEmpty())
			return null;
		else return (AcademicTerm)query.getSingleResult();
		
	}


	public boolean deleteAcademicTerm(Collection<AcademicTerm> academicList) {
		Query query = em.createQuery("UPDATE AcademicTerm a SET isDeleted = true WHERE a in ?1");
		query.setParameter(1, academicList);
		int n = query.executeUpdate();
		if (n > 0) return true;
		return false;
	}
}
