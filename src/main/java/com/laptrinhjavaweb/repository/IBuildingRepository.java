package com.laptrinhjavaweb.repository;

import com.laptrinhjavaweb.entity.BuildingEntity;

public interface IBuildingRepository extends JpaRepository<BuildingEntity, Long> {

	void update(BuildingEntity buildingEntity);
	
	void save (BuildingEntity buildingEntity);
}
