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
package es.ucm.fdi.dalgs.topic.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.supercsv.prefs.CsvPreference;

import es.ucm.fdi.dalgs.acl.service.AclObjectService;
import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.classes.UploadForm;
import es.ucm.fdi.dalgs.domain.Module;
import es.ucm.fdi.dalgs.domain.Topic;
import es.ucm.fdi.dalgs.module.service.ModuleService;
import es.ucm.fdi.dalgs.subject.service.SubjectService;
import es.ucm.fdi.dalgs.topic.repository.TopicRepository;

@Service
public class TopicService {
	@Autowired
	private TopicRepository repositoryTopic;

	@Autowired
	private ModuleService serviceModule;

	@Autowired
	private SubjectService serviceSubject;

	@Autowired
	private AclObjectService manageAclService;

	@Autowired
	private MessageSource messageSource;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Topic> addTopic(Topic topic, Long id_module,
			Long id_degree, Locale locale) {

		boolean success = false;

		Topic topicExists = repositoryTopic.existByCode(topic.getInfo().getCode(),
				id_module);
		ResultClass<Topic> result = new ResultClass<>();

		if (topicExists != null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add(messageSource.getMessage("error.Code", null, locale));

			if (topicExists.getIsDeleted()) {
				result.setElementDeleted(true);
				errors.add(messageSource.getMessage("error.deleted", null,
						locale));
				result.setSingleElement(topicExists);
			} else
				result.setSingleElement(topic);
			result.setErrorsList(errors);
		} else {
			topic.setModule(serviceModule.getModule(id_module, id_degree)
					.getSingleElement());
			success = repositoryTopic.addTopic(topic);

			if (success) {
				topicExists = repositoryTopic.existByCode(topic.getInfo().getCode(),
						id_module);
				success = manageAclService.addACLToObject(topicExists.getId(),
						topicExists.getClass().getName());
				if (success)
					result.setSingleElement(topic);

			} else {
				throw new IllegalArgumentException(
						"Cannot create ACL. Object not set.");

			}
		}
		return result;
	}

	@PreAuthorize("isAuthenticated()")
	@Transactional(readOnly = true)
	public ResultClass<Topic> getAll() {
		ResultClass<Topic> result = new ResultClass<>();
		result.addAll(repositoryTopic.getAll());
		return result;
	}

	@PreAuthorize("hasPermission(#topic, 'WRITE') or hasPermission(#topic, 'ADMINISTRATION')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> modifyTopic(Topic topic, Long id_topic,
			Long id_module, Long id_degree, Locale locale) {
		ResultClass<Boolean> result = new ResultClass<>();

		Topic modifyTopic = repositoryTopic.getTopic(id_topic, id_module, id_degree);

		Topic topicExists = repositoryTopic.existByCode(topic.getInfo().getCode(),
				id_module);

		if (!topic.getInfo().getCode()
				.equalsIgnoreCase(modifyTopic.getInfo().getCode())
				&& topicExists != null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<>();
			errors.add(messageSource.getMessage("error.newCode", null, locale));

			if (topicExists.getIsDeleted()) {
				result.setElementDeleted(true);
				errors.add(messageSource.getMessage("error.deleted", null,
						locale));

			}
			result.setErrorsList(errors);
			result.setSingleElement(false);
		} else {
			modifyTopic.setInfo(topic.getInfo());
			boolean r = repositoryTopic.saveTopic(modifyTopic);
			if (r)
				result.setSingleElement(true);
		}
		return result;
	}

