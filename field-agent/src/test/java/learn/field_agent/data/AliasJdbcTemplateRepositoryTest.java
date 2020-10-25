package learn.field_agent.data;

import learn.field_agent.models.Alias;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AliasJdbcTemplateRepositoryTest {

    @Autowired
    AliasRepository repository;

    @Autowired
    KnownGoodState knownGoodState;

    @BeforeEach
    void setup() {
        knownGoodState.set();
    }

    @Test
    void shouldFindAll() {
        List<Alias> all = repository.findAll();

        assertNotNull(all);
        assertTrue(all.size() > 0);
    }

    @Test
    void shouldFindByExistingId() {
        Alias expected = new Alias();

        expected.setAliasId(2);
        expected.setName("K Dreezy");
        expected.setPersona("Snappin necks and cashin checks");
        expected.setAgentId(2);

         Alias actual = repository.findById(2);
        assertEquals(expected, actual);

        expected.setAliasId(3);
        expected.setName("The Falcon");
        expected.setPersona("Swooping in to save the day");
        expected.setAgentId(2);

        actual = repository.findById(3);
        assertEquals(expected, actual);
    }

    @Test
    void shouldNotFindByMissingId() {
      assertNull(repository.findById(9000));
    }

    @Test
    void shouldAdd() {
        Alias alias = new Alias();
        alias.setName("Knee Col As");
        alias.setPersona(null);
        alias.setAgentId(1);

        Alias actual = repository.add(alias);
        assertNotNull(actual);
        assertTrue(actual.getAliasId() > 0);
        System.out.println(actual.getAliasId());
    }

    @Test
    void shouldUpdateExisting() {
        Alias alias = new Alias();
        alias.setAliasId(2);
        alias.setName("Updated name");
        alias.setPersona("Updated persona");
        alias.setAgentId(2);

        assertTrue(repository.update(alias));
        assertEquals(alias, repository.findById(2));
    }

    @Test
    void shouldNotUpdateMissing() {
        Alias alias = new Alias();
        alias.setAliasId(100);
        alias.setName("Updated name");
        alias.setPersona("Updated persona");
        alias.setAgentId(1);

        assertFalse(repository.update(alias));
    }

    @Test
    void shouldDeleteExisting() {
        assertTrue(repository.deleteById(1));
    }

    @Test
    void shouldNotDeleteMissing() {
        assertFalse(repository.deleteById(1000));
    }

}