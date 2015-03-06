package es.ucm.fdi.dalgs.topic.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Repository;

import es.ucm.fdi.dalgs.domain.Module;
import es.ucm.fdi.dalgs.domain.Topic;

@Repository
public interface TopicDao {

	public boolean addTopic(Topic topic);

	public List<Topic> getAll();

	public boolean saveTopic(Topic topic);

	public Topic getTopic(Long id);

	public boolean deleteTopic(Topic topic);


	public String getNextCode();

	public Topic existByCode(String code);

	public Collection<Topic> getTopicsForModule(Long id);

	public Collection<Topic> getTopicsForModules(Collection<Module> modules);

	public boolean deleteTopicsForModules(Collection<Module> modules);

	public boolean deleteTopicsForModule(Module module);

}