	@PreAuthorize("isAuthenticated()")
	@Transactional(readOnly = true)
	public ResultClass<Topic> getTopic(Long id, Long id_module, Long id_degree) {
		ResultClass<Topic> result = new ResultClass<Topic>();
		result.setSingleElement(repositoryTopic.getTopic(id, id_module, id_degree));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> deleteTopic(Topic topic) {
		ResultClass<Boolean> result = new ResultClass<>();
		Collection<Topic> topics = new ArrayList<>();
		topics.add(topic);
		if (serviceSubject.deleteSubjectsForTopic(topics).getSingleElement()) {
			result.setSingleElement(repositoryTopic.deleteTopic(topic));
			return result;
		}
		result.setSingleElement(false);

		return result;
	}

	@PreAuthorize("isAuthenticated()")
	@Transactional(readOnly = true)
	public ResultClass<Topic> getTopicAll(Long id_topic, Long id_module,
			Long id_degree, Boolean show) {
		ResultClass<Topic> result = new ResultClass<Topic>();
		Topic p = repositoryTopic.getTopic(id_topic, id_module, id_degree);
		if (p != null) {
			p.setSubjects(serviceSubject.getSubjectsForTopic(id_topic, show));

			result.setSingleElement(p);
		}
		return result;
	}

	@PreAuthorize("isAuthenticated()")
	@Transactional(readOnly = true)
	public ResultClass<Topic> getTopicsForModule(Long id, Boolean show) {
		ResultClass<Topic> result = new ResultClass<>();
		result.addAll(repositoryTopic.getTopicsForModule(id, show));
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> deleteTopicsForModules(
			Collection<Module> modules) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		Collection<Topic> topics = repositoryTopic.getTopicsForModules(modules);

		if (serviceSubject.deleteSubjectsForTopic(topics).getSingleElement()) {
			result.setSingleElement(repositoryTopic.deleteTopicsForModules(modules));
			return result;
		}
		result.setSingleElement(false);
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> deleteTopicsForModule(Module module) {
		ResultClass<Boolean> result = new ResultClass<Boolean>();
		if (!module.getTopics().isEmpty())
			if (serviceSubject.deleteSubjectsForTopic(module.getTopics())
					.getSingleElement()) {
				result.setSingleElement(repositoryTopic.deleteTopicsForModule(module));
				return result;
			} else
				result.setSingleElement(false);
		else
			result.setSingleElement(true);

		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Topic> unDeleteTopic(Topic topic, Long id_module,
			Locale locale) {
		Topic t = repositoryTopic.existByCode(topic.getInfo().getCode(), id_module);
		ResultClass<Topic> result = new ResultClass<>();
		if (t == null) {
			result.setHasErrors(true);
			Collection<String> errors = new ArrayList<String>();
			errors.add(messageSource.getMessage("error.ElementNoExists", null,
					locale));
			result.setErrorsList(errors);

		} else {
			if (!t.getIsDeleted()) {
				Collection<String> errors = new ArrayList<String>();
				errors.add(messageSource.getMessage("error.CodeNoDeleted",
						null, locale));
				result.setErrorsList(errors);
			}

			t.setDeleted(false);
			t.setInfo(topic.getInfo());
			boolean r = repositoryTopic.saveTopic(t);
			if (r)
				result.setSingleElement(t);

		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public ResultClass<Boolean> uploadCSV(UploadForm upload, Long id_module, Long id_degree, Locale locale) {
		ResultClass<Boolean> result = new ResultClass<>();
		if (!upload.getFileData().isEmpty()){
			CsvPreference prefers = new CsvPreference.Builder(upload.getQuoteChar()
					.charAt(0), upload.getDelimiterChar().charAt(0),
					upload.getEndOfLineSymbols()).build();

			List<Topic> list = null;
			try {
				FileItem fileItem = upload.getFileData().getFileItem();
				TopicCSV topicUpload = new TopicCSV();

				Module m = serviceModule.getModule(id_module, id_degree)
						.getSingleElement();
				list = topicUpload.readCSVTopicToBean(fileItem.getInputStream(),
						upload.getCharset(), prefers, m);
				if (list == null){
					result.setHasErrors(true);
					result.getErrorsList().add(messageSource.getMessage("error.params", null, locale));
				}
				else{
					result.setSingleElement(repositoryTopic.persistListTopics(list));
					if (result.getSingleElement()) {
						for (Topic c : list) {
							Topic aux = repositoryTopic.existByCode(c.getInfo().getCode(),
									id_module);
							result.setSingleElement(result.getSingleElement()
									&& manageAclService.addACLToObject(aux.getId(), aux
											.getClass().getName()));

						}

					}
				}
			} catch (IOException e) {
				e.printStackTrace();
				result.setSingleElement(false);
			}
		}
		else {
			result.setHasErrors(true);
			result.getErrorsList().add(messageSource.getMessage("error.fileEmpty", null, locale));
		}
		return result;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@Transactional(readOnly = false)
	public void downloadCSV(HttpServletResponse response) throws IOException {

		Collection<Topic> topics = new ArrayList<Topic>();
		topics =  repositoryTopic.getAll();

		if(!topics.isEmpty()){
			TopicCSV topicCSV = new TopicCSV();
			topicCSV.downloadCSV(response, topics);
		}
	}

}
