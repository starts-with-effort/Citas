package uts.Citas.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
// Asegúrate de importar AuthenticationConfiguration si no está ya
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // Necesario si quieres usar @PreAuthorize en métodos de controlador más adelante
public class SecurityConfig{

    // Inyecta tu UserDetailsService personalizado
    private final UsuarioSecurityService usuarioSecurityService;
    private final CustomAuthenticationSuccessHandler successHandler;

    public SecurityConfig(UsuarioSecurityService usuarioSecurityService, 
                          CustomAuthenticationSuccessHandler successHandler) {
        this.usuarioSecurityService = usuarioSecurityService;
        this.successHandler = successHandler;
    }

    @Bean
    public SecurityFilterChain filtroSeguridad(HttpSecurity httpSecurity) throws Exception {
        HeaderWriterLogoutHandler clearSiteData = new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.ALL));

        httpSecurity
                // CSRF habilitado por defecto, Thymeleaf lo soporta bien. Si da problemas, puedes desactivarlo
                // .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        // Permite acceso público a estas rutas y recursos estáticos
                        .requestMatchers(
                                "/login",
                                "/perform_login",
                                "/logout",
                                "/css/**",  
                                "/js/**",
                                "/images/**",
                                "/webjars/**"  
                        ).permitAll()
                        //.requestMatchers("/admin/**").hasRole("Administrador")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")           // Página de login personalizada
                        .loginProcessingUrl("/perform_login") // URL que procesa el login
                        .successHandler(successHandler) // Usar el manejador personalizado
                        .failureUrl("/login?error")     // Redirige aquí si falla el login
                        .permitAll()                   // Permite a todos acceder a la config del formLogin
                )
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "POST")) // Mejor usar POST para logout
                        .addLogoutHandler(clearSiteData)
                        .clearAuthentication(true)
                        .invalidateHttpSession(true)
                        .logoutSuccessUrl("/login?logout") // Redirige aquí tras logout exitoso
                        .permitAll()
                )
                 // Configuración de sesión (ALWAYS es común para apps web tradicionales)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) // O IF_REQUIRED
                        .invalidSessionUrl("/login?invalid") // A dónde ir si la sesión es inválida
                        .maximumSessions(1) // Opcional: permite solo 1 sesión por usuario
                        .expiredUrl("/login?expired") // A dónde ir si la sesión expiró
                );

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(usuarioSecurityService); // Usa tu servicio inyectado
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
