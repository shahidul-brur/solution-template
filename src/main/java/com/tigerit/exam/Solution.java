package com.tigerit.exam;


import static com.tigerit.exam.IO.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;


public class Solution implements Runnable {
     @Override
     public void run() {
    	
    	//number of test cases 
        int T = readLineAsInteger();
        
        //for each test case
        for(int cas = 1; cas <= T; cas++) {
        	
	        int nT = readLineAsInteger(); // number of tables
	        
	        ArrayList<ArrayList<Integer>> []data = new ArrayList[nT]; // values of all tables
	        HashMap<String, Integer> tableId = new HashMap<String, Integer>(); // table id for a table name
	        HashMap<String, Integer> colId = new HashMap<String, Integer>(); // column id for a column name
	        ArrayList<String> []columnList = new ArrayList[nT]; // list of all column names for each table
	        
	        for(int table = 0; table < nT; table++) {
	        	columnList[table] = new ArrayList<String>();
	        	data[table] = new ArrayList<ArrayList<Integer>>();
	        	String tableName = readLine();
	        	tableId.put(tableName, table);
	        	
	        	String line = readLine();
	        	String[] strs = line.split(" ");
	        	Integer nC = Integer.parseInt(strs[0]); // number of columns
	        	Integer nD = Integer.parseInt(strs[1]); // number of data records or row
	        	
	        	// column list
	        	line = readLine();
	        	strs = line.split(" ");
	        	for(int c=0;c<nC;c++) {
	        		String col = strs[c];
	        		columnList[table].add(col);
	        		colId.put(col, c);
	        	}
	        	
	        	//data records
	        	for(int row = 0; row <nD; row++) {
	        		ArrayList<Integer> record = new ArrayList<Integer>();
	        		line = readLine();
		        	String[] num = line.split(" ");
	        		for(int col = 0;col<nC; col++) {
	        			Integer value = Integer.parseInt(num[col]);
	        			record.add(value);
	        		}
	        		data[table].add(record);
	        	}
	        }
	        
	        //process queries
	        
	        Integer nQ = readLineAsInteger();
	        
	        printLine("Test: " + cas);
	        
	        for(int q = 0; q < nQ; q++) {
	        	HashMap<String, Integer> map = new HashMap<String, Integer>(); // mapping of each table name, 0 means first table, 1 means second table
	        	int firstTableId, secondTableId, firstColId, secondColId;
	        	
	        	boolean selectAll = false;
	        	
	        	ArrayList<Integer> []tableColumns = new ArrayList[2]; // which columns will be printed
	        	tableColumns[0] = new ArrayList<Integer>();
	        	tableColumns[1] = new ArrayList<Integer>();
	        	
	        	String line1 = readLine();
	        	String line2 = readLine();
	        	String line3 = readLine();
	        	String line4 = readLine();
			String blankLine = readLine();
	        	
	        	ArrayList<String> tokenizedLine1 = tokenize(line1); // tokenize the first line of the query
	        	if (tokenizedLine1.isEmpty()) { // no column name in the select query
	        		selectAll = true;
	        	}
	        	
	        	ArrayList<String> tokenizedLine2 = tokenize(line2); // tokenize the first line of the query
	        	map.put(tokenizedLine2.get(0), 0); // first token means first table
	        	firstTableId = tableId.get(tokenizedLine2.get(0)); // table id of first table
	        	if(tokenizedLine2.size()>1) // short name of the first table
	        		map.put(tokenizedLine2.get(1), 0);
	        	
	        	ArrayList<String> tokenizedLine3 = tokenize(line3); // tokenize the second line of the query
	        	map.put(tokenizedLine3.get(0), 1); // first token means second table
	        	secondTableId = tableId.get(tokenizedLine3.get(0)); // table id of second table
	        	if(tokenizedLine3.size()>1) // short name of the second table
	        		map.put(tokenizedLine3.get(1), 1);
	        	
	        	if(selectAll == false) { // tokenize the column names from the first query line
	        		for(String str:tokenizedLine1) {
	        			ArrayList<String> parts = partition(str);
	        			tableColumns[map.get(parts.get(0))].add(colId.get(parts.get(1)));
	        		}
	        	}else { // insert all column id's of both table
					for(int i=0;i<data[firstTableId].get(0).size();i++) 
						tableColumns[0].add(i);
					for(int i=0;i<data[secondTableId].get(0).size();i++) 
						tableColumns[1].add(i);
				}
	        	
	        	ArrayList<String> tokenizedLine4 = tokenize(line4); // tokenize the 4th line of the query
	        	ArrayList<String> parts = partition(tokenizedLine4.get(0)); // tokenize the column names from where clause
	        	firstColId = colId.get(parts.get(1)); // matching column id of first table
	        	
	        	parts = partition(tokenizedLine4.get(1));
	        	secondColId = colId.get(parts.get(1)); // matching column id of second table
	        	
	        	ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>(); // for storing join query result
	        	
	        	// the data tables on which query will be applied
	        	ArrayList<ArrayList<Integer>> table1 = data[firstTableId];
	        	ArrayList<ArrayList<Integer>> table2 = data[secondTableId];
	        	
	        	// find the matching
	        	for(int i = 0; i < table1.size();i++) {
	        		ArrayList<Integer> record1 = table1.get(i);
	        		for(int j = 0; j < table2.size(); j++) {
	        			ArrayList<Integer> record2 = table2.get(j);
	        			if(record1.get(firstColId) == record2.get(secondColId)) { // matching found
	        				ArrayList<Integer> current = new ArrayList<Integer>(); // create a new row
	        				for(int k = 0; k < tableColumns[0].size();k++) { //insert the column data from first table, specified in the select query
	        					int c = tableColumns[0].get(k);
	        					Integer value = record1.get(c);
	        					current.add(value);
	        				}
	        				for(int k = 0; k < tableColumns[1].size();k++) { //insert the column data from second table, specified in the select query
	        					int c = tableColumns[1].get(k);
	        					Integer value = record2.get(c);
	        					current.add(value);
	        				}
	        				result.add(current); // insert all column data in the result, new row created
	        			}
	        		}
	        	}
	        	
	        	// sort the rows in lexicographical order
	        	result.forEach(Collections::sort);
	        	Collections.sort(result, (row1, row2) -> row1.get(0).compareTo(row2.get(0)));
	        	
	        	// print the column names of the first table
	        	for(int k = 0; k < tableColumns[0].size();k++) {
					int c = tableColumns[0].get(k);
					if(k>0) System.out.print(" ");
					System.out.print(columnList[firstTableId].get(c));
				}
	        	
	        	
	        	// print the column names of the first table
			for(int k = 0; k < tableColumns[1].size();k++) {
				int c = tableColumns[1].get(k);
				if(k>0 || tableColumns[0].size()>0) System.out.print(" ");
				System.out.print(columnList[secondTableId].get(c));
			}
			printLine("");

			// print the column data from the result table
			for(ArrayList<Integer> row:result) {
				for(int k = 0; k < row.size();k++) {
					if(k>0) System.out.print(" ");
					System.out.print(row.get(k));
				}
				printLine("");
			}
			printLine("");
			//query is ended
	        }
        }
    }
    
