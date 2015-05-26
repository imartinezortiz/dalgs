package es.ucm.fdi.dalgs.mailbox.respository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.sun.mail.handlers.message_rfc822;

import es.ucm.fdi.dalgs.domain.MessageBox;

@Repository
public class MessageBoxRepository {

	protected EntityManager em;
	protected static final Logger logger = LoggerFactory
			.getLogger(MessageBoxRepository.class);

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
	public MessageBox getMessageBox (String from, String to){
		
		Query query = null;

		query = em
				.createQuery("select m FROM MessageBox m where m.from=?1 and m.to=?2");
		query.setParameter(1, from);
		query.setParameter(2, to);
		
		if(query.getResultList().isEmpty())
			return null;
		else return (MessageBox) query.getSingleResult();
	}
}
