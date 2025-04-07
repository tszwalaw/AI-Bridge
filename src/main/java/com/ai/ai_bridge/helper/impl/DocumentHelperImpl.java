package com.ai.ai_bridge.helper.impl;

import com.ai.ai_bridge.helper.DocumentHelper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.ai.document.Document;

import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Component;

@Component
public class DocumentHelperImpl implements DocumentHelper {
    private static final Logger logger = LoggerFactory.getLogger(DocumentHelperImpl.class);

    @Override
    public List<Document> readPDF(File pdfFile) {

        try {
            List<Document> documents = new ArrayList<>();
            PDDocument document = PDDocument.load(pdfFile);

            PDFTextStripper pdfStripper = new PDFTextStripper();
            String text = pdfStripper.getText(document);

            document.close();

            String[] textChunks = text.split("\n\n"); // Example: split by double newlines (i.e., paragraphs)

            for (String chunk : textChunks) {
                Document springAiDocument = new Document(chunk);
                documents.add(springAiDocument);
            }

            return documents;

        } catch (Exception e) {
            logger.error("Error processing PDF file {}: {}", pdfFile.getName(), e.getMessage(), e);
            return new ArrayList<>();
        }

    }
}
