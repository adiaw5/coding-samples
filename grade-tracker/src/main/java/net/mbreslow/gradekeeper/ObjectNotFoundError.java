package net.mbreslow.gradekeeper;

/**
 * Runtime error that occurs when attempting to record a grade for a teacher that does not exist
 */
public class ObjectNotFoundError extends RuntimeException {

    public ObjectNotFoundError() {
    }

    public ObjectNotFoundError(String s) {
        super(s);
    }

    public ObjectNotFoundError(String s, Throwable throwable) {
        super(s, throwable);
    }

    public ObjectNotFoundError(Throwable throwable) {
        super(throwable);
    }
}
