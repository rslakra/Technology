package com.rslakra.liquibaseservice.service;

import com.rslakra.liquibaseservice.persistence.repository.ItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Rohtash Lakra
 * @created 10/20/22 5:47 PM
 */
@ExtendWith(SpringExtension.class)
@Import(ItemService.class)
public class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @MockitoBean
    private ItemRepository itemRepository;

    /**
     *
     */
    @Test
    public void testCreate() {
    }

    /**
     *
     */
    @Test
    public void testFilter() {
    }

    /**
     *
     */
    @Test
    public void testUpdate() {
    }


    /**
     *
     */
    @Test
    public void testDelete() {
    }

}
