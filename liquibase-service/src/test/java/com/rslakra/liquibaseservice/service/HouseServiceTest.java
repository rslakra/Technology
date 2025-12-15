package com.rslakra.liquibaseservice.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.rslakra.liquibaseservice.persistence.entity.House;
import com.rslakra.liquibaseservice.persistence.repository.HouseRepository;
import com.rslakra.liquibaseservice.repository.HouseRepositoryTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Rohtash Lakra
 * @created 10/20/22 5:47 PM
 */
@ExtendWith(SpringExtension.class)
@Import(HouseService.class)
public class HouseServiceTest {

    @Autowired
    private HouseService houseService;

    @MockitoBean
    private HouseRepository houseRepository;

    @BeforeEach
    public void setUp() {
        House rohtash = HouseRepositoryTest.createHouse(1L, "Rohtash", true);
        House singh = HouseRepositoryTest.createHouse(2L, "Singh", false);
        House lakra = HouseRepositoryTest.createHouse(3L, "Lakra", false);
        List<House> allHouses = Arrays.asList(rohtash, singh, lakra);

        // Mock save() to return the house with ID set (simulating JPA behavior)
        Mockito.when(houseRepository.save(Mockito.any(House.class))).thenAnswer(invocation -> {
            House house = invocation.getArgument(0);
            if (house.getId() == null) {
                // If no ID, assign a default one (simulating auto-generation)
                house.setId(1L);
            }
            return house;
        });

        Mockito.when(houseRepository.findById(rohtash.getId())).thenReturn(Optional.of(rohtash));
        Mockito.when(houseRepository.findById(singh.getId())).thenReturn(Optional.of(singh));
        Mockito.when(houseRepository.findById(lakra.getId())).thenReturn(Optional.of(lakra));
        Mockito.when(houseRepository.findAll()).thenReturn(allHouses);
        Mockito.when(houseRepository.findById(-99L)).thenReturn(Optional.empty());
    }

    /**
     *
     */
    @Test
    public void testCreate() {
        House house = HouseRepositoryTest.createHouse("Create", true);
        House found = houseService.create(house);
        assertNotNull(found);
        assertNotNull(found.getId());
        assertEquals(found.getOwner(), house.getOwner());
    }

    /**
     *
     */
    @Test
    public void testFilter() {
        House house = houseService.create(HouseRepositoryTest.createHouse("Create", true));
        assertNotNull(house);
        assertNotNull(house.getId());

        // Mock findById to return the created house
        Mockito.when(houseRepository.findById(house.getId())).thenReturn(Optional.of(house));
        
        House found = houseService.findById(house.getId());
        assertNotNull(found);
        assertNotNull(found.getId());
        assertEquals(found.getOwner(), house.getOwner());
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
