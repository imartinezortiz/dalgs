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
package es.ucm.fdi.dalgs.competence.web;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
import es.ucm.fdi.dalgs.classes.TypeWithEnum.TypeOfCompetence;
import es.ucm.fdi.dalgs.classes.UploadForm;
import es.ucm.fdi.dalgs.competence.service.CompetenceService;
import es.ucm.fdi.dalgs.degree.service.DegreeService;
import es.ucm.fdi.dalgs.domain.Competence;

@Controller
public class CompetenceController {

	@Autowired
	private CompetenceService serviceCompetence;

	// @Autowired
	// private SubjectService serviceSubject;

	@Autowired
	private DegreeService serviceDegree;

	private Boolean showAll;

	public Boolean getShowAll() {
		return showAll;
	}

	public void setShowAll(Boolean showAll) {
		this.showAll = showAll;
	}

	/*
	 * private static final Logger logger = LoggerFactory
	 * .getLogger(CompetenceController.class);
	 */
	// @ModelAttribute("degrees")
	// public List<Degree> degrees() {
	// return serviceDegree.getAll().getE();
	// }

	/**
	 * Methods for adding competences
	 */

	@RequestMapping(value = "/degree/{degreeId}/competence/add.htm", method = RequestMethod.GET)
	public String addCompetenceGET(Model model,
			@PathVariable("degreeId") Long id) {
		// Competence newCompetence = new Competence();

		if (!model.containsAttribute("competence"))
			model.addAttribute("competence", new Competence());
		
		List<TypeOfCompetence> list = Arrays.asList(TypeOfCompetence.values());
		
		model.addAttribute("valueButton", "Add");
		model.addAttribute("typeform", "form.add");
		model.addAttribute("typeofCompetence", list);

		return "competence/form";
	}

