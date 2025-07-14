package fpt.edu.vn.badmintonpro.model;

public class LoginResponse {
    private boolean success;
    private String message;
    private User data;

    public boolean isSuccess() { return success; }
    public String getMessage() { return message; }
    public User getData() { return data; }
}
