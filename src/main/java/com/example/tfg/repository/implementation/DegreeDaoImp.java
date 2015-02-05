package com.example.tfg.repository.implementation;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.example.tfg.domain.Degree;
import com.example.tfg.domain.Subject;
import com.example.tfg.repository.DegreeDao;

@Repository
public class DegreeDaoImp implements DegreeDao {

	protected EntityManager em;

	public EntityManager getEntityManager() {
		return em;
	}

	protected static final Logger logger = LoggerFactory
			.getLogger(DegreeDaoImp.class);

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		try {
			this.em = entityManager;
		} catch (Exception e) {
			logger.error(e.getMessage());

		}
	}

	@Override
	public boolean addDegree(Degree degree) {
		try {
			em.persist(degree);
		} catch (ConstraintViolationException e) {
			logger.error(e.getMessage());
			return false;
		}

		return true;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Degree> getAll() {

		return em.createQuery("select d from Degree d order by d.id")
				.getResultList();

	}

	@Override
	public boolean saveSubject(Degree degree) {
		try {
			em.merge(degree);
		} catch (ConstraintViolationException e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public Degree getDegree(Long id) {

		return em.find(Degree.class, id);
	}

	@Override
	public boolean deleteDegree(Degree degree) {
		// Degree degree = em.getReference(Degree.class, id);
		try {
			degree.setDeleted(true);
			em.merge(degree);

			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Degree> getDegreeByName(Degree degree) {
		Query query = em.createQuery("select d from Degree d where d.name=?1");
		query.setParameter(1, degree.getName());

		return query.getResultList();
	}

	@Override
	public Degree getDegreeSubject(Subject p) {
		Query query = em
				.createQuery("select d from Degree d join d.subjects s where s=?1");
		query.setParameter(1, p);
		if (query.getResultList().isEmpty())
			return null;
		return (Degree) query.getResultList().get(0);
	}

	public String getNextCode() {
		Query query = em.createQuery("Select MAX(e.id ) from Degree e");
		try {
			Long aux = (Long) query.getSingleResult() + 1;
			return "DEG" + aux;
		} catch (Exception e) {
			logger.error(e.getMessage());

			return null;
		}

	}

	public boolean existByCode(String code) {
		Query query = em.createQuery("from Degree d where d.code=?1");
		query.setParameter(1, code);

		if (query.getResultList().isEmpty())
			return false;
		else
			return true;
	}



//	@Override
//	public Degree getDegreeAll(Long id) {
//		Degree degree = em.getReference(Degree.class, id);
//		Query query =
//				em.createQuery("select d from Degree d join d.subjects s join d.competences c where d=?1");
//		query.setParameter(1, degree);
//
//
//		return (Degree) query.getSingleResult();
//	}
}
