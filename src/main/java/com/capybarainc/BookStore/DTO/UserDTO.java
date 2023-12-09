package com.capybarainc.BookStore.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private Long id;
    private String login;
    private String email;
    //private String password;
    private List<AuthorDTO> favouriteAuthors;
    private LocalDate birthDate;
    //private String salt;

}