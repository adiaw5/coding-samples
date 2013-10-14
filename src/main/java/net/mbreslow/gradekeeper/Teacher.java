package net.mbreslow.gradekeeper;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Teacher class holds a roster of students
 */
public class Teacher {
    private String name;
    private ScoringPreferences scoringPreferences = null;
    private Map<String, Student> studentsByName = new HashMap<String, Student>();

    /**
     * Create a new Teacher with the given attributes
     * @param teacherName name of the Teacher
     * @param scoringPreferences Teacher's scoring preferences
     */
    public Teacher(String teacherName, ScoringPreferences scoringPreferences) {
        assert teacherName != null;
        assert scoringPreferences != null;
        this.name = teacherName;
        this.scoringPreferences = scoringPreferences;
    }

    /**
     * Get a particular student by their unique name
     * @param name unique name of the Student in the class
     * @return lookup an individual student in the class by unique name
     */
    public Student getStudent(String name) {
        return studentsByName.get(name);
    }

    /**
     * Get the Collection of all Students in the class
     * @return Collection of Students
     */
    public Collection<Student> getStudents() {
        return studentsByName.values();
    }

    /**
     * Add a new student to the Teacher's roster
     * @param studentName unique name of the Student
     * @return Student object for the new Student
     * @throws NameCollisionError when attempting to add a student by a name that already exists
     */
    public Student addStudent(String studentName) {
        Student student = new Student(studentName);
        Student previous = studentsByName.put(studentName, student);
        if (previous != null) {
            studentsByName.put(studentName, previous);
            throw new NameCollisionError("A student already exists with the name " + studentName);
        }
        return student;

    }

    /**
     * Replace the teacher's scoring preferences
     * @param scoringPreferences updated preferences
     */
    public void setScoringPreferences(ScoringPreferences scoringPreferences) {
        this.scoringPreferences = scoringPreferences;
    }

    /**
     * Get the teacher's scoring preferences
     * @return the teacher's scoringPreferences
     */
    public ScoringPreferences getScoringPreferences() {
        return scoringPreferences;
    }

    /**
     * Accessor method for the 'name' property
     * @return teacher's name
     */
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "name='" + name + '\'' +
                ", scoringPreferences=" + scoringPreferences +
                ", studentsByName=" + studentsByName +
                '}';
    }
}
