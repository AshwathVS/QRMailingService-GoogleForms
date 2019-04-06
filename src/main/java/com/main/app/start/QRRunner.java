package com.main.app.start;



import com.main.app.constants.AppKeys;
import com.main.app.excel.ExcelImporter;

import java.io.File;

public class QRRunner {

    public static void triggerQRJob(File excelInputFile) throws Exception {
        ExcelImporter.iterateExcelAndSendMail(excelInputFile, System.getProperty(AppKeys.EXCEL_MAIL_COLUMN_NAME));
    }
}
