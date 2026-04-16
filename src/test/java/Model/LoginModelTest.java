package Model;

import org.junit.jupiter.api.Test;

public class LoginModelTest {
    @Test
    void testCreateUser() {
        LoginModel model = new LoginModel();
        model.createUser("Test", "Model", "test.model@laplateforme.io", "test");
    }

    @Test
    void testLogin() {
        LoginModel model = new LoginModel();
        model.login("test.model@laplateforme.io", "test");
        
    }
}
