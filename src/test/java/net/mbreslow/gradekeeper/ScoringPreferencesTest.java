package net.mbreslow.gradekeeper;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the ScoringPreferences class
 */
public class ScoringPreferencesTest {
    @Test
    public void testScoringPreferences() throws Exception {

        double weightAssignments = .8;
        double weightExams = .2;
        double extraCreditBonus = 30;

        ScoringPreferences prefs = new ScoringPreferences(weightAssignments, weightExams, extraCreditBonus);
        assertEquals("weightAssignments", weightAssignments, prefs.getWeightAssignments(), 0);
        assertEquals("weightExams", weightExams, prefs.getWeightExams(), 0);
        assertEquals("extraCreditBonus", extraCreditBonus, prefs.getExtraCreditBonus(), 0);
    }

    @Test
    public void testIllegalWeights() throws Exception {
        double weightAssignments = .9;
        double weightExams = .2;
        double extraCreditBonus = 30;

        try {
            ScoringPreferences prefs = new ScoringPreferences(weightAssignments, weightExams, extraCreditBonus);
            fail("Expected IllegalArgumentException was not found");
        } catch (IllegalArgumentException e) {
            final String expected = "weightAssignments (" + weightAssignments + ") + weightExams (" + weightExams + ") must sum to 1.  " + (weightAssignments + weightExams) + " != 1";
            assertEquals(expected, e.getMessage());
        }
    }
}
