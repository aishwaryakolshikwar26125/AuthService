package org.example.userauthservice_may2026.services;

import org.antlr.v4.runtime.misc.Pair;
import org.example.userauthservice_may2026.models.User;

public interface IAuthService {

    public User signUp(String name, String email, String password);

    public Pair<User,String> login(String email, String password);
    public Boolean validateToken(String token);
}
