package com.labes.doe.configuration;

import com.labes.doe.exception.InvalidTokenException;
import com.labes.doe.exception.InvalidUsernamePasswordException;
import com.labes.doe.repository.UserRepository;
import com.labes.doe.service.security.JWTUtil;
import com.labes.doe.service.security.impl.ReactiveUserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final ReactiveUserDetailsServiceImpl reactiveUserDetailsService;
    private final JWTUtil jwtUtil;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String token = authentication.getCredentials().toString();
        String userName = jwtUtil.getUsernameFromToken(token);

        return Mono.just(token)
                .map(jwtUtil::getUsernameFromToken)
                .flatMap(reactiveUserDetailsService::findByUsername)
                .filter(userDetails -> userName.equals(userDetails.getUsername()) && jwtUtil.isTokenValidated(token))
                .switchIfEmpty(Mono.error(new InvalidTokenException()))
                .flatMap(userDetails -> Mono.just( new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities())));
    }


}