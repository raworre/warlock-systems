package com.warlock.user.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("user")
@Data
@Builder
public class UserDocument {
    @Id
    private String id;
    private String username;
    private String password;
    @DBRef
    private ProfileDocument profile;
}
