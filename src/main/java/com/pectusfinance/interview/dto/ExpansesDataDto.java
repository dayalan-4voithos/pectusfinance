package com.pectusfinance.interview.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExpansesDataDto {
	private String departments;
	private String projectName;
	private Double amount;
	private Date date;
	private String memberName;

	public ExpansesDataDto() {
		
	}

	public ExpansesDataDto(String[] data) {
		try {
			this.departments = data[0];
			this.projectName = data[1];
			this.amount = Double.valueOf(data[2].replace("€", "").replace(",", ""));
			this.date = new SimpleDateFormat("dd/MM/yyyy").parse(data[3]);
			this.memberName = data[4];
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public String getDepartments() {
		return departments;
	}

	public void setDepartments(String departments) {
		this.departments = departments;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}
}
