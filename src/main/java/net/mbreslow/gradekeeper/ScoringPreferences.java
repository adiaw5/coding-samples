package net.mbreslow.gradekeeper;

/**
 * Immutable class holds a teacher's scoring preferences (weights)
 */
public class ScoringPreferences {
    private double weightAssignments = 1;
    private double weightExams = 1;
    private double extraCreditBonus = .02f;

    /**
     * Constructor.
     * @param weightAssignments
     * @param weightExams
     * @param extraCreditBonus
     * @throws IllegalArgumentException when weightAssignments + weightExams does not sum to 1
     */
    public ScoringPreferences(double weightAssignments, double weightExams, double extraCreditBonus) {
        this.weightAssignments = weightAssignments;
        this.weightExams = weightExams;
        this.extraCreditBonus = extraCreditBonus;
        if (weightAssignments + weightExams != 1) {
            throw new IllegalArgumentException("weightAssignments (" + weightAssignments + ") + weightExams (" + weightExams + ") must sum to 1.  " + (weightAssignments + weightExams) + " != 1");
        }
    }

    public double getWeightAssignments() {
        return weightAssignments;
    }

    public double getWeightExams() {
        return weightExams;
    }

    public double getExtraCreditBonus() {
        return extraCreditBonus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ScoringPreferences that = (ScoringPreferences) o;

        if (Double.compare(that.extraCreditBonus, extraCreditBonus) != 0) return false;
        if (Double.compare(that.weightAssignments, weightAssignments) != 0) return false;
        if (Double.compare(that.weightExams, weightExams) != 0) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(weightAssignments);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(weightExams);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(extraCreditBonus);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "ScoringPreferences{" +
                "weightAssignments=" + weightAssignments +
                ", weightExams=" + weightExams +
                ", extraCreditBonus=" + extraCreditBonus +
                '}';
    }
}
