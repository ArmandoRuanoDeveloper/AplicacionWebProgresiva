package com.proyecto.servicios.service;

import com.proyecto.servicios.model.onboarding.LoginRequest;

public interface AuthService {
    String login(LoginRequest request);
}