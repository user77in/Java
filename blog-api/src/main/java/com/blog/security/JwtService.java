package com.blog.security;

import lombok.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {
    @Value("${app.jwt.secret}")
    private String secret;


}
