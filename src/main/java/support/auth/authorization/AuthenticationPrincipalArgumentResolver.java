package support.auth.authorization;

import java.util.Objects;
import org.springframework.core.MethodParameter;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import support.auth.context.Authentication;
import support.auth.context.SecurityContextHolder;
import support.auth.userdetails.User;

public class AuthenticationPrincipalArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthenticationPrincipal.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        boolean required = Objects.requireNonNull(parameter.getParameterAnnotation(AuthenticationPrincipal.class)).required();
        if (!required && ObjectUtils.isEmpty(authentication)) {
            return new User();
        }

        return new User(authentication.getPrincipal().toString(), null, authentication.getAge(), authentication.getAuthorities());
    }
}
