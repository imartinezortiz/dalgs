package es.ucm.fdi.dalgs.academicTerm.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import es.ucm.fdi.dalgs.academicTerm.service.AcademicTermService;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.course.service.CourseService;
import es.ucm.fdi.dalgs.degree.service.DegreeService;
import es.ucm.fdi.dalgs.domain.AcademicTerm;

@Controller
public class AcademicTermController {

	@Autowired
	private AcademicTermService serviceAcademicTerm;

	@Autowired
	private DegreeService serviceDegree;

	@Autowired
	private CourseService serviceCourse;



	private Boolean showAll;

	public Boolean getShowAll() {
		return showAll;
	}

	public void setShowAll(Boolean showAll) {
		this.showAll = showAll;
	}


	/**
	 * Methods for adding academicTerms
	 */

	/**
	 * Add AcademicTerm
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "/academicTerm/add.htm", method = RequestMethod.GET)
	protected String getAddNewAcademicTermForm(Model model) {

		if (!model.containsAttribute("addAcademicTerm"))
			model.addAttribute("addAcademicTerm", new AcademicTerm());
		model.addAttribute("degrees", serviceDegree.getAll());


		return "academicTerm/add";

	}

	/**
	 * Add AcademicTerm GET
	 * @param newAcademicTerm
	 * @param resultBinding
	 * @param attr
	 * @return String
	 */
	@RequestMapping(value = "/academicTerm/add.htm", method = RequestMethod.POST, params="Add")
	// Every Post have to return redirect
	public String processAddNewAcademicTerm(
			@ModelAttribute("addAcademicTerm") @Valid AcademicTerm newAcademicTerm,
			BindingResult resultBinding,
			RedirectAttributes attr) {


		if (!resultBinding.hasErrors()) {

			ResultClass<AcademicTerm> resultReturned = serviceAcademicTerm.addAcademicTerm(newAcademicTerm);
			if (resultReturned.hasErrors()){

				attr.addFlashAttribute("addAcademicTerm", newAcademicTerm);

				if (resultReturned.isElementDeleted()){

					attr.addFlashAttribute("unDelete", resultReturned.isElementDeleted());
					attr.addFlashAttribute("addAcademicTerm", resultReturned.getE());
				}
				attr.addFlashAttribute("idDegree", newAcademicTerm.getDegree().getId());
				attr.addFlashAttribute("errors", resultReturned.getErrorsList());

				return "redirect:/academicTerm/add.htm";
			}
			else return "redirect:/academicTerm/page/0.htm?showAll";

		}
		else{
			// Write the binding result errors on the view

			attr.addFlashAttribute("org.springframework.validation.BindingResult.addAcademicTerm", resultBinding);
			attr.addFlashAttribute("addAcademicTerm", newAcademicTerm);
			if (newAcademicTerm.getDegree() != null)
				attr.addFlashAttribute("idDegree", newAcademicTerm.getDegree().getId());


			return "redirect:/academicTerm/add.htm";
		}

	}

	/**
	 * Add AcademicTerm POST
	 * @param academicTerm
	 * @param resultBinding
	 * @param attr
	 * @return String
	 */

	@RequestMapping(value = "/academicTerm/add.htm", method = RequestMethod.POST, params="Undelete")
	// Every Post have to return redirect
	public String undeleteDegree(
			@ModelAttribute("addAcademicTerm") AcademicTerm academicTerm, 
			BindingResult resultBinding,
			RedirectAttributes attr) {

		if(!resultBinding.hasErrors()){

			ResultClass<AcademicTerm> result = serviceAcademicTerm.undeleteAcademic(academicTerm);

			if (!result.hasErrors()){

				attr.addFlashAttribute("academicTerm", result.getE());
				return "redirect:/academicTerm/" + result.getE().getId() + "/modify.htm";


			}else{
				attr.addFlashAttribute("addAcademicTerm", academicTerm);
				if (result.isElementDeleted())
					attr.addFlashAttribute("unDelete", true); 
				attr.addFlashAttribute("errors", result.getErrorsList());
				attr.addFlashAttribute("idDegree", academicTerm.getDegree().getId());
				return "redirect:/academicTerm/add.htm";
			}

		}
		else{
			attr.addFlashAttribute("org.springframework.validation.BindingResult.addAcademicTerm", resultBinding);
			attr.addFlashAttribute("addAcademicTerm", academicTerm);
			if (academicTerm.getDegree() != null)
				attr.addFlashAttribute("idDegree", academicTerm.getDegree().getId());
			return "redirect:/academicTerm/add.htm";
		}
	}

	/**
	 * Methods for list academic terms of a term
	 */

