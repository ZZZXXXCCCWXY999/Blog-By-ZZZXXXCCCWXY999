package xyz.zxcwxy999.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.zxcwxy999.blog.domain.Vote;

public interface VoteRepository extends JpaRepository<Vote, Long> {

}
