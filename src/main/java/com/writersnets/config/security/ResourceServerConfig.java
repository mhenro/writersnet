package com.writersnets.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("writersnets.com");
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST,"/register").permitAll()
                .antMatchers(HttpMethod.GET, "/activate").permitAll()
                .antMatchers(HttpMethod.GET, "/reminder/confirm").permitAll()
                .antMatchers(HttpMethod.GET, "/reminder/password").permitAll()

                .antMatchers(HttpMethod.GET, "/authors").permitAll()
                .antMatchers(HttpMethod.GET, "/count/authors").permitAll()
                .antMatchers(HttpMethod.GET, "/authors/name/{authorName:.+}").permitAll()
                .antMatchers(HttpMethod.GET, "/authors/{authorId:.+}").permitAll()

                .antMatchers(HttpMethod.GET, "/top/authors/rating").permitAll()
                .antMatchers(HttpMethod.GET, "/top/authors/bookcount").permitAll()
                .antMatchers(HttpMethod.GET, "/top/authors/comments").permitAll()
                .antMatchers(HttpMethod.GET, "/top/authors/views").permitAll()
                .antMatchers(HttpMethod.GET, "/top/books/novelties").permitAll()
                .antMatchers(HttpMethod.GET, "/top/books/rating").permitAll()
                .antMatchers(HttpMethod.GET, "/top/books/volume").permitAll()
                .antMatchers(HttpMethod.GET, "/top/books/comments").permitAll()
                .antMatchers(HttpMethod.GET, "/top/books/views").permitAll()

                .antMatchers(HttpMethod.GET, "/books").permitAll()
                .antMatchers(HttpMethod.GET, "/books/name/{bookName}").permitAll()
                .antMatchers(HttpMethod.GET, "/count/books").permitAll()
                .antMatchers(HttpMethod.GET, "/books/{bookId}").permitAll()
                .antMatchers(HttpMethod.GET, "/books/author/{authorId}").permitAll()
                .antMatchers(HttpMethod.GET, "/books/cost/{bookId}").permitAll()
                .antMatchers(HttpMethod.GET, "/genres").permitAll()
                .antMatchers(HttpMethod.GET, "/books/{bookId}/comments").permitAll()
                .antMatchers("/books/comments").permitAll()
                .antMatchers(HttpMethod.GET, "/books/{bookId}/rating/{value}").permitAll()

                .antMatchers(HttpMethod.GET, "/captcha").permitAll()

                .antMatchers(HttpMethod.GET, "/contests").permitAll()
                .antMatchers(HttpMethod.GET, "/contests/participants/{userId}").permitAll()
                .antMatchers(HttpMethod.GET, "/contests/judges/{userId}").permitAll()
                .antMatchers(HttpMethod.GET, "/contests/creators/{creatorId}").permitAll()
                .antMatchers(HttpMethod.GET, "/contests/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/contests/{id}/judges").permitAll()
                .antMatchers(HttpMethod.GET, "/contests/{id}/judges-full").permitAll()
                .antMatchers(HttpMethod.GET, "/contests/{id}/participants").permitAll()
                .antMatchers(HttpMethod.GET, "/contests/{id}/participants-full").permitAll()
                .antMatchers(HttpMethod.GET, "/contests/not-accepted/{userId}").permitAll()
                .antMatchers(HttpMethod.GET, "/contests/{id}/participants-count").permitAll()
                .antMatchers(HttpMethod.GET, "/contests/{id}/judges-count").permitAll()
                .antMatchers(HttpMethod.GET, "/contests/{id}/readiness").permitAll()
                .antMatchers(HttpMethod.GET, "/contests/{id}/ratings").permitAll()
                .antMatchers(HttpMethod.GET, "/contests/{id}/ratings/{bookId}").permitAll()

                .antMatchers(HttpMethod.GET, "/feedback").permitAll()

                .antMatchers(HttpMethod.GET, "/gift/{id}").permitAll()
                .antMatchers(HttpMethod.GET, "/gifts").permitAll()
                .antMatchers(HttpMethod.GET, "/gifts/authors/{userId}").permitAll()

                .antMatchers(HttpMethod.GET, "/reviews").permitAll()
                .antMatchers(HttpMethod.GET, "/reviews/{bookId}").permitAll()
                .antMatchers(HttpMethod.GET, "/review/{reviewId}").permitAll()
                .antMatchers(HttpMethod.GET, "/review/{reviewId}/like").permitAll()
                .antMatchers(HttpMethod.GET, "/review/{reviewId}/dislike").permitAll()

                .antMatchers(HttpMethod.GET, "/sections/{sectionId:.+}").permitAll()

                .antMatchers(HttpMethod.GET, "/series/{userId:.+}").permitAll()

                .antMatchers(HttpMethod.GET, "/count/sessions").permitAll()
                .antMatchers(HttpMethod.GET, "/sessions/{userId}").permitAll()

                .antMatchers("/**").authenticated();
    }
}
