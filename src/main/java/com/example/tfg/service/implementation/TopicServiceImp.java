package com.example.tfg.service.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tfg.domain.Module;
import com.example.tfg.domain.Topic;
import com.example.tfg.repository.TopicDao;
import com.example.tfg.service.ModuleService;
import com.example.tfg.service.SubjectService;
import com.example.tfg.service.TopicService;

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
	public boolean addTopic(Topic topic, Long id_module) {
		Topic existTopic = daoTopic.existByCode(topic.getInfo().getCode());
		Module module = serviceModule.getModule(id_module);
		if(existTopic == null){
			topic.setModule(module);
			module.getTopics().add(topic);
			return daoTopic.addTopic(topic);


		}else if(existTopic.isDeleted()==true){
			existTopic.setInfo(topic.getInfo());
			existTopic.setDeleted(false);
			module.getTopics().add(existTopic);
			return daoTopic.saveTopic(existTopic);

		}
		return false;		
	}

	@PreAuthorize("hasRole('ROLE_USER')")
	@Transactional(readOnly=true)
	public List<Topic> getAll() {
		return daoTopic.getAll();
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly=false)
	public boolean modifyTopic(Topic topic, Long id) {
		Topic topicModify = daoTopic.getTopic(id);
		topicModify.setInfo(topic.getInfo());		

		return daoTopic.saveTopic(topicModify);
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




}
