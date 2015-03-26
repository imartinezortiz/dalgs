package es.ucm.fdi.dalgs.module.web;


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
import es.ucm.fdi.dalgs.domain.Module;
import es.ucm.fdi.dalgs.module.service.ModuleService;

@Controller
public class ModuleController {
	@Autowired
	private ModuleService serviceModule;


	private Boolean showAll;

	public Boolean getShowAll() {
		return showAll;
	}

	public void setShowAll(Boolean showAll) {
		this.showAll = showAll;
	}



	/**
	 * methods for adding modules
	 */
	@RequestMapping(value = "/degree/{degreeId}/module/add.htm", method = RequestMethod.GET)
	public String addModuleGET(Model model, @PathVariable("degreeId") Long id_degree) {

		if(!model.containsAttribute("module"))
			model.addAttribute(new Module());
		model.addAttribute("valueButton", "Add");
		model.addAttribute("typeform", "form.add");
		return "module/form";
	}

	@RequestMapping(value = "/degree/{degreeId}/module/add.htm", method = RequestMethod.POST, params="Add")
	// Every Post have to return redirect
	public String addModulePOST(
			@PathVariable("degreeId") Long id_degree,
			@ModelAttribute("module") Module newModule,
			BindingResult resultBinding, RedirectAttributes attr, Locale locale) {

		if (!resultBinding.hasErrors()){
			ResultClass<Module> result = serviceModule.addModule(newModule, id_degree, locale);
			if (!result.hasErrors())
				return "redirect:/degree/" +id_degree+ ".htm";		
			else{
				if (result.isElementDeleted()){
					attr.addFlashAttribute("unDelete", result.isElementDeleted()); 
					attr.addFlashAttribute("module", result.getSingleElement());
				}
				else attr.addFlashAttribute("module", newModule);
				
				attr.addFlashAttribute("errors", result.getErrorsList());
			}
		}else{
			attr.addFlashAttribute("module", newModule);
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.module",
					resultBinding);

		}
		return "redirect:/degree/"+id_degree+"/module/add.htm";
	}


	@RequestMapping(value = "/degree/{degreeId}/module/add.htm", method = RequestMethod.POST, params="Undelete")
	// Every Post have to return redirect
	public String undeleteModulePOST(
			@ModelAttribute("module") Module module,
			@PathVariable("degreeId") Long id_degree,
			BindingResult resultBinding, RedirectAttributes attr, Locale locale) {

		if (!resultBinding.hasErrors()){
			ResultClass<Module> result = serviceModule.unDeleteModule(module, id_degree, locale);

			if (!result.hasErrors()){
				attr.addFlashAttribute("module", result.getSingleElement());
				return "redirect:/degree/" + id_degree +  "/module/" + result.getSingleElement().getId() + "/modify.htm";
			
			}else{
				if (result.isElementDeleted())
					attr.addAttribute("unDelete", true); 

				attr.addFlashAttribute("errors", result.getErrorsList());

			}
		}else{
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.module",
					resultBinding);

		}
		attr.addFlashAttribute("module", module);
		return "redirect:/degree/"+id_degree+"/module/add.htm";
	}


	/**
	 * Methods for modify modules
	 */
	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/modify.htm", method = RequestMethod.POST)
	public String modifyModulePOST(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("moduleId") Long id_module,
			@ModelAttribute("module") Module modify,
			BindingResult resultBinding, RedirectAttributes attr, Locale locale)

	{
		if (!resultBinding.hasErrors()){
			ResultClass<Boolean> result = serviceModule.modifyModule(modify, id_module, id_degree, locale);
			if (!result.hasErrors())

				return "redirect:/degree/" + id_degree + ".htm";
			else{ 
				attr.addFlashAttribute("errors", result.getErrorsList());
			}
		}
		else{
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.module",
					resultBinding);

		}
		attr.addFlashAttribute("module", modify);
		return "redirect:/degree/"+id_degree+"/module/"+id_module+"/modify.htm";

	}

	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/modify.htm", method = RequestMethod.GET)
	protected String modifyModuleGET(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("moduleId") Long id_module,
			Model model)
					throws ServletException {

		//		ModelAndView model = new ModelAndView();
		if (!model.containsAttribute("module")){
			Module p = serviceModule.getModule(id_module).getSingleElement();
			model.addAttribute("module", p);
		}	
		model.addAttribute("valueButton", "Modify");
		model.addAttribute("typeform", "form.modify");

		return "module/form";
	}

	/**
	 * Methods for delete modules
	 */

	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/delete.htm", method = RequestMethod.GET)
	public String deleteModuleGET(@PathVariable("moduleId") Long id_module,
			@PathVariable("degreeId") Long id_degree)
					throws ServletException {

		if (serviceModule.deleteModule(serviceModule.getModule(id_module).getSingleElement()
				).getSingleElement()) {
			return "redirect:/degree/" + id_degree + ".htm";
		} else
			return "redirect:/error.htm";
	}

	/**
	 * Methods for view modules
	 */
	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}.htm", method = RequestMethod.GET)
	protected ModelAndView getModuleGET(@PathVariable("moduleId") Long id_module,
			@PathVariable("degreeId") Long id_degree,
			@RequestParam(value = "showAll", defaultValue = "false") Boolean show)
					throws ServletException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		Module p = serviceModule.getModuleAll(id_module, show).getSingleElement();
		myModel.put("showAll", show);
		myModel.put("module", p);
		if (p.getTopics() != null)
			myModel.put("topics", p.getTopics());

		return new ModelAndView("module/view", "model", myModel);
	}
	
	@RequestMapping(value = "/degree/{degreeId}/module/{moduleId}/restore.htm")
	// Every Post have to return redirect
	public String restoreModule(@PathVariable("degreeId") Long id_degree,
			@PathVariable("moduleId") Long id_module, Locale locale) {
		ResultClass<Module> result = serviceModule.unDeleteModule(serviceModule.getModule(id_module).getSingleElement(), id_degree, locale);
		if (!result.hasErrors())
			return "redirect:/degree/"+id_degree+".htm";
		else{
			return "redirect:/error.htm";

		}

	}
	
	@RequestMapping(value = "/degree/{degreeId}/module/upload.htm", method = RequestMethod.GET)
	public String uploadGet(Model model) {
		CharsetString charsets = new CharsetString();

		model.addAttribute("className", "Module");
		model.addAttribute("listCharsets", charsets.ListCharsets());
		model.addAttribute("newUpload", new UploadForm("Module"));
		return "upload";
	}

	@RequestMapping(value = "/degree/{degreeId}/module/upload.htm", method = RequestMethod.POST)
	public String uploadPost(
			@ModelAttribute("newUpload") @Valid UploadForm upload,
			BindingResult result, Model model,
			@PathVariable("degreeId") Long id_degree) {

		if (result.hasErrors() || upload.getCharset().isEmpty()) {
			for (ObjectError error : result.getAllErrors()) {
				System.err.println("Error: " + error.getCode() + " - "
						+ error.getDefaultMessage());
			}
			return "upload";
		}

		if (serviceModule.uploadCSV(upload, id_degree))
			return "home";
		else
			return "upload";
	}
	
	

}
