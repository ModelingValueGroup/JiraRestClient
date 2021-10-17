package de.micromata.jira.rest.core.domain;

import java.time.*;
import java.time.format.*;
import java.util.*;

import com.google.gson.*;
import com.google.gson.annotations.*;

/**
 * Author: Christian
 * Date: 12.12.2014.
 */
public class BaseBean implements Comparable<BaseBean> {
    @Expose
    private String expand;
    @Expose
    private String id;
    @Expose
    private String name;
    @Expose
    private String self;

    public String getExpand() {
        return expand;
    }

    public void setExpand(String expand) {
        this.expand = expand;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    @Override
    public int compareTo(BaseBean o) {
        return getKey().compareTo(o.getKey());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BaseBean)) {
            return false;
        }
        return getKey().equals(((BaseBean) o).getKey());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKey());
    }

    private String getKey() {
        return name + "::" + self;
    }

    @Override
    public String toString() {
        return new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(this);
    }

    private static final DateTimeFormatter JIRA_DATE_TIME_FORMAT =
            new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .append(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    .parseLenient()
                    .appendOffset("+HHmm", "")
                    .parseStrict()
                    .toFormatter();

    protected static LocalDateTime toDate(String s) {
        return ZonedDateTime.parse(s, JIRA_DATE_TIME_FORMAT).withZoneSameInstant(ZoneId.systemDefault()).toLocalDateTime();
    }
}
