package net.mbreslow.gradekeeper;

import java.util.ArrayList;
import java.util.List;

/**
 * Keeps a sum Student's test and exam scores as well as the number of extra credit assignments completed and the
 * number of exams/assignments completed.
 * Uses the data in the {@link Student#getWeightedAverage(ScoringPreferences)} method to produce the weighted average.
 * <br/>
 * NOTE: In this version of the code, we do not retain the individual scores as the requirements specify that we should
 * be able to accomodate an unlimited number of assignments and exams and does not require an API for producing a report
 * of the actual records.
 */
public class Student {
    private int extraCredits = 0;
    private String name;

    private int   examsCount = 0;
    private double examsSum = 0;
    private int   assignmentsCount = 0;
    private double assignmentsSum = 0;

    /**
     * Create a new Student with the given Name
     * @param name unique name of th student
     */
    public Student(String name) {
        assert name != null;
        this.name = name;
    }

    /**
     * Record the score for a new assignment
     * @param score score (percentage)
     */
    public synchronized void addAssignment(double score) {
        assignmentsCount++;
        assignmentsSum += score;
    }

    /**
     * Record the score for a new exam
     * @param score score (percentage)
     */
    public synchronized void addExam(double score) {
        examsCount++;
        examsSum += score;
    }

    /**
     * Record the completion of an extra credit assignment
     */
    public synchronized void addExtraCredit() {
        extraCredits++;
    }

    /**
     * Calculate the weighted average given a set of scoring weights
     * @param weights the scoring preferences for a teacher
     * @return weighted average as a percent
     */
    public synchronized double getWeightedAverage(ScoringPreferences weights) {
        double examAverage = 0;
        if (examsCount != 0) {
            examAverage = examsSum/examsCount;
        }
        double assignmentAverage = 0;
        if (assignmentsCount != 0) {
            assignmentAverage = assignmentsSum/assignmentsCount;
            assignmentAverage += extraCredits * weights.getExtraCreditBonus();
        }
        double result = 0;
        if (examsCount != 0 && assignmentsCount != 0) {
            // apply allocations
            result = (examAverage * weights.getWeightExams()) + (assignmentAverage * weights.getWeightAssignments());
        }
        else if (examsCount != 0) {
            result = examAverage;
        }
        else {
            result = assignmentAverage;
        }
        return result;

    }

    /**
     * Accessor for 'name' property
     * @return name of Student
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Student{" +
                "extraCredits=" + extraCredits +
                ", name='" + name + '\'' +
                ", examsCount=" + examsCount +
                ", examsSum=" + examsSum +
                ", assignmentsCount=" + assignmentsCount +
                ", assignmentsSum=" + assignmentsSum +
                '}';
    }
}
