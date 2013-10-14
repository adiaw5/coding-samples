package net.mbreslow.gradekeeper;

import java.util.HashMap;
import java.util.Map;

/**
 * Service implementation class
 */
public class GradeTrackerImpl implements GradeTracker {
    Map<String, Teacher> teachersByName = new HashMap<String, Teacher>();

    @Override
    public void recordAssignmentScore(String teacherName, String studentName, float score) {
        Student student = getStudent(teacherName, studentName);
        student.addAssignment(score);
    }

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

    @Override
    public void recordExamScore(String teacherName, String studentName, float score) {
        Student student = getStudent(teacherName, studentName);
        student.addExam(score);
    }

    @Override
    public void recordExtraCredit(String teacherName, String studentName) {
        Student student = getStudent(teacherName, studentName);
        student.addExtraCredit();
    }

    @Override
    public void addTeacher(String name, ScoringPreferences scoringPreferences) {
        assert scoringPreferences != null;
        if (teachersByName.containsKey(name)) {
            throw new NameCollisionError("Unable to add a teacher named " + name + " because one already exists.  Use unique names.");
        }
        Teacher teacher = new Teacher(name, scoringPreferences);
        teachersByName.put(name, teacher);
    }

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
    public float getStudentAverage(String teacherName, String studentName) {
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
}
