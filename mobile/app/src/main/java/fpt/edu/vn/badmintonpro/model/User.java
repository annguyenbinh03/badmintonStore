package fpt.edu.vn.badmintonpro.model;

public class User {
    private int userId;
    private String email;
    private String password;
    private String fullName;
    private String role;
    private boolean isGoogleLogin;

    public User(int userId, String email, String password, String fullName, String role, boolean isGoogleLogin) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.fullName = fullName;
        this.role = role;
        this.isGoogleLogin = isGoogleLogin;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isGoogleLogin() {
        return isGoogleLogin;
    }

    public void setGoogleLogin(boolean googleLogin) {
        isGoogleLogin = googleLogin;
    }
}
