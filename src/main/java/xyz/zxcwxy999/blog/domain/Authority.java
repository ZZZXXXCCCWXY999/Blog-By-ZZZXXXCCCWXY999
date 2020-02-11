package xyz.zxcwxy999.blog.domain;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;

@Entity //实体的注解
public class Authority implements GrantedAuthority {

    private static final long sericalVersionUID=1L;

    @Id //主键
    @GeneratedValue(strategy= GenerationType.IDENTITY)//自增长策略
    private Long id;//用户的唯一标识

    @Column(nullable = false)//映射为字段，值不能为空
    private String name;//权限名称

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 返回name属性
     * 有这个方法就不用getName方法了
     * @return
     */
    @Override
    public String getAuthority() {
        return name;
    }
}
