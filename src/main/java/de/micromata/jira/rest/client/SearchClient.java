package de.micromata.jira.rest.client;

import java.util.*;
import java.util.concurrent.*;

import de.micromata.jira.rest.core.domain.*;
import de.micromata.jira.rest.core.domain.filter.*;
import de.micromata.jira.rest.core.jql.*;


/**
 * User: Christian Schulze Email: c.schulze@micromata.de Date: 31.07.2014
 */
public interface SearchClient {


    /**
     * Performs an extended search for issues given by the project.
     *
     * @return list of issues
     * @throws de.micromata.jira.rest.core.util.RestException
     */
    CompletableFuture<JqlSearchResult> searchIssues(JqlSearchBean jsb);

    /**
     * Create a new Search Filter for the logged in User
     *
     * @param filter
     * @return
     */
    CompletableFuture<FilterBean> createSearchFilter(FilterBean filter);


    /**
     * Get favorite Filter for JqlSearch for the logged in User
     *
     * @return List of FilterBeans
     */
    CompletableFuture<List<FilterBean>> getFavoriteFilter();

    /**
     * Get Filter by Id
     *
     * @param id the id of the filter
     * @return FilterBean
     */
    CompletableFuture<FilterBean> getFilterById(String id);


}
