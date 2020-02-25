package com.example.demo.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.awt.image.BufferedImage;
import java.util.Hashtable;
import java.util.Map;

public class PayUtil {

    public static BufferedImage getQRCodeImge(String codeUrl) throws WriterException{
        Map<EncodeHintType , Object> hints = new Hashtable();
        hints.put(EncodeHintType.ERROR_CORRECTION , ErrorCorrectionLevel.M);
        hints.put(EncodeHintType.CHARACTER_SET , "utf-8");
        int width = 256;
        BitMatrix bitMatrix = (new MultiFormatWriter()).encode(codeUrl , BarcodeFormat.QR_CODE , width , width , hints);
        BufferedImage bufferedImage = new BufferedImage(width , width , 1);
        for(int x = 0;x < width ; ++x){
            for(int y = 0 ; y < width ; ++y) {
                bufferedImage.setRGB(x, y, bitMatrix.get(x, y) ? -16777216 : -1);
            }
        }

        return bufferedImage;
    }

}