	@RequestMapping(value = "/degree/{degreeId}/competence/add.htm", method = RequestMethod.POST, params = "Add")
	// Every Post have to return redirect
	public String addCompetencePOST(
			@ModelAttribute("competence") Competence newCompetence,
			@PathVariable("degreeId") Long id_degree,
			BindingResult resultBinding, RedirectAttributes attr, Locale locale) {

		if (!resultBinding.hasErrors()) {
			ResultClass<Competence> result = serviceCompetence.addCompetence(
					newCompetence, id_degree, locale);
			if (!result.hasErrors())
				return "redirect:/degree/" + id_degree + ".htm";
			else {

				if (result.isElementDeleted()) {
					attr.addFlashAttribute("unDelete",
							result.isElementDeleted());
					attr.addFlashAttribute("competence",
							result.getSingleElement());

				} else
					attr.addFlashAttribute("competence", newCompetence);
				attr.addFlashAttribute("errors", result.getErrorsList());

			}
		} else {
			attr.addFlashAttribute("competence", newCompetence);
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.competence",
					resultBinding);

		}
		return "redirect:/degree/" + id_degree + "/competence/add.htm";

	}

	/**
	 * Methods for delete competences
	 */
	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/delete.htm", method = RequestMethod.GET)
	public String deleteCompetenceGET(@PathVariable("degreeId") Long id_degree,
			@PathVariable("competenceId") Long id_competence)
			throws ServletException {

		if (serviceCompetence.deleteCompetence(
				serviceCompetence.getCompetence(id_competence, id_degree)
						.getSingleElement()).getSingleElement()) {
			return "redirect:/degree/" + id_degree + ".htm";
		} else
			return "redirect:/error.htm";
	}

	@RequestMapping(value = "/degree/{degreeId}/competence/add.htm", method = RequestMethod.POST, params = "Undelete")
	// Every Post have to return redirect
	public String undeleteDegreePOST(@PathVariable("degreeId") Long id_degree,
			@ModelAttribute("competence") Competence competence,
			BindingResult resultBinding, RedirectAttributes attr, Locale locale) {

		if (!resultBinding.hasErrors()) {

			ResultClass<Competence> result = serviceCompetence
					.unDeleteCompetence(competence, id_degree, locale);

			if (!result.hasErrors()) {
				attr.addFlashAttribute("competence", result.getSingleElement());
				return "redirect:/degree/" + id_degree + "/competence/"
						+ result.getSingleElement().getId() + "/modify.htm";
			} else {

				if (result.isElementDeleted())
					attr.addFlashAttribute("unDelete", true);
				attr.addFlashAttribute("errors", result.getErrorsList());

			}
		} else {
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.competence",
					resultBinding);

		}
		attr.addFlashAttribute("competence", competence);
		return "redirect:/degree/" + id_degree + "/competence/add.htm";
	}

	/**
	 * Methods for modify competences
	 */
	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/modify.htm", method = RequestMethod.POST)
	public String modifyCompetencePOST(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("competenceId") Long id_competence,
			@ModelAttribute("competence") Competence modify,
			BindingResult resultBinding, RedirectAttributes attr, Locale locale)

	{
		if (!resultBinding.hasErrors()) {

			ResultClass<Boolean> result = serviceCompetence.modifyCompetence(
					modify, id_competence, id_degree, locale);
			if (!result.hasErrors())
				return "redirect:/degree/" + id_degree + ".htm";
			else {
				attr.addFlashAttribute("errors", result.getErrorsList());
			}

		} else {
			attr.addFlashAttribute(
					"org.springframework.validation.BindingResult.competence",
					resultBinding);
		}

		attr.addFlashAttribute("module", modify);
		return "redirect:/degree/" + id_degree + "/competence/" + id_competence
				+ "/modify.htm";

	}

	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/modify.htm", method = RequestMethod.GET)
	protected String modifyCompetenceGET(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("competenceId") Long id_competence, Model model)
			throws ServletException {
		if (!model.containsAttribute("competence")) {

			Competence p = serviceCompetence.getCompetence(id_competence,
					id_degree).getSingleElement();
			model.addAttribute("competence", p);
		}
		model.addAttribute("valueButton", "Modify");
		model.addAttribute("typeform", "form.modify");

		return "/competence/form";

	}

	/**
	 * Methods for view competence
	 */
	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}.htm", method = RequestMethod.GET)
	protected ModelAndView getCompetenceGET(
			@PathVariable("degreeId") Long id_degree,
			@PathVariable("competenceId") Long id_competence,
			@RequestParam(value = "showAll", defaultValue = "false") Boolean show)
			throws ServletException {

		Map<String, Object> model = new HashMap<String, Object>();

		Competence p = serviceCompetence.getCompetenceAll(id_competence,
				id_degree, show).getSingleElement();

		if (p != null) {
			model.put("showAll", show);

			model.put("competence", p);
			model.put("learningGoals", p.getLearningGoals());

			return new ModelAndView("competence/view", "model", model);
		}
		return new ModelAndView("exception/notFound", "model", model);

	}

	@RequestMapping(value = "/degree/{degreeId}/competence/{competenceId}/restore.htm")
	// Every Post have to return redirect
	public String restoreCompetence(@PathVariable("degreeId") Long id_degree,
			@PathVariable("competenceId") Long id_competence, Locale locale) {
		ResultClass<Competence> result = serviceCompetence.unDeleteCompetence(
				serviceCompetence.getCompetence(id_competence, id_degree)
						.getSingleElement(), id_degree, locale);
		if (!result.hasErrors())
			return "redirect:/degree/" + id_degree + ".htm";
		else {
			return "redirect:/error.htm";

		}

	}

	@RequestMapping(value = "/degree/{degreeId}/competence/upload.htm", method = RequestMethod.GET)
	public String uploadGet(Model model) {
		CharsetString charsets = new CharsetString();

		model.addAttribute("className", "Competence");
		model.addAttribute("listCharsets", charsets.ListCharsets());
		model.addAttribute("newUpload", new UploadForm("Competence"));
		return "upload";
	}

	@RequestMapping(value = "/degree/{degreeId}/competence/upload.htm", method = RequestMethod.POST)
	public String uploadPost(
			@ModelAttribute("newUpload") @Valid UploadForm upload,
			BindingResult resultBinding, Model model,
			@PathVariable("degreeId") Long id_degree,
			RedirectAttributes attr, Locale locale) {

		
		
		if (resultBinding.hasErrors() || upload.getCharset().isEmpty() || upload.getFileData().getSize() ==0) {
			for (ObjectError error : resultBinding.getAllErrors()) {
				System.err.println("Error: " + error.getCode() + " - "
						+ error.getDefaultMessage());
			}
			return "upload";
		}
		ResultClass<Boolean> result= serviceCompetence.uploadCSV(upload, id_degree, locale);
		if (!result.hasErrors())
			return "redirect:/degree/"+ id_degree+".htm" ;
		else{
			attr.addFlashAttribute("errors", result.getErrorsList());
			return "redirect:/degree/" +id_degree + "/competence/upload.htm";
		}
			
	}
	
	@RequestMapping(value = "/competence/download.htm")
	public void downloadCSV(HttpServletResponse response) throws IOException {
		serviceCompetence.dowloadCSV(response);
		
	}

}