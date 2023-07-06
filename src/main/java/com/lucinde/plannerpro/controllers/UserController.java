package com.lucinde.plannerpro.controllers;

import com.lucinde.plannerpro.dtos.UserInputDto;
import com.lucinde.plannerpro.exceptions.BadRequestException;
import com.lucinde.plannerpro.services.UserService;
import com.lucinde.plannerpro.utils.FieldError;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping(value = "/users")
public class UserController {
    private final UserService userService;
    private final FieldError fieldError = new FieldError();


    public UserController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping(value = "")
    public ResponseEntity<List<UserInputDto>> getUsers() {

        List<UserInputDto> userInputDtos = userService.getUsers();

        return ResponseEntity.ok().body(userInputDtos);
    }

    @GetMapping(value = "/{username}")
    public ResponseEntity<UserInputDto> getUser(@PathVariable("username") String username) {

        UserInputDto optionalUser = userService.getUser(username);


        return ResponseEntity.ok().body(optionalUser);

    }

    @PostMapping(value = "")
    public ResponseEntity<Object> createKlant(@Valid @RequestBody UserInputDto dto, BindingResult br) {
        if(br.hasFieldErrors()) {
            return ResponseEntity.badRequest().body(fieldError.fieldErrorBuilder(br));
        }

        String newUsername = userService.createUser(dto);
        userService.addAuthority(newUsername, "ROLE_MECHANIC");

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{username}")
                .buildAndExpand(newUsername).toUri();

        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/{username}")
    public ResponseEntity<UserInputDto> updateKlant(@PathVariable("username") String username, @RequestBody UserInputDto dto) {

        userService.updateUser(username, dto);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(value = "/{username}")
    public ResponseEntity<Object> deleteKlant(@PathVariable("username") String username) {
        userService.deleteUser(username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(value = "/{username}/authorities")
    public ResponseEntity<Object> getUserAuthorities(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(userService.getAuthorities(username));
    }

    @PostMapping(value = "/{username}/authorities")
    public ResponseEntity<Object> addUserAuthority(@PathVariable("username") String username, @RequestBody Map<String, Object> fields) {
        try {
            String authorityName = (String) fields.get("authority");
            userService.addAuthority(username, authorityName);
            return ResponseEntity.noContent().build();
        }
        catch (Exception ex) {
            throw new BadRequestException();
        }
    }

    @DeleteMapping(value = "/{username}/authorities/{authority}")
    public ResponseEntity<Object> deleteUserAuthority(@PathVariable("username") String username, @PathVariable("authority") String authority) {
        userService.removeAuthority(username, authority);
        return ResponseEntity.noContent().build();
    }

    //*------------------- Eigen methodes -------------------*//
    @GetMapping("/mechanics")
    public ResponseEntity<List<UserInputDto>> getMechanics() {
        List<UserInputDto> userInputDtos = userService.getMechanics();

        return ResponseEntity.ok().body(userInputDtos);
    }

    @GetMapping(value = "/auth/{username}")
    public ResponseEntity<UserInputDto> getUserByAuth(@PathVariable("username") String username) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String requestingUsername = authentication.getName();

        UserInputDto optionalUser = userService.getUserCheckAuth(requestingUsername, username);

        return ResponseEntity.ok().body(optionalUser);

    }

}