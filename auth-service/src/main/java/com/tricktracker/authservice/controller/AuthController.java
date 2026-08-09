package com.tricktracker.authservice.controller;



import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("auth/v1")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private JwtCore jvtCore;

}