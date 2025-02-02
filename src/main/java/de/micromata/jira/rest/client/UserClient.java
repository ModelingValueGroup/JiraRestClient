package de.micromata.jira.rest.client;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import de.micromata.jira.rest.core.domain.*;
import de.micromata.jira.rest.core.domain.permission.*;
import de.micromata.jira.rest.core.util.*;

/**
 * User: Christian Schulze
 * Email: c.schulze@micromata.de
 * Date: 02.08.2014
 */
public interface UserClient {


    /**
     * Returns a List of UserBean which are assignable to Issue in a Project
     *
     * @param projectKey the projectkey
     * @param startAt    start at position (0-based) can be null the default (0)
     * @param maxResults Number of Results (default 50) can be null then default (50)
     * @return The List of a assignable Users, or an Empty List if the logged in User has no permission to get assign Issues
     */
    CompletableFuture<List<UserBean>> getAssignableUserForProject(String projectKey, Integer startAt, Integer maxResults) throws RestException, IOException;


    /**
     * Returns a List of UserBean which are assignable to an Issue
     *
     * @param issueKey   The Issuekey
     * @param startAt    start at position (0-based) can be null the default (0)
     * @param maxResults Number of Results (default 50) can be null then default (50)
     * @return The List of a assignable Users, or an Empty List if the logged in User has no permission to get assign Issues
     */
    CompletableFuture<List<UserBean>> getAssignableUsersForIssue(String issueKey, Integer startAt, Integer maxResults) throws RestException, IOException;

    /**
     * Returns a User by his username
     *
     * @param username The username of the User
     * @return The UserBean for the username or null if the logged in User has no permission to get another user
     */
    CompletableFuture<UserBean> getUserByUsername(String username);

    /**
     * Returns all users
     *
     * @return The list of all users
     */
    CompletableFuture<List<AccountBean>> getAllUsers(Integer startAt, Integer maxResults);

    /**
     * Returns the logged in remote user.
     *
     * @return logged in user
     * @throws RestException
     */
    CompletableFuture<UserBean> getLoggedInRemoteUser() throws RestException, IOException;

    /**
     * Get the Permissions for the logged in User.
     * This contains all Permissons, every permission has a flag if it is set or unset.
     *
     * @return PermissionsBean with all Permission,
     */
    CompletableFuture<MyPermissionsBean> getMyPermissions();
}
