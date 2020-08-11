package com.example.co.services;

import com.example.co.model.Project;
import java.util.Map;

/**
 * This is Project Service Interface
 * There are three main methods
 *  1. getProject: To get details of project
 *  2. createProject: To create project with given details
 *  3. updateProject: To update fields given
 */
public interface ProjectService {

    Project getProject(long id);

    Project createProject(Project project);

    Project updateProject(long projectId, Map<String, Object> patchObject);
}
