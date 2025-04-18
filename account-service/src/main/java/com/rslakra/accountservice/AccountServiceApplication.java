package com.rslakra.accountservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * <pre>
 *  1. Send request to create a clientId/clientSecret.
 *  2. Get accessToken to create the clientId/clientSecret with assertion = ADMIN.
 *  3. Register client with the received accessToken (Step #2)
 *  4. Register user for received clientId (Step #3) setting <code>sub = clientId</code>.
 *  5. User this user's accessToken to access further resources.
 * </pre>
 */
@EnableJpaRepositories
@SpringBootApplication
public class AccountServiceApplication {

    /**
     * @param args
     */
    public static void main(String[] args) {
        SpringApplication.run(AccountServiceApplication.class, args);
    }

}
