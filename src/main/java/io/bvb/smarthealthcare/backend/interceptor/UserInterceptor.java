package io.bvb.smarthealthcare.backend.interceptor;

import io.bvb.smarthealthcare.backend.model.UserResponse;
import io.bvb.smarthealthcare.backend.util.CurrentUserData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Read the user ID from the session
        final Object userResponse = request.getSession().getAttribute("user");
        if (userResponse instanceof UserResponse) {
            CurrentUserData.setUser((UserResponse) userResponse);
        }

        return true;  // Continue with the request
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // Clear ThreadLocal data to prevent memory leaks
        CurrentUserData.clear();
    }
}
