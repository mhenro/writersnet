package org.booklink.services.convertors;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.fit.pdfdom.PDFDomTree;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;

/**
 * Created by mhenr on 01.11.2017.
 */
public class PdfToHtmlConvertor implements BookConvertor<File> {
    @Override
    public String toHtml(File file) throws IOException, ParserConfigurationException {
        PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
        parser.parse();
        COSDocument cosDocument = parser.getDocument();
        PDFTextStripper pdfTextStripper = new PDFTextStripper();
        PDDocument pdDocument = new PDDocument(cosDocument);
        return pdfTextStripper.getText(pdDocument);

/*
        PDDocument pdf = PDDocument.load(file); //load the PDF file using PDFBox
        PDFDomTree parser = new PDFDomTree();   //create the DOM parser
        //Writer output = new PrintWriter("c:\\Java\\nginx\\html\\zazaka.txt", "utf-8");
        //parser.writeText(pdf, output);
        Document dom = parser.createDOM(pdf);   //parse the file and get the DOM Document
        return dom.getDocumentElement().getTextContent();
        */
    }
}
