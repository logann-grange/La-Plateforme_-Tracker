package Model;

import java.time.LocalDate;
import java.util.Arrays;

import org.junit.jupiter.api.Test;


public class GradeModelTest {
    @Test
    void testSelectAllGrade() {
        GradeModel model = new GradeModel();
        System.out.println(Arrays.toString(model.selectAllGrade()));   
    }

    @Test
    void testGetGrade() {
        GradeModel model = new GradeModel();
        System.out.println(Arrays.toString(model.getGrades(1)));   
    }

    @Test
    void testInsertGrade() {
        GradeModel model = new GradeModel();
        model.insertGrade(1, "test", 10, LocalDate.of(2026, 3, 10));
    }

    @Test
    void testUpdateGrade() {
        GradeModel model = new GradeModel();
        model.updateGrade(1, "TEST", 20, LocalDate.of(2026, 1, 1));
    }

    @Test
    void testDeleteGrade() {
        GradeModel model = new GradeModel();
        model.deleteGrade(2);

    }
}
