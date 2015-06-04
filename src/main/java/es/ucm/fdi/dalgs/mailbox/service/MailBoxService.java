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
package es.ucm.fdi.dalgs.mailbox.service;

import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.course.service.CourseService;
import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.domain.Group;
import es.ucm.fdi.dalgs.domain.MessageBox;
import es.ucm.fdi.dalgs.group.service.GroupService;
import es.ucm.fdi.dalgs.mailbox.respository.MailBoxRepository;
import es.ucm.fdi.storage.business.boundary.StorageManager;

/**
 * Get e-mail messages from a POP3/IMAP server
 *
 */
@Service
public class MailBoxService{

	@Value("${mail.protocol}")
	private String protocol;

	@Value("${mail.host}")
	private String host;

	@Value("${mail.port}")
	private String port;

	@Value("${mail.userName}")
	private String userName;

	@Value("${mail.password}")
	private String password;
	
	private static final String pattern = "^\\s*(Re:\\s*)?\\[(course|group):(\\d+)\\]";

	@Autowired
	private MailBoxRepository repositoryMailBox;

	@Autowired
	CourseService serviceCourse;

	@Autowired
	GroupService serviceGroup;

	@Autowired
	private StorageManager storageManager;
	
	// app-config.xml
	@Value("#{attachmentsPrefs[bucket]}")
	private String bucket;
	

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Returns a Properties object which is configured for a POP3/IMAP server
	 */
	private Properties getServerProperties(String protocol, String host,
			String port) {
		Properties properties = new Properties();

		// server setting
		properties.put(String.format("mail.%s.host", protocol), host);
		properties.put(String.format("mail.%s.port", protocol), port);

		// SSL setting
		properties.setProperty(
				String.format("mail.%s.socketFactory.class", protocol),
				"javax.net.ssl.SSLSocketFactory");
		properties.setProperty(
				String.format("mail.%s.socketFactory.fallback", protocol),
				"false");
		properties.setProperty(
				String.format("mail.%s.socketFactory.port", protocol),
				String.valueOf(port));

		return properties;
	}

