package com.personal.pdfsummarizer.user.models;

import com.personal.pdfsummarizer.summarizer.models.Summary;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;

@Document(collection = "users")
public class UserEntity {
    @Id
    private String userID;
    private String username;
    private String password;
    private String email;
    private HashMap<String, Summary> summaries;
}
