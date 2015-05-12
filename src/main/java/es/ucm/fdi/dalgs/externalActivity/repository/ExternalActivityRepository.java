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

package es.ucm.fdi.dalgs.externalActivity.repository;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import es.ucm.fdi.dalgs.activity.repository.ActivityRepository;
import es.ucm.fdi.dalgs.domain.Activity;

@Repository
public class ExternalActivityRepository {
	protected EntityManager em;

	protected static final Logger logger = LoggerFactory
			.getLogger(ActivityRepository.class);

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



	public Boolean deleteExternalActivity(Activity externalActivity) {

		externalActivity.setIsDeleted(true);
		try {

//			em.merge(externalActivity);
			 em.remove(externalActivity);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return false;
		}
	}



	@SuppressWarnings("unchecked") 
	// where s.product.category in" +" (select sb.pbyte from SimpleBean sb where sb.pint>=30000)");
	public Collection<? extends Activity> getExternalActivitiesAll() {
	//	Query query = em.createQuery( "select a from Activity  a inner join Course  c  where c = a.course and a in (select ea from Course c join c.external_activities ea)");// union (select * from _activity  a inner join group_external  ge  where ge.id_activity =  a.id_activity)");
		Query query = em.createQuery( "select a from Activity  a inner join a.course  c inner join c.external_activities ea where  a = ea ");
	
		Query query2 = em.createQuery ("select aa from Activity  aa inner join aa.group  g inner join g.external_activities eaa where  aa = eaa)");
		Collection<Activity> col = new ArrayList<Activity>();
		col.addAll(query.getResultList());
		col.addAll(query2.getResultList());
		return col;
	}

	
	
}
