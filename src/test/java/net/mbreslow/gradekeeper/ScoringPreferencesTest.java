package net.mbreslow.gradekeeper;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created with IntelliJ IDEA.
 * User: marc2112
 * Date: 10/12/13
 * Time: 5:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class ScoringPreferencesTest {
    @Test
    public void testScoringPreferences() throws Exception {

        float weightAssignments = .8f;
        float weightExams = .2f;
        float extraCreditBonus = 30;

        ScoringPreferences prefs = new ScoringPreferences(weightAssignments, weightExams, extraCreditBonus);
        assertEquals("weightAssignments", weightAssignments, prefs.getWeightAssignments(), 0);
        assertEquals("weightExams", weightExams, prefs.getWeightExams(), 0);
        assertEquals("extraCreditBonus", extraCreditBonus, prefs.getExtraCreditBonus(), 0);
    }

    @Test
    public void testIllegalWeights() throws Exception {
        float weightAssignments = .9f;
        float weightExams = .2f;
        float extraCreditBonus = 30;

        try {
            ScoringPreferences prefs = new ScoringPreferences(weightAssignments, weightExams, extraCreditBonus);
            fail("Expected IllegalArgumentException was not found");
        } catch (IllegalArgumentException e) {
            final String expected = "weightAssignments (" + weightAssignments + ") + weightExams (" + weightExams + ") must sum to 1.  " + (weightAssignments + weightExams) + " != 1";
            assertEquals(expected, e.getMessage());
        }
    }
}
