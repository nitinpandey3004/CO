package com.example.co.services;

import com.example.co.exceptions.ConflictException;
import com.example.co.exceptions.NotFoundException;
import com.example.co.model.Project;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class ProjectServiceTest {

    @Autowired
    ProjectService projectService;

    //tests related to get method

    @Test
    public void getNormalProjectWithValidData() {
        long id = 1;
        Project project = projectService.getProject(id);
        assertThat(project).isNotNull();
        assertThat(project.getId()).isEqualTo(id);
    }

    @Test(expected = NotFoundException.class)
    public void getInvalidProjectExpectsNotFound() {
        long id = 12345;
        Project project = projectService.getProject(id);
    }

    //tests related to create method

    @Test
    public void createProjectWithValidData() {
        String jsonBody = "{\n" +
                "\t\"externalId\": \"EXTERNALID\",\n" +
                "\t\"name\": \"Name\",\n" +
                "\t\"sdlcSystem\": {\n" +
                "\t\t\"id\": 1\n" +
                "\t}\n" +
                "}";
        Project project = new Gson().fromJson(jsonBody, Project.class);
        Project project1 = projectService.createProject(project);
        assertThat(project1).isNotNull();
        assertThat(project1.getId()).isNotNull();
        assertThat(project1.getName()).isEqualTo(project.getName());
    }

    @Test
    public void createProjectWithPartialData() {
        String jsonBody = "{\n" +
                "\t\"externalId\": \"EXTERNAL-ID\",\n" +
                "\t\"sdlcSystem\": {\n" +
                "\t\t\"id\": 1\n" +
                "\t}\n" +
                "}";
        Project project = new Gson().fromJson(jsonBody, Project.class);
        Project project1 = projectService.createProject(project);
        assertThat(project1).isNotNull();
        assertThat(project1.getId()).isNotNull();
        assertThat(project1.getExternalId()).isEqualTo(project.getExternalId());
    }

    @Test(expected = NotFoundException.class)
    public void createProjectWithNonExistingData() {
        String jsonBody = "{\n" +
                "\t\"externalId\": \"EXTERNALID\",\n" +
                "\t\"sdlcSystem\": {\n" +
                "\t\t\"id\": 12345\n" +
                "\t}\n" +
                "}";
        Project project = new Gson().fromJson(jsonBody, Project.class);
        Project project1 = projectService.createProject(project);
    }

    @Test(expected = ConflictException.class)
    public void createProjectWithConflictingData() {
        String jsonBody = "{\n" +
                "\t\"externalId\": \"SAMPLEPROJECT\",\n" +
                "\t\"sdlcSystem\": {\n" +
                "\t\t\"id\": 1\n" +
                "\t}\n" +
                "}";
        Project project = new Gson().fromJson(jsonBody, Project.class);
        Project project1 = projectService.createProject(project);
    }

    //tests update methods
    @Test
    public void updateProjectWithAllData() throws JsonProcessingException {
        String jsonBody = "{\n" +
                "\t\"externalId\": \"EXTERNALIDEDITED\",\n" +
                "\t\"name\": \"Name-Edited\",\n" +
                "\t\"sdlcSystem\": {\n" +
                "\t\t\"id\": 1" +
                "\t}\n" +
                "}";
        long projectId = 5;

        Map<String, Object> patchData = new ObjectMapper().readValue(jsonBody, Map.class);
        Project newProject = projectService.updateProject(projectId, patchData);

        assertThat(newProject).isNotNull();
        assertThat(newProject.getId()).isEqualTo(projectId);
        assertThat(newProject.getName()).isEqualTo(patchData.get("name"));
    }

    @Test
    public void updateProjectWithOnlyExternalId() throws JsonProcessingException {
        String jsonBody = "{\n" +
                "\t\"externalId\": \"EXTERNALIDEDITED\"\n" +
                "}";
        long projectId = 6;

        Map<String, Object> patchData = new ObjectMapper().readValue(jsonBody, Map.class);
        Project newProject = projectService.updateProject(projectId, patchData);

        assertThat(newProject).isNotNull();
        assertThat(newProject.getId()).isEqualTo(projectId);
        assertThat(newProject.getExternalId()).isEqualTo(patchData.get("externalId"));
    }

    @Test
    public void updateProjectWithOnlySdlcSystemId() throws JsonProcessingException {
        String jsonBody = "{\n" +
                "\t\"sdlcSystem\": {\n" +
                "\t\t\"id\": 1\n" +
                "\t}\n" +
                "}";
        long projectId = 7;

        Map<String, Object> patchData = new ObjectMapper().readValue(jsonBody, Map.class);
        Project newProject = projectService.updateProject(projectId, patchData);

        assertThat(newProject).isNotNull();
        assertThat(newProject.getId()).isEqualTo(projectId);
        assertThat(((int) newProject.getSdlcSystem().getId())).isEqualTo(((Map)patchData.get("sdlcSystem")).get("id"));
    }

    @Test
    public void updateProjectWithEmptyData() throws JsonProcessingException {
        String jsonBody = "{}";
        long projectId = 8;

        Map<String, Object> patchData = new ObjectMapper().readValue(jsonBody, Map.class);
        Project newProject = projectService.updateProject(projectId, patchData);

        assertThat(newProject).isNotNull();
        assertThat(newProject.getId()).isEqualTo(projectId);
    }

    @Test
    public void updateProjectWithNullName() throws JsonProcessingException {
        String jsonBody = "{\n" +
                "    \"name\": null\n" +
                "}";
        long projectId = 5;

        Map<String, Object> patchData = new ObjectMapper().readValue(jsonBody, Map.class);
        Project newProject = projectService.updateProject(projectId, patchData);

        assertThat(newProject).isNotNull();
        assertThat(newProject.getName()).isNull();
    }

    @Test (expected = NotFoundException.class)
    public void updateProjectWithNonExitingSystem() throws JsonProcessingException {
        String jsonBody = "{\n" +
                "\t\"sdlcSystem\": {\n" +
                "\t\t\"id\": 12345\n" +
                "\t}\n" +
                "}";
        long projectId = 1;

        Map<String, Object> patchData = new ObjectMapper().readValue(jsonBody, Map.class);
        Project newProject = projectService.updateProject(projectId, patchData);
    }

    @Test (expected = ConflictException.class)
    public void updateProjectWithConflictingSystem() throws JsonProcessingException {
        String jsonBody = "{\n" +
                "\t\"sdlcSystem\": {\n" +
                "\t\t\"id\": 2\n" +
                "\t}\n" +
                "}";
        long projectId = 1;

        Map<String, Object> patchData = new ObjectMapper().readValue(jsonBody, Map.class);
        Project newProject = projectService.updateProject(projectId, patchData);
    }

    @Test (expected = ConflictException.class)
    public void updateProjectWithConflictingExternal() throws JsonProcessingException {
        String jsonBody = "{\n" +
                "\t\"externalId\": \"PROJECTX\"\n" +
                "}";
        long projectId = 1;

        Map<String, Object> patchData = new ObjectMapper().readValue(jsonBody, Map.class);
        Project newProject = projectService.updateProject(projectId, patchData);
    }

    @Test (expected = ConflictException.class)
    public void updateProjectWithConflictingSystemAndExternal() throws JsonProcessingException {
        String jsonBody = "{\n" +
                "\t\"externalId\": \"PROJECTX\",\n" +
                "\t\"sdlcSystem\": {\n" +
                "\t\t\"id\": 2\n" +
                "\t}\n" +
                "}";
        long projectId = 1;

        Map<String, Object> patchData = new ObjectMapper().readValue(jsonBody, Map.class);
        Project newProject = projectService.updateProject(projectId, patchData);
    }

    @Test (expected = NotFoundException.class)
    public void updateProjectWithInvalidId() throws JsonProcessingException {
        String jsonBody = "{}";
        long projectId = 1234;

        Map<String, Object> patchData = new ObjectMapper().readValue(jsonBody, Map.class);
        Project newProject = projectService.updateProject(projectId, patchData);
    }


}
