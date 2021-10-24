package com.labes.doe.service.security.impl;

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
                .switchIfEmpty(Mono.error(new UsernameNotFoundException(username)))
                .map(user -> {
                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();

                    if ( Profile.toEnum( user.getProfile() ).equals(Profile.ADMI) ) {
                        authorities.add(new SimpleGrantedAuthority( Profile.CLIE.getRole() ));
                        authorities.add(new SimpleGrantedAuthority( Profile.ADMI.getRole() ));
                    }else{
                        authorities.add(new SimpleGrantedAuthority( Profile.toEnum( user.getProfile() ).getRole() ));
                    }

                    return new UserDetailsImpl(user.getId(), authorities, user.getPassword(), user.getUser());
                });
    }
}
