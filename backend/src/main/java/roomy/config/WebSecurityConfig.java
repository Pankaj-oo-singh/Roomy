package roomy.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import roomy.filters.JwtAuthFilter;

import static javax.swing.text.html.FormSubmitEvent.MethodType.POST;
import static roomy.entities.enums.Role.ADMIN;
import static roomy.entities.enums.Role.CREATOR;
//
//
//@Configuration
//@EnableWebSecurity
//@RequiredArgsConstructor
//public class WebSecurityConfig {
//
//    private final JwtAuthFilter jwtAuthFilter;
////    private final OAuth2SuccessHandler oAuth2SuccessHandler;
//
//    private static final String[] publicRoutes = {
//            "/error", "/auth/**", "/home.html"
//    };
//
//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers(publicRoutes).permitAll()
//                        .requestMatchers(HttpMethod.GET, "/posts/**").permitAll()
//                        .requestMatchers(HttpMethod.POST, "/posts/**")
//                            .hasAnyRole(ADMIN.name(), CREATOR.name())
//                        .anyRequest().authenticated())
//                .csrf(csrfConfig -> csrfConfig.disable())
//                .sessionManagement(sessionConfig -> sessionConfig
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
//                .oauth2Login(oauth2Config -> oauth2Config
//                        .failureUrl("/login?error=true")
////                        .successHandler(oAuth2SuccessHandler)
//                );
////                .formLogin(Customizer.withDefaults());
//
//        return httpSecurity.build();
//    }
//
//    @Bean
//    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
//        return config.getAuthenticationManager();
//    }
//
//}
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    private static final String[] publicRoutes = {
            "/error", "/auth/**", "/home.html"
    };



@Bean
public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(publicRoutes).permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/room/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/room-reviews/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/room-reviews/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/documents/upload").authenticated()
                    .requestMatchers(HttpMethod.GET, "/documents/my-documents").authenticated()
                    .requestMatchers(HttpMethod.PUT, "/documents/**").hasRole("ADMIN")
                    .requestMatchers(HttpMethod.POST, "/profile/upload-image").authenticated()
                    .requestMatchers(HttpMethod.GET, "/profile/**").authenticated()
                    // Anyone can view rooms
                    .requestMatchers(HttpMethod.POST, "/api/room/**").authenticated() // Only logged-in users can create rooms
                    .anyRequest().authenticated()
            )
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    return http.build();
}

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
