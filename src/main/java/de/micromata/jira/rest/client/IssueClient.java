package de.micromata.jira.rest.client;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

import de.micromata.jira.rest.core.domain.*;
import de.micromata.jira.rest.core.domain.update.*;
import de.micromata.jira.rest.core.util.*;

/**
 * The IssueClient provides all Informations for Jira Issues
 */
public interface IssueClient {
    /**
     * Create a new issue
     *
     * @param issue = the issue
     * @return IssueResponse
     * @throws de.micromata.jira.rest.core.util.RestException
     */
    CompletableFuture<IssueResponse> createIssue(IssueBean issue);

    /**
     * Returns a full representation of the issue for the given issue key.
     *
     * @param issueKey = issue key
     * @return all informations for the issue or null if the issue did not exist
     * @throws RestException
     */
    CompletableFuture<IssueBean> getIssueByKey(String issueKey);

    /**
     * update Field of an Issue
     */
    CompletableFuture<IssueBean> updateIssue(String issueKey, IssueUpdate issueUpdate);

    /**
     * Return a Issue with the given Field and Expand Fields.
     *
     * @param issueKey The IssueKey
     * @param fields   The field you want to return.
     * @param expand   The Field which must expand.
     * @return IssueBean or null if the issue did not exist
     */
    CompletableFuture<IssueBean> getIssueByKey(String issueKey, List<String> fields, List<String> expand);

    /**
     * Get Attachement as byte Array, or null
     *
     * @param uri = the uri of the resource
     * @return byte[] or null
     * @throws RestException
     */
    CompletableFuture<Byte[]> getAttachment(URI uri);


    /**
     * Get Attachment as InputStream
     *
     * @param id the Id of the Attachment
     * @return
     */
    InputStream getAttachmentAsStream(long id);


    /**
     * Get Attachment Information for an attachment by id
     *
     * @param id the id of the attachment
     * @return
     */
    CompletableFuture<AttachmentBean> getAttachment(long id);

    /**
     * Save Attachment to Issue
     */
    CompletableFuture<List<AttachmentBean>> saveAttachmentToIssue(String issuekey, File... file);

    /**
     * Returns true if the worklog is successfully transfered to the Issue.
     * <p/>
     * <p>This method is for merging log time for an Issue.
     *
     * @param issueKey = the issue key
     * @param worklog  = the one which would be transfered
     * @return created state
     * @throws RestException
     */
    boolean transferWorklogInIssue(String issueKey,
                                   WorklogBean worklog) throws RestException, IOException, URISyntaxException;

    /**
     * Returns true if the transition update on an Issue success.
     *
     * @param issueKey     = the issue key
     * @param transitionId = the transition id
     * @return success state
     * @throws RestException
     */
    boolean updateIssueTransitionByKey(String issueKey, int transitionId) throws RestException, IOException, URISyntaxException;

    /**
     * Returns available transitions for an Issue in a map with transition id and properties.
     *
     * @param issueKey = the issue key
     * @return List of TransitionBean
     * @throws RestException
     */
    CompletableFuture<List<TransitionBean>> getIssueTransitionsByKey(String issueKey);

    /**
     * Returns a summarized representation of all comments for the given issue.
     *
     * @param issueKey = issue key
     * @return summarized representation of all comments
     * @throws de.micromata.jira.rest.core.util.RestException
     */
    CompletableFuture<CommentsBean> getCommentsByIssue(String issueKey);

    CompletableFuture<WorklogBean> getWorklogByIssue(String issueKey);

    CompletableFuture<WorklogBean> getWorklogByIssue(String issueKey, List<String> fields, List<String> expand);

    /**
     * Add comment to issue.
     *
     * @param issueKey = issue key
     * @param comment  = comment to add
     */
    boolean addCommentToIssue(String issueKey, CommentBean comment) throws RestException, URISyntaxException, IOException;
}
