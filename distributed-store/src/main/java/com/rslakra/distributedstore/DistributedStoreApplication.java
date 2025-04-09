package com.rslakra.distributedstore;

import com.rslakra.distributedstore.ds.KeyValueStore;
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
public class DistributedStoreApplication {
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        KeyValueStore keyValueStore = new KeyValueStore();
        keyValueStore.set("key1", "value1");
        keyValueStore.set("key2", "value2");
        keyValueStore.begin();
        keyValueStore.set("key2", "value2.1");
        keyValueStore.rollback();
        System.out.println(keyValueStore.get("key2"));
        
        SpringApplication.run(DistributedStoreApplication.class, args);
    }
    
}
