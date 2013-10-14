package net.mbreslow.gradekeeper;

import org.junit.Test;

import java.util.Collection;

import static junit.framework.TestCase.*;

/**
 * Unit tests for the Teacher class
 */
public class TeacherTest {
    @Test
    public void testBasics() throws Exception {
        ScoringPreferences preferences = new ScoringPreferences(.8, .2, 1);
        final String teacherName = "Ms. Beth";
        Teacher teacher = new Teacher(teacherName, preferences);
        final String studentName = "jake";
        Student jake = teacher.addStudent(studentName);
        assertNotNull(jake);
        assertEquals(studentName, jake.getName());
        assertSame(jake, teacher.getStudent(studentName));
        assertNull(teacher.getStudent("jack"));
        assertEquals(teacherName, teacher.getName());
        assertSame(preferences, teacher.getScoringPreferences());
        ScoringPreferences preferences2 = new ScoringPreferences(.8, .2,1);
        teacher.setScoringPreferences(preferences2);
        assertSame(preferences2, teacher.getScoringPreferences());
    }

    @Test
    public void testGetStudents() throws Exception {
        ScoringPreferences preferences = new ScoringPreferences(.8, .2, 1);
        final String teacherName = "teacher";
        Teacher t = new Teacher(teacherName, preferences);
        final int numStudents = 10;
        final String studentNamePrefix = "student";
        for (int i=0; i< numStudents; i++) {
            final String studentName = studentNamePrefix + i;
            Student student = t.addStudent(studentName);
            assertSame(student, t.getStudent(studentName));
        }
        Collection<Student> students = t.getStudents();
        assertEquals(numStudents, students.size());
        boolean matches[] = new boolean[numStudents]; // initilized to default value (false)

        for (Student s : students) {
            int studentNum = Integer.parseInt( s.getName().substring(studentNamePrefix.length()) );
            assertFalse("Found student number " + studentNum + " more than once", matches[studentNum]);
            matches[studentNum] = true;
        }

        for (int i=0; i<matches.length; i++) {
            assertTrue("Didn't find student number " + i + " in teacher.getStudents()", matches[i]);
        }
    }

    @Test
    public void testStudentNameCollision() throws Exception {
        ScoringPreferences preferences = new ScoringPreferences(.8, .2, 1);
        final String teacherName = "teacher";
        Teacher t = new Teacher(teacherName, preferences);
        final String studentName = "student";
        Student student = t.addStudent(studentName);
        try {
            t.addStudent(studentName);
            fail("Expected a NameCollisionError but none was thrown");
        }
        catch (NameCollisionError e) {
            final String expectedErrorMessage = "A student already exists with the name " + studentName;
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }

}
