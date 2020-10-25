package learn.field_agent.domain;

import learn.field_agent.data.AgencyAgentRepository;
import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.Agency;
import learn.field_agent.models.AgencyAgent;
import learn.field_agent.models.Agent;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class SecurityClearanceServiceTest {

    @MockBean
    SecurityClearanceRepository securityClearanceRepository;

    @MockBean
    AgencyAgentRepository agencyAgentRepository;

    @Autowired
    SecurityClearanceService service;

    @Test
    void shouldNotAddNull() {
        Result<SecurityClearance> result = service.add(null);
        assertNull(result.getPayload());
        assertEquals(ResultType.INVALID, result.getType());
        System.out.println(result.getMessages());
    }

    @Test
    void shouldNotAddNullOrBlankName() {
        SecurityClearance badSecurityClearance = new SecurityClearance(0, null);
        Result<SecurityClearance> result = service.add(badSecurityClearance);
        assertEquals(ResultType.INVALID, result.getType());
        assertNull(result.getPayload());
        System.out.println(result.getMessages());

        badSecurityClearance = new SecurityClearance(0, " ");
        result = service.add(badSecurityClearance);
        assertEquals(ResultType.INVALID, result.getType());
        assertNull(result.getPayload());
        System.out.println(result.getMessages());
    }

    @Test
    void shouldNotAddDuplicateName() {
        List<SecurityClearance> securityClearances = List.of(
                new SecurityClearance(1, "Test"),
                new SecurityClearance(2, "Test Test")
        );

        when(securityClearanceRepository.findAll()).thenReturn(securityClearances);

        SecurityClearance duplicateName = new SecurityClearance(0, "test");

        Result<SecurityClearance> result = service.add(duplicateName);
        assertEquals(ResultType.INVALID, result.getType());
        assertNull(result.getPayload());
        System.out.println(result.getMessages());
    }

    @Test
    void shouldNotAddWithIdNotZero() {
        SecurityClearance badSecurityClearance = new SecurityClearance(1, "Test");
        Result<SecurityClearance> result = service.add(badSecurityClearance);
        assertEquals(ResultType.INVALID, result.getType());
        assertNull(result.getPayload());
        System.out.println(result.getMessages());
    }

    @Test
    void shouldAdd() {
        SecurityClearance securityClearanceIn = new SecurityClearance(0, "Test");
        SecurityClearance securityClearanceOut = new SecurityClearance(1, "Test");

        when(securityClearanceRepository.add(securityClearanceIn)).thenReturn(securityClearanceOut);

        Result<SecurityClearance> result = service.add(securityClearanceIn);

        assertEquals(ResultType.SUCCESS, result.getType());
        assertEquals(securityClearanceOut, result.getPayload());
    }

    @Test
    void shouldNotUpdateNull() {
        Result<SecurityClearance> result = service.update(null);
        assertNull(result.getPayload());
        assertEquals(ResultType.INVALID, result.getType());
        System.out.println(result.getMessages());
    }

    @Test
    void shouldNotUpdateNullOrBlankName() {
        SecurityClearance badSecurityClearance = new SecurityClearance(1, null);
        Result<SecurityClearance> result = service.update(badSecurityClearance);
        assertEquals(ResultType.INVALID, result.getType());
        System.out.println(result.getMessages());

        badSecurityClearance = new SecurityClearance(1, " ");
        result = service.update(badSecurityClearance);
        assertEquals(ResultType.INVALID, result.getType());
        System.out.println(result.getMessages());
    }

    @Test
    void shouldNotUpdateDuplicateName() {
        List<SecurityClearance> securityClearances = List.of(
                new SecurityClearance(1, "Test"),
                new SecurityClearance(2, "Test Test")
        );

        when(securityClearanceRepository.findAll()).thenReturn(securityClearances);

        SecurityClearance badSecurityClearance = new SecurityClearance(5, "test");
        Result<SecurityClearance> result = service.update(badSecurityClearance);
        assertEquals(ResultType.INVALID, result.getType());
        System.out.println(result.getMessages());
    }

    @Test
    void shouldNotUpdateMissing() {
        List<SecurityClearance> securityClearances = List.of(
                new SecurityClearance(1, "Test"),
                new SecurityClearance(2, "Test Test")
        );

        when(securityClearanceRepository.findAll()).thenReturn(securityClearances);

        SecurityClearance badSecurityClearance = new SecurityClearance(5, "This will not work");
        Result<SecurityClearance> result = service.update(badSecurityClearance);
        assertEquals(ResultType.NOT_FOUND, result.getType());
        System.out.println(result.getMessages());
    }

    @Test
    void shouldUpdateExisting() {
        SecurityClearance goodSecurityClearance = new SecurityClearance(1, "Test");

        when(securityClearanceRepository.update(goodSecurityClearance)).thenReturn(true);

        Result<SecurityClearance> result = service.update(goodSecurityClearance);

        assertEquals(ResultType.SUCCESS, result.getType());
    }

    @Test
    void shouldNotDeleteIdInUse() {
        SecurityClearance securityClearance = new SecurityClearance(1, "Secret");

        Agent agent = new Agent();
        agent.setAgentId(1);
        agent.setFirstName("Kristin");
        agent.setMiddleName("M.");
        agent.setLastName("Doering");
        agent.setDob(LocalDate.of(1996, 3, 24));
        agent.setHeightInInches(66);

        AgencyAgent agencyAgent = new AgencyAgent();
        agencyAgent.setAgencyId(1);
        agencyAgent.setIdentifier("Dingo Nevada");
        agencyAgent.setActivationDate(LocalDate.of(2006, 3, 24));
        agencyAgent.setActive(true);
        agencyAgent.setAgent(agent);
        agencyAgent.setSecurityClearance(securityClearance);

        List<AgencyAgent> agencyAgents = List.of(agencyAgent);

        when(agencyAgentRepository.findAll()).thenReturn(agencyAgents);

        Result<SecurityClearance> result = service.deleteById(1);
        assertEquals(ResultType.INVALID, result.getType());
        System.out.println(result.getMessages());
    }

    @Test
    void shouldNotDeleteMissingId() {
        Result<SecurityClearance> result = service.deleteById(1);
        assertEquals(ResultType.NOT_FOUND, result.getType());
        System.out.println(result.getMessages());
    }

    @Test
    void shouldDeleteExistingId() {
        when(securityClearanceRepository.deleteById(1)).thenReturn(true);
        Result<SecurityClearance> result = service.deleteById(1);
        assertEquals(ResultType.SUCCESS, result.getType());
    }
}



















