package de.micromata.jira.rest.client;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;

import de.micromata.jira.rest.core.domain.*;
import de.micromata.jira.rest.core.domain.field.*;
import de.micromata.jira.rest.core.domain.system.*;
import de.micromata.jira.rest.core.util.*;

/**
 * The SystemClient provides all Information about the Jira System Configuration
 */
public interface SystemClient {

    /**
     * Return the Configuration of the remote Jira Instanz
     *
     * @return ConfigurationBean
     * @throws RestException
     * @throws IOException
     */
    CompletableFuture<ConfigurationBean> getConfiguration();

    /**
     * Returns a list of all issue types visible to the connected client.
     *
     * @return list of issue types
     * @throws de.micromata.jira.rest.core.util.RestException
     */
    CompletableFuture<List<IssuetypeBean>> getIssueTypes();

    /**
     * Returns a list of all statuses.
     *
     * @return list of statuses
     * @throws RestException
     */
    CompletableFuture<List<StatusBean>> getStates();


    /**
     * Returns a List of all Priority Object from the Remote Jira.
     *
     * @return
     * @throws RestException
     */
    CompletableFuture<List<PriorityBean>> getPriorities();


    /**
     * Return a List of all Field configure in Jira, standard and custom
     *
     * @return a List of FieldBean
     */
    CompletableFuture<List<FieldBean>> getAllFields();


    /**
     * Return all Custom Field configure in the Jira
     *
     * @return a List of FieldBean
     */
    CompletableFuture<List<FieldBean>> getAllCustomFields();

    /**
     * Return a Custom Field by Id
     */
    CompletableFuture<FieldBean> getCustomFieldById(String id);


    /**
     * Get the Attachment Meta Information for the jira instanz
     *
     * @return AttachmentMetaBean
     */
    CompletableFuture<AttachmentMetaBean> getAttachmentMeta();

    /**
     * Creates a Custom Field
     *
     * @param fieldBean The CreateFieldBean with the create Informations
     * @return The created Field as FieldBean
     */
    CompletableFuture<FieldBean> createCustomField(CreateFieldBean fieldBean);

}
