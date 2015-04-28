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
package es.ucm.fdi.dalgs.domain.acl;

/*Default  permissions to use in the ACL system*/
import org.springframework.security.acls.domain.AbstractPermission;
import org.springframework.security.acls.model.Permission;

public class BasePermission extends AbstractPermission {
	/**
	 * 
	 */

	/* Default permissions to use in the ACL system */
	private static final long serialVersionUID = 1L;
	public static final Permission READ = new BasePermission(1 << 0, 'R'); // 1
	public static final Permission WRITE = new BasePermission(1 << 1, 'W'); // 2
	public static final Permission CREATE = new BasePermission(1 << 2, 'C'); // 4
	public static final Permission DELETE = new BasePermission(1 << 3, 'D'); // 8
	public static final Permission ADMINISTRATION = new BasePermission(1 << 4,
			'A'); // 16

	protected BasePermission(int mask) {
		super(mask);
	}

	protected BasePermission(int mask, char code) {
		super(mask, code);
	}
}