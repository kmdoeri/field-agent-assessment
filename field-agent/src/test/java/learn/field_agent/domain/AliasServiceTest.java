package learn.field_agent.domain;

import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.Agent;
import learn.field_agent.models.Alias;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class AliasServiceTest {

    @MockBean
    AliasRepository aliasRepository;

    @Autowired
    AliasService service;

    @Test
    void shouldNotAddNull() {
        Result<Alias> result = service.add(null);
        assertNull(result.getPayload());
        assertEquals(ResultType.INVALID, result.getType());
        System.out.println(result.getMessages());
    }

    @Test
    void shouldNotAddNullOrBlankName() {
        Alias alias = new Alias();
        alias.setName(null);
        Result<Alias> result = service.add(alias);
        assertNull((result.getPayload()));
        assertEquals(ResultType.INVALID, result.getType());
        System.out.println(result.getMessages());

        alias.setName("  ");
        result = service.add(alias);
        assertNull((result.getPayload()));
        assertEquals(ResultType.INVALID, result.getType());
        System.out.println(result.getMessages());
    }

    @Test
    void shouldNotAddIfPersonaNullWithDuplicateNames() {
        Alias alias = new Alias();
        alias.setName("Dingo Nevada");

        List<Alias> aliases = List.of(alias);

        when(aliasRepository.findAll()).thenReturn(aliases);

        Alias needsPersona = new Alias();
        needsPersona.setName("Dingo Nevada");

        Result<Alias> result = service.add(needsPersona);
        assertNull(result.getPayload());
        assertEquals(ResultType.INVALID, result.getType());
        System.out.println(result.getMessages());
    }

    @Test
    void shouldNotAddWithIdNotZero() {
        Alias badAlias = new Alias();
        badAlias.setName("Kristin");
        badAlias.setAliasId(4);
        Result<Alias> result = service.add(badAlias);
        assertEquals(ResultType.INVALID, result.getType());
        assertNull(result.getPayload());
        System.out.println(result.getMessages());
    }

    @Test
    void shouldAdd() {
        Alias aliasIn = makeAliasIn();
        Alias aliasOut = makeAliasOut();

        when(aliasRepository.add(aliasIn)).thenReturn(aliasOut);
        Result<Alias> result = service.add(aliasIn);
        assertEquals(ResultType.SUCCESS, result.getType());
        assertEquals(aliasOut, result.getPayload());
    }

    @Test
    void shouldNotUpdateNullOrBlankName() {
        Alias alias = new Alias();
        alias.setName(null);
        Result<Alias> result = service.update(alias);
        assertEquals(ResultType.INVALID, result.getType());
        System.out.println(result.getMessages());

        alias.setName("  ");
        result = service.update(alias);
        assertEquals(ResultType.INVALID, result.getType());
        System.out.println(result.getMessages());
    }

    @Test
    void shouldNotUpdateNull() {
        Result<Alias> result = service.update(null);
        assertEquals(ResultType.INVALID, result.getType());
        System.out.println(result.getMessages());
    }

    @Test
    void shouldNotUpdateIfPersonaNullWithDuplicateNames() {
        Alias alias = new Alias();
        alias.setName("Dingo Nevada");

        List<Alias> aliases = List.of(alias);

        when(aliasRepository.findAll()).thenReturn(aliases);

        Alias needsPersona = new Alias();
        needsPersona.setName("Dingo Nevada");

        Result<Alias> result = service.update(needsPersona);
        assertEquals(ResultType.INVALID, result.getType());
        System.out.println(result.getMessages());
    }

    @Test
    void shouldNotUpdateIfIdIsZeroOrNegative() {
        Alias alias = makeAliasIn();
        Result<Alias> result = service.update(alias);
        assertEquals(ResultType.INVALID, result.getType());
        System.out.println(result.getMessages());

        alias.setAliasId(-10);
        result = service.update(alias);
        assertEquals(ResultType.INVALID, result.getType());
        System.out.println(result.getMessages());
    }

    @Test
    void shouldNotUpdateMissing() {
        Alias alias = makeAliasOut();
        Result<Alias> result = service.update(alias);
        assertEquals(ResultType.NOT_FOUND, result.getType());
        System.out.println(result.getMessages());
    }

    @Test
    void shouldUpdate() {
        Alias alias = makeAliasOut();
        when(aliasRepository.update(alias)).thenReturn(true);

        Result<Alias> result = service.update(alias);

        assertEquals(ResultType.SUCCESS, result.getType());
    }

    Agent makeAgent() {
        Agent agent = new Agent();
        agent.setAgentId(1);
        agent.setFirstName("Kristin");
        agent.setMiddleName("M.");
        agent.setLastName("Doering");
        agent.setDob(LocalDate.of(1996, 3, 24));
        agent.setHeightInInches(66);
        return agent;
    }

    Alias makeAliasIn() {
        Alias alias = new Alias();
        alias.setAliasId(0);
        alias.setName("Dingo Nevada");
        alias.setAgentId(1);
        return alias;
    }

    Alias makeAliasOut() {
        Alias alias = new Alias();
        alias.setAliasId(1);
        alias.setName("Dingo Nevada");
        alias.setAgentId(1);
        return alias;
    }



}