package Model;

import org.junit.jupiter.api.Test;

public class LoginModelTest {
    @Test
    void testCreateUser() {
        loginModel model = new loginModel();
        model.createUser("Test", "Model", "test.model@laplateforme.io", "test");
    }

    @Test
    void testLogin() {
        loginModel model = new loginModel();
        model.login("test.model@laplateforme.io", "test");
        
    }
}
