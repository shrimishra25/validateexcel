package com.aws.codestar.projecttemplates.Bean;

import com.google.gson.annotations.Expose;

public class TransactionResponse {
	@Expose
	private String empid;
	@Expose
	private String empname;
	@Expose
	private String duname;
	@Expose
	private String level;
	@Expose
	private String location;
	
	public TransactionResponse() {
		
	}

	public TransactionResponse(String empid, String empname, String duname,String level,String location) {
	    this.empid = empid;
	    this.empname = empname;
	    this.duname = duname;
	    this.level = level;
	    this.location = location;
	}
}
