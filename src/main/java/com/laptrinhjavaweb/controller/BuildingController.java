package com.laptrinhjavaweb.controller;

import java.util.List;

import com.laptrinhjavaweb.dto.BuildingDTO;
import com.laptrinhjavaweb.service.IBuidingService;
import com.laptrinhjavaweb.service.impl.BuildingService;

public class BuildingController {
	static final String DB_URL = "jdbc:mysql://localhost:3306/estate32020module1part1";

	// Database credentials
	static final String USER = "root";
	static final String PASS = "123456789";

	public static void main(String[] args) {
//		// data nhận từ client người dùng
//		String[] types = new String[] { "TANG_TRET", "NGUYEN_CAN" };
//		Integer rentAreaFrom = 300;
//		Integer rentAreaTo = 400;
		IBuidingService buildingservice = new BuildingService();
		List<BuildingDTO> result = buildingservice.findAll();
		for (BuildingDTO item : result) {
			System.out.println("tên tòa nhà : " + item.getName());
			System.out.println("phường :" + item.getWard());
		}
	}
}
