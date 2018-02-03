package org.booklink.services.convertors;

import org.apache.poi.xwpf.converter.xhtml.XHTMLConverter;
import org.apache.poi.xwpf.converter.xhtml.XHTMLOptions;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.booklink.models.exceptions.TextConvertingException;

import java.io.*;

/**
 * Created by mhenr on 01.11.2017.
 */
public class DocToHtmlConvertor implements BookConvertor<File> {
    @Override
    public String toHtml(File file) throws TextConvertingException {
        /* load docx into XWPFDocument */
        try {
            InputStream is = new FileInputStream(file);
            XWPFDocument document = new XWPFDocument(is);
            XWPFWordExtractor extractor = new XWPFWordExtractor(document);
            return extractor.getText();
        } catch(IOException e) {
            throw new TextConvertingException(e.getMessage());
        }
    }
}
