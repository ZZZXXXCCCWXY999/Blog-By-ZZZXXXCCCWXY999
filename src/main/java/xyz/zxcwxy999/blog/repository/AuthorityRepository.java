package xyz.zxcwxy999.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.zxcwxy999.blog.domain.Authority;

public interface AuthorityRepository extends JpaRepository<Authority,Long> {

}
