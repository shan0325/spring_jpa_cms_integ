package com.spring.cms.config.security;

import com.spring.cms.controller.RestControllerBase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

/**
 * WebSecurityConfigurerAdapter 인터페이스의 구현체입니다.
 * Spring Security 의 가장 기본적인 설정이며 JWT 를 사용하지 않더라도 이 설정은 기본으로 들어갑니다.
 * 오버라이드한 configure 내부에서 각종 설정들을 추가해줍니다.
 * 각 설정에 대한 설명은 주석을 확인하면 됩니다.
 */

@Slf4j
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CustomUserDetailService userDetailService;
    private final SecurityResourceService securityResourceService;

    //permitAll 필터 사용을 위한 자원 설정
    private String[] permitAllResources = {RestControllerBase.API_URI_PREFIX + "/auth/**"};

    // h2 database 테스트가 원활하도록 관련 API 들은 전부 무시
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/h2-console/**", "/favicon.ico", "/docs/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            // CSRF 설정 Disable
            .csrf().disable()

            // exception handling 할 때 우리가 만든 클래스를 추가
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .accessDeniedHandler(jwtAccessDeniedHandler)

            // h2-console 을 위한 설정을 추가
            .and()
            .headers()
            .frameOptions()
            .sameOrigin()

            // 시큐리티는 기본적으로 세션을 사용
            // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

            // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스를 적용
            // 로그인, 회원가입 API 는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
            .and()
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(customFilterSecurityInterceptor(), FilterSecurityInterceptor.class)
            .authorizeRequests()
//                .antMatchers(RestControllerBase.API_URI_PREFIX + "/auth/**").permitAll()
            .anyRequest().authenticated(); // 나머지 API 는 전부 인증 필요
    }

    /*@Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }*/

    //비밀번호 암호화를 위한 Encoder 설정
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public FilterSecurityInterceptor customFilterSecurityInterceptor() throws Exception {

//        FilterSecurityInterceptor filterSecurityInterceptor = new FilterSecurityInterceptor();
//        //3가지 속성을 설정해주어야 한다.
//        filterSecurityInterceptor.setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource());
//        filterSecurityInterceptor.setAccessDecisionManager(affirmativeBased());
//        filterSecurityInterceptor.setAuthenticationManager(authenticationManagerBean());
//
//        return filterSecurityInterceptor;

        //permitAllFilter 적용
        PermitAllFilter permitAllFilter = new PermitAllFilter(permitAllResources);
        permitAllFilter.setSecurityMetadataSource(urlFilterInvocationSecurityMetadataSource()); // 권한정보 셋팅
        permitAllFilter.setAccessDecisionManager(affirmativeBased());
        permitAllFilter.setAuthenticationManager(authenticationManagerBean()); // 인증매니저

        return permitAllFilter;
    }

    private AccessDecisionManager affirmativeBased() {
        return new AffirmativeBased(getAccessDecisionVoters());
    }

    private List<AccessDecisionVoter<?>> getAccessDecisionVoters() {
        return Arrays.asList(new RoleVoter());
    }

    @Bean
    public UrlFilterInvocationMetadataSource urlFilterInvocationSecurityMetadataSource() throws Exception {
        return new UrlFilterInvocationMetadataSource(urlResourcesMapFactoryBean().getObject(), securityResourceService);
    }

    private UrlResourceMapFactoryBean urlResourcesMapFactoryBean() {
        UrlResourceMapFactoryBean urlResourceMapFactoryBean = new UrlResourceMapFactoryBean();
        urlResourceMapFactoryBean.setSecurityResourceService(securityResourceService);
        return urlResourceMapFactoryBean;
    }

}
