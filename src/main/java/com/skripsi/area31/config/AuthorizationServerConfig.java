package com.skripsi.area31.config;

import com.skripsi.area31.util.FieldName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

@Configuration @EnableAuthorizationServer public class AuthorizationServerConfig
    extends AuthorizationServerConfigurerAdapter {
    @Autowired private MongoTokenStore mongoTokenStore;
    @Autowired private AuthenticationManager authenticationManager;

    @Primary @Bean public BCryptPasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Override public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {
        configurer.inMemory().withClient(FieldName.Constants.CLIENT_ID)
            .secret(encoder().encode(FieldName.Constants.CLIENT_SECRET))
            .authorizedGrantTypes(FieldName.Constants.GRANT_TYPE_PASSWORD,
                FieldName.Constants.AUTHORIZATION_CODE, FieldName.Constants.REFRESH_TOKEN,
                FieldName.Constants.IMPLICIT)
            .scopes(FieldName.Constants.SCOPE_READ, FieldName.Constants.SCOPE_WRITE,
                FieldName.Constants.TRUST)
            .accessTokenValiditySeconds(FieldName.Constants.ACCESS_TOKEN_VALIDITY_SECONDS).
            refreshTokenValiditySeconds(FieldName.Constants.REFRESH_TOKEN_VALIDITY_SECONDS);
    }

    @Override public void configure(AuthorizationServerEndpointsConfigurer endpoints)
        throws Exception {
        endpoints.tokenStore(tokenStore()).authenticationManager(authenticationManager);
    }

    @Bean public TokenStore tokenStore() {
        return mongoTokenStore;
    }
}
