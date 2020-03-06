package xyz.zxcwxy999.blog.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.zxcwxy999.blog.domain.Catalog;
import xyz.zxcwxy999.blog.domain.User;
import xyz.zxcwxy999.blog.repository.CatalogRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CatalogServiceImpl implements CatalogService {

    @Autowired
    CatalogRepository catalogRepository;

    @Override
    public Catalog saveCatelog(Catalog catalog) {
        List<Catalog>list=catalogRepository.findByUserAndName(catalog.getUser(),catalog.getName());
        if(list!=null&&list.size()>0){
            throw new IllegalArgumentException("该分类已经存在了");
        }
        return catalogRepository.save(catalog);
    }

    @Override
    public void removeCatalog(Long id) {
        catalogRepository.deleteById(id);
    }

    @Override
    public Optional<Catalog> getCatalogById(Long id) {
        return catalogRepository.findById(id);
    }

    @Override
    public List<Catalog> listCatalogs(User user) {
        return catalogRepository.findByUser(user);
    }
}
