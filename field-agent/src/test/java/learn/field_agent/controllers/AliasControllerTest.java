package learn.field_agent.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import learn.field_agent.data.AliasRepository;
import learn.field_agent.models.Alias;
import learn.field_agent.models.SecurityClearance;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.Mockito.when;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AliasControllerTest {

    @MockBean
    AliasRepository repository;

    @Autowired
    MockMvc mockMvc;

    @Test
    void findAllShouldReturn200WhenFound() throws Exception{
        List<Alias> all = List.of(
               new Alias(1, "Dingo Nevada", null, 1),
               new Alias(2, "Dingo New Hampshire", null, 1)
        );

        ObjectMapper jsonMapper = new ObjectMapper();
        String expectedJson = jsonMapper.writeValueAsString(all);

        when(repository.findAll()).thenReturn(all);

        mockMvc.perform(get("/api/alias"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void findAllShouldReturn200WhenEmpty() throws Exception {
        mockMvc.perform(get("/api/alias")).andExpect(status().isOk());
    }

    @Test
    void shouldReturn404WhenFindingMissingId() throws Exception {
        var request = get("/api/alias/1");
        mockMvc.perform(request).andExpect(status().isNotFound());
    }

    @Test
    void shouldReturn200WhenFindingExistingId() throws Exception {
        Alias alias = new Alias(1, "Name", null, 1);

        ObjectMapper jsonMapper = new ObjectMapper();
        String expectedJson = jsonMapper.writeValueAsString(alias);

        when(repository.findById(1)).thenReturn(alias);

        var request = get("/api/alias/1");

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(expectedJson));
    }

    @Test
    void addShouldReturn400WhenAddingNull() throws Exception {
        ObjectMapper jsonMapper = new ObjectMapper();
        String jsonIn = jsonMapper.writeValueAsString(null);

        var request = post("/api/alias")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonIn);
        mockMvc.perform(request).andExpect(status().isBadRequest());
    }

    @Test
    void addShouldReturn400WhenInvalid() throws Exception {

    }
}