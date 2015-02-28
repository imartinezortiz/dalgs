package com.example.tfg.service;

import java.util.Collection;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.tfg.classes.ResultClass;
import com.example.tfg.domain.Module;
import com.example.tfg.domain.Topic;

@Service
public interface TopicService {
	public ResultClass<Boolean> addTopic(Topic newTopic, Long id_module);

	public List<Topic> getAll();

	public ResultClass<Boolean> modifyTopic(Topic modify, Long id);

	public Topic getTopic(Long id);

	public boolean deleteTopic(Long id);

	public Topic getTopicAll(Long id_topic);

	public Collection<Topic> getTopicsForModule(Long id);

//	public boolean modifyTopic(Topic topic);

	public boolean deleteTopicsForModules(Collection<Module> modules);

	public boolean deleteTopicsForModule(Module module);
	
	public ResultClass<Boolean> unDeleteTopic(Topic topic);
}
