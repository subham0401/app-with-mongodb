package com.shubham.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Email;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Document(collection = "user")
public class User {
    @Id
    private String _id;

    @NotNull(message = "userID can not be null")
    private String userID;


    @NotEmpty(message = "userName can not be blank as well as can't be null")
    private String userName;

    @Email(message = "userEmail e-Mail ID is not valid")
    private String userEmail;
}



