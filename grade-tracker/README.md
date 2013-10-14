For this coding sample I was asked to solve the following problem:

Implement a solution to the following problem using the latest major released version of Java. Your project should
include unit tests using the open source JUnit framework. The program should be an object-oriented API and should not
include a user interface of any kind. There is no need to provide any form of data persistence.

**_We are looking for clean, well-factored, object-oriented code that has accompanying JUnit tests. Your design should be
“as simple as possible but not simpler” and appropriate to the level of the problem at hand. In other words, doing
everything in a main method is far too little (and not object-oriented), and using EJB is far, far too much._**

Here are the requirements:

Imagine that you are working on one of a number of small teams building an application for schools to help manage
students’ grades, administration of sick days and other information. Your team has been chosen to implement the small
server-side piece that manages tracking of students’ grades.

The application should allow for tracking scores of an unlimited number of assignments and exams. The application should
allow each teacher to have configurable percentages for both types. Grading is done on a weighted average as per the
percentages entered by the teacher. In addition to the normal scores, students are allowed to do optional assignments
for extra credit. Extra credit works as follows: when a student turns in an extra credit assignment, it adds a
configurable number of percentage points to the student’s then current weighted average grade for Assignments only, and
this amount is carried forward cumulatively.

In our first release, we are only going to consider one endless grading period, so do not allow for multiple periods
(such as quarters or semesters) or calculations across multiple periods.

Here is the minimal test data that must be showed to work in your sample:

Tom Teacherman percentage allocations:

* Assignments: 10.1%
* Exams: 89.9%
* Extra Credit Assignment: add 2% to current weight average grade

Sally Student’s grade score log:

1. Assignment #1: 85/100
2. Assignment #2: 88/100
3. Extra Credit Assignment completed
4. Assignment #3: 92/100
5. Exam #1: 91/100

To demonstrate exactly how the program should calculate the average weight grade along with the Extra Credit, here is
Sally’s cumulative grade after each entry in the grade book:

1. After Assignment #1 is logged in the system: 85%
2. After Assignment #2 is logged in the system: 86.5%
3. After Extra Credit Assignment is logged in the system: 88.5%
4. After Assignment #2 is logged in the system: 90.33%
5. After Exam #1 is logged in the system: 90.93%

You can see from this result that the system should always report the grade as a percentage, and should consider the
fact that if no Exams (in this case) have taken place, the allocations are ignored.

___

To keep things as simple as possible, I create a service interface called GradeTracker that has methods for recording
scores for students.  I also make the following assumptions:

* No two teachers in the system will have the same name.  I use teacher name as a unique identifier for a Teacher.
* No two students in the same class will have the same name.  I use student name as a unique identifier within a class.
* We do not need to retain the individual scores that a student achieves on an exam or an assignment.  There are no
requirements to produce a report of the student's scores, only to produce their weighted average.
* Assignment weights and exam weights will always sum to 1.

_Class Diagram_
![class diagram](https://raw.github.com/mbreslow/coding-sample/master/grade-tracker/docs/diagram.png)

The _Student_ class is where the arithmetic is coded in the `getWeightedAverage(ScoringPreferences weights)` method.

_Thread Safety_

The API has been made thread-safe by:
* Synchronizing methods that change data in `Student`
* Using ConcurrentHashMaps for the maps in `GradeTrackerImpl` and `Teacher`
