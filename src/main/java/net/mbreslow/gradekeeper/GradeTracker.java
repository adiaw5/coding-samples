package net.mbreslow.gradekeeper;

/**
 * GradeTracker service interface defines the API for the GradeTracker service
 */
public interface GradeTracker {
    /**
     * Record the score for an assignment
     * @param teacherName unique name of the teacher
     * @param studentName unique name of the student
     * @param score score for the assignment
     */
    void recordAssignmentScore(String teacherName, String studentName, float score);

    /**
     * Record the score for an exam
     * @param teacherName unique name of the teacher
     * @param studentName unique name of the student
     * @param score score for the assignment
     */
    void recordExamScore(String teacherName, String studentName, float score);


    /**
     * Record an extra-credit for a student
     * @param teacherName unique name of the teacher
     * @param studentName unique name of the student
     */
    void recordExtraCredit(String teacherName, String studentName);

    /**
     * Add a new teacher to the system
     * @param name teacher's name
     * @param scoringPreferences teacher's scoring preferences
     */
    void addTeacher(String name, ScoringPreferences scoringPreferences);

    /**
     * Update teacher's scoring preferences
     * @param name teacher's name
     * @param scoringPreferences teacher's scoring preferences
     */
    void updateTeacher(String name, ScoringPreferences scoringPreferences);

    /**
     * Get the student's current weighted average
     * @param teacherName name of the teacher of the student's class
     * @param studentName name of the student
     */
    float getStudentAverage(String teacherName, String studentName);
}

