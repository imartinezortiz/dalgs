package es.ucm.fdi.dalgs.topic.web;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.ucm.fdi.dalgs.classes.CharsetString;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.classes.UploadForm;
import es.ucm.fdi.dalgs.domain.Topic;
import es.ucm.fdi.dalgs.topic.service.TopicService;

@Controller
public class TopicController {

	@Autowired
	private TopicService serviceTopic;

	private Boolean showAll;

	public Boolean getShowAll() {
		return showAll;
	}

	public void setShowAll(Boolean showAll) {
		this.showAll = showAll;
	}

	/**
	 * methods for adding topics
	 */
	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/topic/add.htm", method = RequestMethod.GET)
	public String addTopicGET(Model model, @PathVariable("degreeId") Long id_degree) {
		if(!model.containsAttribute("topic"))
			model.addAttribute("topic", new Topic());
		model.addAttribute("valueButton", "Add");
		model.addAttribute("typeform", "form.add");
		return "topic/form";
	}

	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/topic/add.htm", method = RequestMethod.POST, params="Add")
	public String addTopicPOST(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("moduleId") Long id_module,
			@ModelAttribute("topic") Topic newTopic,
			BindingResult resultBinding, RedirectAttributes attr, Locale locale) {

		if (!resultBinding.hasErrors()){

			ResultClass<Topic> result = serviceTopic.addTopic(newTopic, id_module, id_degree, locale);
			if (!result.hasErrors())
				return "redirect:/degree/" + id_degree + "/module/" + id_module + ".htm";
			else{

				if (result.isElementDeleted()){
					attr.addFlashAttribute("unDelete", result.isElementDeleted()); 
					attr.addFlashAttribute("topic", result.getSingleElement());
				}else attr.addFlashAttribute("topic", newTopic);
				attr.addFlashAttribute("errors", result.getErrorsList());

			}
		}else{
			attr.addFlashAttribute("topic", newTopic);
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.topic",
					resultBinding);			
		}
		
		return "redirect:/degree/"+ id_degree+"/module/"+ id_module+"/topic/add.htm";
	}
	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/topic/add.htm", method = RequestMethod.POST, params="Undelete")
	// Every Post have to return redirect
	public String undeleteTopic(
			@ModelAttribute("topic") Topic topic, 
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("moduleId") Long id_module,
			BindingResult resultBinding, RedirectAttributes attr, Locale locale) {


		if (!resultBinding.hasErrors()){
			ResultClass<Topic> result = serviceTopic.unDeleteTopic(topic, id_module, locale);


			if (!result.hasErrors()){
				attr.addFlashAttribute("topic", result.getSingleElement());

				return "redirect:/degree/" + id_degree + "/module/" + id_module + "/topic/"+result.getSingleElement().getId()+"/modify.htm";
			}else{

				if (result.isElementDeleted())
					attr.addFlashAttribute("unDelete", true); 
				attr.addFlashAttribute("errors", result.getErrorsList());
		
			}
		}else{
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.topic",
					resultBinding);
		}


		attr.addFlashAttribute("topic", topic);
		return "reidrect:/degree/"+ id_degree+"/module/"+ id_module+"/topic/add.htm";
	}


	/**
	 * Methods for modify topics
	 */
	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/topic/{topicId}/modify.htm", method = RequestMethod.POST)
	public String modifyTopicPOST(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("moduleId") Long id_module,
			@PathVariable("topicId") Long id_topic,
			@ModelAttribute("topic") Topic modify,
			BindingResult resultBinding, RedirectAttributes attr, Locale locale)

	{

		if (!resultBinding.hasErrors()){	
			ResultClass<Boolean> result = serviceTopic.modifyTopic(modify, id_topic, id_module, locale);
			if (!result.hasErrors())
				return "redirect:/degree/" + id_degree + "/module/" + id_module + ".htm";
			else{
				attr.addFlashAttribute("errors", result.getErrorsList());


			}
		}else{
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.topic",
					resultBinding);
		}

		attr.addFlashAttribute("topic", modify);
		return "redirect:/degree/"+id_degree+"/module/"+id_module+"/topic/"+id_topic+"/modify.htm";

	}

	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/topic/{topicId}/modify.htm", method = RequestMethod.GET)
	public String modifyTopicGET(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("moduleId") Long id_module,
			@PathVariable("topicId") Long id_topic,
			Model model)throws ServletException {

		if (!model.containsAttribute("topic")){
			Topic p = serviceTopic.getTopic(id_topic, id_module).getSingleElement();
			model.addAttribute("topic", p);
		}
		model.addAttribute("valueButton", "Modify");
		model.addAttribute("typeform", "form.modify");

		return "topic/form";
	}

	/**
	 * Methods for delete topics
	 */

	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/topic/{topicId}/delete.htm", method = RequestMethod.GET)
	public String deleteTopicGET(@PathVariable("topicId") Long id_topic,
			@PathVariable("moduleId") Long id_module,
			@PathVariable("degreeId") Long id_degree)
					throws ServletException {

		if (serviceTopic.deleteTopic(serviceTopic.getTopic(id_topic, id_module).getSingleElement()).getSingleElement()) {
			return "redirect:/degree/" + id_degree + "/module/"+ id_module + ".htm";
		} else
			return "redirect:/error.htm";
	}

	/**
	 * Methods for view topics
	 */
	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/topic/{topicId}.htm", method = RequestMethod.GET)
	public ModelAndView getTopicGET(@PathVariable("topicId") Long id_topic,
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("moduleId") Long id_module,
			@RequestParam(value = "showAll", defaultValue = "false") Boolean show)
					throws ServletException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		Topic p = serviceTopic.getTopicAll(id_topic, id_module,show).getSingleElement();
		myModel.put("showAll", show);
		myModel.put("topic", p);
		if (p.getSubjects() != null)
			myModel.put("subjects", p.getSubjects());


		return new ModelAndView("topic/view", "model", myModel);
	}
	
	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/topic/{topicId}/restore.htm")
	// Every Post have to return redirect
	public String restoreTopic(@PathVariable("degreeId") Long id_degree,
			@PathVariable("moduleId") Long id_module,
			@PathVariable("topicId") Long id_topic, Locale locale) {
		ResultClass<Topic> result = serviceTopic.unDeleteTopic(serviceTopic.getTopic(id_topic, id_module).getSingleElement(), id_module, locale);
		if (!result.hasErrors())
			return "redirect:/degree/"+id_degree+"/module/"+id_module+".htm";
		else{
			return "redirect:/error.htm";

		}

	}
	
	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/topic/upload.htm", method = RequestMethod.GET)
	public String uploadGet(Model model) {
		CharsetString charsets = new CharsetString();

		model.addAttribute("className", "Topic");
		model.addAttribute("listCharsets", charsets.ListCharsets());
		model.addAttribute("newUpload", new UploadForm("Topic"));
		return "upload";
	}

	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/topic/upload.htm", method = RequestMethod.POST)
	public String uploadPost(
			@ModelAttribute("newUpload") @Valid UploadForm upload,
			BindingResult result, Model model,
			@PathVariable("moduleId") Long id_module,
			@PathVariable("degreeId") Long id_degree){

		if (result.hasErrors() || upload.getCharset().isEmpty()) {
			for (ObjectError error : result.getAllErrors()) {
				System.err.println("Error: " + error.getCode() + " - "
						+ error.getDefaultMessage());
			}
			return "upload";
		}

		if (serviceTopic.uploadCSV(upload, id_module, id_degree))
			return "home";
		else
			return "upload";
	}

}
