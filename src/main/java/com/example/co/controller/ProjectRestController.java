package com.example.co.controller;

import com.example.co.model.Project;
import com.example.co.services.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

/**
 * This is controller file for Project Rest Apis
 * It contains 3 main Methods:
 * 	1. Get
 * 	2. POST
 * 	3. PATCH
 */
@RestController
@RequestMapping(ProjectRestController.ENDPOINT)
@Api(produces = MediaType.APPLICATION_JSON_VALUE, tags = "Project")
public class ProjectRestController {

	public static final String ENDPOINT = "/api/v2/projects";
	public static final String GET_PROJECT_URI = "/{id}";
	public static final String POST_CREATE_PROJECT_URI = "";
	public static final String PATH_VARIABLE_ID = "id";
	public static final String UPDATE_PROJECT_URI = "/{projectId}";

	private static final String API_PARAM_ID = "ID";

	@Autowired
	private ProjectService projectService;

	/**
	 * To get the details of the project with given projectId
	 * @param projectId: Identifier of the project
	 * @return Project
	 */
	@ApiOperation("Get a Project")
	@RequestMapping(value=GET_PROJECT_URI, method=RequestMethod.GET)
	@ResponseBody
	public Project getProject(
			@ApiParam(name = API_PARAM_ID, required = true)
			@PathVariable(PATH_VARIABLE_ID)
			final long projectId) {
		return projectService.getProject(projectId);
	}

	/**
	 * Create a new project with provided details
	 * Expects valid project object
	 * @param project: Object with project details
	 * @return created project
	 */
	@ApiOperation("Create a Project")
	@RequestMapping(value=POST_CREATE_PROJECT_URI, method=RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.CREATED)
	public Project createProject(@Valid @RequestBody Project project) {
		return	projectService.createProject(project);
	}

	/**
	 * This method is used for updating the project
	 * @param projectId: Identifier of the project to be updated
	 * @param patchObject: Object with fields to be updated
	 * @return : project
	 */
	@ApiOperation("Update a Project")
	@RequestMapping(value = UPDATE_PROJECT_URI, method = RequestMethod.PATCH)
	@ResponseBody
	public Project updateProject(@PathVariable(value = "projectId") Long projectId, @RequestBody Map<String, Object> patchObject) {
		return projectService.updateProject(projectId, patchObject);
	}
}
