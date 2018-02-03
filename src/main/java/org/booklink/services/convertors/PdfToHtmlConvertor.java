package org.booklink.services.convertors;

import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.io.RandomAccessFile;
import org.apache.pdfbox.pdfparser.PDFParser;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.booklink.models.exceptions.TextConvertingException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

/**
 * Created by mhenr on 01.11.2017.
 */
public class PdfToHtmlConvertor implements BookConvertor<File> {
    @Override
    public String toHtml(File file) throws TextConvertingException {
        try {
            PDFParser parser = new PDFParser(new RandomAccessFile(file, "r"));
            parser.parse();
            COSDocument cosDocument = parser.getDocument();
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            PDDocument pdDocument = new PDDocument(cosDocument);
            return pdfTextStripper.getText(pdDocument);
        } catch(IOException e) {
            throw new TextConvertingException(e.getMessage());
        }
    }
}
