package de.micromata.jira.rest.junit;

import org.junit.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import de.micromata.jira.rest.core.domain.*;
import de.micromata.jira.rest.core.domain.meta.*;
import de.micromata.jira.rest.core.util.*;

/**
 * User: Christian Schulze
 * Email: c.schulze@micromata.de
 * Date: 10.08.2014
 */
@Ignore
public class TestProjectClient extends BaseTest {


    @Test
    public void testGetProjectByKey() throws RestException, IOException, ExecutionException, InterruptedException {
        final Future<ProjectBean> future  = jiraRestClient.getProjectClient().getProjectByKey(PROJECT_TO_SEARCH);
        final ProjectBean         project = future.get();
        Assert.assertNotNull(project);
        Assert.assertEquals(PROJECT_TO_SEARCH, project.getKey());
    }

    @Test
    public void testGetAllProjects() throws RestException, IOException, ExecutionException, InterruptedException {
        final Future<List<ProjectBean>> future       = jiraRestClient.getProjectClient().getAllProjects();
        final List<ProjectBean>         projectBeans = future.get();
        Assert.assertNotNull(projectBeans);
        Assert.assertFalse(projectBeans.isEmpty());
    }

    @Test
    public void testGetProjectVersions() throws RestException, IOException, ExecutionException, InterruptedException {
        final Future<List<VersionBean>> future       = jiraRestClient.getProjectClient().getProjectVersions(PROJECT_TO_SEARCH);
        final List<VersionBean>         versionBeans = future.get();
        Assert.assertNotNull(versionBeans);
        Assert.assertFalse(versionBeans.isEmpty());
    }

    @Test
    public void testGetProjectComponents() throws RestException, IOException, ExecutionException, InterruptedException {
        final Future<List<ComponentBean>> future         = jiraRestClient.getProjectClient().getProjectComponents(PROJECT_TO_SEARCH);
        final List<ComponentBean>         componentBeans = future.get();
        Assert.assertNotNull(componentBeans);
        Assert.assertFalse(componentBeans.isEmpty());
    }

    @Test
    public void testGetIssueTypesMetaForProject() throws ExecutionException, InterruptedException {
        Future<MetaBean> future   = jiraRestClient.getProjectClient().getIssueTypesMetaForProject(PROJECT_TO_SEARCH);
        MetaBean         metaBean = future.get();
        Assert.assertNotNull(metaBean);
    }
}
