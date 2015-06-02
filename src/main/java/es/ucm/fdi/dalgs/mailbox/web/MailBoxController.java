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
package es.ucm.fdi.dalgs.mailbox.web;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import es.ucm.fdi.dalgs.classes.ResultClass;
import es.ucm.fdi.dalgs.domain.MessageBox;
import es.ucm.fdi.dalgs.mailbox.service.MailBoxService;

@Controller
public class MailBoxController {



	@Autowired
	private MailBoxService serviceMailBox;


	@RequestMapping(value = "/mailbox", method = RequestMethod.GET)
	public String getMailBox(Model model,
			@RequestParam(value = "messageId", defaultValue = "-1") Long id_message) {


        ResultClass<MessageBox> msgs= serviceMailBox.getMessages();
        model.addAttribute("showReplies",id_message);
        model.addAttribute("mails", msgs);

        return "mail/list";
	}


	
}