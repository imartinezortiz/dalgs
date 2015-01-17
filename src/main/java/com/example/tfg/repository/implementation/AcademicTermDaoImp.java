package com.example.tfg.repository.implementation;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.example.tfg.domain.AcademicTerm;
import com.example.tfg.domain.Activity;
import com.example.tfg.domain.Degree;
import com.example.tfg.repository.AcademicTermDao;

@Repository
public class AcademicTermDaoImp implements AcademicTermDao {

	protected EntityManager em;

	public EntityManager getEntityManager() {
		return em;
	}

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		try {
			this.em = entityManager;
		} catch (Exception e) {
		}
	}

	public boolean addAcademicTerm(AcademicTerm academicTerm) {

		try {
			em.persist(academicTerm);
			return true;
		} catch (Exception e) {
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
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	public List<AcademicTerm> getAcademicsTerm(String term) {
		Query query = em
				.createQuery("select a from AcademicTerm a where a.term=?");

		query.setParameter(1, term);

		if (query.getResultList().isEmpty())
			return null;

		return query.getResultList();

	}

	public boolean deleteAcademicTerm(String term) {
		List<AcademicTerm> academicsTerms = getAcademicsTerm(term);

		for (AcademicTerm a : academicsTerms) {
			a.setDeleted(true);
			try {

				em.merge(a);
				// em.remove(academicTerm);
				
			} catch (Exception e) {
				return false;
			}

		}
		return true;
	}

	@SuppressWarnings("unchecked")
	public List<AcademicTerm> getAcademicTermsForDegree(Long id_degree) {
		Degree degree = em.getReference(Degree.class, id_degree);

		Query query = em
				.createQuery("select a from AcademicTerm a where a.degree=?1");
		query.setParameter(1, degree);

		if (query.getResultList().isEmpty())
			return null;

		return query.getResultList();
	}

	public boolean existByTerm(String term) {
		Query query = em.createQuery("from AcademicTerm a where a.term=?1");
		query.setParameter(1, term);

		if (query.getResultList().isEmpty())
			return false;
		else
			return true;
	}

	@SuppressWarnings("unchecked")
	public List<String> getAllTerms() {

		Query query = em.createQuery("select distinct term from AcademicTerm ");
		if (query.getResultList().isEmpty())
			return null;

		return query.getResultList();
	}

	@Override
	public AcademicTerm getAcademicTermById(Long id) {
		
			return em.find(AcademicTerm.class, id);

		
	}

	@Override
	public AcademicTerm getAcademicTermDegree(String term, Long id_degree) {
		Degree degree = em.getReference(Degree.class, id_degree);

		Query query = em
				.createQuery("select a from AcademicTerm a  where a.term=?1 and a.degree=?2");
		query.setParameter(1, term);
		query.setParameter(2, degree);

		if (query.getResultList().isEmpty())
			return null;
		else
			return (AcademicTerm) query.getSingleResult();
	}

}
