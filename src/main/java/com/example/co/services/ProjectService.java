package com.example.co.services;

import com.example.co.model.Project;
import java.util.Map;

public interface ProjectService {

    Project getProject(long id);

    Project createProject(Project project);

    Project updateProject(long projectId, Map<String, Object> patchObject);
}
