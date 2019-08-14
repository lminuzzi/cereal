package br.com.cereal.cerealsul.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Value("${user.oauth.clientId}")
    private String clientID;
    @Value("${user.oauth.clientSecret}")
    private String clientSecret;
    @Value("${user.oauth.grantTypePassword}")
    private String grantTypePassword;
    @Value("${user.oauth.authorizationCode}")
    private String authorizationCode;
    @Value("${user.oauth.refreshToken}")
    private String refreshToken;
    @Value("${user.oauth.implicit}")
    private String implicit;
    @Value("${user.oauth.scopeRead}")
    private String scopeRead;
    @Value("${user.oauth.scopeWrite}")
    private String scopeWrite;
    @Value("${user.oauth.trust}")
    private String trust;
    @Value("${user.oauth.signingKey}")
    private String signingKey;
//    private int ACCESS_TOKEN_VALIDITY_SECONDS = 1 * 60 * 60;
//    private int FREFRESH_TOKEN_VALIDITY_SECONDS = 6 * 60 * 60;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Bean
    public JwtAccessTokenConverter accessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(signingKey);
        return converter;
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(accessTokenConverter());
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer configurer) throws Exception {

        configurer
                .inMemory()
                .withClient(clientID)
                .secret(clientSecret)
                .authorizedGrantTypes(grantTypePassword, authorizationCode/*, refreshToken, implicit*/)
                .scopes(scopeRead, scopeWrite, trust);
//                .accessTokenValiditySeconds(ACCESS_TOKEN_VALIDITY_SECONDS)
//                .refreshTokenValiditySeconds(FREFRESH_TOKEN_VALIDITY_SECONDS);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
        endpoints.tokenStore(tokenStore())
                .authenticationManager(authenticationManager)
                .accessTokenConverter(accessTokenConverter());
    }

}