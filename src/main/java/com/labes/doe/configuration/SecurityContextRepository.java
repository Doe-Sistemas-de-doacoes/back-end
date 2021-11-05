package com.labes.doe.configuration;

import com.labes.doe.model.User;
import com.labes.doe.model.enumeration.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Component
public class SecurityContextRepository implements ServerSecurityContextRepository {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange exchange, SecurityContext context) {
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load( ServerWebExchange exchange ) {
        return Mono.justOrEmpty (
                    exchange.getRequest()
                    .getHeaders()
                    .getFirst(HttpHeaders.AUTHORIZATION)
                )
                .filter(b -> b.startsWith("Bearer "))
                .map(subs -> subs.substring(7))
                .flatMap(token ->
                    Mono.just(new UsernamePasswordAuthenticationToken (
                            null,
                            token,
                            Collections.EMPTY_LIST
                        )
                    )
                )
                .flatMap(auth -> authenticationManager.authenticate(auth))
                .map(SecurityContextImpl::new);
    }
}
