package com.example.tfg.repository.implementation;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import com.example.tfg.domain.AcademicTerm;
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

	@SuppressWarnings("unchecked")
	public List<AcademicTerm> getAll() {

		return em.createQuery(
				"select a from AcademicTerm a inner join a.degree d order by a.id")
				.getResultList();
	}

	public boolean saveAcademicTerm(AcademicTerm academicTerm) {
		try {
			em.merge(academicTerm);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public AcademicTerm getAcademicTerm(Long id) {
		return em.find(AcademicTerm.class, id);

	}

	public boolean deleteAcademicTerm(Long id) {
		AcademicTerm academicTerm = em.getReference(AcademicTerm.class, id);
		academicTerm.setDeleted(true);
		try {
			
			em.merge(academicTerm);
			//em.remove(academicTerm);
			return true;
		} catch (Exception e) {
			return false;
		}

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

	public boolean existByCode(String term) {
		Query query = em.createQuery("from AcademicTerm a where a.term=?1");
		query.setParameter(1, term);

		if (query.getResultList().isEmpty())
			return false;
		else
			return true;
	}






}
