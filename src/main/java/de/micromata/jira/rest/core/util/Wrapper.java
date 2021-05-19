package de.micromata.jira.rest.core.util;

public final class Wrapper extends RuntimeException {
    public Wrapper(Throwable t) {
        super(t);
    }

    public Wrapper(String msg, Throwable t) {
        super(msg, t);
    }
}
