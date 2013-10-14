package net.mbreslow.gradekeeper;

/**
 * Immutable class holds a teacher's scoring preferences
 */
public class ScoringPreferences {
    private float weightAssignments = 1;
    private float weightExams = 1;
    private float extraCreditBonus = .02f;

    /**
     * Constructor.
     * @param weightAssignments
     * @param weightExams
     * @param extraCreditBonus
     * @throws IllegalArgumentException when weightAssignments + weightExams does not sum to 1
     */
    public ScoringPreferences(float weightAssignments, float weightExams, float extraCreditBonus) {
        this.weightAssignments = weightAssignments;
        this.weightExams = weightExams;
        this.extraCreditBonus = extraCreditBonus;
        if (weightAssignments + weightExams != 1) {
            throw new IllegalArgumentException("weightAssignments (" + weightAssignments + ") + weightExams (" + weightExams + ") must sum to 1.  " + (weightAssignments + weightExams) + " != 1");
        }
    }

    public float getWeightAssignments() {
        return weightAssignments;
    }

    public float getWeightExams() {
        return weightExams;
    }

    public float getExtraCreditBonus() {
        return extraCreditBonus;
    }
}
