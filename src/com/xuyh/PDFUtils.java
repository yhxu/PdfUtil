package com.xuyh;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @Author: XUYH
 * @Description:
 * @Date: 2018/5/9
 * @Version:
 */

public class PDFUtils {
    public static void main(String[] args) {
        String path = "D:/yxdir/test.pdf";

        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File("D:/yxdir/abc.pdf")));
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            // 将pdf文件先加水印然后输出
            setWordMark(bos, path, format.format(cal.getTime()) + "  下载使用人：" + "测试user");
//            setImageMark(bos, path, "C:/Users/XUYH/Downloads/68S58T7300B50003.JPG");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    public static void setWordMark(BufferedOutputStream bos, String input, String waterMarkName) throws DocumentException, IOException {
        PdfReader reader  = new PdfReader(input);
        PdfStamper stamper = new PdfStamper(reader, bos);
        int total = reader.getNumberOfPages() + 1;
        PdfContentByte content;
        BaseFont base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H",BaseFont.EMBEDDED);
        PdfGState gs = new PdfGState();
        for (int i = 1; i < total; i++) {
            content = stamper.getOverContent(i);// 在内容上方加水印
            //content = stamper.getUnderContent(i);//在内容下方加水印
            gs.setFillOpacity(0.2f); // 透明度
            content.setGState(gs);
            content.beginText();
            content.setColorFill(BaseColor.BLACK);
            content.setFontAndSize(base, 50);
            content.setTextMatrix(70, 200);
            content.showTextAligned(Element.ALIGN_CENTER, waterMarkName, 300, 350, 55);
            content.endText();
        }
        stamper.close();
    }
    public static void setImageMark(BufferedOutputStream bos, String input, String path) throws DocumentException, IOException {
        PdfReader  reader  = new PdfReader(input);
        PdfStamper stamper = new PdfStamper(reader, bos);
        int total = reader.getNumberOfPages() + 1;
        PdfContentByte content;
        PdfGState gs = new PdfGState();
        for (int i = 1; i < total; i++) {
            content = stamper.getOverContent(i);// 在内容上方加水印
//            content = stamper.getUnderContent(i);//在内容下方加水印
            gs.setFillOpacity(0.3f); // 透明度
            content.setGState(gs);
            Image image = Image.getInstance(path);
            image.setAlignment(Image.TEXTWRAP);
            // 设置边框属性
//            image.setBorder(Image.BOX);
//            image.setBorderWidth(10);
//            image.setBorderColor(Color.WHITE);
//            image.setRotationDegrees(30);//旋转
            Rectangle rectangle = reader.getPageSize(i);
            float width  = rectangle.getWidth();
            float height = rectangle.getHeight();
            image.scaleToFit(width, height);//大小
            image.setAbsolutePosition(0, height/2);
//            image.setAbsolutePosition(200, 206);
            content.addImage(image);
        }
        stamper.close();
    }
}
