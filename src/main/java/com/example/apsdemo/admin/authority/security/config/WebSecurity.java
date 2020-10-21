package com.example.apsdemo.admin.authority.security.config;

import com.example.apsdemo.admin.authority.security.filter.CustomAuthenticationFilter;
import com.example.apsdemo.admin.authority.security.filter.JwtAuthenticationTokenFilter;
import com.example.apsdemo.admin.authority.security.handler.EntryPointUnauthorizedHandler;
import com.example.apsdemo.admin.authority.security.handler.MyAuthenticationFailureHandler;
import com.example.apsdemo.admin.authority.security.handler.MyAuthenticationSuccessHandler;
import com.example.apsdemo.admin.authority.security.handler.RestAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsUtils;

/**
 * Spring Security 配置类
 *
 * @EnableGlobalMethodSecurity 开启注解的权限控制，默认是关闭的。
 * prePostEnabled：使用表达式实现方法级别的控制，如：@PreAuthorize("hasRole('ADMIN')")
 * securedEnabled: 开启 @Secured 注解过滤权限，如：@Secured("ROLE_ADMIN")
 * jsr250Enabled: 开启 @RolesAllowed 注解过滤权限，如：@RolesAllowed("ROLE_ADMIN")
 */
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    private final EntryPointUnauthorizedHandler entryPointUnauthorizedHandler;
    private final RestAccessDeniedHandler restAccessDeniedHandler;
    private final MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
    private final MyAuthenticationFailureHandler myAuthenticationFailureHandler;

    public WebSecurity(UserDetailsService userDetailsService, JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter, EntryPointUnauthorizedHandler entryPointUnauthorizedHandler, RestAccessDeniedHandler restAccessDeniedHandler, MyAuthenticationSuccessHandler myAuthenticationSuccessHandler, MyAuthenticationFailureHandler myAuthenticationFailureHandler) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationTokenFilter = jwtAuthenticationTokenFilter;
        this.entryPointUnauthorizedHandler = entryPointUnauthorizedHandler;
        this.restAccessDeniedHandler = restAccessDeniedHandler;
        this.myAuthenticationSuccessHandler = myAuthenticationSuccessHandler;
        this.myAuthenticationFailureHandler = myAuthenticationFailureHandler;
    }

    /**
     * 从容器中取出 AuthenticationManagerBuilder，执行方法里面的逻辑之后，放回容器
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        // 内存认证
        //auth.inMemoryAuthentication().passwordEncoder(passwordEncoder()).withUser("admin").password(passwordEncoder().encode("admin")).roles("ADMIN");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry registry = http.authorizeRequests();
        //让Spring security 放行所有preflight request（cors 预检请求）
        registry.requestMatchers(CorsUtils::isPreFlightRequest).permitAll();

        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and().authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 放行swagger的一些请求
                .antMatchers("/swagger-ui.html","/webjars/**","/v2/**","/swagger-resources/**","/avatar/**").permitAll()
                .anyRequest().authenticated()   // 任何请求,登录后可以访问
                .and().formLogin()
                .and().headers().cacheControl();

        //在 UsernamePasswordAuthenticationFilter 之前添加 JwtAuthenticationTokenFilter
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);
        //用重写的Filter替换掉原有的UsernamePasswordAuthenticationFilter
        http.addFilterAt(customAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        // 处理异常情况：认证失败和权限不足
        http.exceptionHandling().authenticationEntryPoint(entryPointUnauthorizedHandler).accessDeniedHandler(restAccessDeniedHandler);


    }

    //注册自定义的UsernamePasswordAuthenticationFilter
    @Bean
    CustomAuthenticationFilter customAuthenticationFilter() throws Exception {
        CustomAuthenticationFilter filter = new CustomAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
        filter.setAuthenticationFailureHandler(myAuthenticationFailureHandler);
        filter.setFilterProcessesUrl("/user/login");

        //这句很关键，重用WebSecurityConfigurerAdapter配置的AuthenticationManager，不然要自己组装AuthenticationManager
        filter.setAuthenticationManager(authenticationManagerBean());
        return filter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // 创建默认DelegatingPasswordEncoder
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    /**
     * 跨域处理
     * @return
     */
    /*@Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration cors = new CorsConfiguration();
        cors.setAllowCredentials(true);
        cors.addAllowedOrigin("*");
        cors.addAllowedHeader("*");
        cors.addAllowedMethod("*");
        configurationSource.registerCorsConfiguration("/**", cors);
        return new CorsFilter(configurationSource);
    }*/


}