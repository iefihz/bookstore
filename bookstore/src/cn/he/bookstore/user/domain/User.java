package cn.he.bookstore.user.domain;

/**
 * JavaBean，对应数据库
 * @author He
 *
 */
public class User {
	
	//用户id，用于主键
	private String uid;
	
	//用户名
	private String username;
	
	//用户名
	private String password;
	
	//用户邮箱
	private String email;
	
	//校验码
	private String code;
	
	//是否校验
	private boolean state;

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isState() {
		return state;
	}

	public void setState(boolean state) {
		this.state = state;
	}

	@Override
	public String toString() {
		return "User [uid=" + uid + ", username=" + username + ", password=" + password + ", email=" + email + ", code="
				+ code + ", state=" + state + "]";
	}
	
	
}
