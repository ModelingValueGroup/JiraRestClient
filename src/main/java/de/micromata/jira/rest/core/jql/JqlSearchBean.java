/*
 * Copyright 2013 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.micromata.jira.rest.core.jql;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

/**
 * JQL search requirements.
 *
 * @author Christian Schulze
 * @author Vitali Filippow
 */
public class JqlSearchBean {
    /**
     * Result list start at.
     */
    @Expose
    private Integer      startAt;
    //
    /**
     * Maximum result list size.
     */
    @Expose
    private Integer      maxResults;
    //
    /**
     * Result fields for a query.
     */
    @Expose
    private List<String> fields;
    @Expose
    private String       jql;
    @Expose
    private List<String> expand = new ArrayList<>();

    public JqlSearchBean() {
    }

    public JqlSearchBean(String jql) {
        this.jql = jql;
    }

    /**
     * Adds fields which should be returned after the request.
     *
     * @param fields = returned fields
     */
    public JqlSearchBean addField(EField... fields) {
        for (EField f : fields) {
            getFields().add("" + f);
        }
        return this;
    }

    public JqlSearchBean addNotField(EField... fields) {
        for (EField f : fields) {
            getFields().add("-" + f);
        }
        return this;
    }

    public JqlSearchBean addExpand(EField... fields) {
        for (EField field : fields) {
            getExpand().add(field.toString());
        }
        return this;
    }

    /**
     * Gets the start at.
     *
     * @return the start at
     */
    public Integer getStartAt() {
        return startAt;
    }

    /**
     * Sets the start at.
     *
     * @param startAt the new start at
     */
    public JqlSearchBean setStartAt(Integer startAt) {
        this.startAt = startAt;
        return this;
    }

    /**
     * Gets the max result.
     *
     * @return the max result
     */
    public Integer getMaxResults() {
        return maxResults;
    }

    /**
     * Sets the max result.
     *
     * @param maxResults the new max result
     */
    public JqlSearchBean setMaxResults(Integer maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    /**
     * Gets the fields.
     *
     * @return the fields
     */
    public List<String> getFields() {
        if (fields == null) {
            fields = new ArrayList<>();
        }
        return fields;
    }

    public String getJql() {
        return jql;
    }

    public JqlSearchBean setJql(String jql) {
        this.jql = jql;
        return this;
    }

    public List<String> getExpand() {
        if (expand == null) {
            expand = new ArrayList<>();
        }
        return expand;
    }
}
