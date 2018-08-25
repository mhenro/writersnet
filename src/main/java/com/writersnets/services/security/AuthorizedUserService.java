package com.writersnets.services.security;

import com.writersnets.models.entities.users.User;
import com.writersnets.models.exceptions.UnauthorizedUserException;
import com.writersnets.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by mhenr on 26.01.2018.
 */
@Service
@Transactional(readOnly = true)
public class AuthorizedUserService {
    private AuthorRepository authorRepository;

    @Autowired
    public AuthorizedUserService(final AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public User getAuthorizedUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();
        final User user = authorRepository.findById(currentUser).orElseThrow(() -> new UnauthorizedUserException("User is not found"));
        return user;
    }
}
