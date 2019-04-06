package com.main.app.excel;

import com.google.zxing.WriterException;
import com.main.app.constants.AppKeys;
import com.main.app.mail.MailUtils;
import com.main.app.qr.QRGenerator;
import com.sun.media.sound.InvalidDataException;
import com.utils.FileUtils;
import com.utils.LoggingUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbookFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

public class ExcelImporter {

    public static void iterateExcelAndSendMail(File file, String columnNameForMail) throws IOException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
        Workbook workbook = XSSFWorkbookFactory.createWorkbook(file, true);
        Sheet excelSheet = workbook.getSheetAt(0);
        int headingRowNumber = Integer.parseInt(System.getProperty(AppKeys.EXCEL_HEADING_ROW_NUMBER));
        Map<String, Integer> headingDetails = readHeading(excelSheet.getRow(headingRowNumber));
        boolean isCustomMail = Boolean.parseBoolean(System.getProperty(AppKeys.APP_TEST_CUSTOM_MAIL_BODY));
        if(headingDetails == null) {
            throw new InvalidDataException("Unable to find headings in the excel sheet.. Check the sheet and try again.!");
        }
        else {
            if(!headingDetails.containsKey(columnNameForMail)) {
                throw new InvalidDataException("Unable to find email column in the excel sheet.. Specified column name is: " + columnNameForMail);
            }
        }
        Iterator<Row> rowIterator = excelSheet.rowIterator();
        int iter = 0;
        //Skipping ahead to actual value
        while(iter <= headingRowNumber && rowIterator.hasNext()) { rowIterator.next(); iter++;}
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();
            Map<String, String> rowValue = readRow(row, headingDetails);
            String recipientMail = rowValue.get(columnNameForMail);
            if(rowValue != null && com.utils.StringUtils.isValidMail(recipientMail, false)) {
                try {
                    QRGenerator.generateQRFile(rowValue.toString(), recipientMail);
                    boolean isMailSuccess = MailUtils.sendMail(recipientMail, recipientMail, isCustomMail);
                    if(isMailSuccess) {
                        LoggingUtils.log(Level.INFO, "Mail sent to: " + recipientMail);
                    }
                    else {
                        LoggingUtils.log(Level.SEVERE, "Error while sending mail to: " + recipientMail);
                    }
                    FileUtils.deleteQRFile(recipientMail);
                }
                catch (WriterException ex) {
                    LoggingUtils.log(Level.SEVERE, "Error while sending mail to: " + recipientMail);
                    LoggingUtils.log(Level.INFO, ex.getMessage());
                    ex.printStackTrace();
                }
            }
        }
    }

    private static Map<String, Integer> readHeading(Row row) {
        Iterator<Cell> cellIterator = row.cellIterator();
        Map<String, Integer> headingDetails = null;
        int columnNumber = 0;
        while(cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            if(!StringUtils.isBlank(cell.getStringCellValue())) {
                if(headingDetails == null) {
                    headingDetails = new HashMap<String, Integer>();
                }
                headingDetails.put(cell.getStringCellValue(), columnNumber);
            }
            columnNumber++;
        }
        return headingDetails;
    }

    private static Map<String, String> readRow(Row row, Map<String, Integer> headingDetails) {
        Map<String, String> cellValue = null;
        for(String heading : headingDetails.keySet()) {
            String value;
            Cell cell = row.getCell(headingDetails.get(heading));
            try {
                value = cell.getStringCellValue();

            } catch (Exception ex) {
                try {
                    value = Double.toString(cell.getNumericCellValue());
                } catch (Exception ex1) {
                    try {
                        value = Boolean.toString(cell.getBooleanCellValue());
                    } catch (Exception ex2) {
                        try {
                            value = cell.getDateCellValue().toString();
                        } catch (Exception ec3) {
                            value = "NULL";
                        }
                    }
                }
            }
            if (cellValue == null) {
                cellValue = new HashMap<String, String>();
            }
            cellValue.put(heading, value);

        }
        return cellValue;
    }

}
