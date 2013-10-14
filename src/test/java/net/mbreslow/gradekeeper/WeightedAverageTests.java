package net.mbreslow.gradekeeper;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: marc2112
 * Date: 10/12/13
 * Time: 5:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class WeightedAverageTests {
    @Test
    public void testGivenSample() throws Exception {
        ScoringPreferences tomTeachermanAllocations = new ScoringPreferences(0.101f, 0.899f, 2f);
        Student sally = new Student("Sally Student");
        sally.addAssignment(85f);
        assertEquals("after assignment 1", 85f, roundToTwoPlaces(sally.getWeightedAverage(tomTeachermanAllocations)), 0);

        sally.addAssignment(88f);
        assertEquals("after assignment 2", 86.5f, roundToTwoPlaces(sally.getWeightedAverage(tomTeachermanAllocations)), 0);

        sally.addExtraCredit();
        assertEquals("after extra credit 1", 88.5f, roundToTwoPlaces(sally.getWeightedAverage(tomTeachermanAllocations)), 0);

        sally.addAssignment(92f);
        assertEquals("after assignment 3", 90.33f, roundToTwoPlaces(sally.getWeightedAverage(tomTeachermanAllocations)), 0);

        sally.addExam(91f);
        assertEquals("after exam 1", 90.93f, roundToTwoPlaces(sally.getWeightedAverage(tomTeachermanAllocations)), 0);
    }

    @Test
    public void testWithAllZeros() throws Exception {
        ScoringPreferences tomTeachermanAllocations = new ScoringPreferences(1f, 0f, 0f);
        Student sally = new Student("Sally Student");
        assertEquals("after exam 1", 0.0f, roundToTwoPlaces(sally.getWeightedAverage(tomTeachermanAllocations)), 0);
    }

    @Test
    public void testZerosAndAPlusses() throws Exception {
        ScoringPreferences tomTeachermanAllocations = new ScoringPreferences(0.101f, 0.899f, 2f);
        Student sally = new Student("Sally Student");
        sally.addAssignment(85f);
        assertEquals("after assignment 1", 85f, roundToTwoPlaces(sally.getWeightedAverage(tomTeachermanAllocations)), 0);

        sally.addAssignment(88f);
        assertEquals("after assignment 2", 86.5f, roundToTwoPlaces(sally.getWeightedAverage(tomTeachermanAllocations)), 0);

        sally.addExtraCredit();
        assertEquals("after extra credit 1", 88.5f, roundToTwoPlaces(sally.getWeightedAverage(tomTeachermanAllocations)), 0);

        sally.addAssignment(92f);
        assertEquals("after assignment 3", 90.33f, roundToTwoPlaces(sally.getWeightedAverage(tomTeachermanAllocations)), 0);

        sally.addExam(91f);
        assertEquals("after exam 1", 90.93f, roundToTwoPlaces(sally.getWeightedAverage(tomTeachermanAllocations)), 0);

        sally.addExam(0f);
        assertEquals("after exam 2", 50.03f, roundToTwoPlaces(sally.getWeightedAverage(tomTeachermanAllocations)), 0);

        sally.addExam(107f);
        assertEquals("after exam 3", 68.46f, roundToTwoPlaces(sally.getWeightedAverage(tomTeachermanAllocations)), 0);

    }

    @Test
    public void testWithNoAssignments() throws Exception {
        ScoringPreferences tomTeachermanAllocations = new ScoringPreferences(0.101f, 0.899f, 2f);
        Student sally = new Student("Sally Student");
        sally.addExam(82.5f);
        sally.addExam(68.4f);
        sally.addExam(107f);
        assertEquals(85.96666667f, sally.getWeightedAverage(tomTeachermanAllocations), 0);
    }

    @Test
    public void testWithNoExams() throws Exception {
        ScoringPreferences tomTeachermanAllocations = new ScoringPreferences(0.101f, 0.899f, 2f);
        Student sally = new Student("Sally Student");
        sally.addAssignment(82.5f);
        sally.addAssignment(68.4f);
        sally.addAssignment(107f);
        assertEquals(85.96666667f, sally.getWeightedAverage(tomTeachermanAllocations), 0);
    }



    @Test
    public void testRoundingFunction() {
        assertEquals(1.01f, roundToTwoPlaces(1.01111111111f), 0);
        assertEquals(1.01f, roundToTwoPlaces(1.01f), 0);
        assertEquals(1.01f, roundToTwoPlaces(1.009f), 0);
    }


    private float roundToTwoPlaces(float number) {
        return (float) Math.round(number * 100) / 100;
    }
}
