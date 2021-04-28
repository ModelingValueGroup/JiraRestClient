package de.micromata.jira.rest.core.domain.update;

/**
 * User: Christian Schulze
 * Email: c.schulze@micromata.de
 * Date: 29.10.2014
 */
public enum Operation {
    SET("set"),
    EDIT("edit"),
    REMOVE("remove");

    private final String name;

    Operation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
