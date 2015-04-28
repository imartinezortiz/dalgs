/**
 * This file is part of D.A.L.G.S.
 *
 * D.A.L.G.S is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * D.A.L.G.S is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with D.A.L.G.S.  If not, see <http://www.gnu.org/licenses/>.
 */
package es.ucm.fdi.dalgs.academicTerm.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
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
	 * 
	 * @param model
	 * @return String
	 */
	@RequestMapping(value = "/academicTerm/add.htm", method = RequestMethod.GET)
	public String addAcademicTermGET(Model model) {

		if (!model.containsAttribute("addAcademicTerm"))
			model.addAttribute("addAcademicTerm", new AcademicTerm());
		model.addAttribute("degrees", serviceDegree.getAll());

		return "academicTerm/add";

	}

	/**
	 * Add AcademicTerm GET
	 * 
	 * @param newAcademicTerm
	 * @param resultBinding
	 * @param attr
	 * @param locale
	 * @return String
	 */
	@RequestMapping(value = "/academicTerm/add.htm", method = RequestMethod.POST, params = "Add")
	// Every Post have to return redirect
	public String addAcademicTermPOST(
			@ModelAttribute("addAcademicTerm") @Valid AcademicTerm newAcademicTerm,
			BindingResult resultBinding, RedirectAttributes attr, Locale locale) {

		this.validate(newAcademicTerm, resultBinding);

		if (!resultBinding.hasErrors()) {

			ResultClass<AcademicTerm> resultReturned = serviceAcademicTerm
					.addAcademicTerm(newAcademicTerm, locale);
			if (resultReturned.hasErrors()) {

				if (resultReturned.isElementDeleted()) {

					attr.addFlashAttribute("unDelete",
							resultReturned.isElementDeleted());
					attr.addFlashAttribute("addAcademicTerm",
							resultReturned.getSingleElement());
				} else
					attr.addFlashAttribute("addAcademicTerm", newAcademicTerm);

				attr.addFlashAttribute("idDegree", newAcademicTerm.getDegree()
						.getId());
				attr.addFlashAttribute("errors", resultReturned.getErrorsList());

				return "redirect:/academicTerm/add.htm";
			} else
				return "redirect:/academicTerm/page/0.htm?showAll";

		} else {
			// Write the binding result errors on the view

			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.addAcademicTerm",
					resultBinding);
			attr.addFlashAttribute("addAcademicTerm", newAcademicTerm);
			if (newAcademicTerm.getDegree() != null)
				attr.addFlashAttribute("idDegree", newAcademicTerm.getDegree()
						.getId());

			return "redirect:/academicTerm/add.htm";
		}

	}

	/**
	 * Add AcademicTerm POST
	 * 
	 * @param academicTerm
	 * @param resultBinding
	 * @param attr
	 * @param locale
	 * @return String
	 */

	@RequestMapping(value = "/academicTerm/add.htm", method = RequestMethod.POST, params = "Undelete")
	// Every Post have to return redirect
	public String undeleteDegree(
			@ModelAttribute("addAcademicTerm") AcademicTerm academicTerm,
			BindingResult resultBinding, RedirectAttributes attr, Locale locale) {

		if (!resultBinding.hasErrors()) {

			ResultClass<AcademicTerm> result = serviceAcademicTerm
					.restoreAcademic(academicTerm, locale);

			if (!result.hasErrors()) {

				attr.addFlashAttribute("academicTerm",
						result.getSingleElement());
				return "redirect:/academicTerm/"
						+ result.getSingleElement().getId() + "/modify.htm";

			} else {
				attr.addFlashAttribute("addAcademicTerm", academicTerm);
				if (result.isElementDeleted())
					attr.addFlashAttribute("unDelete", true);
				attr.addFlashAttribute("errors", result.getErrorsList());
				attr.addFlashAttribute("idDegree", academicTerm.getDegree()
						.getId());
				return "redirect:/academicTerm/add.htm";
			}

		} else {
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.addAcademicTerm",
					resultBinding);
			attr.addFlashAttribute("addAcademicTerm", academicTerm);
			if (academicTerm.getDegree() != null)
				attr.addFlashAttribute("idDegree", academicTerm.getDegree()
						.getId());
			return "redirect:/academicTerm/add.htm";
		}
	}

	/**
	 * Methods for list academic terms of a term
	 */

	/**
	 * list Academic
	 * 
	 * @param pageIndex
	 * @param showAll
	 * @return ModelAndView
	 * @throws ServletException
	 * @throws IOException
	 */
	@RequestMapping(value = "/academicTerm/page/{pageIndex}.htm")
	public ModelAndView academicTermsGET(
			@PathVariable("pageIndex") Integer pageIndex,
			@RequestParam(value = "showAll", defaultValue = "false") Boolean showAll)
			throws ServletException, IOException {

		Map<String, Object> myModel = new HashMap<String, Object>();

		ResultClass<AcademicTerm> p = serviceAcademicTerm.getAcademicTerms(
				pageIndex, showAll);
		myModel.put("showAll", showAll);
		myModel.put("academicTerms", p);
		Integer numberOfPages = serviceAcademicTerm.numberOfPages(showAll)
				.getSingleElement();
		myModel.put("numberOfPages", numberOfPages);
		myModel.put("currentPage", pageIndex);

		setShowAll(showAll);

		return new ModelAndView("academicTerm/list", "model", myModel);
	}

	/**
	 * View AcademicTerm
	 * 
	 * @param id_academic
	 * @return ModelAndView
	 * @throws ServletException
	 */
	@RequestMapping(value = "/academicTerm/{academicId}.htm", method = RequestMethod.GET)
	public ModelAndView academicTermGET(
			@PathVariable("academicId") Long id_academic,
			@RequestParam(value = "showAll", defaultValue = "false") Boolean show)
			throws ServletException {
		Map<String, Object> model = new HashMap<String, Object>();

		ResultClass<AcademicTerm> result = serviceAcademicTerm.getAcademicTerm(
				id_academic, show);
		AcademicTerm a = result.getSingleElement();
		if (a != null) {
			model.put("academicTerm", a);
			model.put("showAll", show);
			model.put("courses", a.getCourses());

			setShowAll(show);
			return new ModelAndView("academicTerm/view", "model", model);
		}

		return new ModelAndView("exception/notFound", "model", model);

	}

	/**
	 * Methods for modifying a Term
	 */

	/**
	 * Modify academicTerm GET
	 * 
	 * @param id_academic
	 * @param model
	 * @return String
	 * @throws ServletException
	 */
	@RequestMapping(value = "/academicTerm/{academicId}/modify.htm", method = RequestMethod.GET)
	public String modifyAcademictermGET(
			@PathVariable("academicId") Long id_academic, Model model)
			throws ServletException {

		if (!model.containsAttribute("academicTerm")) {
			AcademicTerm aT = serviceAcademicTerm.getAcademicTerm(id_academic,
					false).getSingleElement();

			model.addAttribute("academicTerm", aT);

		}

		return "academicTerm/modify";
	}

	/**
	 * modify academic POST
	 * 
	 * @param id_academic
	 * @param newTerm
	 * @param bindingResult
	 * @param model
	 * @param attr
	 * @return String
	 */
	@RequestMapping(value = "/academicTerm/{academicId}/modify.htm", method = RequestMethod.POST)
	public String modifyAcademictermPOST(
			@PathVariable("academicId") Long id_academic,
			@ModelAttribute("academicTerm") @Valid AcademicTerm newTerm,
			BindingResult bindingResult, Model model, RedirectAttributes attr,
			Locale locale) {

		if (!bindingResult.hasErrors()
				|| bindingResult.hasFieldErrors("degree")) {

			ResultClass<Boolean> resultReturned = serviceAcademicTerm
					.modifyAcademicTerm(newTerm, id_academic, locale);
			if (!resultReturned.hasErrors())
				return "redirect:/academicTerm/page/0.htm?showAll";
			else {
				model.addAttribute("academicTerm", newTerm);

				attr.addFlashAttribute("errors", resultReturned.getErrorsList());

				attr.addFlashAttribute("academicTerm", newTerm);
				return "redirect:/academicTerm/" + id_academic + "/modify.htm";
			}

		} else {

			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.academicTerm",
					bindingResult);
			attr.addFlashAttribute("addAcademicTerm", newTerm);
			return "redirect:/academicTerm/" + id_academic + "/modify.htm";

		}

	}

	/**
	 * Delete an academicTerm
	 * 
	 * @param id_academic
	 * @return String
	 * @throws ServletException
	 */
	@RequestMapping(value = "/academicTerm/{academicId}/delete.htm", method = RequestMethod.GET)
	public String deleteAcademicTermGET(
			@PathVariable("academicId") Long id_academic)
			throws ServletException {

		if (serviceAcademicTerm.deleteAcademicTerm(
				serviceAcademicTerm.getAcademicTerm(id_academic, false)
						.getSingleElement()).getSingleElement()) {
			return "redirect:/academicTerm/page/0.htm?showAll=" + showAll;
		} else
			return "redirect:/error.htm";
	}

	/**
	 * Restore academicTerm
	 * 
	 * @param id_Academic
	 * @return String
	 */
	@RequestMapping(value = "/academicTerm/{academicId}/restore.htm")
	// Every Post have to return redirect
	public String restoreAcademicTerm(
			@PathVariable("academicId") Long id_academic, Locale locale) {
		ResultClass<AcademicTerm> result = serviceAcademicTerm.restoreAcademic(
				(serviceAcademicTerm.getAcademicTerm(id_academic, false)
						.getSingleElement()), locale);
		if (!result.hasErrors())

			return "redirect:/academicTerm/page/0.htm?showAll=" + showAll;
		else {
			return "redirect:/error.htm";

		}

	}

	@RequestMapping(value = "/academicTerm/{academicId}/clone.htm")
	// Every Post have to return redirect
	public String copyAcademicTerm(
			@PathVariable("academicId") Long id_academic, Locale locale) {

		AcademicTerm at_aux = new AcademicTerm();
		at_aux = serviceAcademicTerm.getAcademicTerm(id_academic, false)
				.getSingleElement();
		if (serviceAcademicTerm.copyAcademicTerm(at_aux, locale)
				.getSingleElement() != null) {

			return "redirect:/academicTerm/page/0.htm?showAll=" + showAll;
		}
		return "redirect:/error.htm";
	}

	/**
	 * Validate a new Academic Term
	 * 
	 * @param academicTerm
	 * @param errors
	 */
	public void validate(AcademicTerm academicTerm, Errors errors) {

		if (academicTerm.getDegree() == null) {
			errors.rejectValue("degree", "validation.negative",
					"You must select a degree");
		}

		if (!errors.hasFieldErrors("term")) {
			String pattern = "\\b\\d{4}\\b *\\/\\b\\d{4}\\b";
			// Create a Pattern object
			Pattern r = Pattern.compile(pattern);

			// Now create matcher object.
			Matcher m = r.matcher(academicTerm.getTerm());
			if (!m.find()) {
				errors.rejectValue("term", "validation.negative",
						"Pattern dismatch (yyyy/yyyy)");
			} else {
				String[] years = academicTerm.getTerm().split("/");
				String year1 = years[0];
				String year2 = years[1];
				if ((Integer.parseInt(year2) - Integer.parseInt(year1)) != 1) {
					errors.rejectValue("term", "validation.negative",
							"The term must be 2 consecutive years");
				}
			}
		}
	}

}