package com.rslakra.thymeleaffragments;

import static org.assertj.core.api.Assertions.assertThat;

import com.rslakra.thymeleaffragments.persistence.repository.CityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = ThymeleafFragmentsApplication.class)
class ThymeleafFragmentsApplicationTest {

    @Autowired
    private CityRepository cityRepository;

    @Test
    void shouldStartSpringContext() {
        assertThat(cityRepository).isNotNull();
    }

}