	/**
	 * Returns new messages and fetches details for each message.
	 */
	@Transactional(readOnly=false)
	public ResultClass<Boolean> downloadEmails() {


		ResultClass<Boolean> result = new ResultClass<>();
		Properties properties = getServerProperties(protocol, host, port);
		Session session = Session.getDefaultInstance(properties);

		try {

			// connects to the message store
			Store store = session.getStore(protocol);
			
			store.connect(userName, password);
			// opens the inbox folder
			Folder folderInbox = store.getFolder("INBOX");
			folderInbox.open(Folder.READ_ONLY);

			// fetches new messages from server
			Message[] messages = folderInbox.getMessages();
			for (int i = 0; i < messages.length; i++) {
				Message msg = messages[i];



				String[] idHeaders= msg.getHeader("MESSAGE-ID");
				if (idHeaders != null && idHeaders.length > 0){

					MessageBox exists =  repositoryMailBox.getMessageBox(idHeaders[0]);
					if (exists == null){

						MessageBox messageBox = new MessageBox();
						messageBox.setSubject(msg.getSubject());
						messageBox.setCode(idHeaders[0]);
						
						messageBox.setFrom(InternetAddress.toString(msg.getFrom()));
						messageBox.setTo(parseAddresses(msg
								.getRecipients(RecipientType.TO)));


//						String[] replyHeaders = msg.getHeader("In-Reply-TO");
						String[] replyHeaders = msg.getHeader("References");
						
											
						if (replyHeaders != null && replyHeaders.length > 0){
							StringTokenizer tokens = new StringTokenizer(replyHeaders[0]);
							MessageBox parent = repositoryMailBox.getMessageBox(tokens.nextToken());
							if (parent != null) parent.addReply(messageBox);
						}



						
						//						saveParts(msg.getContent(), filename,  msg.getContentType());
//						File file = saveParts(msg);
//						messageBox.setFile(filename);
						result = parseSubjectAndCreate(messageBox, msg);
					}
				}
			}


			folderInbox.close(false);
			store.close();

			return result;

		} catch (NoSuchProviderException ex) {
			System.out.println("No provider for protocol: " + protocol);
			ex.printStackTrace();
		} catch (MessagingException ex) {
			System.out.println("Could not connect to the message store");
			ex.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		result.setSingleElement(false);
		return result;
	}

	private ResultClass<Boolean> parseSubjectAndCreate(MessageBox messageBox, Message msg) throws MessagingException, IOException{
		

		ResultClass<Boolean> result = new ResultClass<>();

		
		Pattern p = Pattern.compile(pattern);            	
		Matcher m = p.matcher(messageBox.getSubject());
		if (m.matches()){
			
			String mimeType = msg.getAllHeaders() + msg.getContentType();
			String key = getStorageKey(Long.parseLong(m.group(3)));

			storageManager.putObject(bucket, key, mimeType, msg.getInputStream());			
			String aaa=storageManager.getUrl(bucket, key).toExternalForm();
			messageBox.setFile(aaa);

			if (("group").equalsIgnoreCase(m.group(2))){
				Group group = serviceGroup.getGroupFormatter(Long.parseLong(m.group(3)));
				group.getMessages().add(messageBox);				
				result = serviceGroup.updateGroup(group);
				

			}

			else if (("course").equalsIgnoreCase(m.group(2))){
				Course course = serviceCourse.getCourseFormatter(Long.parseLong(m.group(3)));
				course.getMessages().add(messageBox);
				result = serviceCourse.updateCourse(course);

			}

		}
		return result;
	}
	private String parseAddresses(Address[] address) {
		String listAddress = "";

		if (address != null) {
			for (int i = 0; i < address.length; i++) {
				listAddress += address[i].toString() + ", ";
			}
		}
		if (listAddress.length() > 1) {
			listAddress = listAddress.substring(0, listAddress.length() - 2);
		}

		return listAddress;
	}

//	private File saveParts(Message msg)//, String filename)
//			throws IOException, MessagingException
//	{
//		File file= null;
//		OutputStream out = null;
//		InputStream in = null;
//		
//		try{
//			
//			in = msg.getInputStream();
//			int k;
////			filename = filename + ".txt";
//			file = File.createTempFile("tmp", ".txt");
//			;
////			FileUtils.copyInputStreamToFile(msg.getInputStream(), file);
//		
//			out = new FileOutputStream(file);
//			while ((k = in.read()) != -1) {
//				out.write(k);
//			}
//			
//		} catch (Exception ex) {
//
//			ex.printStackTrace();
//		}
//		//		try {
//		//			if (content instanceof Multipart) {
//		//				Multipart multi = ((Multipart)content);
//		//				int parts = multi.getCount();
//		//				for (int j=0; j < parts; ++j) {
//		//					MimeBodyPart part = (MimeBodyPart)multi.getBodyPart(j);
//		//					if (part.getContent() instanceof Multipart) {
//		//						// part-within-a-part, do some recursion...
//		//						saveParts(part.getContent(), filename);
//		//					}
//		//					else {
//		//						String extension = "";
//		//						if (part.isMimeType("text/html")) {
//		//							extension = "html";
//		//						}
//		//						else {
//		//							if (part.isMimeType("text/plain")) {
//		//								extension = "txt";
//		//							}
//		//							else {
//		//								//  Try to get the name of the attachment
//		//								extension = part.getDataHandler().getName();
//		//							}
//		//							filename = filename + "." + extension;
//		//							System.out.println("... " + filename);
//		//							out = new FileOutputStream(new File(filename));
//		//
//		//							in = part.getInputStream();
//		//							int k;
//		//							while ((k = in.read()) != -1) {
//		//								out.write(k);
//		//							}
//		//						}
//		//					}
//		//				}
//		//			}
//		//			else if (msg instanceof String){
//		//				try {
//		//					filename = filename+".txt";
//		//					if (msg != null) {
//		//						System.out.println("... " + filename);
//		//						out = new FileOutputStream(new File(filename));
//		//						String c = msg.toString();
//		//						out.write(c.getBytes());
//		//
//		//
//		//					}
//		//
//		//
//		//				} catch (Exception ex) {
//		//
//		//					ex.printStackTrace();
//		//				}
//		//			}
//		//		}
//		finally {
//			if (in != null) { in.close(); }
//			if (out != null) { out.flush(); out.close(); }
//		}
//		return file;
//	}

	@Transactional(readOnly=true)
	public ResultClass<MessageBox> getMessages() {


		ResultClass<MessageBox> result = new ResultClass<>();

		result.addAll(repositoryMailBox.getAllMessages());

		return result;

	}
	
	private String getStorageKey(Long id) {
		return "attachment/"+Long.toString(id);
	}




}