package web.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import web.dto.UserDto;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@JsonIgnoreProperties({"hibernateLazyInitializer"})
@Entity
@Table(name = "Users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "name")
    @NotEmpty(message = "Имя не может быть пустым")
    @Size(min =2,max = 32,message = "имя должно быть от 2 до 32 символов")
    private String name;

    @Column(name = "last_name")
    @NotEmpty(message = "фамилия не может быть пустой")
    @Size(min =2,max = 32,message = "фамилмя должна быть от 2 до 32 символов")
    private String lastName;

    @Column(name = "age")
    @Min(value = 0, message = "полных лет должно быть от 0 до 128")
    @Max(value = 127, message = "полных лет должно быть от 0 до 128")
    private byte age;
    @NotEmpty(message = "Логин не должен быть пустым")
    @Pattern(regexp = "[A-Za-z]{2,15}", message = "Логин должен быть от 2 до 32 сивмолов, без пробелов, и только латинские символы")
    @Size(min = 2, max = 15, message = "Длина логина должны быть от 2 до 15 символов")
    @Column(name = "login", unique = true)
    private String userName;
    @Column(name = "password", nullable = false)
    @NotEmpty(message = "пароль не должен быть пустым")
    private String password;


    @ManyToMany(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    private Set<Role> roles = new HashSet<>();

    public User (){

    }
    public User(UserDto userDto) {
        this.id = userDto.getId();
        this.name = userDto.getName();
        this.lastName = userDto.getLastName();
        this.age = userDto.getAge();
        this.password = userDto.getPassword();
        this.userName = userDto.getUserName();
    }

    public User(Long id, String name, String lastName, byte age, String password, Set<Role> roles, String userName) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.password = password;
        this.roles = roles;
        this.userName = userName;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public byte getAge() {
        return age;
    }

    public void setAge(byte age) {
        this.age = age;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }


}
