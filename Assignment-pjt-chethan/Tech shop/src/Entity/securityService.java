package Entity;
import Exception.AuthorizationException;
import Exception.AuthenticationException;
public class securityService {
    private static final String VALID_USERNAME = "user123";
    private static final String VALID_PASSWORD = "password123";

    public securityService() {
    }

    public void authenticateUser(String username, String password) throws AuthenticationException {
        if (!this.isValidCredentials(username, password)) {
            throw new AuthenticationException("Authentication failed. Invalid username or password.");
        } else {
            System.out.println("User authenticated successfully!");
        }
    }

    public void authorizeAccess(String username) throws AuthorizationException {
        if (!this.isAdminUser(username)) {
            throw new AuthorizationException("Unauthorized access. User does not have sufficient privileges.");
        } else {
            System.out.println("Authorization successful. User has access to sensitive information.");
        }
    }

    private boolean isValidCredentials(String username, String password) {
        return "user123".equals(username) && "password123".equals(password);
    }

    private boolean isAdminUser(String username) {
        return "user123".equals(username);
    }
}
