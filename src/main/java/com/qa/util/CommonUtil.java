package com.qa.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.qa.base.Base;

public class CommonUtil extends Base {

	  //Read data from excel
	
	public static Object[][] readDataFromExcel(String Sheet){
		XSSFWorkbook wb = null;
		XSSFSheet xs;
		int i,j;
		Object[][]data;
		FileInputStream fi =null;
				
		try {
			fi =new FileInputStream(prop.getProperty("ExcelPath"));
		    try {
				wb=new XSSFWorkbook(fi);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		   xs=wb.getSheet(Sheet);
				
		   int row=xs.getLastRowNum();
		   int col=xs.getRow(0).getLastCellNum();
		   data=new Object[row][col];
		   
		   for( i=0;i<row;i++)
		   {			   
			   for (j=0;j<col;j++)
			   {				   
				   data[i][j]=xs.getRow(i+1).getCell(j).toString();
						
			    }
			}
		   
	    try {
				wb.close();
				fi.close();
						
			} catch (IOException e) {
			    e.printStackTrace();
			}
		return data;
	     }
}
