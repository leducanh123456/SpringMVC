package com.laptrinhjavaweb.service.impl;

import java.util.ArrayList;
import java.util.List;

import com.laptrinhjavaweb.dto.BuildingDTO;
import com.laptrinhjavaweb.entity.BuildingEntity;
import com.laptrinhjavaweb.repository.IBuildingRepository;
import com.laptrinhjavaweb.repository.impl.BuildingRepository;
import com.laptrinhjavaweb.service.IBuidingService;

public class BuildingService implements IBuidingService {

	IBuildingRepository buildingRepository = new BuildingRepository();

	@Override
	public List<BuildingDTO> findAll() {
		List<BuildingEntity> buildingEntities = buildingRepository.findAll();
		List<BuildingDTO> buildingDTOs = new ArrayList<BuildingDTO>();
		for (BuildingEntity item : buildingEntities) {
			BuildingDTO buildingDTO = new BuildingDTO();
			buildingDTO.setName(item.getName());
			buildingDTO.setWard(item.getWard());
			buildingDTOs.add(buildingDTO);
		}
		return buildingDTOs;
	}

}
