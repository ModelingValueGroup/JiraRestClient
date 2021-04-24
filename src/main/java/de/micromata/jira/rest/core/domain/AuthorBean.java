package de.micromata.jira.rest.core.domain;

import com.google.gson.annotations.Expose;

public class AuthorBean extends UserBean {
    @Expose
    private String         accountId;
    @Expose
    private String         accountType;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }
}
