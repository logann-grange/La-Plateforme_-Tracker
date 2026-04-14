package Controller;
import Model.StudentModel;
import javafx.scene.Scene;
import Vue.Components.ActionBar;

public class StudentControler {
    
    private ActionBar vue;
    private StudentModel model;
    private Scene scene;
    

    public void studentControler(ActionBar vue, StudentModel model, Scene scene) {
        this.vue = vue;
        this.model = model;
        this.scene = scene;
    
        this.vue.getAddButton().setOnAction(e -> handleAddStudent());
    }

    public void handleAddStudent(){
        
        String firstName = vue.getFirstNameField().getText();
        String lastName = vue.getLastNameField().getText();
        int age = Integer.parseInt(vue.getAgeField().getText());
        int garde = Integer.parseInt(vue.getGradeField().getText());

        if (firstName == null || firstName.trim().isEmpty() || lastName == null || lastName.trim().isEmpty() || age <= 0 || garde < 0 || garde > 20) {} 
        else {
            this.model.addStudent(firstName, lastName, age, garde);
        }
    }

}
