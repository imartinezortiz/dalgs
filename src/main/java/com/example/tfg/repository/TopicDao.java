package com.example.tfg.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.example.tfg.domain.Topic;

@Repository
public interface TopicDao {

	public boolean addTopic(Topic topic);

	public List<Topic> getAll();

	public boolean saveTopic(Topic topic);

	public Topic getTopic(Long id);

	public boolean deleteTopic(Long id_topic);


	public String getNextCode();

	public Topic existByCode(String code);

}
