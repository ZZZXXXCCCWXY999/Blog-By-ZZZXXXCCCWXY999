package xyz.zxcwxy999.blog.domain;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;



@Entity
public class User {
    @Id//主键
    @GeneratedValue(strategy= GenerationType.IDENTITY)//自增策略
    private Long id;//实体一个唯一标识

    @NotEmpty(message = "姓名不能为空")
    @Size(min = 2,max = 20)
    @Column(nullable = false,length = 20)//映射为字段，值不能为空
    private String name;

    @NotEmpty(message = "邮箱不能为空")
    @Size(max = 50)
    @Email(message = "邮箱格式不对")
    @Column(nullable = false,length = 50,unique = true)
    private String email;

    @NotEmpty(message = "账号不能为空")
    @Size(min = 3,max = 20)
    @Column(nullable = false,length = 20,unique = true)
    private String username;//用户账号，用户登录是的唯一标识

    @NotEmpty(message = "密码不能为空")
    @Size(max = 100)
    @Column(length = 100)
    private  String password;//登录时密码

    @Column(length = 20)
    private String avatar;//头像图片地址

    protected User(){//无参构造方法，设为protected防止直接使用

    }

    public User(String name,String email,String username,String password){
        this.name=name;
        this.email=email;
        this.username=username;
        this.password=password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return String.format("User[id=%d,name='%s',username='%s',email='%s']",id,name,username,email);
    }
}
