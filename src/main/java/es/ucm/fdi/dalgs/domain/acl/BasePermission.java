package es.ucm.fdi.dalgs.domain.acl;

/*Default  permissions to use in the ACL system*/
import org.springframework.security.acls.domain.AbstractPermission;
import org.springframework.security.acls.model.Permission;

public class BasePermission extends AbstractPermission {
	/**
	 * 
	 */
	
	/*Default  permissions to use in the ACL system*/
	private static final long serialVersionUID = 1L;
	public static final Permission READ = new BasePermission(1 << 0, 'R'); // 1
	public static final Permission WRITE = new BasePermission(1 << 1, 'W'); // 2
	public static final Permission CREATE = new BasePermission(1 << 2, 'C'); // 4
	public static final Permission DELETE = new BasePermission(1 << 3, 'D'); // 8
	public static final Permission ADMINISTRATION = new BasePermission(1 << 4, 'A'); // 16

	protected BasePermission(int mask) {
		super(mask);
	}

	protected BasePermission(int mask, char code) {
		super(mask, code);
	}
}