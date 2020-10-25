package learn.field_agent.data;

import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SecurityClearanceJdbcTemplateRepositoryTest {

    @Autowired
    SecurityClearanceJdbcTemplateRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindById() {
        SecurityClearance secret = new SecurityClearance(1, "Secret");
        SecurityClearance topSecret = new SecurityClearance(2, "Top Secret");

        SecurityClearance actual = repository.findById(1);
        assertEquals(secret, actual);

        actual = repository.findById(2);
        assertEquals(topSecret, actual);

        actual = repository.findById(30);
        assertEquals(null, actual);
    }

    @Test
    void shouldFindAll() {
        List<SecurityClearance> all = repository.findAll();

        System.out.println(all);

        assertNotNull(all);
        assertTrue(all.size() > 0);
    }

    @Test
    void shouldAdd() {
        SecurityClearance clearance = new SecurityClearance();
        clearance.setName("Super Duper Top Secret");

        SecurityClearance actual = repository.add(clearance);

        assertNotNull(actual);
        assertTrue(actual.getSecurityClearanceId() > 0);
    }

    @Test
    void shouldUpdateExisting() {
        SecurityClearance clearance = new SecurityClearance(2, "Confidential");

        assertTrue(repository.update(clearance));
        assertEquals(clearance, repository.findById(2));
    }

    @Test
    void shouldNotUpdateMissing() {
        SecurityClearance clearance = new SecurityClearance(200, "Confidential");

        assertFalse(repository.update(clearance));
    }

    @Test
    void shouldDeleteExisting() {
        assertTrue(repository.deleteById(3));
    }

    @Test
    void shouldNotDeleteMissing() {
        assertFalse(repository.deleteById(5000));
    }
}