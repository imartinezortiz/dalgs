package es.ucm.fdi.dalgs.topic.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.domain.Module;
import es.ucm.fdi.dalgs.domain.Topic;
import es.ucm.fdi.dalgs.module.service.ModuleService;
import es.ucm.fdi.dalgs.subject.service.SubjectService;
import es.ucm.fdi.dalgs.topic.repository.TopicRepository;

@Service
public class TopicService {
	@Autowired
	private TopicRepository daoTopic;

	@Autowired
	private ModuleService serviceModule;

	@Autowired
	private SubjectService serviceSubject;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly=false)	
	public ResultClass<Boolean> addTopic(Topic topic, Long id_module) {

		Topic topicExists = daoTopic.existByCode(topic.getInfo().getCode(), id_module);
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		if( topicExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code already exists");

			if (topicExists.getIsDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");

			}
			result.setE(false);
			result.setErrorsList(errors);
		}
		else{
			topic.setModule(serviceModule.getModule(id_module).getE());
			boolean r = daoTopic.addTopic(topic);
			if (r) 
				result.setE(true);
		}
		return result;		
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly=true)
	public ResultClass<List<Topic>> getAll() {
		ResultClass<List<Topic>> result = new ResultClass<List<Topic>>();
		result.setE(daoTopic.getAll());
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly=false)
	public ResultClass<Boolean> modifyTopic(Topic topic, Long id_topic, Long id_module) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		Topic modifyTopic = daoTopic.getTopic(id_topic);

		Topic topicExists = daoTopic.existByCode(topic.getInfo().getCode(), id_module);

		if(!topic.getInfo().getCode().equalsIgnoreCase(modifyTopic.getInfo().getCode()) && 
				topicExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("New code already exists");

			if (topicExists.getIsDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");

			}
			result.setErrorsList(errors);
			result.setE(false);
		}
		else{
			modifyTopic.setInfo(topic.getInfo());
			boolean r = daoTopic.saveTopic(modifyTopic);
			if (r) 
				result.setE(true);
		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly=true)
	public ResultClass<Topic> getTopic(Long id) {
		ResultClass<Topic> result = new ResultClass<Topic>();
		result.setE(daoTopic.getTopic(id));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly=false)
	public ResultClass<Boolean> deleteTopic(Long id) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		Topic topic = daoTopic.getTopic(id);
		Collection<Topic> topics = new ArrayList<Topic>();
		topics.add(topic);
		if (serviceSubject.deleteSubjectsForTopic(topics).getE()){
			result.setE(daoTopic.deleteTopic(topic));
			return result;
		}
		result.setE(false);

		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly=true)
	public ResultClass<Topic> getTopicAll(Long id_topic) {
		ResultClass<Topic> result = new ResultClass<Topic>();
		Topic p = daoTopic.getTopic(id_topic);
		p.setSubjects(serviceSubject.getSubjectsForTopic(id_topic).getE());
		result.setE(p);
		return result;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly=true)
	public ResultClass<Collection<Topic>> getTopicsForModule(Long id) {
		ResultClass<Collection<Topic>> result = new ResultClass<Collection<Topic>>();
		result.setE(daoTopic.getTopicsForModule(id));
		return result;
	}


	//	@PreAuthorize("hasRole('ROLE_ADMIN')")
	//	@Transactional(readOnly=false)
	////	public boolean modifyTopic(Topic topic) {
	////
	////		return daoTopic.saveTopic(topic);
	////	}
	//	public boolean modifyTopic(Topic topic) {
	//		return daoTopic.saveTopic(topic);
	//	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly=false)
	public ResultClass<Boolean> deleteTopicsForModules(Collection<Module> modules) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		Collection<Topic> topics = daoTopic.getTopicsForModules(modules);

		if(serviceSubject.deleteSubjectsForTopic(topics).getE()){
			result.setE(daoTopic.deleteTopicsForModules(modules));
			return result;
		}
		result.setE(false);
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly=false)
	public ResultClass<Boolean> deleteTopicsForModule(Module module) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		if(!module.getTopics().isEmpty())
			if(serviceSubject.deleteSubjectsForTopic(module.getTopics()).getE()){
				result.setE(daoTopic.deleteTopicsForModule(module));
				return result;
			}
			else result.setE(false);
		else result.setE(true);
		
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = false)
	public ResultClass<Boolean> unDeleteTopic(Topic topic, Long id_module) {
		Topic t = daoTopic.existByCode(topic.getInfo().getCode(), id_module);
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		if(t == null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code doesn't exist");
			result.setErrorsList(errors);

		}
		else{
			if(!t.getIsDeleted()){
				Collection<String> errors = new ArrayList<String>();
				errors.add("Code is not deleted");
				result.setErrorsList(errors);
			}

			t.setDeleted(false);
			t.setInfo(topic.getInfo());
			boolean r = daoTopic.saveTopic(t);
			if(r) 
				result.setE(true);	

		}
		return result;
	}
}

