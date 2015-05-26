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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import es.ucm.fdi.dalgs.domain.MessageBox;
import es.ucm.fdi.dalgs.mailbox.service.MailBoxService;

@Controller
public class MailBoxController {



	@Autowired
	private MailBoxService serviceMailBox;


	@RequestMapping(value = "/mailbox")
	public ModelAndView getMailBox(Model model) {
		
		Map<String, Object> myModel = new HashMap<String, Object>();

        Collection<MessageBox> msgs= serviceMailBox.downloadEmails();

		myModel.put("mails", msgs);

		return new ModelAndView("mail/list", "model", myModel);
	}

	
}