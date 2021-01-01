package com.app.bookstore.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.xhtmlrenderer.pdf.ITextRenderer;

/**
 * @author Ananth Shanmugam
 */
@Component
public class PdfGenerator {
    @Autowired
    private TemplateEngine templateEngine;

    @SuppressWarnings("rawtypes")
    public File createPdf(String templateName, Map map) throws Exception {
        Assert.notNull(templateName, "The templateName cannot be empty");
        Context ctx = new Context();
        if (map != null) {
            Iterator itMap = map.entrySet().iterator();
            while (itMap.hasNext()) {
				Map.Entry pair = (Map.Entry) itMap.next();
                ctx.setVariable(pair.getKey().toString(), pair.getValue());
            }
        }

        String processedHtml = templateEngine.process(templateName, ctx);
        FileOutputStream os = null;
        String fileName = UUID.randomUUID().toString();
        final File outputFile = File.createTempFile(fileName, ".pdf");
        try {
            os = new FileOutputStream(outputFile);
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(processedHtml);
            renderer.layout();
            renderer.createPDF(os,false);
            renderer.finishPDF();
            System.out.println("PDF was created successfully");
        }
        finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) { e.printStackTrace(); }
            }
        }
        return outputFile;
    }
}
