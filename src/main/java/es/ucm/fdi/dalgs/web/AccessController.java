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
package es.ucm.fdi.dalgs.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccessController {

	@RequestMapping("/login.htm")
	public String login(Model model,
			@RequestParam(required = false) String message) {
		model.addAttribute("message", message);
		return "access/login";
	}

	@RequestMapping(value = "/denied.htm")
	public String denied() {
		return "access/denied";
	}

	@RequestMapping(value = "/login/failure.htm")
	public String loginFailure() {
		String message = "Login Failure!";
		return "redirect:/login.htm?message=" + message;
	}

	@RequestMapping(value = "/logout/success")
	public String logoutSuccess() {
		String message = "Logout Success!";
		return "redirect:/login.htm?message=" + message;
	}
	
	@RequestMapping(value = "/logout")
	public String logout() {
		return "redirect:/login.htm";
	}

}