package de.micromata.jira.rest.core.domain.permission;

import com.google.gson.annotations.*;

/**
 * User: Christian Schulze (c.schulze@micromata.de)
 * Date: 09.04.16
 */
public class MyPermissionsBean {

    @Expose
    @SerializedName("permissions")
    private PermissionsBean permissions;

    public PermissionsBean getPermissions() {
        return permissions;
    }

    public void setPermissions(PermissionsBean permissions) {
        this.permissions = permissions;
    }
}
