package com.pectusfinance.interview.serviceimpl;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;
import com.pectusfinance.interview.dto.ExpansesDataDto;
import com.pectusfinance.interview.service.ExpancesDataService;

@Service
public class ExpansesDataServiceImpl implements ExpancesDataService {

	private static final String FILE_PATH = "expanses.csv";

	private List<String[]> readAllLines(Path filePath) throws Exception {
		try (Reader reader = Files.newBufferedReader(filePath)) {
			try (CSVReader csvReader = new CSVReader(reader)) {
				return csvReader.readAll();
			}
		}
	}

	@Override
	public List<ExpansesDataDto> getAllExpansesData() {
		List<ExpansesDataDto> expansesData = null;
		try {
			List<String[]> data = readAllLines(Path.of(FILE_PATH));
			data.remove(0);
			expansesData = data.stream().map(ExpansesDataDto::new).toList();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return expansesData;
	}
}
