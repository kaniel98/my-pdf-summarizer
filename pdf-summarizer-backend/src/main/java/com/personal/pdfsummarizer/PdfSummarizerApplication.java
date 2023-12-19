package com.personal.pdfsummarizer;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@OpenAPIDefinition(
        info = @io.swagger.v3.oas.annotations.info.Info(
                title = "my-pdf-summarizer API",
                version = "1.0",
                description = "Documentation my-pdf-summarizer API v1.0 \n This is a personal ongoing project",
                contact = @io.swagger.v3.oas.annotations.info.Contact(
                        name = "Kaniel Koh",
                        email = "kankoh98@gmail.com"
                )
        )
)
public class PdfSummarizerApplication {

    public static void main(String[] args) {
        SpringApplication.run(PdfSummarizerApplication.class, args);
    }

}
