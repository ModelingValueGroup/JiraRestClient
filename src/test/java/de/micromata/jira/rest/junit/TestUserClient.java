package de.micromata.jira.rest.junit;

import org.junit.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import de.micromata.jira.rest.core.domain.*;
import de.micromata.jira.rest.core.domain.permission.*;
import de.micromata.jira.rest.core.util.*;

/**
 * User: Christian Schulze
 * Email: c.schulze@micromata.de
 * Date: 09.08.2014
 */
@Ignore
public class TestUserClient extends BaseTest {


    @Test
    public void testGetUserByUsername() throws RestException, IOException, ExecutionException, InterruptedException {
        Future<UserBean> future   = jiraRestClient.getUserClient().getUserByUsername(USERNAME_TO_SEARCH);
        final UserBean   userBean = future.get();
        Assert.assertNotNull(userBean);
    }

    @Test
    public void testGetLoggedInUser() throws RestException, IOException, ExecutionException, InterruptedException {
        Future<UserBean> future   = jiraRestClient.getUserClient().getLoggedInRemoteUser();
        final UserBean   userBean = future.get();
        Assert.assertNotNull(userBean);
    }

    @Test
    public void testGetAssignableUserForProject() throws RestException, IOException, ExecutionException, InterruptedException {
        Future<List<UserBean>> future    = jiraRestClient.getUserClient().getAssignableUserForProject(PROJECT_TO_SEARCH, null, null);
        final List<UserBean>   userBeans = future.get();
        Assert.assertNotNull(userBeans);
        Assert.assertEquals(2, userBeans.size());
    }

    @Test
    public void testGetAssignableUsersForIssue() throws RestException, IOException, ExecutionException, InterruptedException {
        Future<List<UserBean>> future    = jiraRestClient.getUserClient().getAssignableUsersForIssue(ISSUEKEY_TO_SEARCH, null, null);
        final List<UserBean>   userBeans = future.get();
        Assert.assertNotNull(userBeans);
        Assert.assertEquals(2, userBeans.size());
    }

    @Test
    public void testGetMyPermissions() throws ExecutionException, InterruptedException {
        Future<MyPermissionsBean> future            = jiraRestClient.getUserClient().getMyPermissions();
        MyPermissionsBean         myPermissionsBean = future.get();
        Assert.assertNotNull(myPermissionsBean);
    }
}
