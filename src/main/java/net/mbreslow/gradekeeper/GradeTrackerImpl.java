package net.mbreslow.gradekeeper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service implementation class.
 */
public class GradeTrackerImpl implements GradeTracker {
    // use a ConcurrentHashMap as multiple threads could be adding teachers and retrieving teachers concurrently
    Map<String, Teacher> teachersByName = new ConcurrentHashMap<String, Teacher>();

    /**
     * Record the score for an assignment
     * @param teacherName unique name of the teacher
     * @param studentName unique name of the student
     * @param score score for the assignment
     */
    @Override
    public void recordAssignmentScore(String teacherName, String studentName, double score) {
        Student student = getStudent(teacherName, studentName);
        student.addAssignment(score);
    }

    /**
     * Lookup a student for a given teacher.  If not found, create one.
     * @param teacherName unique name of the teacher
     * @param studentName unique name of the student
     * @return student for the given name
     */
    private Student getStudent(String teacherName, String studentName) {
        Teacher teacher = teachersByName.get(teacherName);
        if (teacher == null) {
            throw new ObjectNotFoundError("No teacher with the name " + teacherName + " exists" );
        }
        Student student = teacher.getStudent(studentName);
        if (student == null) {
            student = teacher.addStudent(studentName);
        }
        return student;
    }

    /**
     * Record the score for an exam
     * @param teacherName unique name of the teacher
     * @param studentName unique name of the student
     * @param score score for the assignment
     */
    @Override
    public void recordExamScore(String teacherName, String studentName, double score) {
        Student student = getStudent(teacherName, studentName);
        student.addExam(score);
    }

    /**
     * Record an extra-credit for a student
     * @param teacherName unique name of the teacher
     * @param studentName unique name of the student
     */
    @Override
    public void recordExtraCredit(String teacherName, String studentName) {
        Student student = getStudent(teacherName, studentName);
        student.addExtraCredit();
    }

    /**
     * Add a new teacher to the system
     * @param name teacher's name
     * @param scoringPreferences teacher's scoring preferences
     */
    @Override
    public void addTeacher(String name, ScoringPreferences scoringPreferences) {
        assert scoringPreferences != null;
        if (teachersByName.containsKey(name)) {
            throw new NameCollisionError("Unable to add a teacher named " + name + " because one already exists.  Use unique names.");
        }
        Teacher teacher = new Teacher(name, scoringPreferences);
        teachersByName.put(name, teacher);
    }

    /**
     * Update teacher's scoring preferences
     * @param name teacher's name
     * @param scoringPreferences teacher's scoring preferences
     */
    @Override
    public void updateTeacher(String name, ScoringPreferences scoringPreferences) {
        Teacher teacher = teachersByName.get(name);
        if (teacher == null) {
            teacher = new Teacher(name, scoringPreferences);
        }
        else {
            teacher.setScoringPreferences(scoringPreferences);
        }
    }

    /**
     * Get the student's current weighted average
     *
     * @param teacherName name of the teacher of the student's class
     * @param studentName name of the student
     * @throws ObjectNotFoundError when teacher or student not found
     */
    @Override
    public double getStudentAverage(String teacherName, String studentName) {
        Teacher teacher = teachersByName.get(teacherName);
        if (teacher == null) {
            throw new ObjectNotFoundError("No teacher found for name " + teacherName);
        }
        Student student = teacher.getStudent(studentName);
        if (student == null) {
            throw new ObjectNotFoundError("No student found for name " + studentName + " for teacher " + teacherName);
        }
        ScoringPreferences preferences = teacher.getScoringPreferences();
        return student.getWeightedAverage(preferences);
    }



    @Override
    public String toString() {
        return "GradeTrackerImpl{" +
                "teachersByName=" + teachersByName +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GradeTrackerImpl)) return false;

        GradeTrackerImpl that = (GradeTrackerImpl) o;

        if (!teachersByName.equals(that.teachersByName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return teachersByName.hashCode();
    }
}
