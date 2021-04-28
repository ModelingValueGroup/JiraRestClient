package de.micromata.jira.rest.client;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import de.micromata.jira.rest.core.domain.ComponentBean;
import de.micromata.jira.rest.core.domain.ProjectBean;
import de.micromata.jira.rest.core.domain.VersionBean;
import de.micromata.jira.rest.core.domain.meta.MetaBean;
import de.micromata.jira.rest.core.util.RestException;

/**
 * The IssueClient provides all Informations for Jira Issues
 * <p/>
 * User: Christian Schulze c.schulze@micromata.de
 */
public interface ProjectClient {


    /**
     * Returns a list of all projects the logged in User can see..
     *
     * @return list of projects
     * @throws RestException
     */
    CompletableFuture<List<ProjectBean>> getAllProjects();

    /**
     * Returns a full representation of the project for the given key.
     *
     * @param projectKey = the project key
     * @return all informations for the project
     * @throws RestException
     */
    CompletableFuture<ProjectBean> getProjectByKey(final String projectKey);

    /**
     * Returns a list of all versions for a project.
     *
     * @param projectKey = the project key
     * @return list of versions
     * @throws RestException
     */
    CompletableFuture<List<VersionBean>> getProjectVersions(final String projectKey);


    /**
     * Returns a list of all components for a project.
     *
     * @param projectKey = the project key
     * @return list of components
     * @throws RestException
     */
    CompletableFuture<List<ComponentBean>> getProjectComponents(final String projectKey);


    /**
     * Return the Meta Data for the IssueTypes of a Project. This includes all possible IssueTypes and the Fields including the AllowedValues
     *
     * @param projectKey
     * @return
     */
    CompletableFuture<MetaBean> getIssueTypesMetaForProject(final String projectKey);

}
