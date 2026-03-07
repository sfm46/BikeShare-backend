package com.bikeshare.controllers;

import com.bikeshare.dto.MessageResponse;
import com.bikeshare.dto.UpdatePushTokenRequest;
import com.bikeshare.models.User;
import com.bikeshare.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/push-token")
    public ResponseEntity<MessageResponse> updatePushToken(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UpdatePushTokenRequest request
    ) {
        userService.updatePushToken(user.getId(), request.getToken());
        return ResponseEntity.ok(new MessageResponse("Push token updated successfully"));
    }
}
