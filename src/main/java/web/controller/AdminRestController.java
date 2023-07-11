package web.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.context.support.DefaultMessageSourceResolvable;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import web.dto.UserDto;
import web.exeption.ExceptionInfo;
import web.exeption.UserUsernameExistException;
import web.model.Role;
import web.model.User;
import web.service.RoleService;

import org.springframework.web.bind.annotation.PutMapping;
import web.service.UserService;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AdminRestController {
    private final UserService userService;

    private final RoleService roleService;


    public AdminRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;

    }

    @GetMapping("/admin")
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<User> getUser(@PathVariable(name = "id") long id) {
        User user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    //    @PostMapping("/admin")
//    public ResponseEntity<String> addUser(@RequestBody UserDto userDto) {
//        try {
//            User user = new User(userDto);
//        Set<Role> roles = new HashSet<>();
//        for (String roleName : userDto.getRoles()) {
//            String fullRole = "ROLE_" + roleName;
//            roles.add(roleService.getRole(fullRole));
//        }
//        user.setRoles(roles);
//        userService.saveUser(user);
//        return new ResponseEntity<>(HttpStatus.OK);
//        } catch (Exception ex) {
//        // Обработка исключения IllegalArgumentException
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
//    }
//    }
    @PostMapping("/admin")
    public ResponseEntity<User> addUser(@RequestBody UserDto userDto) {
        User user = new User(userDto);
        Set<Role> roles = new HashSet<>();
        for (String roleName : userDto.getRoles()) {
            String fullRole = "ROLE_" + roleName;
            roles.add(roleService.getRole(fullRole));
        }
        user.setRoles(roles);
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }




    @PutMapping("admin")
    public ResponseEntity<User> editUser(@RequestBody UserDto userDto) {
        User user = new User(userDto);
        Set<Role> roles = new HashSet<>();
        for (String roleName : userDto.getRoles()) {
            String fullRole = "ROLE_" + roleName;
            roles.add(roleService.getRole(fullRole));
        }
        user.setRoles(roles);
        userService.saveUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("admin/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable("id") long id) {
        userService.removeUser(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("admin/roles")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

    private String getErrorsFromBindingResult(BindingResult bindingResult) {
        return bindingResult.getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("; "));
    }

}
