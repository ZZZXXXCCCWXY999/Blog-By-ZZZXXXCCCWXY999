package xyz.zxcwxy999.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.zxcwxy999.blog.domain.Catalog;
import xyz.zxcwxy999.blog.domain.User;

import java.util.List;

public interface CatalogRepository extends JpaRepository<Catalog, Long> {

    /**
     * 根据用户查询
     * @param user
     * @return
     */
    List<Catalog> findByUser(User user);

    /**
     * 根据用户查询
     * @param user
     * @param name
     * @return
     */
    List<Catalog> findByUserAndName(User user, String name);
}
