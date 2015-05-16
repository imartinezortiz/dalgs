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
package es.ucm.fdi.dalgs.degree.web;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
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
import es.ucm.fdi.dalgs.degree.service.DegreeService;
import es.ucm.fdi.dalgs.domain.Degree;

@Controller
public class DegreeController {

	@Autowired
	private DegreeService serviceDegree;

	private Boolean showAll;

	public Boolean getShowAll() {
		return showAll;
	}

	public void setShowAll(Boolean showAll) {
		this.showAll = showAll;
	}

	@RequestMapping(value = "/degree/add.htm", method = RequestMethod.GET)
	public String addDegreeGET(Model model) {
		if (!model.containsAttribute("degree"))
			model.addAttribute("degree", new Degree());
		model.addAttribute("valueButton", "Add");
		model.addAttribute("typeform", "form.add");
		return "degree/form";
	}

	@RequestMapping(value = "/degree/add.htm", method = RequestMethod.POST, params = "Add")
	// Every Post have to return redirect
	public String addDegreePOST(@ModelAttribute("addDegree") Degree newDegree,
			BindingResult resultBinding, RedirectAttributes attr, Locale locale) {

		if (!resultBinding.hasErrors()) {
			ResultClass<Degree> result = serviceDegree.addDegree(newDegree,
					locale);
			if (!result.hasErrors())
				return "redirect:/degree/page/0.htm?showAll=" + showAll;
			else {

				if (result.isElementDeleted()) {
					attr.addFlashAttribute("unDelete",
							result.isElementDeleted());
					attr.addFlashAttribute("degree", result.getSingleElement());
				} else
					attr.addFlashAttribute("degree", newDegree);
				attr.addFlashAttribute("errors", result.getErrorsList());

			}
		} else {
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.degree",
					resultBinding);
			attr.addFlashAttribute("degree", newDegree);

		}
		return "redirect:/degree/add.htm";
	}

	@RequestMapping(value = "/degree/add.htm", method = RequestMethod.POST, params = "Undelete")
	// Every Post have to return redirect
	public String undeleteDegree(@ModelAttribute("degree") Degree degree,
			BindingResult resultBinding, RedirectAttributes attr, Locale locale) {

		if (!resultBinding.hasErrors()) {
			ResultClass<Degree> result = serviceDegree.unDeleteDegree(degree,
					locale);

			if (!result.hasErrors()) {
				// if (created)
				attr.addFlashAttribute("degree", result.getSingleElement());
				return "redirect:/degree/" + result.getSingleElement().getId()
						+ "/modify.htm";
			} else {
				attr.addFlashAttribute("degree", degree);
				if (result.isElementDeleted())
					attr.addFlashAttribute("unDelete", true);
				attr.addFlashAttribute("errors", result.getErrorsList());

			}
		} else {
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.degree",
					resultBinding);
			attr.addFlashAttribute("degree", degree);

		}
		return "redirect:/degree/add.htm";
	}

	@RequestMapping(value = "/degree/{id_degree}/restore.htm")
	// Every Post have to return redirect
	public String restoreDegree(@PathVariable("id_degree") Long id_degree,
			Locale locale) {
		ResultClass<Degree> result = serviceDegree.unDeleteDegree(serviceDegree
				.getDegree(id_degree).getSingleElement(), locale);
		if (!result.hasErrors())
			return "redirect:/degree/page/0.htm?showAll=" + showAll;
		else {
			return "redirect:/error.htm";

		}

	}

	/**
	 * Methods for listing degrees
	 */

	@RequestMapping(value = "/degree/page/{pageIndex}.htm")
	public ModelAndView degreesGET(
			@PathVariable("pageIndex") Integer pageIndex,
			@RequestParam(value = "showAll", defaultValue = "false") Boolean showAll)
			throws ServletException, IOException {

		Map<String, Object> model = new HashMap<String, Object>();

		ResultClass<Degree> result = serviceDegree.getDegrees(pageIndex,
				showAll);
		ResultClass<Integer> numberOfPages = serviceDegree
				.numberOfPages(showAll);
		model.put("showAll", showAll);

		model.put("numberOfPages", numberOfPages.getSingleElement());
		model.put("currentPage", pageIndex);
		model.put("degrees", result);

		setShowAll(showAll);

		return new ModelAndView("degree/list", "model", model);
	}

	/**
	 * Methods for modify degrees
	 */

	@RequestMapping(value = "/degree/{degreeId}/modify.htm", method = RequestMethod.POST)
	public String modifyDegreePOST(@PathVariable("degreeId") Long id_degree,
			@ModelAttribute("degree") Degree modify,
			BindingResult resultBinding, RedirectAttributes attr, Locale locale)

	{

		if (!resultBinding.hasErrors()) {
			ResultClass<Boolean> result = serviceDegree.modifyDegree(modify,
					id_degree, locale);
			if (!result.hasErrors())
				return "redirect:/degree/page/0.htm?showAll=" + showAll;
			else {
				attr.addFlashAttribute("errors", result.getErrorsList());
			}
		} else {
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.degree",
					resultBinding);

		}
		attr.addFlashAttribute("degree", modify);
		return "redirect:/degree/" + id_degree + "/modify.htm";

	}

	@RequestMapping(value = "/degree/{degreeId}/modify.htm", method = RequestMethod.GET)
	public String modifyDegreeGET(@PathVariable("degreeId") Long id, Model model)
			throws ServletException {

		if (!model.containsAttribute("degree")) {
			Degree p = serviceDegree.getDegree(id).getSingleElement();
			model.addAttribute("degree", p);
		}
		model.addAttribute("valueButton", "Modify");
		model.addAttribute("typeform", "form.modify");
		return "degree/form";
	}

	/**
	 * Methods for delete degrees
	 */

	@RequestMapping(value = "/degree/{degreeId}/delete.htm", method = RequestMethod.GET)
	public String deleteDegreeGET(@PathVariable("degreeId") Long id_degree)
			throws ServletException {

		Degree degree = serviceDegree.getDegree(id_degree).getSingleElement();

		if (serviceDegree.deleteDegree(degree).getSingleElement()) {
			return "redirect:/degree/page/0.htm?showAll=" + showAll;
		} else
			return "redirect:/error.htm";
	}

	/**
	 * Methods for view degrees
	 */
	@RequestMapping(value = "/degree/{degreeId}.htm", method = RequestMethod.GET)
	public ModelAndView getDegreeGET(
			@PathVariable("degreeId") Long id_degree,
			@RequestParam(value = "showAll", defaultValue = "false") Boolean show)
			throws ServletException {

		Map<String, Object> model = new HashMap<String, Object>();

		Degree p = serviceDegree.getDegreeAll(id_degree, show)
				.getSingleElement();
		if (p != null) {
			model.put("showAll", show);
			model.put("degree", p);
			if (p.getModules() != null)
				model.put("modules", p.getModules());
			if (p.getCompetences() != null)
				model.put("competences", p.getCompetences());

			return new ModelAndView("degree/view", "model", model);
		}
		return new ModelAndView("exception/notFound", "model", model);

	}

	@RequestMapping(value = "/degree/upload.htm", method = RequestMethod.GET)
	public String uploadGet(Model model) {
		CharsetString charsets = new CharsetString();

		model.addAttribute("className", "Degree");
		model.addAttribute("listCharsets", charsets.ListCharsets());
		model.addAttribute("newUpload", new UploadForm("Degree"));
		return "upload";
	}

	@RequestMapping(value = "/degree/upload.htm", method = RequestMethod.POST)
	public String uploadPost(
			@ModelAttribute("newUpload") @Valid UploadForm upload,
			BindingResult resultBinding, Model model, Locale locale, RedirectAttributes attr) {

		if (resultBinding.hasErrors() || upload.getCharset().isEmpty()) {
			for (ObjectError error : resultBinding.getAllErrors()) {
				System.err.println("Error: " + error.getCode() + " - "
						+ error.getDefaultMessage());
			}
			return "upload";
		}
		ResultClass<Boolean> result = serviceDegree.uploadCSV(upload, locale); 
		
		if (!result.hasErrors())
			return "redirect:/degree/page/0.htm";
		else{
			attr.addFlashAttribute("errors", result.getErrorsList());
			return "redirect:/degree/upload.htm";
		}
			
	}

	@RequestMapping(value = "/degree/download.htm")
	public void downloadCSV(HttpServletResponse response) throws IOException {
		serviceDegree.downloadCSV(response);
	}
}
