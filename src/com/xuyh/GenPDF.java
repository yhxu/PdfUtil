package com.xuyh;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @Author: XUYH
 * @Description: java创建pdf文档 原文链接https://www.cnblogs.com/shuilangyizu/p/5760928.html
 * @Date: 2019/3/7
 * @Version:
 */

public class GenPDF {
    private Document document;// 建立一个Document对象

    private Font keyfont; // 设置字体大小
    private Font textfont;// 设置字体大小
    private int  maxWidth;

    public Font getKeyfont() {
        return keyfont;
    }

    public void setKeyfont(Font keyfont) {
        this.keyfont = keyfont;
    }

    public Font getTextfont() {
        return textfont;
    }

    public void setTextfont(Font textfont) {
        this.textfont = textfont;
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public static GenPDF getInstance(File file) throws Exception{
        return getInstance(file, 520);
    }
    public static GenPDF getInstance(String filePath) throws Exception{
        return getInstance(filePath, 520);
    }
    public static GenPDF getInstance(File file, int maxWidth) throws Exception{
        return new GenPDF(file, maxWidth);
    }
    public static GenPDF getInstance(String filePath, int maxWidth) throws Exception{
        File file = new File(filePath);
        file.createNewFile();
        return getInstance(file, maxWidth);
    }
    private GenPDF(){

    }
    private GenPDF(File file, int maxWidth) throws Exception{
        this.document = new Document();
        this.document.setPageSize(PageSize.A4);// 设置页面大小
        PdfWriter.getInstance(this.document, new FileOutputStream(file));
        this.document.open();
        this.maxWidth = maxWidth;

        BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
        this.keyfont   = new Font(bfChinese, 8, Font.BOLD);  // 设置字体大小
        this.textfont  = new Font(bfChinese, 8, Font.NORMAL);// 设置字体大小
    }

    public GenPDF(File file, int maxWidth, Font keyfont, Font textfont) throws Exception{
        this.document = new Document();
        this.document.setPageSize(PageSize.A4);// 设置页面大小
        PdfWriter.getInstance(this.document, new FileOutputStream(file));
        this.document.open();
        this.maxWidth = maxWidth;
        this.keyfont  = keyfont;  // 设置字体大小
        this.textfont = textfont; // 设置字体大小
    }

    public PdfPCell createCell(String value, Font font, int align){
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }

    public PdfPCell createCell(String value, boolean isKeyWord, int align){
        return createCell(value, isKeyWord ? keyfont : textfont, align);
    }

    public PdfPCell createKeyCell(String value,int align){
        return createCell(value, true, align);
    }

    public PdfPCell createKeyCell(String value){
        return createCell(value, true);
    }
    public PdfPCell createCell(String value, boolean isKeyWord){
        return createCell(value, isKeyWord ? keyfont : textfont);
    }
    public PdfPCell createCell(String value, Font font){
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPhrase(new Phrase(value, font));
        return cell;
    }

    public PdfPCell createKeyCell(String value, int align, int colspan, boolean boderFlag){
        return createCell(value, true, align, colspan, boderFlag);
    }

    public PdfPCell createCell(String value, boolean isKeyWord, int align, int colspan, boolean boderFlag){
        return createCell(value, isKeyWord ? keyfont : textfont, align, colspan, boderFlag);
    }

    public PdfPCell createCell(String value, Font font, int align, int colspan, boolean boderFlag){
        PdfPCell cell = new PdfPCell();
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(align);
        cell.setColspan(colspan);
        cell.setPhrase(new Phrase(value, font));
        cell.setPadding(3.0f);
        if(!boderFlag){
            cell.setBorder(0);
            cell.setPaddingTop(15.0f);
            cell.setPaddingBottom(8.0f);
        }
        return cell;
    }
    public PdfPTable createTable(int colNumber){
        PdfPTable table = new PdfPTable(colNumber);
        try{
            table.setTotalWidth(maxWidth);
            table.setLockedWidth(true);
            table.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.getDefaultCell().setBorder(1);
        }catch(Exception e){
            e.printStackTrace();
        }
        return table;
    }

    public void close(PdfPTable table) throws Exception {
        if(null != table)
            document.add(table);
        document.close();
    }
    public void writePdf(String title, String filePath, String createTime, String authorName) throws Exception{
        //1.新建document对象
        //第一个参数是页面大小。接下来的参数分别是左、右、上和下页边距。
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        //2.建立一个书写器(Writer)与document对象关联，通过书写器(Writer)可以将文档写入到磁盘中。
        //创建 PdfWriter 对象 第一个参数是对文档对象的引用，第二个参数是文件的实际名称，在该名称中还会给出其输出路径。
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
        //3.打开文档
        document.open();
        //4.向文档中添加内容
        //通过 com.lowagie.text.Paragraph 来添加文本。可以用文本及其默认的字体、颜色、大小等等设置来创建一个默认段落
        Paragraph pt = new Paragraph("zhong:-"+title, keyfont);//设置字体样式
        pt.setAlignment(1);//设置文字居中 0靠左   1，居中     2，靠右
        document.add(pt);
        document.add(new Paragraph("\n"));
        pt=new Paragraph(createTime+"\t\t\t\t\t\t"+authorName, keyfont);
        pt.setAlignment(2);
        document.add(pt);
        document.add(new Paragraph("\n"));
        document.add(new Paragraph(createTime+"\t\t\t\t\t\t"+authorName, keyfont));
        document.add(new Paragraph("\n"));
        document.add(new Paragraph("Some more text on the 胜多负少的身份的分公司的风格发的电饭锅的分公司的分公司的的分公司电饭锅是的分公司的风格的分公司的分公司的复合弓好几顿饭发的寡鹄单凫过好地方风格和的发干活的风格和发干活的风格和地方过电饭锅好地方干活的风格和电饭锅好地方干活负少的身份的分公司的风格发的电饭锅的分公司的分公司的的分公司电饭锅是的分公司的风格的分公司的分公司的复合弓好几顿饭发的寡鹄单凫过好地方风格和的发干活的风格和发干活的风格和地方过电饭锅好地方干活的风格和电饭锅好地方干活负少的身份的分公司的风格发的电饭锅的分公司的分公司的的分公司电饭锅是的分公司的风格的分公司的分公司的复合弓好几顿饭发的寡鹄单凫过好地方风格和的发干活的风格和发干活的风格和地方过电饭锅好地方干活的风格和电饭锅好地方干活的风格和符合斯蒂夫 first page with different color andsdfsadfffffffffffffffffffffffffff font type.", keyfont));
        //5.关闭文档
        close(null);
    }
    public static void main(String[] args) throws Exception {
        Thread thread = null;
        for(int i=0;i<5;i++){
            thread = new test();
            thread.start();
        }

    }
    public static class test extends Thread{
        @Override
        public void run() {
            try {
                GenPDF.run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public static void run() throws Exception{
        long id = Thread.currentThread().getId();
        System.out.println(id +"开始");
        GenPDF genPDFins = getInstance("D:/yxdir/test"+ id +".pdf");
        PdfPTable table = genPDFins.createTable(4);
        table.addCell(genPDFins.createKeyCell("学生信息列表：", Element.ALIGN_LEFT, 4, false));
        table.addCell(genPDFins.createKeyCell("姓名："));
        table.addCell(genPDFins.createKeyCell("年龄："));
        table.addCell(genPDFins.createKeyCell("性别："));
        table.addCell(genPDFins.createKeyCell("住址："));
        Thread.sleep(10000);
        for(int i=0; i<5; i++){
            table.addCell(genPDFins.createCell("姓名"+i, false));
            table.addCell(genPDFins.createCell(""+(i+15), false));
            table.addCell(genPDFins.createCell((i%2==0)?"男":"女", false));
            table.addCell(genPDFins.createCell("地址" + i, false));
        }
        genPDFins.close(table);
        System.out.println(id +"结束");
    }
}
