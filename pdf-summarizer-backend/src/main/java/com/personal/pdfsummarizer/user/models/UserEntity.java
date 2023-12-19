package com.personal.pdfsummarizer.user.models;

import com.personal.pdfsummarizer.summarizer.models.Summary;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;

@Document(collection = "users")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserEntity {

    @Transient
    public static final String SEQUENCE_NAME = "users_sequence";
    
    @Id
    private String userID;
    private String password;
    @Indexed(unique = true)
    private String email;
    private HashMap<String, Summary> summaries;
}
