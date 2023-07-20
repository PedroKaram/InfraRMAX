package com.rmax.Auth.AuthDTOs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterDTO{
    private int ID;
    private String username;
    private String password;
    private String email;
    private String firstName;
    private String lastName;
}