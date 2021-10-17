package de.micromata.jira.rest.core.domain.customFields;

import java.util.*;

import de.micromata.jira.rest.core.domain.*;

/**
 * Created by cschulc on 22.02.16.
 */
public class UserSelectBean extends CustomFieldBaseBean {

    private List<UserBean> users;

    public List<UserBean> getUsers() {
        if (users == null) {
            users = new ArrayList<>();
        }
        return users;
    }

    public void setUsers(List<UserBean> users) {
        this.users = users;
    }
}
