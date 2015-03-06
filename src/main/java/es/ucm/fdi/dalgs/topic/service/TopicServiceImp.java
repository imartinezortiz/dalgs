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
import es.ucm.fdi.dalgs.topic.repository.TopicDao;

@Service
public class TopicServiceImp implements TopicService {
	@Autowired
	private TopicDao daoTopic;

	@Autowired
	private ModuleService serviceModule;

	@Autowired
	private SubjectService serviceSubject;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly=false)	
	public ResultClass<Boolean> addTopic(Topic topic, Long id_module) {
		
		Topic topicExists = daoTopic.existByCode(topic.getInfo().getCode());
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		if( topicExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code already exists");

			if (topicExists.isDeleted()){
				result.setElementDeleted(true);
				errors.add("Element is deleted");

			}
			result.setE(false);
			result.setErrorsList(errors);
		}
		else{
			topic.setModule(serviceModule.getModule(id_module));
			boolean r = daoTopic.addTopic(topic);
			if (r) 
				result.setE(true);
		}
		return result;		
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly=true)
	public List<Topic> getAll() {
		return daoTopic.getAll();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly=false)
	public ResultClass<Boolean> modifyTopic(Topic topic, Long id) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();

		Topic modifyTopic = daoTopic.getTopic(id);
		
		Topic topicExists = daoTopic.existByCode(topic.getInfo().getCode());
		
		if(!topic.getInfo().getCode().equalsIgnoreCase(modifyTopic.getInfo().getCode()) && 
				topicExists != null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("New code already exists");

			if (topicExists.isDeleted()){
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
	public Topic getTopic(Long id) {
		return daoTopic.getTopic(id);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly=false)
	public boolean deleteTopic(Long id) {
		Topic topic = daoTopic.getTopic(id);
		Collection<Topic> topics = new ArrayList<Topic>();
		topics.add(topic);
		if (serviceSubject.deleteSubjectsForTopic(topics))
			return daoTopic.deleteTopic(topic);
		return false;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly=true)
	public Topic getTopicAll(Long id_topic) {

		Topic p = daoTopic.getTopic(id_topic);
		p.setSubjects(serviceSubject.getSubjectsForTopic(id_topic));

		return p;
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly=true)
	public Collection<Topic> getTopicsForModule(Long id) {

		return daoTopic.getTopicsForModule(id);
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
	public boolean deleteTopicsForModules(Collection<Module> modules) {
		Collection<Topic> topics = daoTopic.getTopicsForModules(modules);
		if(serviceSubject.deleteSubjectsForTopic(topics))
			return daoTopic.deleteTopicsForModules(modules);
		return false;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly=false)
	public boolean deleteTopicsForModule(Module module) {

		if(serviceSubject.deleteSubjectsForTopic(module.getTopics()))
			return daoTopic.deleteTopicsForModule(module);
		return false;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")	
	@Transactional(readOnly = false)
	public ResultClass<Boolean> unDeleteTopic(Topic topic) {
		Topic t = daoTopic.existByCode(topic.getInfo().getCode());
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		if(t == null){
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add("Code doesn't exist");
			result.setErrorsList(errors);

		}
		else{
			if(!t.isDeleted()){
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
