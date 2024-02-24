package com.rslakra.accountservice.service;

import com.rslakra.accountservice.persistence.domain.User;
import com.rslakra.accountservice.persistence.domain.UserDetail;
import com.rslakra.accountservice.persistence.domain.UserType;

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
