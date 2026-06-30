package org.example.userauthservice_may2026.services;

import org.example.userauthservice_may2026.models.User;

public interface IAuthService {

    public User signUp(String name, String email, String password);

    public User login(String email,String password);
}
