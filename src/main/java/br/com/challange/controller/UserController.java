package br.com.challange.controller;

import br.com.challange.models.User;
import br.com.challange.response.ApiResponse;
import br.com.challange.security.jwt.JwtResponse;
import br.com.challange.security.jwt.JwtUtils;
import br.com.challange.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserController {

    private final UserService service;
    private final JwtUtils jwtUtils;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<User>> saveUser(@RequestBody User user) {
        ApiResponse<User> response = service.saveUser(user);
        return ResponseEntity.status(response.getStatus()).body(response);
    }


    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> authenticateUser(@RequestBody User loginRequest) {
        ApiResponse<JwtResponse> response = service.login(loginRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/token")
    public ResponseEntity<Boolean> checkToken(@RequestParam String token) {
        var valid = jwtUtils.validateJwtToken(token);
        return ResponseEntity.status(valid ? HttpStatus.OK.value() : HttpStatus.FORBIDDEN.value()).body(valid);
    }

    @GetMapping("/check")
    public ResponseEntity<ApiResponse<User>> getUserLogged(){
        ApiResponse<User> response = service.getUserLogged();
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
