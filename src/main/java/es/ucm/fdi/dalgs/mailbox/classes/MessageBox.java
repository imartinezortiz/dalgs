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
package es.ucm.fdi.dalgs.mailbox.classes;


public class MessageBox {
	
	private Integer id;
	private String recipients;
	private String owner;
	private String recipientsCC;
	private String subject;
	private String sentDate;
	private String message;
	
	public MessageBox() {
		super();
	}
	
	
	
	public MessageBox(Integer id,  String owner, String recipients,
			String recipientsCC, String subject, String sentDate, String message) {
		super();
		this.id = id;
		this.owner = owner;

		this.recipients = recipients;
		this.recipientsCC = recipientsCC;
		this.subject = subject;
		this.sentDate = sentDate;
		this.message = message;
	}



	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getrecipients() {
		return recipients;
	}
	public void setrecipients(String recipients) {
		this.recipients = recipients;
	}
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getrecipientsCC() {
		return recipientsCC;
	}
	public void setrecipientsCC(String recipientsCC) {
		this.recipientsCC = recipientsCC;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getSentDate() {
		return sentDate;
	}
	public void setSentDate(String sentDate) {
		this.sentDate = sentDate;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public String toString() {
		return "\nMessageBox #" + id +
				" \n\tFrom:"+ owner + 
				" \n\tTo:" + recipients + 
				" \n\tCC:" + recipientsCC + 
				" \n\tSubject:"+ subject + ""+
				" \n\tSent Date:" + sentDate + 
				" \n\tMessage:" + message + "\n";
	}
	
}
