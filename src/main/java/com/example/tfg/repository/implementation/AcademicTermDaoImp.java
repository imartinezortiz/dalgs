package com.example.tfg.repository.implementation;

import java.util.List;
import java.util.logging.Logger;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.transaction.RollbackException;

import org.springframework.dao.DataIntegrityViolationException;
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
		} catch (PersistenceException e) {
			return false;
		 } catch (DataIntegrityViolationException e) {
	        //LOGGER.info("unable to remove channel, try to close the channel.", e);
	        return false;
	    }catch (Exception e) {
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
	public List<AcademicTerm> getAcademicsTerm(){//String term) {
		Query query = em
				.createQuery("select a from AcademicTerm a order by a.term DESC");

		//query.setParameter(1, term);

		if (query.getResultList().isEmpty())
			return null;

		return query.getResultList();

	}

	/*public boolean deleteTerm(String term) {
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
	*/
	public boolean deleteAcademicTerm(Long id_academic) {
		//Degree degree = em.getReference(Degree.class, id_degree);

		AcademicTerm academic =  em.getReference(AcademicTerm.class, id_academic);

		
			academic.setDeleted(true);
			try {

				em.merge(academic);
				// em.remove(academicTerm);
				return true;
				
			} catch (Exception e) {
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

/*	@SuppressWarnings("unchecked")
	public List<String> getAllTerms() {

		Query query = em.createQuery("select distinct term from AcademicTerm ");
		if (query.getResultList().isEmpty())
			return null;

		return query.getResultList();
	}
*/
	@Override
	public AcademicTerm getAcademicTermById(Long id) {
		
			return em.find(AcademicTerm.class, id);

		
	}

	@Override
	public AcademicTerm getAcademicTerm(Long id_academic) {
		return  em.getReference(AcademicTerm.class, id_academic);
	}

	
	public boolean exists(String term, Long id_degree) {
		Degree degree = em.getReference(Degree.class, id_degree);

		Query query = em
				.createQuery("select a from AcademicTerm a where a.degree=?1 and a.term=?2");
		query.setParameter(1, degree);
		query.setParameter(2, term);


		if (query.getResultList().isEmpty())
			return false;

		return true;
	}

	public Long isDisabled(String term, Long id_degree) {
		Degree degree = em.getReference(Degree.class, id_degree);

		Query query = em
				.createQuery("select a from AcademicTerm a where a.degree=?1 and a.term=?2 and a.isDeleted=1");
		query.setParameter(1, degree);
		query.setParameter(2, term);


		if (query.getResultList().isEmpty())
			return null;

		AcademicTerm aux = (AcademicTerm) query.getSingleResult();
		return aux.getId();
	}
	
	@Override
	public boolean existTerm(String term) {

		Query query = em
				.createQuery("select a from AcademicTerm a where  a.term=?1");
		query.setParameter(1, term);


		if (query.getResultList().isEmpty())
			return false;

		return true;
	}

	@Override
	public boolean modifyTerm(String term, String newTerm) {
		Query query = em
				.createQuery("UPDATE AcademicTerm SET term =?1 WHERE term=?2");
		query.setParameter(1, newTerm);
		query.setParameter(2, term);

		query.executeUpdate();
		if (query.executeUpdate() >=0)
			return true;
		return false;
	}
}
