package com.example.co.services;

import com.example.co.exceptions.ConflictException;
import com.example.co.model.*;
import com.example.co.exceptions.NotFoundException;
import com.example.co.repositories.ProjectRepository;
import com.example.co.repositories.SdlcSystemRepository;
import lombok.RequiredArgsConstructor;

import org.aspectj.weaver.ast.Not;
import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

	private final ProjectRepository projectRepository;
	private final SdlcSystemRepository sdlcSystemRepository;

	public Project getProject(long id) {
		return projectRepository.findById(id).orElseThrow(() -> new NotFoundException(Project.class, id));
	}

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
