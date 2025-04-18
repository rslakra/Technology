package com.rslakra.alertservice.service;

import com.rslakra.alertservice.persistence.domain.Group;

import java.util.Set;
import java.util.UUID;

/**
 * @author Rohtash Lakra
 * @created 9/15/23 2:27 PM
 */
public interface GroupService {

    /**
     * Creates a new group with the provided details and returns the GroupId.
     * @param group
     */
    public void addGroup(Group group);

    /**
     * Allows a user to join a specific group by providing the GroupId.
     * @param groupId
     * @param userIds
     */
    public void joinGroup(UUID groupId, UUID... userIds);

    /**
     * Allows a user to leave a specific group by providing the GroupId.
     *
     * @param groupId
     * @param userIds
     */
    public void leaveGroup(UUID groupId, UUID... userIds);

    /**
     *
     * @param groups
     * @return
     */
    public Group leaveGroup(Group... groups);

    /**
     * Retrieves the list of groups in which a user is a member by providing the UserId.
     *
     * @return
     */
    public Set<Group> getGroups();

    /**
     * Retrieves the list of members belonging to a specific group by providing the GroupId.
     *
     * @param groupId
     * @return
     */
    public Set<UUID> getMembers(UUID groupId);

    /**
     * Retrieves the messages sent in a specific group by providing the GroupId.
     *
     * @param groupId
     * @param timeStamp
     * @return
     */
    public Set<UUID> getGroupMessages(UUID groupId, Long timeStamp);

}
