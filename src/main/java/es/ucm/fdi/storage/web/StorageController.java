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
package es.ucm.fdi.storage.web;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import es.ucm.fdi.storage.business.boundary.StorageManager;
import es.ucm.fdi.storage.business.entity.StorageObject;

@Controller
public class StorageController {

	private static final Logger logger = LoggerFactory
			.getLogger("es.ucm.fdi.storage");

	@Autowired
	private StorageManager storage;
	
	@RequestMapping(method=RequestMethod.GET, value="/storage/**")
	public void servirArchivos(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String ctxPath = request.getContextPath();
		String objectId = request.getRequestURI();
		objectId = objectId.substring(ctxPath.length()+"/storage/".length());
		int pos = objectId.indexOf('/');
		if (pos < 0) {
			response.sendError(400, "Upps");
			return;
		}
		String bucket = objectId.substring(0, pos);
		String key = objectId.substring(pos+1);
		StorageObject object = storage.getObject(bucket, key);
		String mimeType = object.getMimeType();
		long length = object.getContentLength();

		// set content attributes for the response
		response.setContentType(mimeType);
		response.setContentLength((int) length);

		try {
			object.transferTo(response.getOutputStream());
			response.flushBuffer();
		} catch (IOException ex) {
			logger.error("Error writing file to output stream. bucket=>'{}', key=>'{}'", bucket, key,
					ex);
			response.sendError(500, "Upps");
		}
	}
}
