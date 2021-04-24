package de.micromata.jira.rest.core.domain;

import com.google.gson.annotations.Expose;

public class IssuetypeBean extends BaseBean {
    @Expose
    private String  description;
    @Expose
    private String  iconUrl;
    @Expose
    private boolean subtask;
    @Expose
    private long    avatarId;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public boolean isSubtask() {
        return subtask;
    }

    public void setSubtask(boolean subtask) {
        this.subtask = subtask;
    }

    public long getAvatarId() {
        return avatarId;
    }

    public void setAvatarId(long avatarId) {
        this.avatarId = avatarId;
    }
}
