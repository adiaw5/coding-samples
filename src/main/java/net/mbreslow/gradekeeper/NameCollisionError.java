package net.mbreslow.gradekeeper;

/**
 * Runtime error thrown when there is a name collision.  For example when a teacher or student with the same name is added
 */
public class NameCollisionError extends RuntimeException {
    public NameCollisionError() {
    }

    public NameCollisionError(String s) {
        super(s);
    }

    public NameCollisionError(String s, Throwable throwable) {
        super(s, throwable);
    }

    public NameCollisionError(Throwable throwable) {
        super(throwable);
    }
}