	/**
	 * list Academic
	 * @param pageIndex
	 * @param showAll
	 * @return ModelAndView
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/academicTerm/page/{pageIndex}.htm")
	protected ModelAndView formViewAcademicTerm(@PathVariable("pageIndex") Integer pageIndex, 
			@RequestParam(value = "showAll", defaultValue="false") Boolean showAll)
					throws ServletException, IOException {

		Map<String, Object> myModel = new HashMap<String, Object>();


		ResultClass<AcademicTerm> p = serviceAcademicTerm.getAcademicsTerm(pageIndex, showAll);
		myModel.put("showAll", showAll);
		myModel.put("academicTerms", p);
		Integer numberOfPages = serviceAcademicTerm.numberOfPages(showAll).getE();
		myModel.put("numberOfPages",numberOfPages );
		myModel.put("currentPage", pageIndex);

		setShowAll(showAll);

		return new ModelAndView("academicTerm/list", "model", myModel);
	}


	/**
	 * View AcademicTerm
	 * @param id_academic
	 * @return ModelAndView
	 * @throws ServletException
	 */
	@RequestMapping(value = "/academicTerm/{academicId}.htm", method = RequestMethod.GET)
	protected ModelAndView academicTermGET(
			@PathVariable("academicId") Long id_academic)
					throws ServletException {
		Map<String, Object> myModel = new HashMap<String, Object>();

		AcademicTerm a = serviceAcademicTerm.getAcademicTerm(id_academic).getE();
		myModel.put("academicTerm", a);

		myModel.put("courses", a.getCourses());

		return new ModelAndView("academicTerm/view", "model", myModel);
	}

	/**
	 * Methods for modifying a Term
	 */

	/**
	 * Modify academicTerm GET
	 * @param id_academic
	 * @param model
	 * @return String
	 * @throws ServletException
	 */
	@RequestMapping(value = "/academicTerm/{academicId}/modify.htm", method = RequestMethod.GET)
	protected String modifyAcademictermGET(
			@PathVariable("academicId") Long id_academic, Model model)
					throws ServletException {

		if (!model.containsAttribute("academicTerm")){
			AcademicTerm aT = serviceAcademicTerm.getAcademicTerm(id_academic).getE();

			model.addAttribute("academicTerm", aT);

		}


		return "academicTerm/modify";
	}
	
	/**
	 * modify academic POST
	 * @param id_academic
	 * @param newTerm
	 * @param bindingResult
	 * @param model
	 * @param attr
	 * @return String
	 */
	@RequestMapping(value = "/academicTerm/{academicId}/modify.htm", method = RequestMethod.POST)
	public String formModifySystem(@PathVariable("academicId") Long id_academic,
			@ModelAttribute("academicTerm") @Valid AcademicTerm newTerm,
			BindingResult bindingResult, Model model,
			RedirectAttributes attr) {

		if ((!bindingResult.hasErrors()) || (bindingResult.hasErrors() && bindingResult.getFieldError().getField().equals("degree"))) {

			ResultClass<Boolean> resultReturned = serviceAcademicTerm.modifyAcademicTerm(newTerm, id_academic);
			if (!resultReturned.hasErrors())
				return "redirect:/academicTerm/page/0.htm?showAll";
			else{
				model.addAttribute("academicTerm", newTerm);

				attr.addFlashAttribute("errors", resultReturned.getErrorsList());

				attr.addFlashAttribute("academicTerm", newTerm);
				return "redirect:/academicTerm/" + id_academic + "/modify.htm";
			}

		}
		else{

			attr.addFlashAttribute("org.springframework.validation.BindingResult.academicTerm", bindingResult);
			attr.addFlashAttribute("addAcademicTerm", newTerm);
			return "redirect:/academicTerm/" + id_academic + "/modify.htm";

		}


	}


	/**
	 * Delete an academicTerm
	 * @param id_academic
	 * @return String
	 * @throws ServletException
	 */
	@RequestMapping(value = "/academicTerm/{academicId}/delete.htm", method = RequestMethod.GET)
	public String deleteAcademicTermGET(
			@PathVariable("academicId") Long id_academic)
					throws ServletException {

		if (serviceAcademicTerm.deleteAcademicTerm(serviceAcademicTerm.getAcademicTerm(id_academic).getE()).getE()) {
			return "redirect:/academicTerm/page/0.htm?showAll="+showAll;
		} else
			return "redirect:/error.htm";
	}

	/**
	 * Restore academicTerm
	 * @param id_Academic
	 * @return String
	 */
	@RequestMapping(value = "/academicTerm/{academicId}/restore.htm")
	// Every Post have to return redirect
	public String restoreAcademicTerm(@PathVariable("academicId") Long id_academic) {
		ResultClass<AcademicTerm> result = serviceAcademicTerm.undeleteAcademic((serviceAcademicTerm.getAcademicTerm(id_academic).getE()));
		if (!result.hasErrors())

			return "redirect:/academicTerm/page/0.htm?showAll="+showAll;
		else{
			return "redirect:/error.htm";

		}

	}

}