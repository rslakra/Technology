package com.rslakra.distributedstore.service;

import com.rslakra.distributedstore.persistence.domain.User;
import com.rslakra.distributedstore.persistence.domain.UserDetail;
import com.rslakra.distributedstore.persistence.domain.UserType;

/**
 * @author Rohtash Lakra
 * @created 12/5/22 20:12 PM
 */
public class UserBuilder {

    private final User user = new User();
    private String alertName;
    private String userName;

    /**
     * @param userType
     * @return
     */
    public UserBuilder setAlertType(UserType userType) {
        user.setUserType(userType);
        return this;
    }

    /**
     * @param userDetail
     * @return
     */
    public UserBuilder setAlertParams(UserDetail userDetail) {
        user.setUserDetail(userDetail);
        return this;
    }

}
