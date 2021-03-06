package com.labes.doe.service.security.impl;

import com.labes.doe.exception.InvalidUsernamePasswordException;
import com.labes.doe.model.enumeration.Profile;
import com.labes.doe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReactiveUserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final UserRepository repository;

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return repository
                .findByUser(username)
                .switchIfEmpty(Mono.error(new InvalidUsernamePasswordException()))
                .map(user -> {
                    List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("CLIE"));
                    return new UserDetailsImpl(user.getId(), authorities, user.getPassword(), user.getUser());
                });
    }
}
