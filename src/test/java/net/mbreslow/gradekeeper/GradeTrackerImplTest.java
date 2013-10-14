package net.mbreslow.gradekeeper;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;
import static org.junit.Assert.*;

/**
 * Unit tests for the {@link GradeTracker} service
 */
public class GradeTrackerImplTest {
    /**
     * Create a new GradeTrackerImpl for testing.  In the wild, we'd expect this to be created using a configurable
     * factory like Spring BeanFactory or dependency injection like Spring, Guice or CDI
     */
    private GradeTracker service = new GradeTrackerImpl();

    @Test
    public void testAddUpdateTeacher() throws Exception {
        ScoringPreferences preferences = new ScoringPreferences(.8,.2,3);
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
        ScoringPreferences preferences1 = new ScoringPreferences(0.2, 0.8, 2);
        ScoringPreferences preferences2 = new ScoringPreferences(0.4, 0.6, 2);
        String teacherName = "teacher";
        String studentName = "student";
        service.addTeacher(teacherName, preferences1);
        final double examScore = 60;
        service.recordExamScore(teacherName, studentName, examScore);
        final double assignmentScore = 80;
        service.recordAssignmentScore(teacherName, studentName, assignmentScore);
        double avg = service.getStudentAverage(teacherName, studentName);
        double expectedAvg = examScore * preferences1.getWeightExams() + assignmentScore * preferences1.getWeightAssignments();
        assertEquals(expectedAvg, avg, 0);
        service.updateTeacher(teacherName, preferences2);
        avg = service.getStudentAverage(teacherName, studentName);
        expectedAvg = examScore * preferences2.getWeightExams() + assignmentScore * preferences2.getWeightAssignments();
        assertEquals("weights didn't change after updateTeacher call as expected", expectedAvg, avg, 0);
    }

    @Test
    public void testRecordExtraCredit() throws Exception {
        ScoringPreferences preferences1 = new ScoringPreferences(0.2, 0.8, 2);
        String teacherName = "teacher";
        String studentName = "student2";
        service.addTeacher(teacherName, preferences1);
        final double examScore = 60;
        service.recordExamScore(teacherName, studentName, examScore);
        final double assignmentScore = 80;
        service.recordAssignmentScore(teacherName, studentName, assignmentScore);
        double avg = service.getStudentAverage(teacherName, studentName);
        double expectedAvg = examScore * preferences1.getWeightExams() + assignmentScore * preferences1.getWeightAssignments();
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



    @Test
    public void testTeacherNameCollision() throws Exception {
        ScoringPreferences preferences = new ScoringPreferences(.8, .2, 1);
        final String teacherName = "teacher collides";
        service.addTeacher(teacherName, preferences);


        try {
            service.addTeacher(teacherName, preferences);
            fail("Expected a NameCollisionError but none was thrown");
        }
        catch (NameCollisionError e) {
            final String expectedErrorMessage = "Unable to add a teacher named " + teacherName + " because one already exists.  Use unique names.";
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}
