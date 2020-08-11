package com.example.co.services;

import com.example.co.exceptions.ConflictException;
import com.example.co.exceptions.NotFoundException;
import com.example.co.model.Project;
import com.example.co.model.SdlcSystem;
import com.example.co.model.UtilityConverter;
import com.example.co.repositories.ProjectRepository;
import com.example.co.repositories.SdlcSystemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

/**
 * This is Service Implementation class for Project
 */
@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

	private final ProjectRepository projectRepository;
	private final SdlcSystemRepository sdlcSystemRepository;

	/**
	 * Gets the project from db
	 * Return NotFoundException if id not found in db
	 * @param id: Identifier for project
	 * @return project
	 */
	public Project getProject(long id) {
		return projectRepository.findById(id).orElseThrow(() -> new NotFoundException(Project.class, id));
	}

	/**
	 * Creates a project and insert new row in db
	 * @param project
	 * @return : project
	 */
	@Override
	public Project createProject(Project project) {
		try {
			project.setCreatedDate(Instant.now());
			project.setLastModifiedDate(Instant.now());
			project.setSdlcSystem(sdlcSystemRepository.findById(project.getSdlcSystem().getId())
					.orElseThrow(() -> new NotFoundException(SdlcSystem.class, project.getSdlcSystem().getId())));
			return projectRepository.save(project);
		}
		catch (DataIntegrityViolationException e) {
			throw new ConflictException("Integrity Constraint violated.", e);
		}
	}

	/**
	 * Updates the provided fields in patchObject in db
	 * @param projectId : Identifier of the project to be updated
	 * @param patchObject : Object with the keys and values to be updated
	 * @return project
	 */
	@Override
	public Project updateProject(long projectId, Map<String, Object> patchObject) {
		try{
			Project oldProject = projectRepository.findById(projectId)
					.orElseThrow(() -> new NotFoundException(Project.class, projectId));
			UtilityConverter.getProject(oldProject, patchObject, sdlcSystemRepository);
			oldProject.setLastModifiedDate(Instant.now());
			return projectRepository.save(oldProject);
		} catch (DataIntegrityViolationException e) {
			throw new ConflictException("Integrity Constraint violated.", e);
		}
	}
}
