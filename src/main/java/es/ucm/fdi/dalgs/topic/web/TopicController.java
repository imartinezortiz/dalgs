package es.ucm.fdi.dalgs.topic.web;

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

import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.domain.Topic;
import es.ucm.fdi.dalgs.topic.service.TopicService;

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
			@ModelAttribute("addTopic") Topic newTopic,
			Model model) {
		
		ResultClass<Boolean> result = serviceTopic.addTopic(newTopic, id_module);
		if (!result.hasErrors())
//		if (created)
			return "redirect:/degree/" + id_degree + "/module/" + id_module + ".htm";
		else{
			model.addAttribute("addTopic", newTopic);
			if (result.isElementDeleted())
				model.addAttribute("unDelete", result.isElementDeleted()); 
			model.addAttribute("errors", result.getErrorsList());
			return "topic/add";
		}
	}
	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/topic/add.htm", method = RequestMethod.POST, params="Undelete")
	// Every Post have to return redirect
	public String undeleteDegree(
			@ModelAttribute("addTopic") Topic topic, 
			Model model,
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("moduleId") Long id_module) {
		
		ResultClass<Boolean> result = serviceTopic.unDeleteTopic(topic);
		
		if (!result.hasErrors())
//		if (created)
			return "redirect:/degree/" + id_degree + "/module/" + id_module + ".htm";
		else{
			model.addAttribute("addTopic", topic);
			if (result.isElementDeleted())
				model.addAttribute("unDelete", true); 
			model.addAttribute("errors", result.getErrorsList());
			return "topic/add";
		}
	}


	/**
	 * Methods for modify topics
	 */
	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/topic/{topicId}/modify.htm", method = RequestMethod.POST)
	public String formModifyTopic(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("moduleId") Long id_module,
			@PathVariable("topicId") Long id_topic,
			@ModelAttribute("modifyTopic") Topic modify,
			Model model)

	{
		ResultClass<Boolean> result = serviceTopic.modifyTopic(modify, id_topic);
		if (!result.hasErrors())
//			if (created)
				return "redirect:/degree/" + id_degree + "/module/" + id_module + ".htm";
			else{
				model.addAttribute("modifyTopic", modify);
				if (result.isElementDeleted()){
					model.addAttribute("addTopic", modify);
					model.addAttribute("unDelete", true); 
					model.addAttribute("errors", result.getErrorsList());
					return "topic/add";
				}	
				model.addAttribute("errors", result.getErrorsList());
				return "topic/modify";
			}
			
		
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
			return "redirect:/degree/" + id_degree + "/module/"+ id_module + ".htm";
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

		Topic p = serviceTopic.getTopicAll(id_topic);

		myModel.put("topic", p);
		if (p.getSubjects() != null)
			myModel.put("subjects", p.getSubjects());
		

		return new ModelAndView("topic/view", "model", myModel);
	}
	
}
