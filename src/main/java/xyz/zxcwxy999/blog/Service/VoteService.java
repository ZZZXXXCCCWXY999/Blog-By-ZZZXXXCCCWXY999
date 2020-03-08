package xyz.zxcwxy999.blog.Service;

import org.springframework.data.jpa.repository.JpaRepository;
import xyz.zxcwxy999.blog.domain.Vote;

import java.util.Optional;

public interface VoteService {
    /**
     * 根据id获取vote
     * @param id
     * @return
     */
    Optional<Vote>getVoteById(Long id);

    /**
     * 删除vote
     * @param id
     */
    void removeVote(Long id);
}
