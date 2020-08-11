package com.example.co.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.IsNull;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * This file tests ProjectRestController.java Integration
 * For Integration Testing we haven't used mocked service
 * Instead we have used standalone projectRestController directly
 */

@SpringBootTest
@RunWith(SpringRunner.class)
public class ProjectRestControllerIntegrationTest {

    private MockMvc mvc;

    @Autowired
    ProjectRestController projectRestController;

    @Before
    public void setup() {
        this.mvc = standaloneSetup(this.projectRestController).build();
    }

    //tests related to get

    @Test
    public void getNormalProject() throws Exception{
        this.mvc.perform(MockMvcRequestBuilders
                .get("/api/v2/projects/1")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").exists());
    }

    @Test
    public void getWithIllegalPath() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
                .get("/api/v2/projects/whatever")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getWithInvalidPath() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
                .get("/api/v2/projects/whatever")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getInvalidId() throws Exception {
        this.mvc.perform(MockMvcRequestBuilders
                .get("/api/v2/projects/12345")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    //tests related to create

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void createProjectWithFullPayloadExpects201() throws Exception{
        String jsonBody = "{\n" +
                "\t\"externalId\": \"EXTERNALID\",\n" +
                "\t\"name\": \"Name\",\n" +
                "\t\"sdlcSystem\": {\n" +
                "\t\t\"id\": 1\n" +
                "\t}\n" +
                "}";
        this.mvc.perform(MockMvcRequestBuilders
                .post("/api/v2/projects")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").exists());
    }

    @Test
    public void createProjectWithPartialPayloadExpects201() throws Exception{
        String jsonBody = "{\n" +
                "\t\"externalId\": \"EXTERNAL-ID\",\n" +
                "\t\"sdlcSystem\": {\n" +
                "\t\t\"id\": 1\n" +
                "\t}\n" +
                "}";
        this.mvc.perform(MockMvcRequestBuilders
                .post("/api/v2/projects")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").exists());
    }

    @Test
    public void createProjectWithIllegalValueExpects400() throws Exception{
        String jsonBody = "{\n" +
                "\t\"sdlcSystem\": {\n" +
                "\t\t\"id\": \"Whatever\"\n" +
                "\t}\n" +
                "}";
        this.mvc.perform(MockMvcRequestBuilders
                .post("/api/v2/projects")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createProjectWithoutExternalIdExpects400() throws Exception{
        String jsonBody = "{\n" +
                "\t\"sdlcSystem\": {\n" +
                "\t\t\"id\": 1\n" +
                "\t}\n" +
                "}";
        this.mvc.perform(MockMvcRequestBuilders
                .post("/api/v2/projects")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createProjectPayloadWithoutSystemExpects400() throws Exception{
        String jsonBody = "{\n" +
                "\t\"externalId\": \"EXTERNAL-ID\"\n" +
                "}";
        this.mvc.perform(MockMvcRequestBuilders
                .post("/api/v2/projects")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createProjectNonExistingSystemExpects400() throws Exception{
        String jsonBody = "{\n" +
                "\t\"externalId\": \"EXTERNALID\",\n" +
                "\t\"sdlcSystem\": {\n" +
                "\t\t\"id\": 12345\n" +
                "\t}\n" +
                "}";
        this.mvc.perform(MockMvcRequestBuilders
                .post("/api/v2/projects")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void createProjectConflictingSystemExpects400() throws Exception{
        String jsonBody = "{\n" +
                "\t\"externalId\": \"SAMPLEPROJECT\",\n" +
                "\t\"sdlcSystem\": {\n" +
                "\t\t\"id\": 1\n" +
                "\t}\n" +
                "}";
        this.mvc.perform(MockMvcRequestBuilders
                .post("/api/v2/projects")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    //tests related to patch

    @Test
    public void patchWithFullPayloadExpect200() throws Exception {
        String jsonBody = "{\n" +
                "\t\"externalId\": \"EXTERNALIDEDITED\",\n" +
                "\t\"name\": \"Name-Edited\",\n" +
                "\t\"sdlcSystem\": {\n" +
                "\t\t\"id\": 1\n" +
                "\t}\n" +
                "}";
        String id = "5";
        this.mvc.perform(MockMvcRequestBuilders
                .patch("/api/v2/projects/" + id)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Name-Edited"));
    }

    @Test
    public void patchWithOnlyExternalIdExpect200() throws Exception {
        String jsonBody = "{\n" +
                "\t\"externalId\": \"EXTERNALIDEDITED\"\n" +
                "}";
        String id = "6";
        this.mvc.perform(MockMvcRequestBuilders
                .patch("/api/v2/projects/" + id)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.externalId").value("EXTERNALIDEDITED"));
    }

    @Test
    public void patchWithOnlySdlcSystmeIdExpect200() throws Exception {
        String jsonBody = "{\n" +
                "\t\"sdlcSystem\": {\n" +
                "\t\t\"id\": 1\n" +
                "\t}\n" +
                "}";
        String id = "7";
        this.mvc.perform(MockMvcRequestBuilders
                .patch("/api/v2/projects/" + id)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.sdlcSystem.id").value("1"));
    }

    @Test
    public void patchWithEmptyPayloadIdExpect200() throws Exception {
        String jsonBody = "{\n" +
                "}";
        String id = "8";
        this.mvc.perform(MockMvcRequestBuilders
                .patch("/api/v2/projects/" + id)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Project Three"));
    }

    @Test
    public void patchWithNullNameExpect200() throws Exception {
        String jsonBody = "{\n" +
                "    \"name\": null\n" +
                "}";
        String id = "5";
        this.mvc.perform(MockMvcRequestBuilders
                .patch("/api/v2/projects/" + id)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(IsNull.nullValue()));
    }

    @Test
    public void patchWithIllegalValueExpect400() throws Exception {
        String jsonBody = "{\n" +
                "\t\"sdlcSystem\": {\n" +
                "\t\t\"id\": \"Whatever\"\n" +
                "\t}\n" +
                "}";
        String id = "1";
        this.mvc.perform(MockMvcRequestBuilders
                .patch("/api/v2/projects/" + id)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void patchWithNonExistingSystemExpect404() throws Exception {
        String jsonBody = "{\n" +
                "\t\"sdlcSystem\": {\n" +
                "\t\t\"id\": 12345\n" +
                "\t}\n" +
                "}";
        String id = "1";
        this.mvc.perform(MockMvcRequestBuilders
                .patch("/api/v2/projects/" + id)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    public void patchWithConflictingSystemExpect409() throws Exception {
        String jsonBody = "{\n" +
                "\t\"sdlcSystem\": {\n" +
                "\t\t\"id\": 2\n" +
                "\t}\n" +
                "}";
        String id = "1";
        this.mvc.perform(MockMvcRequestBuilders
                .patch("/api/v2/projects/" + id)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void patchWithConflictingExternalIdExpect409() throws Exception {
        String jsonBody = "{\n" +
                "\t\"externalId\": \"PROJECTX\"\n" +
                "}";
        String id = "1";
        this.mvc.perform(MockMvcRequestBuilders
                .patch("/api/v2/projects/" + id)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void patchWithConflictingSystemExternalIdExpect409() throws Exception {
        String jsonBody = "{\n" +
                "\t\"externalId\": \"PROJECTX\",\n" +
                "\t\"sdlcSystem\": {\n" +
                "\t\t\"id\": 2\n" +
                "\t}\n" +
                "}";
        String id = "1";
        this.mvc.perform(MockMvcRequestBuilders
                .patch("/api/v2/projects/" + id)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void patchWithIllegalPathVariableExpect400() throws Exception {
        String jsonBody = "{}";
        this.mvc.perform(MockMvcRequestBuilders
                .patch("/api/v2/projects/whatever")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void patchWithInvalidPathVariableExpect400() throws Exception {
        String jsonBody = "{}";
        String id = "1234";
        this.mvc.perform(MockMvcRequestBuilders
                .patch("/api/v2/projects/" + id)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound());
    }
}
