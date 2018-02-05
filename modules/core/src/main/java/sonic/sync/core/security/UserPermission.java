package sonic.sync.core.security;

import java.io.Serializable;

public class UserPermission implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public UserPermission(String creatorName, PermissionType write) {
		// TODO Auto-generated constructor stub
	}

	public UserPermission(UserPermission userPermission) {
		// TODO Auto-generated constructor stub
	}

	public enum PermissionType {
		WRITE
		
	}

	public String getUserId() {
		// TODO Auto-generated method stub
		return null;
	}

	public PermissionType getPermission() {
		// TODO Auto-generated method stub
		return null;
	}

}
