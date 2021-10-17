package de.micromata.jira.rest.core.domain.system;

import com.google.gson.annotations.*;

/**
 * Created by cschulc on 11.09.2016.
 */
public class TimeTrackingConfigurationBean {
    @Expose
    private long   workingHoursPerDay;
    @Expose
    private long   workingDaysPerWeek;
    @Expose
    private String timeFormat;
    @Expose
    private String defaultUnit;

    public long getWorkingHoursPerDay() {
        return workingHoursPerDay;
    }

    public void setWorkingHoursPerDay(long workingHoursPerDay) {
        this.workingHoursPerDay = workingHoursPerDay;
    }

    public long getWorkingDaysPerWeek() {
        return workingDaysPerWeek;
    }

    public void setWorkingDaysPerWeek(long workingDaysPerWeek) {
        this.workingDaysPerWeek = workingDaysPerWeek;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

    public String getDefaultUnit() {
        return defaultUnit;
    }

    public void setDefaultUnit(String defaultUnit) {
        this.defaultUnit = defaultUnit;
    }
}
