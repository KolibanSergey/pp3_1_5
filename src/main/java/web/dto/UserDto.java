package web.dto;



import java.util.Set;

public class UserDto {
    private Long id;

    private String name;
    private String lastName;
    private byte age;


    private String userName;
    private String password;
    private Set<String> roles;

    public UserDto() {
    }

    public UserDto(Long id, String name, String lastName, byte age, String userName, String password, Set<String > roles) {
        this.id = id;
        this.name = name;
        this.lastName = lastName;
        this.age = age;
        this.userName = userName;
        this.password = password;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
