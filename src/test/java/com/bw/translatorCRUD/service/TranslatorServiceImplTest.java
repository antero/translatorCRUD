package com.bw.translatorCRUD.service;

import com.bw.translatorCRUD.exception.TranslatorNotFoundException;
import com.bw.translatorCRUD.model.Translator;
import com.bw.translatorCRUD.repository.TranslatorRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TranslatorServiceImplTest {
    @Autowired
    private TranslatorRepository translatorRepository;

    @Autowired
    private TranslatorService translatorService;

    @Test
    void findAll() {
        Translator t1 = new Translator("Fulano Silva", "fulano@gmail.com");
        Translator t2 = new Translator("Sicrano Jose", "sicrano@gmail.com");

        translatorRepository.saveAll(Arrays.asList(t1, t2));

        List<Long> ids = Arrays.asList(t1.getId(), t2.getId());
        List<Translator> translators = translatorService.findAll()
                                                        .stream()
                                                        .filter(t -> ids.contains(t.getId()))
                                                        .collect(Collectors.toList());
        assertEquals(2, translators.size());
    }

    @Test
    void create() {
        Translator t1 = new Translator("Fulano Silva", "fulano@gmail.com");
        assertNull(t1.getCreated());

        t1 = translatorService.create(t1);
        assertNotNull(t1.getCreated());
    }

    @Test
    void findById() {
        Translator t1 = new Translator("Fulano Silva", "fulano@gmail.com");
        translatorRepository.save(t1);
        assertDoesNotThrow(() -> translatorService.findById(t1.getId()));
    }

    @Test
    void findByIdWithInvalidId() {
        assertThrows(TranslatorNotFoundException.class, () -> translatorService.findById(0L));
        assertThrows(TranslatorNotFoundException.class, () -> translatorService.findById(-1L));
        assertThrows(TranslatorNotFoundException.class, () -> translatorService.findById(1000L));
    }

    @Test
    void update() {
        Translator t1 = new Translator("Fulano Silva", "fulano@gmail.com");
        translatorRepository.save(t1);

        Map<String, Object> payload = new HashMap<>() {
            {
                put("name", "Fulano Arruda");
            }
        };
        Translator updatedT1 = translatorService.update(t1.getId(), payload);

        assertEquals(t1.getId(), updatedT1.getId());
        assertEquals(t1.getEmail(), updatedT1.getEmail());
        assertNotEquals(t1.getName(), updatedT1.getName());
    }

    @Test
    void deleteById() {
        Translator t1 = new Translator("Fulano Silva", "fulano@gmail.com");
        translatorRepository.save(t1);

        assertDoesNotThrow(() -> translatorService.deleteById(t1.getId()));
    }

    @Test
    void deleteByIdWithInvalidId() {
        assertThrows(TranslatorNotFoundException.class, () -> translatorService.deleteById(1L));
    }
}