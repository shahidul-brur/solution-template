package com.tigerit.exam;

import static com.tigerit.exam.IO.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Solution implements Runnable {
	
	ArrayList<String> []columnList = new ArrayList[12]; // list of all column names for each table
	HashMap<String, Integer> []columnId = new HashMap[12];
	ArrayList<ArrayList<Integer>> []data = new ArrayList[12]; // values of all tables
    HashMap<String, Integer> tableId = new HashMap<String, Integer>(); // table id for a table name
    
	@Override
	public void run() {
		
		//number of test cases?
	    int T = readLineAsInteger();
	    
	    //process each test case
	    for(int cas = 1; cas <= T; cas++) {
	    	
	    	// number of tables?
	        int nT = readLineAsInteger();
	        tableId = new HashMap<String, Integer>();
	        for(int table = 0; table < nT; table++) {
	        	
	        	String tableName = readLine();
	        	tableId.put(tableName, table);
	        	//printLine(tableName);
	        	
	        	// number of columns and rows
	        	String line = readLine();
	        	String []num = line.split(" ");
	        	int nC = Integer.parseInt(num[0]); // number of columns
	        	int nD = Integer.parseInt(num[1]); // number of data records or row
	        	
	        	takeColumnList(table, nC); // input column names
	        	takeDataRecords(table, nC, nD); // input data of the tables
	        }
	        
	        //process queries
	        
	        Integer nQ = readLineAsInteger();
	        
	        printLine("Test: " + cas);
	        
	        for(int q = 0; q < nQ; q++) {
	        	processQuery();
	        }
	    }
	}
	
	private void processQuery() {
		
    	ArrayList<String> []tokenizedLine = new ArrayList[4];
    	String firstTableShortName = new String();
    	for(int i = 0; i < 4; i++) {
    		tokenizedLine[i] = new ArrayList<String>();
    		String line = readLine();
    		tokenizedLine[i] = tokenize(line);
    	}
    	readLine(); // empty line
    	
    	String firstTable = tokenizedLine[1].get(0);
    	int firstTableId = tableId.get(firstTable); // table id of first table
    	if(tokenizedLine[1].size()>1)
    		firstTableShortName = tokenizedLine[1].get(1);
    	
    	int secondTableId = tableId.get(tokenizedLine[2].get(0)); // table id of second table
    	
    	int tid;
    	//before equal sign
    	ArrayList<String> parts = partition(tokenizedLine[3].get(0)); // first_table.first_column
    	if(parts.get(0).equals(firstTable) || parts.get(0).equals(firstTableShortName))
    		tid = firstTableId;
    	else tid = secondTableId;
    	int firstColId = columnId[tid].get(parts.get(1)); // matching column id of first table
    	
    	// after equal sign
    	parts = partition(tokenizedLine[3].get(1)); // second_table.second_column
    	if(parts.get(0).equals(firstTable) || parts.get(0).equals(firstTableShortName))
    		tid = firstTableId;
    	else tid = secondTableId;
    	int secondColId = columnId[tid].get(parts.get(1)); // matching column id of second table
    	
    	// which column should be printed?
    	ArrayList<Element> select = new ArrayList<Element>(); 
    	boolean selectAll = false;
    	
    	if (tokenizedLine[0].isEmpty()) { // * in first line, no column name in the select query
    		selectAll = true;
    	}
    	else {
    		// take each pair of table_name.column_name from first line
    		for(int i = 0; i < tokenizedLine[0].size(); i++) {
    			
    			Element element = new Element();
    			ArrayList<String> cur = partition(tokenizedLine[0].get(i)); // partition at dot
    			
    			if (cur.get(0).equals(firstTable) || cur.get(0).equals(firstTableShortName))
    				element.TableId = firstTableId;
    			else element.TableId = secondTableId;
    			
    			element.ColumnId = columnId[element.TableId].get(cur.get(1));
    			select.add(element);
    		}
    	}
    	
    	 // for store the join query result
    	ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
    	
    	// start to find the matching
    	for(int i = 0; i < data[firstTableId].size(); i++) { // for each row of first table
    		
    		for(int j = 0; j < data[secondTableId].size(); j++) { // for each row of second table
    			
    			// if both values at required columns are equal, then..
    			if(data[firstTableId].get(i).get(firstColId) == data[secondTableId].get(j).get(secondColId)) {
    				
    				ArrayList<Integer> current = new ArrayList<Integer>(); // create a new row and add it to result
    				
    				if(selectAll == true) {
    					 // insert all column data from both table
    					current.addAll(data[firstTableId].get(i));
    					current.addAll(data[secondTableId].get(j));
        				result.add(current);
        				continue;
    				}
    				// add the values of the selected columns
    				for(int k = 0; k < select.size(); k++) {
    					Integer value = 0;
    					if(select.get(k).TableId == firstTableId)
    						value = data[firstTableId].get(i).get(select.get(k).ColumnId);
    					else 
    						value = data[secondTableId].get(j).get(select.get(k).ColumnId);
    					current.add(value);
    				}
    				result.add(current);
    			}
    		}
    	}
    	
    	// sort the rows in lexicographical order
    	Collections.sort(result, new Comparator<ArrayList<Integer>>() {
    		@Override
            public int compare(ArrayList<Integer> row1, ArrayList<Integer> row2){
            	for(int i = 0; i < row1.size(); i++) {
            		if(row1.get(i)>row2.get(i)) return 1;
            		if(row1.get(i) < row2.get(i)) return -1;
            	}
                return 0;
            }
		});
    	
    	// print the column names...
    	if(selectAll == true) {
    		// print all column names of both table
    		boolean space = false;
    		for(int i = 0; i < columnList[firstTableId].size(); i++) {
    			if(space) System.out.print(" ");
    			System.out.print(columnList[firstTableId].get(i));
    			space = true;
    		}
    		for(int i = 0; i < columnList[secondTableId].size(); i++) {
    			if(space) System.out.print(" ");
    			System.out.print(columnList[secondTableId].get(i));
    			space = true;
    		}
    	}
    	else {
    		// print the selected column names
			for(int i = 0; i < tokenizedLine[0].size(); i++) {
				if(i>0) System.out.print(" ");
				System.out.print(partition(tokenizedLine[0].get(i)).get(1));
			}
		}
		printLine("");

		// print the result table
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
	
	private void takeColumnList(int table, int nC) {
		columnList[table] = new ArrayList<String>();
    	columnId[table] = new HashMap<String, Integer>();
    	String line = readLine();
    	String[] strs = line.split(" ");
    	
    	for(int c=0;c<nC;c++) {
    		String col = strs[c];
    		columnList[table].add(col);
    		columnId[table].put(col, c);
    	}
	}
	private void takeDataRecords(int table, int nC, int nD) {
		data[table] = new ArrayList<ArrayList<Integer>>();
		for(int row = 0; row <nD; row++) {
    		ArrayList<Integer> record = new ArrayList<Integer>();
    		String line = readLine();
        	String[] num = line.split(" ");
    		for(int col = 0;col<nC; col++) {
    			Integer value = Integer.parseInt(num[col]);
    			record.add(value);
    		}
    		data[table].add(record);
    	}
	}
	private ArrayList<String> tokenize(String str){ // string tokenization helper function
		ArrayList<String> tokens = new ArrayList<String>();
		String token = new String();
		int i = 0;
		while(str.charAt(i)!=' ') i++; // ignore first token: select, from, join, where 
		i++;
		if(str.charAt(i) == '*') {
			return tokens;
		}
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
    class Element{
    	int TableId, ColumnId;
    	public Element() {
			// TODO Auto-generated constructor stub
		}
    }
}

