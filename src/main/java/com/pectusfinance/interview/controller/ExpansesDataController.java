package com.pectusfinance.interview.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pectusfinance.interview.dto.ExpansesDataDto;
import com.pectusfinance.interview.service.ExpancesDataService;

@RestController
@RequestMapping("expanses")
public class ExpansesDataController {
	@Autowired
	ExpancesDataService expancesDataService;
	
	@GetMapping("/getAllExpenses/{filter}")
	public List<ExpansesDataDto> getAllExpansesData(@PathVariable String filter){
		return expancesDataService.getAllExpansesData(filter);
	}
}
