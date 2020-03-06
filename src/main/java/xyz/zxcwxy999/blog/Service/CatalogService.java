package xyz.zxcwxy999.blog.Service;

import xyz.zxcwxy999.blog.domain.Catalog;
import xyz.zxcwxy999.blog.domain.User;

import java.util.List;
import java.util.Optional;

public interface CatalogService {

    /**
     * 保存Catalog
     * @param catalog
     * @return
     */
    Catalog saveCatelog(Catalog catalog);

    /**
     * 删除Catalog
     * @param id
     */
    void removeCatalog(Long id);

    /**
     * 根据Id获取Catalog
     * @param id
     * @return
     */
    Optional<Catalog> getCatalogById(Long id);

    /**
     * 获取Catalog列表
     * @param user
     * @return
     */
    List<Catalog> listCatalogs(User user);
}
