package com.xuyh;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * @Author: XUYH
 * @Description: pdf工具类
 * @Date: 2018/5/9
 * @Version:
 */

public class PDFUtils {
    private static final String POINT = ".";

    public static void main(String[] args) {
        String srcPath  = "D:/yxdir/test.pdf";
        String descPath = "D:/yxdir/abc.pdf";
        try {
            pdf2images(srcPath);
            setWordMark(descPath, srcPath, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(Calendar.getInstance().getTime()) + "  下载使用人：" + "测试user");
            setImageMark(descPath, srcPath, "C:/Users/XUYH/Downloads/68S58T7300B50003.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置文字水印
     *
     * @param descFile 目标文件
     * @param srcFile 源文件
     * @param waterMarkName 水印文字
     * @throws DocumentException
     * @throws IOException
     */
    public static void setWordMark(String descFile, String srcFile, String waterMarkName) throws DocumentException, IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(descFile)));

        PdfReader  reader  = new PdfReader(srcFile);
        PdfStamper stamper = new PdfStamper(reader, bos);
        int total = reader.getNumberOfPages() + 1;
        PdfContentByte content;
        BaseFont  base = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.EMBEDDED);
        PdfGState gs   = new PdfGState();
        for (int i = 1; i < total; i++) {
            content = stamper.getOverContent(i); // 在内容上方加水印
//            content = stamper.getUnderContent(i); //在内容下方加水印
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

    /**
     * 设置图片水印 使用ice框架，商用请官网购买http://www.icesoft.org/java/home.jsf
     *
     * @param descFile 目标文件
     * @param srcFile 源文件
     * @param imagePath 水印图片路径
     * @throws DocumentException
     * @throws IOException
     */
    public static void setImageMark(String descFile, String srcFile, String imagePath) throws DocumentException, IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(descFile)));
        PdfReader  reader  = new PdfReader(srcFile);
        PdfStamper stamper = new PdfStamper(reader, bos);
        int total = reader.getNumberOfPages() + 1;
        PdfContentByte content;
        PdfGState gs = new PdfGState();
        for (int i = 1; i < total; i++) {
            content = stamper.getOverContent(i);// 在内容上方加水印
//            content = stamper.getUnderContent(i);//在内容下方加水印
            gs.setFillOpacity(0.3f); // 透明度
            content.setGState(gs);
            Image image = Image.getInstance(imagePath);
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

    /**
     * pdf转图片方法
     *
     * @param srcFilePath pdf文件路径
     * @param descFilePath 生成图片文件路径
     * @param scale 缩放比例
     * @param rotation 旋转角度
     */
    public static void pdf2images(String srcFilePath, String descFilePath, float scale, float rotation) throws IOException {
        String descFileName = descFilePath.substring(0, descFilePath.indexOf(POINT));
        String descFileType = descFilePath.substring(descFilePath.indexOf(POINT)+1);
        Document document = new Document();
        document.setFile(srcFilePath);
        for (int i = 0; i < document.getNumberOfPages(); i++) {
            BufferedImage image = (BufferedImage) document.getPageImage(i, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, rotation, scale);
            File file = new File(descFileName + " (" + (i + 1) + ")" + POINT + descFileType);
            ImageIO.write(image, descFileType, file);
            image.flush();
        }
        document.dispose();
    }
    public static void pdf2images(String srcFilePath, String descFilePath) throws IOException {
        pdf2images(srcFilePath, descFilePath, 2.5f, 0f);
    }
    public static void pdf2images(String srcFilePath) throws IOException {
        String descFilePath = srcFilePath.substring(0, srcFilePath.indexOf(POINT)) + POINT + "png";
        pdf2images(srcFilePath, descFilePath);
    }
}
