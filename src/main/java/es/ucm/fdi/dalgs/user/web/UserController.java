package es.ucm.fdi.dalgs.user.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
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
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import es.ucm.fdi.dalgs.classes.CharsetString;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.classes.UploadForm;
import es.ucm.fdi.dalgs.classes.ValidatorUtil;
import es.ucm.fdi.dalgs.course.service.CourseService;
import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.domain.Group;
import es.ucm.fdi.dalgs.domain.User;
import es.ucm.fdi.dalgs.group.service.GroupService;
import es.ucm.fdi.dalgs.user.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService serviceUser;

	@Autowired
	private GroupService serviceGroup;

	@Autowired
	private CourseService serviceCourse;

	private Boolean showAll;

	public Boolean getShowAll() {
		return showAll;
	}

	public void setShowAll(Boolean showAll) {
		this.showAll = showAll;
	}

	private String typeOfUser = "";

	public String getTypeOfUser() {
		return typeOfUser;
	}

	public void setTypeOfUser(String typeOfUser) {
		this.typeOfUser = typeOfUser;
	}

	/**
	 * Methods for list academic terms of a term
	 */
	@RequestMapping(value = "/user/page/{pageIndex}.htm")
	public ModelAndView getUsersGET(
			@PathVariable("pageIndex") Integer pageIndex,
			@RequestParam(value = "showAll", defaultValue = "false") Boolean show,
			@RequestParam(value = "typeOfUser") String typeOfUser)
			throws ServletException {

		Map<String, Object> model = new HashMap<String, Object>();

		List<User> p = serviceUser.getAll(pageIndex, show, typeOfUser);

		model.put("users", p);
		model.put("numberOfPages", serviceUser.numberOfPages());
		model.put("currentPage", pageIndex);
		model.put("showAll", show);
		model.put("typeOfUser", typeOfUser);

		setTypeOfUser(typeOfUser);
		setShowAll(show);
		return new ModelAndView("user/list", "model", model);
	}

	@RequestMapping(value = "/user/{userId}.htm", method = RequestMethod.GET)
	public ModelAndView getUserGET(@PathVariable("userId") Long id_user)
			throws ServletException {

		Map<String, Object> model = new HashMap<String, Object>();

		User user = serviceUser.getUser(id_user);
		if (user != null) {
			model.put("userDetails", user);

			ResultClass<Group> groups = new ResultClass<Group>();
			if (serviceUser.hasRole(user, "ROLE_PROFESSOR")) {
				ResultClass<Course> courses = new ResultClass<Course>();
				courses = serviceCourse.getCourseForCoordinator(id_user);
				groups = serviceGroup.getGroupsForProfessor(id_user);

				if (!courses.isEmpty()) {
					model.put("courses", courses);
				}

			} else if (serviceUser.hasRole(user, "ROLE_STUDENT")) {
				groups = serviceGroup.getGroupsForStudent(id_user);
			}

			if (!groups.isEmpty())
				model.put("groups", groups);

			return new ModelAndView("user/view", "model", model); // Admin
																	// view
		}
		return new ModelAndView("exception/notFound", "model", model);
	}

	/**
	 * Methods for add a single user
	 */
	@RequestMapping(value = "/user/add.htm", method = RequestMethod.GET)
	public String addUserGET(Model model) {

		User user = new User();

		// Test

		List<String> roles = new ArrayList<String>();
		roles.add("ROLE_ADMIN");
		roles.add("ROLE_USER");
		roles.add("ROLE_STUDENT");
		roles.add("ROLE_PROFESSOR");

		model.addAttribute("roles", roles);

		// ----------------------

		model.addAttribute("addUser", user);
		return "user/add";

	}

	@RequestMapping(value = "/user/add.htm", method = RequestMethod.POST)
	// Every Post have to return redirect
	public String addUserPOST(@ModelAttribute("addUser") @Valid User user,
			BindingResult result, Model model) {

		ValidatorUtil validator = new ValidatorUtil();
		if (!validator.validateEmail(user.getEmail()))
			return "redirect://user/add.htm";

		if (!result.hasErrors()) {

			boolean created = serviceUser.addUser(user);
			if (created)
				return "redirect:/user/page/0.htm?showAll=" + showAll
						+ "&typeOfUser=" + typeOfUser;
			else
				return "redirect:/user/add.htm";
		}
		return "redirect:/error.htm";
	}

	/** Method method to insert users massively */

	@RequestMapping(value = "/user/upload.htm", method = RequestMethod.GET)
	public String uploadGet(Model model) {
		CharsetString charsets = new CharsetString();

		model.addAttribute("className", "User");
		model.addAttribute("listCharsets", charsets.ListCharsets());
		model.addAttribute("newUpload", new UploadForm("User"));
		model.addAttribute("typeOfUser", typeOfUser);
		return "upload";
	}

	@RequestMapping(value = "/user/upload.htm", method = RequestMethod.POST)
	public String uploadPost(
			@ModelAttribute("newUpload") @Valid UploadForm upload,
			BindingResult result, Model model) {

		if (result.hasErrors() || upload.getCharset().isEmpty()) {
			for (ObjectError error : result.getAllErrors()) {
				System.err.println("Error: " + error.getCode() + " - "
						+ error.getDefaultMessage());
			}
			return "upload";
		}

		if (serviceUser.uploadCVS(upload, typeOfUser))
			return "home";
		else
			return "upload";
	}

	@RequestMapping(value = "/user/{userId}/modify.htm", method = RequestMethod.GET)
	public String modifyUserGET(@PathVariable("userId") Long id_user,
			Model model) {

		User user = serviceUser.getUser(id_user);

		model.addAttribute("modifyUser", user);
		return "user/modify";

	}

	@RequestMapping(value = "/user/{userId}/modify.htm", method = RequestMethod.POST)
	// Every Post have to return redirect
	public String modifyUserPOST(@PathVariable("userId") Long id_user,
			@ModelAttribute("modifyUser") @Valid User user,
			BindingResult result, Model model) {

		User user_aux = serviceUser.getUser(id_user);

		user_aux.setUsername(user.getUsername());
		user_aux.setFirstName(user.getFirstName());
		user_aux.setLastName(user.getLastName());

		if (serviceUser.saveUser(user_aux)) {

			return "redirect:/user/page/0.htm?showAll=" + showAll
					+ "&typeOfUser=" + typeOfUser;
		}

		return "redirect:/error.htm";
	}

	@RequestMapping(value = "/user/{userId}/status.htm", method = RequestMethod.GET)
	public String disabledUser(@PathVariable("userId") Long id_user)
			throws ServletException {

		User user = serviceUser.getUser(id_user);
		if (user.isEnabled())
			user.setEnabled(false);
		else
			user.setEnabled(true);

		if (serviceUser.saveUser(user)) {
			return "redirect:/user/page/0.htm?showAll=" + showAll
					+ "&typeOfUser=" + typeOfUser;
		} else
			return "redirect:/error.htm";
	}
	
	@RequestMapping(value = "/user/download.htm")
	public void downloadCSV(HttpServletResponse response) throws IOException {

		 String csvFileName = "users.csv";
		 
	        response.setContentType("text/csv");
	 
	        // creates mock data
	        String headerKey = "Content-Disposition";
	        String headerValue = String.format("attachment; filename=\"%s\"",
	                csvFileName);
	        response.setHeader(headerKey, headerValue);
	 

	        Collection<User> users = new ArrayList<User>();
	        users =  serviceUser.getAll();

	        // uses the Super CSV API to generate CSV data from the model data
	        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(),
	                CsvPreference.STANDARD_PREFERENCE);
	         
	        String[] header = {"firstName", "lastName", "email", "roles"};
	 
	        csvWriter.writeHeader(header);
	 
	        for (User usr : users) {
	            csvWriter.write(usr, header);
	        }
	        csvWriter.close();  
	}
	

}
