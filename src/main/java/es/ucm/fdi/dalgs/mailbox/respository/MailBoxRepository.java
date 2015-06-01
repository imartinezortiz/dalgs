package es.ucm.fdi.dalgs.mailbox.respository;

import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;





import es.ucm.fdi.dalgs.domain.MessageBox;

@Repository
public class MailBoxRepository {

	protected EntityManager em;
	protected static final Logger logger = LoggerFactory
			.getLogger(MailBoxRepository.class);

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
	
	public boolean addMessageBox(MessageBox messageBox){
		
		try{
			em.persist(messageBox);
			return true;
		}catch(PersistenceException e){
			logger.debug("can't create");
			return false;
			
		}
		
	}
	public MessageBox getMessageBox (String code){
		
		Query query = null;

		query = em
				.createQuery("select m FROM MessageBox m where m.code=?1");
		query.setParameter(1, code);
		
		if(query.getResultList().isEmpty())
			return null;
		else return (MessageBox) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public Collection<? extends MessageBox> getAllMessages() {

		Query query = null;

		query = em
				.createQuery("FROM MessageBox m");
		
		
		return query.getResultList();
	}
}
