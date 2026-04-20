package Model;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.Test;

public class StudentModelTest {

    @Test
    void testSelectAllStudent() {
        StudentModel model = new StudentModel();
        
        var result = model.selectAllStudent();
        
        assertNotNull(result);
        System.out.println(Arrays.toString(result));
    }

    @Test
    void testDeleteStudent() {
        StudentModel model = new StudentModel();
        model.deleteStudent(6);
    }

    @Test
    void testUpdateStudent() {
        StudentModel model = new StudentModel();
        model.updateStudent(2, "B", "A", 30, 17);
    }

    @Test
    void testAddStudent() {
        StudentModel model = new StudentModel();
        model.addStudent("test", "TEST", 50, 15);
    }

    @Test
    void testFilter(){
        StudentModel model = new StudentModel();
        System.out.println(Arrays.toString(model.filter(1, "test3", "TEST3", "45", "20")));
        System.out.println(Arrays.toString(model.filter(1, "test3", "TEST3", ">23", ">10")));
        System.out.println(Arrays.toString(model.filter(1, "test3", "TEST3", "<100", "<100")));
        System.out.println(Arrays.toString(model.filter(0, "", "", "", "")));
        
    }

    @Test
    void testSort() {
        StudentModel model = new StudentModel();
        System.out.println(Arrays.toString(model.sort("grade", 1)));
        System.out.println(Arrays.toString(model.sort("age", 0))); // ordre vide
    }

    @Test
    void testFilterSort() {
        StudentModel model = new StudentModel();
        System.out.println(Arrays.toString(model.filter(1, "test3", "TEST3", "45", "20")));
        System.out.println(Arrays.toString(model.sort("grade", 1)));
    }

    @Test
    void testUpdateStudentAverage() {
        StudentModel model = new StudentModel();
        model.updateStudentAverage(2, 20);
    }
}