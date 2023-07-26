package com.example.taskforwork.passwordservice;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;


public class CustomPasswordEncoder extends BCryptPasswordEncoder {

    private static final int STRENGTH = 12; // BCrypt strength parameter
    private static final long FIXED_SEED = 123456789L; // Fixed seed for SecureRandom

    @Override
    public String encode(CharSequence rawPassword) {
        // Use SecureRandom with a fixed seed to generate a salt
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.setSeed(FIXED_SEED);

        String salt = BCrypt.gensalt(STRENGTH, secureRandom);
        return BCrypt.hashpw(rawPassword.toString(), salt);
    }

}