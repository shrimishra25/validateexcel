package com.aws.codestar.projecttemplates.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Iterator;
import java.net.URL;
import java.lang.*;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.aws.codestar.projecttemplates.Bean.TransactionResponse;

/**
 * Basic Spring web service controller that handles all GET requests.
 */
@RestController
@RequestMapping("/")
public class HelloWorldController {

	private static final String MESSAGE_FORMAT = "Hello %s!";

    @RequestMapping(value="/validate", method = RequestMethod.GET, produces = "application/json")
    public String helloWorldGet(@RequestParam(value = "id", defaultValue = "") String id) {
        return excelResponse(id);
    }

    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity helloWorldPost(@RequestParam(value = "name", defaultValue = "World") String name) {
        return ResponseEntity.ok(createResponse(name));
    }

    private String createResponse(String name) {
        return new JSONObject().put("Output", String.format(MESSAGE_FORMAT, name)).toString();
    }
    
    private String excelResponse(String id) {
        String str = "Append here: ";
        ArrayList<String> data = new ArrayList<String>();
    	String dataString;
        File excelFile = new File("event.xlsx");
        boolean emp_present = false;
        //str += HelloWorldController.class.getResource("event.xlsx").getPath();
        try {
        		ClassLoader classLoader = getClass().getClassLoader();
        		File file = new File(classLoader.getResource("/event.xlsx").getFile());
        		FileInputStream fis = new FileInputStream(file);
        
        		// we create an XSSF Workbook object for our XLSX Excel File
        		XSSFWorkbook workbook = new XSSFWorkbook(fis);
        		// we get first sheet
        		XSSFSheet sheet = workbook.getSheetAt(0);
        		System.out.println("Sheet : "+ sheet.getFirstRowNum());
        		// we iterate on rows
        		Iterator<Row> rowIt = sheet.iterator();
        		//int cellSize = sheet.getRow(0).getLastCellNum();
        		rowIt.next();
        		while(rowIt.hasNext() && !emp_present) {
        			Row row = rowIt.next();  
        			// iterate on cells for the current row
        			Iterator<Cell> cellIterator = row.cellIterator();
        
        			while (cellIterator.hasNext()) {
        				Cell cell = cellIterator.next();
        				//change cell type to string as default it was taking as general
        				cell.setCellType(Cell.CELL_TYPE_STRING);
        				//str += cell.toString()+" ";
        				if(id.trim().equals(cell.toString().trim()) || emp_present) {
        					data.add(cell.toString());
        					//data +=cell.toString();
        					emp_present = true;
        				}
        			}
        			if(!data.isEmpty()) {
        				emp_present = true;
        				break;
        			}
        		}  
        		workbook.close();
        		fis.close();      
        } catch (FileNotFoundException e) {
            str += "File not found";
        } catch (IOException e) {
            str += "IO Exception caught";
        }
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
		if(emp_present) {
			dataString = gson.toJson(new TransactionResponse(data.get(0), data.get(1),
					data.get(2), data.get(3), data.get(4)));
		} else {
			dataString = gson.toJson(new TransactionResponse(null,null,null,null,null));
		}
		
        StringBuilder sb = new StringBuilder();
        if(!emp_present) {

			  System.out.println(json); //String msg = new
			  JSONObject().put("empexists", emp_present).toString(); sb.append(new
			  JSONObject().put("empdetail", dataString).toString()); sb.append(new
			  JSONObject().put("statuscode", "200").toString()); sb.append(new
			  JSONObject().put("statusmessage", "OK").toString());
			 
        } else {
        	sb.append(new JSONObject().put("empexists", emp_present).toString());
        	sb.append(new JSONObject().put("empdetail", dataString).toString());
        	sb.append(new JSONObject().put("statuscode", "201").toString());
        	sb.append(new JSONObject().put("statusmessage", "Failed to update").toString());
        }
        //String msg = list1.toString();
        //sb.append(new JSONObject().put("employee_present", emp_present).toString());
       // String msg = new JSONObject().put("employee_present", emp_present).toString();
        return sb.toString();
    }
}