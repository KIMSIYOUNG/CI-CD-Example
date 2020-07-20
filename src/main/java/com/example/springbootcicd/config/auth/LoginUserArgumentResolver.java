package com.example.springbootcicd.config.auth;

import javax.servlet.http.HttpSession;

import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {
    private final HttpSession httpSession;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        final boolean isLoginUser = parameter.getParameterAnnotation(LoginUser.class) != null;
        final boolean isUserClass = SessionUser.class.equals(parameter.getParameterType());

        return isLoginUser && isUserClass;
    }

    @Override
    public Object resolveArgument(final MethodParameter parameter, final ModelAndViewContainer mavContainer,
        final NativeWebRequest webRequest, final WebDataBinderFactory binderFactory) throws Exception {
        return httpSession.getAttribute("user");
    }
}
