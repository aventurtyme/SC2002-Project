package sce.sc2002.alif.project;

public abstract class User {
	//attributes
	protected String userID;
	protected String name;
	protected String password;
	
	//constructor
	public User(String userID, String name, String password) {
        this.userID = userID;
        this.name = name;
        this.password = (password == null || password.isBlank()) ? "password" : password;
    }
	
	//accessor
	public String getUserID() {
		return userID;
	}
	public String getName() {
		return name;
	}
	
	//methods
	public boolean checkPassword(String pw) {
		return password.equals(pw);
	}
	
	//modifier
	public void changePassword(String newPassword) {
		if (newPassword != null && !newPassword.isBlank()) {
			this.password = newPassword;
		}
	}
	
	public String toString() {
		return String.format("%s (%s)", name, userID);
	}
}
