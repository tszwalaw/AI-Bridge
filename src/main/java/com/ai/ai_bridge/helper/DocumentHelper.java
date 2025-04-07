package com.ai.ai_bridge.helper;

import org.springframework.ai.document.Document;

import java.io.File;
import java.util.List;

public interface DocumentHelper {
    List<Document> readPDF(File file);
}
