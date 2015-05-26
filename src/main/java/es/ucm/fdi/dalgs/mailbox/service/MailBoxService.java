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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage.RecipientType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.dalgs.course.service.CourseService;
import es.ucm.fdi.dalgs.domain.Course;
import es.ucm.fdi.dalgs.domain.Group;
import es.ucm.fdi.dalgs.domain.MessageBox;
import es.ucm.fdi.dalgs.group.service.GroupService;
import es.ucm.fdi.dalgs.mailbox.respository.MessageBoxRepository;

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
	
	@Autowired
	private MessageBoxRepository repositoryMessageBox;

	@Autowired
	CourseService serviceCourse;

	@Autowired
	GroupService serviceGroup;


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
	public Collection<MessageBox> downloadEmails() {
		//    	String pattern= "^\\s*\\[(course|group):(\\d+)\\]";
		String pattern = "\\[(course|group):(\\d+)\\]";


		Properties properties = getServerProperties(protocol, host, port);
		Session session = Session.getDefaultInstance(properties);

		try {

			Collection<MessageBox> messagesbox = new ArrayList<MessageBox>();

			// connects to the message store
			Store store = session.getStore(protocol);
			store.connect("dalgs.tfg15@gmail.com", "bd5118af22b7c18989991eb0da3eefb6cfeba1f4");

			// opens the inbox folder
			Folder folderInbox = store.getFolder("INBOX");
			folderInbox.open(Folder.READ_ONLY);

			// fetches new messages from server
			Message[] messages = folderInbox.getMessages();
			for (int i = 0; i < messages.length; i++) {
				Message msg = messages[i];

				MessageBox messageBox = new MessageBox();
				messageBox.setSubject(msg.getSubject());
				messageBox.setFrom(msg.getFrom()[0].toString());
				
				
	
				messageBox.setTo(parseAddresses(msg
		                        .getRecipients(RecipientType.TO)));
               
				String filename = "/Users/RobertoGS/Desktop/prueba";
				saveParts(msg.getContent(), filename);
				messageBox.setFile(filename);
				Pattern p = Pattern.compile(pattern);            	
				Matcher m = p.matcher(messageBox.getSubject());

				if (m.matches()){


					if ((m.group(1)).equalsIgnoreCase("group")){
						Group group = serviceGroup.getGroupFormatter(Long.parseLong(m.group(2)));
//						repositoryMessageBox.addMessageBox(messageBox);
						group.getMessages().add(messageBox);
						serviceGroup.updateGroup(group);
					}

					else if ((m.group(1)).equalsIgnoreCase("course")){
						Course course = serviceCourse.getCourseFormatter(Long.parseLong(m.group(2)));
						course.getMessages().add(messageBox);
						serviceCourse.updateCourse(course);
					}

				}
			}

			//            for (int i = 0; i < messages.length; i++) {
			//            	
			//            	
			//                Message msg = messages[i];
			//                Address[] fromAddress = msg.getFrom();
			//                String from = fromAddress[0].toString();
			//                String subject = msg.getSubject();
			//                String toList = parseAddresses(msg
			//                        .getRecipients(RecipientType.TO));
			//                String ccList = parseAddresses(msg
			//                        .getRecipients(RecipientType.CC));
			//                String sentDate = msg.getSentDate().toString();
			// 
			//                String contentType = msg.getContentType();
			//                String messageContent = "";
			// 
			//                
			//                
			//                if (contentType.contains("text/plain")
			//                        || contentType.contains("text/html")) {
			//                    try {
			//                        Object content = msg.getContent();
			//                        if (content != null) {
			//                            messageContent = content.toString();
			//                        }
			//                    } catch (Exception ex) {
			//                        messageContent = "[Error downloading content]";
			//                        ex.printStackTrace();
			//                    }
			//                }
			//                 else if (contentType.contains("multipart/"))
			//                {
			//                	 try {
			//                         Multipart mp = (Multipart) msg.getContent();
			//                         messageContent = mp.getBodyPart(0).getContent().toString();
			//     				} catch (IOException e) {
			//                        messageContent = "[Error downloading content]";
			//     					e.printStackTrace();
			//     				}
			//                    
			//                }
			// 
			//            	MessageBox msgBox = new MessageBox(i+1, from, toList, ccList,subject,sentDate, messageContent);
			//            	messagesbox.add(new MessageBox(i+1, from, toList, ccList,subject,sentDate, messageContent));
			//            	
			//                // print out details of each message
			//                System.out.println(msgBox.toString());
			//            }

			// disconnect
			folderInbox.close(false);
			store.close();

			return messagesbox;

		} catch (NoSuchProviderException ex) {
			System.out.println("No provider for protocol: " + protocol);
			ex.printStackTrace();
		} catch (MessagingException ex) {
			System.out.println("Could not connect to the message store");
			ex.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
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

	 public static void saveParts(Object content, String filename)
			  throws IOException, MessagingException
			  {
			    OutputStream out = null;
			    InputStream in = null;
			    try {
			      if (content instanceof Multipart) {
			        Multipart multi = ((Multipart)content);
			        int parts = multi.getCount();
			        for (int j=0; j < parts; ++j) {
			          MimeBodyPart part = (MimeBodyPart)multi.getBodyPart(j);
			          if (part.getContent() instanceof Multipart) {
			            // part-within-a-part, do some recursion...
			            saveParts(part.getContent(), filename);
			          }
			          else {
			            String extension = "";
			            if (part.isMimeType("text/html")) {
			              extension = "html";
			            }
			            else {
			              if (part.isMimeType("text/plain")) {
			                extension = "txt";
			              }
			              else {
			                //  Try to get the name of the attachment
			                extension = part.getDataHandler().getName();
			              }
			              filename = filename + "." + extension;
			              System.out.println("... " + filename);
			              out = new FileOutputStream(new File(filename));
			              
			              in = part.getInputStream();
			              int k;
			              while ((k = in.read()) != -1) {
			                out.write(k);
			              }
			            }
			          }
			        }
			      }
			    }
			    finally {
			      if (in != null) { in.close(); }
			      if (out != null) { out.flush(); out.close(); }
			    }
			  }


}