    private ArrayList<String> tokenize(String str){ // string tokenization helper function
    	ArrayList<String> tokens = new ArrayList<String>();
    	String token = new String();
    	int i = 0;
    	while(str.charAt(i)!=' ') i++; // ignore first token: select, from, join, where 
    	i++;
    	if(str.charAt(i) == '*')
    		return tokens;
    	for(; i <= str.length(); i++) {
    		if(i == str.length() || str.charAt(i) == ' ' || str.charAt(i)==',' || str.charAt(i)=='=') {
    			if(token.length()>0)
    				tokens.add(token);
    			token = "";
    			while(i + 1 < str.length() && ( str.charAt(i+1)==' ' || str.charAt(i+1) == ',' || str.charAt(i+1)== '=' ) )
    				i++;
    		}
    		else token += str.charAt(i);
    	}
    	return tokens;
    }
    
    private ArrayList<String> partition(String str){ // separate the table table name and column names by dot(.)
    	ArrayList<String> tokens = new ArrayList<String>();
    	for(int i = 0; i < str.length(); i++) {
    		if(str.charAt(i) == '.') {
    			tokens.add(str.substring(0,i));
    			tokens.add(str.substring(i+1));
    			return tokens;
    		}
    	}
    	tokens.add(str);
    	return tokens;
    }
}
