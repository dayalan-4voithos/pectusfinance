package com.pectusfinance.interview.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pectusfinance.interview.dto.ExpansesDataDto;
import com.pectusfinance.interview.service.ExpancesDataService;

@RestController
public class ExpansesDataController {
	@Autowired
	ExpancesDataService expancesDataService;
	
	@GetMapping("/getAllExpenses")
	public List<ExpansesDataDto> getAllExpansesData(){
		
		return expancesDataService.getAllExpansesData();
	}
}
