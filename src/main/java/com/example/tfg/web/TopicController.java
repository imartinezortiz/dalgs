package com.example.tfg.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.example.tfg.domain.Topic;
import com.example.tfg.service.TopicService;

@Controller
public class TopicController {

	@Autowired
	private TopicService serviceTopic;
	

	
	/**
	 * methods for adding topics
	 */
	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/topic/add.htm", method = RequestMethod.GET)
	public String getAddNewTopicForm(Model model, @PathVariable("degreeId") Long id_degree) {
		Topic newTopic = new Topic();
		// newDegree.setCode(serviceDegree.getNextCode());

		model.addAttribute("addTopic", newTopic);
		return "topic/add";
	}

	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/topic/add.htm", method = RequestMethod.POST)
	// Every Post have to return redirect
	public String processAddNewTopic(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("moduleId") Long id_module,
			@ModelAttribute("addTopic") Topic newTopic) {
		
		boolean created = serviceTopic.addTopic(newTopic, id_degree);
		if (created)
			return "redirect:/degree/" +id_degree+ ".htm";
		else
			return "redirect:/error.htm";
	}



	/**
	 * Methods for modify topics
	 */
	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/topic/{topicId}/modify.htm", method = RequestMethod.POST)
	public String formModifyTopic(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("moduleId") Long id_module,
			@PathVariable("topicId") Long id_topic,
			@ModelAttribute("modifyTopic") Topic modify)

	{
		// modify.setId(id);
		boolean modified = serviceTopic.modifyTopic(modify, id_topic);
		if (modified)
			return "redirect:/degree/" + id_degree + ".htm";
		else
			return "redirect:/error.htm";
	}

	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/topic/{topicId}/modify.htm", method = RequestMethod.GET)
	protected ModelAndView formModifyTopics(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("moduleId") Long id_module,
			@PathVariable("topicId") Long id_topic)
			throws ServletException {
		
		ModelAndView model = new ModelAndView();
		Topic p = serviceTopic.getTopic(id_topic);
		model.addObject("modifyTopic", p);
		model.setViewName("topic/modify");

		return model;
	}

	/**
	 * Methods for delete topics
	 */

	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/topic/{topicId}/delete.htm", method = RequestMethod.GET)
	public String formDeleteTopics(@PathVariable("topicId") Long id_topic,
						@PathVariable("moduleId") Long id_module,
						@PathVariable("degreeId") Long id_degree)
						throws ServletException {

		if (serviceTopic.deleteTopic(id_topic)) {
			return "redirect:/degree/" + id_degree + ".htm";
		} else
			return "redirect:/error.htm";
	}

	/**
	 * Methods for view topics
	 */
	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/topic/{topicId}.htm", method = RequestMethod.GET)
	protected ModelAndView formViewTopic(@PathVariable("topicId") Long id_topic,
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("moduleId") Long id_module)
					throws ServletException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		// Degree p = serviceDegree.getDegree(id);

		Topic p = serviceTopic.getTopicAll(id_topic, id_degree);

		myModel.put("topic", p);
		if (p.getSubjects() != null)
			myModel.put("topics", p.getSubjects());
		

		return new ModelAndView("topic/view", "model", myModel);
	}
	
}
