package de.micromata.jira.rest.core.domain;

import com.google.gson.annotations.*;

public class UserBean extends BaseBean {
    @Expose
    private boolean        active;
    @Expose
    private AvatarUrlsBean avatarUrls;
    @Expose
    private String         displayName;
    @Expose
    private String         emailAddress;
    @Expose
    private String         timeZone;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public AvatarUrlsBean getAvatarUrls() {
        return avatarUrls;
    }

    public void setAvatarUrls(AvatarUrlsBean avatarUrls) {
        this.avatarUrls = avatarUrls;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
