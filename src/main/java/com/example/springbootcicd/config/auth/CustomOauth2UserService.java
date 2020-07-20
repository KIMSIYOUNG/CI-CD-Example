package com.example.springbootcicd.config.auth;

import java.util.Collections;

import javax.servlet.http.HttpSession;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.springbootcicd.domain.user.User;
import com.example.springbootcicd.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(final OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        final OAuth2User oAuth2User = delegate.loadUser(userRequest);

        final String registrationId = userRequest.getClientRegistration().getRegistrationId();
        final String userNameAttributeName = userRequest.getClientRegistration()
            .getProviderDetails()
            .getUserInfoEndpoint()
            .getUserNameAttributeName();
        OAuthAttributes authAttributes = OAuthAttributes.of(registrationId, userNameAttributeName,
            oAuth2User.getAttributes());

        User user = saveOrUpdate(authAttributes);
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
            authAttributes.getAttributes(),
            authAttributes.getNameAttributeKey()
        );
    }

    private User saveOrUpdate(final OAuthAttributes authAttributes) {
        User user = userRepository.findByEmail(authAttributes.getEmail())
            .map(entity -> entity.update(authAttributes.getName(), authAttributes.getPicture()))
            .orElse(authAttributes.toEntity());

        return userRepository.save(user);
    }
}
