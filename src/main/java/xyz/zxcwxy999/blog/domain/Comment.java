package xyz.zxcwxy999.blog.domain;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

public class Comment implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id//主键
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增长策略
    private Long id;//用户唯一标识

    @NotEmpty(message = "评论内容不能为空")
    @Size(min = 2, max = 500)
    @Column(nullable = false)//映射为字段，值不能为空
    private String content;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)//映射为字段，值不能为空
    @org.hibernate.annotations.CreationTimestamp//自动创建时间
    private Timestamp createTime;

    protected Comment() {

    }

    public Comment(User user,String content) {
        this.content = content;
        this.user = user;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }


}

