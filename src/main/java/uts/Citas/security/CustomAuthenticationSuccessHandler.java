package uts.Citas.security;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final Map<String, String> roleTargetUrlMap;

    public CustomAuthenticationSuccessHandler() {
        roleTargetUrlMap = new HashMap<>();
        // Configura las URLs de destino según el rol
        roleTargetUrlMap.put("ROLE_ADMINISTRADOR", "/admin/menu");
        roleTargetUrlMap.put("ROLE_GENERAL", "/medico/menu");
        roleTargetUrlMap.put("ROLE_ESPECIALISTA", "/especialista/menu");
        roleTargetUrlMap.put("ROLE_SECRETARIA", "/secretaria/menu");
        roleTargetUrlMap.put("ROLE_PACIENTE", "/paciente/menu");
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String targetUrl = determineTargetUrl(authorities);
        
        // Redirecciona al usuario a la página correspondiente
        response.sendRedirect(targetUrl);
    }
    
    private String determineTargetUrl(Collection<? extends GrantedAuthority> authorities) {
        // Busca el rol del usuario y devuelve la URL correspondiente
        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
            if (roleTargetUrlMap.containsKey(role)) {
                return roleTargetUrlMap.get(role);
            }
        }
        
        // URL por defecto si no se encuentra un rol específico
        return "/login";
    }
}
