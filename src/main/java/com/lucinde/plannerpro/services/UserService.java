package com.lucinde.plannerpro.services;

import com.lucinde.plannerpro.dtos.UserInputDto;
import com.lucinde.plannerpro.dtos.UserOutputDto;
import com.lucinde.plannerpro.exceptions.BadRequestException;
import com.lucinde.plannerpro.exceptions.UsernameNotFoundException;
import com.lucinde.plannerpro.models.Authority;
import com.lucinde.plannerpro.models.User;
import com.lucinde.plannerpro.repositories.UserRepository;
import com.lucinde.plannerpro.utils.RandomStringGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<UserOutputDto> getUsers() {
        List<UserOutputDto> collection = new ArrayList<>();
        List<User> list = userRepository.findAll();
        for (User user : list) {
            collection.add(fromUser(user));
        }
        return collection;
    }

    public UserOutputDto getUser(String username) {
        UserOutputDto dto;
        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()){
            dto = fromUser(user.get());
        }else {
            throw new UsernameNotFoundException(username);
        }
        return dto;
    }

    public UserInputDto getUserWithPassword(String username) {
        UserInputDto dto;
        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()){
            dto = fromUserInput(user.get());
        }else {
            throw new UsernameNotFoundException(username);
        }
        return dto;
    }

    public boolean userExists(String username) {
        return userRepository.existsById(username);
    }

    public String createUser(UserInputDto userInputDto) {
        String randomString = RandomStringGenerator.generateAlphaNumeric(20);
        userInputDto.setApikey(randomString);
        //passwordencoder voeg ik to in toUser-methode waardoor onderstaande regel kan vervallen
        //passwordEncoder.encode(userDto.password);
        User newUser = userRepository.save(toUser(userInputDto));
        return newUser.getUsername();
    }

    public void deleteUser(String username) {
        userRepository.deleteById(username);
    }

    public void updateUser(String username, UserInputDto newUser) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        if(newUser.apikey != null)
            user.setApikey(newUser.apikey);
        if(newUser.password != null)
            user.setPassword(passwordEncoder.encode(newUser.getPassword()));
        if(newUser.email != null)
            user.setEmail(newUser.getEmail());
        if(newUser.enabled != null)
            user.setEnabled(newUser.getEnabled());
        userRepository.save(user);
    }

    public Set<Authority> getAuthorities(String username) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        UserInputDto userInputDto = fromUserInput(user);
        return userInputDto.getAuthorities();
    }

    public void addAuthority(String username, String authority) {

        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();

        // Check of de gebruiker de rol al heeft
        Set<Authority> userAuthorities = user.getAuthorities();
        for (Authority existingAuthority : userAuthorities) {
            if (existingAuthority.getAuthority().equals(authority)) {
                throw new BadRequestException("Gebruiker heeft deze rol al");
            }
        }

        user.addAuthority(new Authority(username, authority));
        userRepository.save(user);
    }

    public void removeAuthority(String username, String authority) {
        if (!userRepository.existsById(username)) throw new UsernameNotFoundException(username);
        User user = userRepository.findById(username).get();
        Authority authorityToRemove = user.getAuthorities().stream().filter((a) -> a.getAuthority().equalsIgnoreCase(authority)).findAny().get();
        user.removeAuthority(authorityToRemove);
        userRepository.save(user);
    }

    //*------------------- Eigen methodes -------------------*//
    public List<UserOutputDto> getMechanics() {
        List<UserOutputDto> collection = new ArrayList<>();
        List<User> list = userRepository.findByAuthority("ROLE_MECHANIC");
        for (User user : list) {
            collection.add(fromUser(user));
        }
        return collection;
    }

    public UserOutputDto getUserCheckAuth(String requestingUsername, String targetUsername) {
        UserOutputDto targetDto;

        Optional<User> user = userRepository.findById(targetUsername);
        if (user.isPresent()) {
            User targetUser = user.get();

            // Check of de requestingUser de juiste rechten heeft
            if (isAllowedToAccess(requestingUsername, targetUser)) {
                targetDto = fromUser(targetUser);
            } else {
                throw new BadRequestException("U heeft geen toegang tot deze gegevens");
            }
        } else {
            throw new UsernameNotFoundException(targetUsername);
        }

        return targetDto;
    }

    private boolean isAllowedToAccess(String requestingUsername, User targetUser) {
        // Admin and Planner kunnen bij alle data
        if (isRoleAdmin(requestingUsername) || isRolePlanner(requestingUsername)) {
            return true;
        }

        // Mechanics kunnen alleen bij eigen data
        if (isRoleMechanic(requestingUsername)) {
            return requestingUsername.equals(targetUser.getUsername());
        }

        return false;
    }

    private boolean isRoleAdmin(String username) {
        User user = userRepository.findById(username).orElse(null);
        if (user != null) {
            for (Authority authority : user.getAuthorities()) {
                if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isRolePlanner(String username) {
        User user = userRepository.findById(username).orElse(null);
        if (user != null) {
            for (Authority authority : user.getAuthorities()) {
                if (authority.getAuthority().equals("ROLE_PLANNER")) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isRoleMechanic(String username) {
        User user = userRepository.findById(username).orElse(null);
        if (user != null) {
            for (Authority authority : user.getAuthorities()) {
                if (authority.getAuthority().equals("ROLE_MECHANIC")) {
                    return true;
                }
            }
        }
        return false;
    }

    //*------------------- Einde Eigen methodes -------------------*//


    public static UserOutputDto fromUser(User user) {

        var dto = new UserOutputDto();

        dto.username = user.getUsername();
        dto.enabled = user.isEnabled();
        dto.email = user.getEmail();
        dto.authorities = user.getAuthorities();
        dto.scheduleTask = user.getScheduleTask();

        return dto;
    }

    public static UserInputDto fromUserInput(User user) {

        var dto = new UserInputDto();

        dto.username = user.getUsername();
        dto.password = user.getPassword();
        dto.enabled = user.isEnabled();
        dto.email = user.getEmail();
        dto.apikey = user.getApikey();
        dto.authorities = user.getAuthorities();
        dto.scheduleTask = user.getScheduleTask();

        return dto;
    }

    public User toUser(UserInputDto userInputDto) {

        var user = new User();

        user.setUsername(userInputDto.getUsername());
        user.setPassword(passwordEncoder.encode(userInputDto.getPassword()));
        user.setEnabled(userInputDto.getEnabled());
        user.setApikey(userInputDto.getApikey());
        user.setEmail(userInputDto.getEmail());
        user.setScheduleTask(userInputDto.getScheduleTask());

        return user;
    }
}
