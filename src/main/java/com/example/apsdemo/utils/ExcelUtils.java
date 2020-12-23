package com.example.apsdemo.utils;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.util.StringUtils;

public class ExcelUtils {

    private ExcelUtils(){}

    public static ExcelUtils newInstance(){return new ExcelUtils();}
    /**
     * 导出excel
     * @param headerName （excel列名称）
     * @param headerKey （导出对象属性名）
     * @param sheetName （excel 页签名称）
     * @param dataList （导出的数据）
     * @return
     */
    public HSSFWorkbook createExcelHSSF(String[] headerName, String[] headerKey, String sheetName, List dataList) {
        try {
            if (headerKey.length <= 0) {
                return null;
            }
            if (dataList.size() <= 0) {
                return null;
            }
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet;
            if ((sheetName == null) || (sheetName.equals("")))
                sheet = wb.createSheet("Sheet1");
            else {
                sheet = wb.createSheet(sheetName);
            }
            HSSFRow row = sheet.createRow(0);
            HSSFCellStyle style = wb.createCellStyle();
            style.setAlignment(HorizontalAlignment.GENERAL);
            HSSFCell cell = null;
            if (headerName.length > 0) {
                for (int i = 0; i < headerName.length; i++) {
                    cell = row.createCell(i);
                    cell.setCellValue(headerName[i]);
                    cell.setCellStyle(style);

                }
            }
            int n = 0;
            HSSFCellStyle contextstyle = wb.createCellStyle();
            contextstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00_);(#,##0.00)"));

            HSSFCellStyle contextstyle1 = wb.createCellStyle();
            HSSFDataFormat format = wb.createDataFormat();
            contextstyle1.setDataFormat(format.getFormat("@"));

            HSSFCell cell0 = null;
            HSSFCell cell1 = null;

            for (Iterator localIterator = dataList.iterator(); localIterator.hasNext();) {
                Object obj = localIterator.next();
                Field[] fields = obj.getClass().getDeclaredFields();
                row = sheet.createRow(n + 1);
                for (int j = 0; j < headerKey.length; j++) {
                    if (headerName.length <= 0) {
                        cell0 = row.createCell(j);
                        cell0.setCellValue(headerKey[j]);
                        cell0.setCellStyle(style);

                    }
                    for (int i = 0; i < fields.length; i++) {
                        if (fields[i].getName().equals(headerKey[j])) {
                            fields[i].setAccessible(true);
                            if (fields[i].get(obj) == null) {
                                row.createCell(j).setCellValue("");
                                break;
                            }
                            if ((fields[i].get(obj) instanceof Number)) {
                                cell1 = row.createCell(j);
                                cell1.setCellType(CellType.NUMERIC);
                                cell1.setCellStyle(contextstyle);
                                cell1.setCellValue(Double.parseDouble(fields[i].get(obj).toString()));
                                break;
                            }
                            if ("".equals(fields[i].get(obj))) {
                                cell1 = row.createCell(j);
                                cell1.setCellStyle(contextstyle1);
                                row.createCell(j).setCellValue("");
                                cell1.setCellType(CellType.STRING);
                                break;
                            }
                            row.createCell(j).setCellValue(fields[i].get(obj).toString());
                            break;
                        }

                    }
                }
                n++;
            }
            for (int i = 0; i < headerKey.length; i++) {
                sheet.setColumnWidth(i, headerKey[i].getBytes().length*2*256);
            }
            HSSFWorkbook localHSSFWorkbook1 = wb;
            return localHSSFWorkbook1;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
        }
    }

    public SXSSFWorkbook createExcelSXSSF(String[] headerName, String[] headerKey, String sheetName, List dataList) throws Exception {
        try {
            if (headerKey.length <= 0) {
                return null;
            }
            if (dataList.size() <= 0) {
                return null;
            }
            SXSSFWorkbook workbook = new SXSSFWorkbook();
            // 生成一个表格
            Sheet sheet = workbook.createSheet();
            // 设置sheet名字
            workbook.setSheetName(0, StringUtils.isEmpty(sheetName) ? "sheet1":sheetName);
            /* **********************第二行标题start*************************** */
            // 表格第二行标题
            Row headTitleSecond = sheet.createRow(0);
            // 设置样式
            CellStyle firstRowcellStyle = workbook.createCellStyle();
            firstRowcellStyle.setAlignment(HorizontalAlignment.GENERAL);
            Cell  cell = null;
            if (headerName.length > 0) {
                for (int i = 0; i < headerName.length; i++) {
                    cell = headTitleSecond.createCell(i);
                    cell.setCellValue(headerName[i]);
                    cell.setCellStyle(firstRowcellStyle);
                }
            }
            int n = 0;
            CellStyle contextstyle = workbook.createCellStyle();
            contextstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00_);(#,##0.00)"));

            CellStyle contextstyle1 = workbook.createCellStyle();
            DataFormat format = workbook.createDataFormat();
            contextstyle1.setDataFormat(format.getFormat("@"));
            Row row = null;
            Cell cell0 = null;
            Cell cell1 = null;
            // 遍历集合数据，产生数据行
            for (Iterator localIterator = dataList.iterator(); localIterator.hasNext();) {
                Object obj = localIterator.next();
                Field[] fields = obj.getClass().getDeclaredFields();
                row = sheet.createRow(n + 1);
                for (int j = 0; j < headerKey.length; j++) {
                    if (headerName.length <= 0) {
                        cell0 = row.createCell(j);
                        cell0.setCellValue(headerKey[j]);
                        cell0.setCellStyle(firstRowcellStyle);
                    }
                    for (int i = 0; i < fields.length; i++) {
                        if (fields[i].getName().equals(headerKey[j])) {
                            fields[i].setAccessible(true);
                            if (fields[i].get(obj) == null) {
                                row.createCell(j).setCellValue("");
                                break;
                            }
                            if ((fields[i].get(obj) instanceof Number)) {
                                cell1 = row.createCell(j);
                                cell1.setCellType(CellType.NUMERIC);
                                cell1.setCellStyle(contextstyle);
                                cell1.setCellValue(Double.parseDouble(fields[i].get(obj).toString()));
                                break;
                            }
                            if ("".equals(fields[i].get(obj))) {
                                cell1 = row.createCell(j);
                                cell1.setCellStyle(contextstyle1);
                                row.createCell(j).setCellValue("");
                                cell1.setCellType(CellType.STRING);
                                break;
                            }
                            row.createCell(j).setCellValue(fields[i].get(obj).toString());
                            break;
                        }

                    }
                }
                n++;
            }
            for (int i = 0; i < headerKey.length; i++) {
                sheet.setColumnWidth(i, headerKey[i].getBytes().length*2*256);
            }
            SXSSFWorkbook localSXSSFWorkbook1 = workbook;
            return localSXSSFWorkbook1;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
        }
    }

}
