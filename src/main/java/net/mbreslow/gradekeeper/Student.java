package net.mbreslow.gradekeeper;

import java.util.ArrayList;
import java.util.List;

/**
 * Keep track of student test and assignment scores.  Capable of producing a weighted average score for the student.
 */
public class Student {
    private int extraCredits = 0;
    private String name;

    private int   examsCount = 0;
    private float examsSum = 0;
    private int   assignmentsCount = 0;
    private float assignmentsSum = 0;

    /**
     * Create a new Student with the given Name
     * @param name unique name of th student
     */
    public Student(String name) {
        this.name = name;
    }

    /**
     * Record the score for a new assignment
     * @param score score (percentage)
     */
    public synchronized void addAssignment(float score) {
        assignmentsCount++;
        assignmentsSum += score;
    }

    /**
     * Record the score for a new exam
     * @param score score (percentage)
     */
    public synchronized void addExam(float score) {
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
    public synchronized float getWeightedAverage(ScoringPreferences weights) {
        float examAverage = 0;
        if (examsCount != 0) {
            examAverage = examsSum/examsCount;
        }
        float assignmentAverage = 0;
        if (assignmentsCount > 0) {
            assignmentAverage = assignmentsSum/assignmentsCount;
            assignmentAverage += extraCredits * weights.getExtraCreditBonus();
        }
        float result = 0;
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
}
