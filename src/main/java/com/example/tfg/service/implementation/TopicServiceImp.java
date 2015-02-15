package com.example.tfg.service.implementation;

import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.tfg.domain.Module;
import com.example.tfg.domain.Topic;
import com.example.tfg.repository.TopicDao;
import com.example.tfg.service.ModuleService;
import com.example.tfg.service.TopicService;

@Service
public class TopicServiceImp implements TopicService {
	@Autowired
	private TopicDao daoTopic;
	
	@Autowired
	private ModuleService serviceModule;
	
	@Transactional(readOnly=false)
	public boolean addTopic(Topic topic, Long id_module) {
		Topic existTopic = daoTopic.existByCode(topic.getInfo().getCode());
		Module module = serviceModule.getModule(id_module);
		if(existTopic == null){
			topic.setModule(module);
			module.getTopics().add(topic);
			if(daoTopic.addTopic(topic))
				return serviceModule.modifyModule(module);
		
				
		}else if(existTopic.isDeleted()==true){
			existTopic.setInfo(topic.getInfo());
			existTopic.setDeleted(false);
			module.getTopics().add(existTopic);
			if(daoTopic.saveTopic(existTopic))
				return serviceModule.modifyModule(module);
	
		}
		return false;		
	}

	@Transactional(readOnly=true)
	public List<Topic> getAll() {
		return daoTopic.getAll();
	}

	@Transactional(readOnly=false)
	public boolean modifyTopic(Topic modify, Long id) {
		Topic topic = daoTopic.getTopic(id);
		topic.setInfo(modify.getInfo());

		

		return daoTopic.saveTopic(topic);
	}

	@Transactional(readOnly=true)
	public Topic getTopic(Long id) {
		return daoTopic.getTopic(id);
	}

	@Transactional(readOnly=false)
	public boolean deleteTopic(Long id) {
		return daoTopic.deleteTopic(id);
	}

	@Transactional(readOnly=true)
	public Topic getTopicAll(Long id_topic, Long id_module) {
		
		Topic p = daoTopic.getTopic(id_topic);
		Module d = serviceModule.getModule(id_module);
		p.setModule(d);
		return p;
	}

	@Override
	public Collection<Topic> getTopicsForModule(Long id) {
		
		return daoTopic.getTopicsForModule(id);
	}
}
