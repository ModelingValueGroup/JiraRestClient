package de.micromata.jira.rest.core.domain.customFields;

import java.util.*;

import de.micromata.jira.rest.core.domain.*;

/**
 * Created by cschulc on 22.02.16.
 */
public class VersionSelectBean extends CustomFieldBaseBean {

    private List<VersionBean> versions;

    public List<VersionBean> getVersions() {
        if (versions == null) {
            versions = new ArrayList<>();
        }
        return versions;
    }

    public void setVersions(List<VersionBean> versions) {
        this.versions = versions;
    }
}
