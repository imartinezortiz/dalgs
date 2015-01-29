package com.example.tfg.web;


import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;

import javax.validation.Valid;

import org.apache.commons.fileupload.FileItem;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.constraint.UniqueHashCode;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.example.tfg.classes.UploadForm;
import com.example.tfg.domain.User;

@Controller
public class UserController {

	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserController.class);

	
	
	@SuppressWarnings("rawtypes")
	@ModelAttribute("listCharsets")
	public List<String> ListCharsets() {
		List<String> listCharsets = new ArrayList<String>();
		SortedMap charsets = Charset.availableCharsets();
		Set names = charsets.keySet();
		for (Iterator e = names.iterator(); e.hasNext();) {
			String name = (String) e.next();
			Charset charset = (Charset) charsets.get(name);
			listCharsets.add(charset.displayName());
		}
		return listCharsets;
	}

	
	
	
	@RequestMapping(value = "/upload/{entityClass}.htm", method = RequestMethod.GET)
	public String uploadGet(@PathVariable("entityClass") String entityClass, Model model) {
		
		model.addAttribute("newUpload", new  UploadForm(entityClass));
		return "upload";
	}

	@RequestMapping(value = "/upload/{entityClass}.htm", method = RequestMethod.POST)
	public String uploadPost(@PathVariable("entityClass") String entityClass, @ModelAttribute("newUpload") @Valid UploadForm upload,
			BindingResult result, Model model) {
		
		if (result.hasErrors() || upload.getCharset().isEmpty()){
	      for(ObjectError error : result.getAllErrors()){
	        System.err.println("Error: " + error.getCode() +  " - " + error.getDefaultMessage());
	      }
	      return "upload";
	    }
		

		CsvPreference prefers= 
				//new CsvPreference.Builder('"', ';',"\n").build();
		new CsvPreference.Builder(upload.getQuoteChar().charAt(0), upload.getDelimiterChar().charAt(0), upload.getEndOfLineSymbols()).build();

		List<User> list = null;
		try {
	
			FileItem fileItem = upload.getFileData().getFileItem();
			list = readCSVUserToBean(fileItem.getInputStream(), upload.getCharset(), prefers);//getInputStream().toString()
			
			for (Object object : list) {
				User user = (User) object;
				System.out.println(user.getFirstName());
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			return "upload";
		}
		
		
		return "home";
	}
	


	@SuppressWarnings("unused")
	private static List<User> readCSVUserToBean(InputStream in , String charsetName,CsvPreference csvPreference ) throws IOException {
		CsvBeanReader beanReader = null;
		List<User> usrs = new ArrayList<User>();
		try {
			beanReader = new CsvBeanReader(new InputStreamReader(in, Charset.forName(charsetName)), csvPreference);
			// the name mapping provide the basis for bean setters
			final String[] nameMapping = new String[] { "id", "firstname","lastname", "password", "username" };
			// just read the header, so that it don't get mapped to User 
			// object
			final String[] header = beanReader.getHeader(true);
			final CellProcessor[] processors = getUserProcessors();

			User u;

			while ((u = beanReader.read(User.class, nameMapping, processors)) != null) {
				usrs.add(u);
				//serviceUser.add(u);
			}

		} finally {
			if (beanReader != null) {
				beanReader.close();
			}
		}
		return usrs;
	}

	/*CellProcessors have to correspond to the entity database fields */
	private static CellProcessor[] getUserProcessors() {

		final CellProcessor[] processors = new CellProcessor[] {
				new ParseLong(), // ID (must be unique)
				new NotNull(), // FirstName
				new NotNull(), // Lastname
				new UniqueHashCode(), // Username
				new NotNull() // Password
		};
		return processors;
	}

}
