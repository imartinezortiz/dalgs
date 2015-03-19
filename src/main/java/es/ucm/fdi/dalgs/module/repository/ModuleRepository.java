package es.ucm.fdi.dalgs.module.repository;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import es.ucm.fdi.dalgs.domain.Degree;
import es.ucm.fdi.dalgs.domain.Module;

@Repository
public class ModuleRepository {
	protected EntityManager em;

	public EntityManager getEntityManager() {
		return em;
	}

	protected static final Logger logger = LoggerFactory
			.getLogger(ModuleRepository.class);

	@PersistenceContext
	public void setEntityManager(EntityManager entityManager) {
		try {
			this.em = entityManager;
		} catch (Exception e) {
			logger.error(e.getMessage());

		}
	}

	public boolean addModule(Module module) {
		try {
			em.persist(module);
		} catch (ConstraintViolationException e) {
			logger.error(e.getMessage());
			return false;
		}

		return true;
	}


	@SuppressWarnings("unchecked")
	public List<Module> getAll() {
		return em.createQuery("select m from Module m where m.isDeleted = false order by m.id ")
				.getResultList();
	}


	public boolean saveModule(Module module) {
		try {
			em.merge(module);
		} catch (ConstraintViolationException e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}


	public Module getModule(Long id) {
		return em.find(Module.class, id);
	}


	public boolean deleteModule(Module module) {
		try {
			//			Module module = em.getReference(Module.class, id_module);
			module.setDeleted(true);
			em.merge(module);

			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}


	public String getNextCode() {
		// TODO Auto-generated method stub
		return null;
	}


	public Module existByCode(String code, Long id_degree) {
		Degree d = em.getReference(Degree.class, id_degree);
		Query query = em.createQuery("select m from Module m where m.info.code=?1 and m.degree = ?2");
		query.setParameter(1, code);
		query.setParameter(2, d);
		if (query.getResultList().isEmpty())
			return null;
		else return (Module) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")

	public Collection<Module> getModulesForDegree(Long id, Boolean show) {
		Degree degree = em.getReference(Degree.class, id);

		if(!show){
			Query query = em
					.createQuery("select m from Module m where m.degree=?1 and m.isDeleted='false'");
			query.setParameter(1, degree);
			return (List<Module>) query.getResultList();
		}else{
			Query query = em
					.createQuery("select m from Module m where m.degree=?1");
			query.setParameter(1, degree);

			return (List<Module>) query.getResultList();
		}
	}


	public boolean deleteModulesForDegree(Degree d) {
		try {
			Query query = em
					.createQuery("UPDATE Module m SET m.isDeleted = true where m.degree=?1");

			query.setParameter(1, d);
			query.executeUpdate();

		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
		return true;
	}
}
