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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import org.springframework.stereotype.Service;

import es.ucm.fdi.dalgs.mailbox.classes.IMAP;
import es.ucm.fdi.dalgs.mailbox.classes.MessageBox;
 
/**
 * Get e-mail messages from a POP3/IMAP server
 *
 */
@Service
public class MailBoxService{
 
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
    public Collection<MessageBox> downloadEmails(IMAP imap) {
        Properties properties = getServerProperties(imap.getProtocol(), imap.getHost(), imap.getPort());
        Session session = Session.getDefaultInstance(properties);
 
        try {
        	
        	Collection<MessageBox> messagesbox = new ArrayList<MessageBox>();
            
        	// connects to the message store
            Store store = session.getStore(imap.getProtocol());
            store.connect(imap.getUserName(), imap.getPassword());
 
            // opens the inbox folder
            Folder folderInbox = store.getFolder("INBOX");
            folderInbox.open(Folder.READ_ONLY);
 
            // fetches new messages from server
            Message[] messages = folderInbox.getMessages();
 
            for (int i = 0; i < messages.length; i++) {
            	
            	
                Message msg = messages[i];
                Address[] fromAddress = msg.getFrom();
                String from = fromAddress[0].toString();
                String subject = msg.getSubject();
                String toList = parseAddresses(msg
                        .getRecipients(RecipientType.TO));
                String ccList = parseAddresses(msg
                        .getRecipients(RecipientType.CC));
                String sentDate = msg.getSentDate().toString();
 
                String contentType = msg.getContentType();
                String messageContent = "";
 
                
                
                if (contentType.contains("text/plain")
                        || contentType.contains("text/html")) {
                    try {
                        Object content = msg.getContent();
                        if (content != null) {
                            messageContent = content.toString();
                        }
                    } catch (Exception ex) {
                        messageContent = "[Error downloading content]";
                        ex.printStackTrace();
                    }
                }
                 else if (contentType.contains("multipart/"))
                {
                	 try {
                         Multipart mp = (Multipart) msg.getContent();
                         messageContent = mp.getBodyPart(0).getContent().toString();
     				} catch (IOException e) {
                        messageContent = "[Error downloading content]";
     					e.printStackTrace();
     				}
                    
                }
 
            	MessageBox msgBox = new MessageBox(i+1, from, toList, ccList,subject,sentDate, messageContent);
            	messagesbox.add(new MessageBox(i+1, from, toList, ccList,subject,sentDate, messageContent));
            	
                // print out details of each message
                System.out.println(msgBox.toString());
            }
 
            // disconnect
            folderInbox.close(false);
            store.close();
            
            return messagesbox;
            
        } catch (NoSuchProviderException ex) {
            System.out.println("No provider for protocol: " + imap.getProtocol());
            ex.printStackTrace();
        } catch (MessagingException ex) {
            System.out.println("Could not connect to the message store");
            ex.printStackTrace();
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
 
}