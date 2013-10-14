package net.mbreslow.gradekeeper;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: marc2112
 * Date: 10/13/13
 * Time: 8:45 PM
 * To change this template use File | Settings | File Templates.
 */
public class GradeTrackerImplTest {
    /**
     * Create a new GradeTrackerImpl for testing.  In the wild, we'd expect this to be created using a configurable
     * factory like Spring BeanFactory or dependency injection like Spring, Guice or CDI
     */
    private GradeTracker service = new GradeTrackerImpl();

    @Test
    public void testAddUpdateTeacher() throws Exception {
        ScoringPreferences preferences = new ScoringPreferences(.8f,.2f,3);
        final String teacherName = "teacher1";
        service.addTeacher(teacherName, preferences);
        try {
            service.updateTeacher(teacherName, preferences);
        }
        catch (ObjectNotFoundError e) {
            fail("Unexpected error should only occur when the teacher doesn't exist");
        }
    }

    @Test
    public void testRecordAssignmentScore() throws Exception {
        ScoringPreferences preferences1 = new ScoringPreferences(0.2f, 0.8f, 2f);
        ScoringPreferences preferences2 = new ScoringPreferences(0.4f, 0.6f, 2f);
        String teacherName = "teacher";
        String studentName = "student";
        service.addTeacher(teacherName, preferences1);
        final float examScore = 60;
        service.recordExamScore(teacherName, studentName, examScore);
        final float assignmentScore = 80;
        service.recordAssignmentScore(teacherName, studentName, assignmentScore);
        float avg = service.getStudentAverage(teacherName, studentName);
        float expectedAvg = examScore * preferences1.getWeightExams() + assignmentScore * preferences1.getWeightAssignments();
        assertEquals(expectedAvg, avg, 0);
        service.updateTeacher(teacherName, preferences2);
        avg = service.getStudentAverage(teacherName, studentName);
        expectedAvg = examScore * preferences2.getWeightExams() + assignmentScore * preferences2.getWeightAssignments();
        assertEquals("weights didn't change after updateTeacher call as expected", expectedAvg, avg, 0);
    }

    @Test
    public void testRecordExtraCredit() throws Exception {
        ScoringPreferences preferences1 = new ScoringPreferences(0.2f, 0.8f, 2f);
        String teacherName = "teacher";
        String studentName = "student2";
        service.addTeacher(teacherName, preferences1);
        final float examScore = 60;
        service.recordExamScore(teacherName, studentName, examScore);
        final float assignmentScore = 80;
        service.recordAssignmentScore(teacherName, studentName, assignmentScore);
        float avg = service.getStudentAverage(teacherName, studentName);
        float expectedAvg = examScore * preferences1.getWeightExams() + assignmentScore * preferences1.getWeightAssignments();
        assertEquals(expectedAvg, avg, 0);
        service.recordExtraCredit(teacherName, studentName);
        avg = service.getStudentAverage(teacherName, studentName);
        expectedAvg = examScore * preferences1.getWeightExams() + ((assignmentScore + preferences1.getExtraCreditBonus()) * preferences1.getWeightAssignments());
        assertEquals(expectedAvg, avg, 0);
    }

    @Test
    public void testMissingTeacher() throws Exception {
        final String missingTeacherName = "Missing Teacher";
        final String studentName = "student";
        try {
            service.recordExamScore(missingTeacherName, studentName, 100);
            fail("Expected to get ObjectNotFoundError");
        }
        catch (ObjectNotFoundError e) {
            final String expected = "No teacher with the name " + missingTeacherName + " exists";
            assertEquals(expected, e.getMessage());
        }
    }
}
