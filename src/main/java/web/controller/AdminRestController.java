package web.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import web.dto.UserDto;

import web.exception_handler.CustomExceptionHandler;
import web.exception_handler.ErrorResponse;
import web.model.Role;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;


import javax.validation.*;

import org.springframework.web.bind.annotation.PutMapping;


import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hibernate.tool.schema.SchemaToolingLogging.LOGGER;

@RestController
@RequestMapping("/api")
public class AdminRestController extends ValidationException {
    private final UserService userService;

    private final RoleService roleService;



    public AdminRestController ( UserService userService, RoleService roleService) {
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


    @PostMapping("/admin")
    public ResponseEntity<?> addUser(@Valid @RequestBody UserDto userDto) {
        try {
        User user = new User(userDto);
        Set<Role> roles = new HashSet<>();
        for (String roleName : userDto.getRoles()) {
            String fullRole = "ROLE_" + roleName;
            roles.add(roleService.getRole(fullRole));
        }
        user.setRoles(roles);
        userService.saveUser(user);
        return ResponseEntity.ok(user);

        }catch (ConstraintViolationException e) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
                errorMessage.append(violation.getMessage()).append("\n");
            }
            LOGGER.error("Ошибка ввода данных: " + errorMessage);

            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(errorMessage.toString()));
        }

    }
    @ExceptionHandler
    public ResponseEntity<CustomExceptionHandler> handleException(Exception exception) {
        CustomExceptionHandler data = new CustomExceptionHandler();
        data.setInfo(exception.getMessage());
        System.out.println(data);

        return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseEntity<String> handleValidationException(MethodArgumentNotValidException ex) {
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        String errorMessage = "";
        for (ObjectError error : errors) {
            errorMessage += error.getDefaultMessage() + "\n";
        }
        return ResponseEntity.badRequest().body(errorMessage);
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



}
