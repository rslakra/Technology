package com.rslakra.distributedstore.service;

import com.rslakra.distributedstore.persistence.domain.AlertType;
import com.rslakra.distributedstore.persistence.domain.User;
import com.rslakra.distributedstore.persistence.domain.UserDetail;

/**
 * @author Rohtash Lakra
 * @created 12/5/22 20:12 PM
 */
public class UserBuilder {

    private final User user = new User();
    private String alertName;
    private String userName;

    /**
     * @param alertType
     * @return
     */
    public UserBuilder setAlertType(AlertType alertType) {
        user.setAlertType(alertType);
        return this;
    }

    /**
     * @param userDetail
     * @return
     */
    public UserBuilder setAlertDetails(UserDetail userDetail) {
        user.setUserDetail(userDetail);
        return this;
    }

}
