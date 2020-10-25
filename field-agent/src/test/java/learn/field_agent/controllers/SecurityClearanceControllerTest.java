package learn.field_agent.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import learn.field_agent.data.AgencyAgentRepository;
import learn.field_agent.data.SecurityClearanceRepository;
import learn.field_agent.models.AgencyAgent;
import learn.field_agent.models.Agent;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityClearanceControllerTest {

    @MockBean
    SecurityClearanceRepository securityClearanceRepository;

    @MockBean
    AgencyAgentRepository agencyAgentRepository;

    @Autowired
    MockMvc mockMvc;

    @Test
    void findAllShouldReturn200WhenFound() throws Exception {
        List<SecurityClearance> all = List.of(
                new SecurityClearance(1, "Name 1"),
                new SecurityClearance(2, "Name 2"),
                new SecurityClearance(3, "Name 3")
        );

        ObjectMapper jsonMapper = new ObjectMapper();
        String expectedJson = jsonMapper.writeValueAsString(all);

        when(securityClearanceRepository.findAll()).thenReturn(all);

        mockMvc.perform(get("/api/security"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));

    }

    @Test
    void findAllShouldReturn200WhenEmpty() throws Exception {
        var request = get("/api/security");

        mockMvc.perform(request).andExpect(status().isOk());

    }

    @Test
    void shouldReturn404WhenFindingMissingId() throws Exception {
        var request = get("/api/security/1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn200WhenFindingExistingId() throws Exception {
        SecurityClearance clearance = new SecurityClearance(1, "Name");

        ObjectMapper jsonMapper = new ObjectMapper();
        String expectedJson = jsonMapper.writeValueAsString(clearance);

        when(securityClearanceRepository.findById(1)).thenReturn(clearance);

        var request = get("/api/security/1")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void addShouldReturn400WhenEmpty() throws Exception {
        var request = post("/api/security")
                .contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    void addShouldReturn400WhenInvalid() throws Exception {
        SecurityClearance clearance = new SecurityClearance(1, null);

        ObjectMapper jsonMapper = new ObjectMapper();
        String expectedJson = jsonMapper.writeValueAsString(clearance);

        var request = post("/api/security")
                .contentType(MediaType.APPLICATION_JSON)
                .content(expectedJson);

        mockMvc.perform(request)
                .andExpect(status().isBadRequest());
    }

    @Test
    void addShouldReturn415WhenMultipart() throws Exception {
        ObjectMapper jsonMapper = new ObjectMapper();

        SecurityClearance clearance = new SecurityClearance(0, "Name");
        String jsonIn = jsonMapper.writeValueAsString(clearance);

        var request = post("/api/security")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .content(jsonIn);

        mockMvc.perform(request)
                .andExpect(status().isUnsupportedMediaType());
    }

    @Test
    void addShouldReturn201() throws Exception {
        SecurityClearance securityClearanceIn = new SecurityClearance(0, "name");
        SecurityClearance securityClearanceOut = new SecurityClearance(1, "name");

        when(securityClearanceRepository.add(any())).thenReturn(securityClearanceOut);

        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonIn = jsonMapper.writeValueAsString(securityClearanceIn);
        String jsonOut = jsonMapper.writeValueAsString(securityClearanceOut);

        var request = post("/api/security")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonIn);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonOut));
    }

    @Test
    void updateShouldReturn409WhenIdDoesNotMatchRequestBody() throws Exception {
        SecurityClearance securityClearance = new SecurityClearance(1, "name");

        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonIn = jsonMapper.writeValueAsString(securityClearance);

        var request = put("/api/security/3")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonIn);

        mockMvc.perform(request).andExpect(status().isConflict());
    }

    @Test
    void shouldReturn400WhenUpdatingBlankName() throws Exception {
        SecurityClearance securityClearance = new SecurityClearance(1, "  ");

        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonIn = jsonMapper.writeValueAsString(securityClearance);

        var request = put("/api/security/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonIn);

        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn404WhenUpdatingClearanceNotFound() throws Exception {
        SecurityClearance securityClearance = new SecurityClearance(1, "Name");

        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonIn = jsonMapper.writeValueAsString(securityClearance);

        var request = put("/api/security/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonIn);

        mockMvc.perform(request).andExpect(status().isNotFound());
    }

    @Test
    void updateShouldReturn204() throws Exception {
        SecurityClearance securityClearance = new SecurityClearance(1, "Name");

        when(securityClearanceRepository.update(any())).thenReturn(true);

        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonIn = jsonMapper.writeValueAsString(securityClearance);

        var request = put("/api/security/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonIn);

        mockMvc.perform(request).andExpect(status().isNoContent());
    }

    @Test
    void shouldReturn400WhenDeleteByIdInUse() throws Exception {
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

        var request = delete("/api/security/1");

        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturn404WhenDeletingMissingId() throws Exception {
        var request = delete("/api/security/1");

        mockMvc.perform(request).andExpect(status().isNotFound());
    }

    @Test
    void deleteByIdShouldReturn204() throws Exception {

        when(securityClearanceRepository.deleteById(1)).thenReturn(true);

        var request = delete("/api/security/1");

        mockMvc.perform(request).andExpect(status().isNoContent());
    }

}