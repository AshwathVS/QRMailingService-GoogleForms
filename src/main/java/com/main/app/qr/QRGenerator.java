package com.main.app.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.utils.FileUtils;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QRGenerator {

    private static final int QR_HEIGHT = 300;
    private static final int QR_WIDTH = 300;
    private static final String FILE_LOCATION = FileUtils.QR_BASE_LOCATION;

    public static void generateQRFile(String text, String fileName) throws WriterException, IOException {
        if(!fileName.contains(".png")) {
            fileName += ".png";
        }
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(text, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT);
        //System.out.println(bitMatrix.toString());
        Path path = FileSystems.getDefault().getPath(FILE_LOCATION + fileName);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", path);
    }
}
