package com.pectusfinance.interview.serviceimpl;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;
import com.pectusfinance.interview.dto.ExpansesDataDto;
import com.pectusfinance.interview.service.ExpancesDataService;

@Service
public class ExpansesDataServiceImpl implements ExpancesDataService {

	private static final String ASC = "asc";
	private static final String DESC = "desc";
	private static final String SORT = "sort";
	private static final String VALUE = "value";
	private static final String DATE = "date";
	private static final String MEMBER_NAME = "member_name";
	private static final String AMOUNT = "amount";
	private static final String PROJECT_NAME = "project_name";
	private static final String CONDITION = "condition";
	private static final String DEPARTMENTS = "departments";
	private static final String FILED = "filed";
	private static final String FIELD_AND_CONDITION_SPLITER = "__";
	private static final String EQUALS = "equals";
	private static final String NOT_EQUALS = "n_eq";
	private static final String GT = "gt";
	private static final String LT = "lt";
	private static final String LT_EQ = "lt_eq";
	private static final String GT_EQ = "gt_eq";
	private static final String FILE_PATH = "expanses.csv";

	List<String> VALID_FILEDS = List.of(DEPARTMENTS, PROJECT_NAME, AMOUNT, MEMBER_NAME, DATE);

	@Override
	public List<ExpansesDataDto> getAllExpansesData(String filter) {
		List<ExpansesDataDto> expansesData = null;
		try {
			List<String[]> data = readAllLines(Path.of(FILE_PATH));
			data.remove(0);
			expansesData = data.stream().map(ExpansesDataDto::new).toList();
			List<HashMap<String, String>> filterModels = getFiltersAndConditions(filter);

			for (HashMap<String, String> filterModel : filterModels) {
				if (filterModel.get(FILED).equals(DEPARTMENTS)) {
					expansesData = getFilteredDepartment(expansesData, filterModel);
				} else if (filterModel.get(FILED).equals(PROJECT_NAME)) {
					expansesData = getFilteredProject(expansesData, filterModel);
				} else if (filterModel.get(FILED).equals(MEMBER_NAME)) {
					expansesData = getFilteredMembers(expansesData, filterModel);
				} else if (filterModel.get(FILED).equals(DATE)) {
					expansesData = getFilteredDate(expansesData, filterModel);
				} else if (filterModel.get(FILED).equals(AMOUNT)) {
					expansesData = getFilteredAmount(expansesData, filterModel);
				} else if (filterModel.get(FILED).equals(SORT)) {

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return expansesData;
	}

	private List<String[]> readAllLines(Path filePath) throws Exception {
		try (Reader reader = Files.newBufferedReader(filePath)) {
			try (CSVReader csvReader = new CSVReader(reader)) {
				return csvReader.readAll();
			}
		}
	}

	private List<HashMap<String, String>> getFiltersAndConditions(String filter) {
		String sort = null;
		List<HashMap<String, String>> filtes = new ArrayList<>();
		if (filter.contains(SORT)) {
			String sortField = "";
			String sortOrder = "";
			if (filter.contains(DESC)) {
				sort = filter.substring(filter.indexOf(SORT), filter.lastIndexOf(DESC));
				sortOrder = DESC;
				filter = filter.replace(DESC, "");
			} else {
				sort = filter.substring(filter.indexOf(SORT), filter.lastIndexOf(ASC));
				sortOrder = ASC;
				filter = filter.replace(ASC, "");
			}
			filter = filter.replace("&" + sort, "");
			sortField = sort.replace("order=", "").replace("sort=", "").replace("&", "");
			filter = filter.replace(sort, "");
			HashMap<String, String> filterMap = new HashMap<>();
			filterMap.put(FILED, sortField);
			filterMap.put(CONDITION, "sort");
			filterMap.put(VALUE, sortOrder);
			filtes.add(filterMap);

		}

		String[] filtersAndCondition = filter.strip().split("&");

		for (String txt : filtersAndCondition) {

			String[] fieldAndCondition = txt.split("=");
			String filed = fieldAndCondition[0];
			String value = fieldAndCondition[1];
			String condition = EQUALS;
			if (fieldAndCondition[0].contains(FIELD_AND_CONDITION_SPLITER)) {
				condition = filed.split(FIELD_AND_CONDITION_SPLITER)[1];
				filed = filed.split(FIELD_AND_CONDITION_SPLITER)[0];
			}
			if (VALID_FILEDS.contains(filed)) {
				HashMap<String, String> filterMap = new HashMap<>();
				filterMap.put(FILED, filed);
				filterMap.put(CONDITION, condition);
				filterMap.put(VALUE, value);
				filtes.add(filterMap);
			}
		}
		return filtes;
	}

	private List<ExpansesDataDto> getFilteredAmount(List<ExpansesDataDto> expansesData,
			HashMap<String, String> filterModel) {
		if (filterModel.get(CONDITION).equals(EQUALS)) {
			expansesData = expansesData.stream()
					.filter(exData -> exData.getAmount().equals(Double.valueOf(filterModel.get(VALUE)))).toList();
		} else if (filterModel.get(CONDITION).equals(NOT_EQUALS)) {
			expansesData = expansesData.stream()
					.filter(exData -> !exData.getAmount().equals(Double.valueOf(filterModel.get(VALUE)))).toList();
		} else if (filterModel.get(CONDITION).equals(GT)) {
			expansesData = expansesData.stream()
					.filter(exData -> exData.getAmount() > Double.valueOf(filterModel.get(VALUE))).toList();
		} else if (filterModel.get(CONDITION).equals(LT)) {
			expansesData = expansesData.stream()
					.filter(exData -> exData.getAmount() < Double.valueOf(filterModel.get(VALUE))).toList();
		} else if (filterModel.get(CONDITION).equals(GT_EQ)) {
			expansesData = expansesData.stream()
					.filter(exData -> exData.getAmount() >= Double.valueOf(filterModel.get(VALUE))).toList();
		} else if (filterModel.get(CONDITION).equals(LT_EQ)) {
			expansesData = expansesData.stream()
					.filter(exData -> exData.getAmount() <= Double.valueOf(filterModel.get(VALUE))).toList();
		} else if (filterModel.get(CONDITION).equals(SORT)) {
			if (filterModel.get(VALUE).equals(DESC)) {
				expansesData = expansesData.stream()
						.sorted(Comparator.comparingDouble(ExpansesDataDto::getAmount).reversed()).toList();
			} else {
				expansesData = expansesData.stream().sorted(Comparator.comparingDouble(ExpansesDataDto::getAmount))
						.toList();
			}

		}
		return expansesData;
	}

	private List<ExpansesDataDto> getFilteredDate(List<ExpansesDataDto> expansesData,
			HashMap<String, String> filterModel) {
		if (filterModel.get(CONDITION).equals(EQUALS)) {
			expansesData = expansesData.stream().filter(exData -> exData.getDate().equals(filterModel.get(VALUE)))
					.toList();
		} else if (filterModel.get(CONDITION).equals(NOT_EQUALS)) {
			expansesData = expansesData.stream().filter(exData -> !exData.getDate().equals(filterModel.get(VALUE)))
					.toList();
		} else if (filterModel.get(CONDITION).equals(SORT)) {
			expansesData = expansesData.stream().sorted(Comparator.comparing(ExpansesDataDto::getDate)).toList();
		}
		return expansesData;
	}

	private List<ExpansesDataDto> getFilteredMembers(List<ExpansesDataDto> expansesData,
			HashMap<String, String> filterModel) {
		if (filterModel.get(CONDITION).equals(EQUALS)) {
			expansesData = expansesData.stream().filter(exData -> exData.getMemberName().equals(filterModel.get(VALUE)))
					.toList();
		} else if (filterModel.get(CONDITION).equals(NOT_EQUALS)) {
			expansesData = expansesData.stream()
					.filter(exData -> !exData.getMemberName().equals(filterModel.get(VALUE))).toList();
		} else if (filterModel.get(CONDITION).equals(SORT)) {
			if (filterModel.get(VALUE).equals(DESC)) {
				expansesData = expansesData.stream().sorted(Comparator.comparing(ExpansesDataDto::getMemberName)).toList();	
			}else {
				expansesData = expansesData.stream().sorted(Comparator.comparing(ExpansesDataDto::getMemberName).reversed()).toList();
			}
			
		}
		return expansesData;
	}

	private List<ExpansesDataDto> getFilteredProject(List<ExpansesDataDto> expansesData,
			HashMap<String, String> filterModel) {
		if (filterModel.get(CONDITION).equals(EQUALS)) {
			expansesData = expansesData.stream()
					.filter(exData -> exData.getProjectName().equals(filterModel.get(VALUE))).toList();
		} else if (filterModel.get(CONDITION).equals(NOT_EQUALS)) {
			expansesData = expansesData.stream()
					.filter(exData -> !exData.getProjectName().equals(filterModel.get(VALUE))).toList();
		} else if (filterModel.get(CONDITION).equals(SORT)) {
			if (filterModel.get(VALUE).equals(DESC)) {
				expansesData = expansesData.stream()
						.sorted(Comparator.comparing(ExpansesDataDto::getProjectName).reversed()).toList();
			} else {
				expansesData = expansesData.stream().sorted(Comparator.comparing(ExpansesDataDto::getProjectName))
						.toList();
			}

		}
		return expansesData;
	}

	private List<ExpansesDataDto> getFilteredDepartment(List<ExpansesDataDto> expansesData,
			HashMap<String, String> filterModel) {
		if (filterModel.get(CONDITION).equals(EQUALS)) {
			expansesData = expansesData.stream()
					.filter(exData -> exData.getDepartments().equals(filterModel.get(VALUE))).toList();
		} else if (filterModel.get(CONDITION).equals(NOT_EQUALS)) {
			expansesData = expansesData.stream()
					.filter(exData -> !exData.getDepartments().equals(filterModel.get(VALUE))).toList();
		} else if (filterModel.get(CONDITION).equals(SORT)) {
			if (filterModel.get(VALUE).equals(DESC)) {
				expansesData = expansesData.stream()
						.sorted(Comparator.comparing(ExpansesDataDto::getDepartments).reversed()).toList();
			} else {
				expansesData = expansesData.stream().sorted(Comparator.comparing(ExpansesDataDto::getDepartments))
						.toList();
			}
		}
		return expansesData;
	}
}
