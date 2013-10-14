package net.mbreslow.gradekeeper;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests surrounding the weighted average calculation
 */
public class StudentTest {
    @Test
    public void testExamCountZero() throws Exception {
        ScoringPreferences tomTeachermanAllocations = new ScoringPreferences(0.101, 0.899, 2);
        Student sally = new Student("New Student");
        assertEquals(0, sally.getWeightedAverage(tomTeachermanAllocations), 0);
        sally.addExam(100);
        assertEquals(100, sally.getWeightedAverage(tomTeachermanAllocations), 0);
    }

    @Test
    public void testGivenSample() throws Exception {
        ScoringPreferences tomTeachermanAllocations = new ScoringPreferences(0.101, 0.899, 2);
        Student sally = new Student("Sally Student");
        assertEquals(0, sally.getWeightedAverage(tomTeachermanAllocations), 0);
        sally.addAssignment(85);
        assertEquals("after assignment 1", 85, roundToTwoPlaces(sally.getWeightedAverage(tomTeachermanAllocations)), 0);

        sally.addAssignment(88);
        assertEquals("after assignment 2", 86.5, roundToTwoPlaces(sally.getWeightedAverage(tomTeachermanAllocations)), 0);

        sally.addExtraCredit();
        assertEquals("after extra credit 1", 88.5, roundToTwoPlaces(sally.getWeightedAverage(tomTeachermanAllocations)), 0);

        sally.addAssignment(92);
        assertEquals("after assignment 3", 90.33, roundToTwoPlaces(sally.getWeightedAverage(tomTeachermanAllocations)), 0);

        sally.addExam(91);
        assertEquals("after exam 1", 90.93, roundToTwoPlaces(sally.getWeightedAverage(tomTeachermanAllocations)), 0);
    }

    @Test
    public void testWithAllZeros() throws Exception {
        ScoringPreferences tomTeachermanAllocations = new ScoringPreferences(1, 0, 0);
        Student sally = new Student("Sally Student");
        assertEquals("after exam 1", 0.0, roundToTwoPlaces(sally.getWeightedAverage(tomTeachermanAllocations)), 0);
    }

    @Test
    public void testZerosAndAPlusses() throws Exception {
        ScoringPreferences tomTeachermanAllocations = new ScoringPreferences(0.101, 0.899, 2);
        Student sally = new Student("Sally Student");
        sally.addAssignment(85);
        assertEquals("after assignment 1", 85, roundToTwoPlaces(sally.getWeightedAverage(tomTeachermanAllocations)), 0);

        sally.addAssignment(88);
        assertEquals("after assignment 2", 86.5, roundToTwoPlaces(sally.getWeightedAverage(tomTeachermanAllocations)), 0);

        sally.addExtraCredit();
        assertEquals("after extra credit 1", 88.5, roundToTwoPlaces(sally.getWeightedAverage(tomTeachermanAllocations)), 0);

        sally.addAssignment(92);
        assertEquals("after assignment 3", 90.33, roundToTwoPlaces(sally.getWeightedAverage(tomTeachermanAllocations)), 0);

        sally.addExam(91);
        assertEquals("after exam 1", 90.93, roundToTwoPlaces(sally.getWeightedAverage(tomTeachermanAllocations)), 0);

        sally.addExam(0);
        assertEquals("after exam 2", 50.03, roundToTwoPlaces(sally.getWeightedAverage(tomTeachermanAllocations)), 0);

        sally.addExam(107);
        assertEquals("after exam 3", 68.46, roundToTwoPlaces(sally.getWeightedAverage(tomTeachermanAllocations)), 0);

    }

    @Test
    public void testWithNoAssignments() throws Exception {
        ScoringPreferences tomTeachermanAllocations = new ScoringPreferences(0.101, 0.899, 2);
        Student sally = new Student("Sally Student");
        sally.addExam(82.5);
        sally.addExam(68.4);
        sally.addExam(107);
        assertEquals(85.97, roundToTwoPlaces(sally.getWeightedAverage(tomTeachermanAllocations)), 0);
    }

    @Test
    public void testWithNoExams() throws Exception {
        ScoringPreferences tomTeachermanAllocations = new ScoringPreferences(0.101, 0.899, 2);
        Student sally = new Student("Sally Student");
        sally.addAssignment(82.5);
        sally.addAssignment(68.4);
        sally.addAssignment(107);
        assertEquals(85.97, roundToTwoPlaces(sally.getWeightedAverage(tomTeachermanAllocations)), 0);
    }



    @Test
    public void testRoundingFunction() {
        assertEquals(1.01, roundToTwoPlaces(1.01111111111), 0);
        assertEquals(1.01, roundToTwoPlaces(1.01), 0);
        assertEquals(1.01, roundToTwoPlaces(1.009), 0);
    }


    private double roundToTwoPlaces(double number) {
        return (double) Math.round(number * 100) / 100;
    }
}
