package xyz.zxcwxy999.blog.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xyz.zxcwxy999.blog.domain.Authority;
import xyz.zxcwxy999.blog.repository.AuthorityRepository;


import java.util.Optional;

@Service("AuthorityService")
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public Optional<Authority> getAuthorityById(Long id) {
        return authorityRepository.findById(id);
    }
}
