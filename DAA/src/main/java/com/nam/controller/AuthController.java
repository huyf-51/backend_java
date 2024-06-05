package com.nam.controller;

import com.nam.exception.UserException;
import com.nam.payload.request.LoginRequest;
import com.nam.payload.request.SignupEmployeeRequest;
import com.nam.payload.response.ApiResponse;
import com.nam.payload.response.JwtResponse;
import com.nam.security.jwt.JwtProvider;
import com.nam.security.services.UserDetailsImpl;
import com.nam.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtProvider jwtProvider;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    // private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup/employee")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> createEmployee(@RequestBody SignupEmployeeRequest request) throws UserException {
        userService.createEmployee(request);
        return new ResponseEntity<>(new ApiResponse("Signup Success for Employee with email: " + request.getEmail(), true), HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtProvider.generateToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        //RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getId());

        return ResponseEntity.ok(new JwtResponse(jwt, "", userDetails.getId(),
                userDetails.getUsername(), userDetails.getEmail(), roles));
    }

//    @PostMapping("/refreshtoken")
//    public ResponseEntity<?> refreshtoken(@Valid @RequestBody TokenRefreshRequest request) {
//        String requestRefreshToken = request.getRefreshToken();
//
//        return refreshTokenService.findByToken(requestRefreshToken)
//                .map(refreshTokenService::verifyExpiration)
//                .map(RefreshToken::getUser)
//                .map(user -> {
//                    String token = jwtProvider.generateTokenByEmail(user.getEmail());
//                    return ResponseEntity.ok(new TokenRefreshResponse(token, requestRefreshToken));
//                })
//                .orElseThrow(() -> new TokenRefreshException(requestRefreshToken,
//                        "Refresh token is not in database!"));
//    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long userId = userDetails.getId();
        //refreshTokenService.deleteByUserId(userId);
        return ResponseEntity.ok(new ApiResponse("Sign out successful!", true));
    }

}