package com.theunitedtraders.javatest.controller;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.test.context.TestSecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration({"classpath:applicationContext-test.xml"})
public class GreetingControllerTest {

    private static final String MAIN_URI = "/";

    @Autowired
    private WebApplicationContext wac;

    private MockMvc mockMvcComplex, mockMvcPlain;

    @Before
    public void setup() {
        mockMvcComplex = MockMvcBuilders
                .webAppContextSetup(wac)
//                .alwaysDo(print()) //MockMvcResultHandlers.print()
                .apply(springSecurity())
                .defaultRequest(get(MAIN_URI).contentType(MediaType.APPLICATION_JSON))
                .alwaysExpect(content().contentType(MediaType.APPLICATION_JSON))
                .alwaysExpect(status().isOk())
                .build();

        mockMvcPlain = MockMvcBuilders
                .webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @After
    public void clearContext() {
        TestSecurityContextHolder.clearContext();
    }

    @Test
    @WithMockUser(username = "user", password = "user", roles = "USER")
    public void testWrongMethod() throws Exception {
        mockMvcPlain.perform(post(MAIN_URI).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(containsString("\"message\":\"Method not supported\"")))
        ;
    }

    @Test
    @WithMockUser(username = "blablabla", password = "blablabla", roles = {"BLAHHH"})
    public void testWrongUser() throws Exception {
        mockMvcPlain.perform(get(MAIN_URI))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", password = "admin", roles = {"USER", "ADMIN"})
    public void testForAdmin() throws Exception {
        mockMvcComplex.perform(get(MAIN_URI))
                .andExpect(content().string(containsString("{\"message\":\"Hail to the king!\",\"count\":1}")));
    }

    @Test
    @WithMockUser(username = "user", password = "user", roles = "USER")
    public void testForUser() throws Exception {
        mockMvcComplex.perform(get(MAIN_URI));
        mockMvcComplex.perform(get(MAIN_URI));
        mockMvcComplex.perform(get(MAIN_URI));
        mockMvcComplex.perform(get(MAIN_URI))
                .andExpect(content().string(containsString("{\"message\":\"Welcome user!\",\"count\":4}")));
    }

    @Test
    public void testForOther() throws Exception {
        mockMvcPlain
                .perform(
                        get(MAIN_URI)
                                .with(user("other")
                                        .password("other")
                                        .roles("OTHER"))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isForbidden())
        ;
    }

    @EnableWebSecurity(/*debug = true*/)
    @Configuration
    static class Config extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf().disable()
                    .authorizeRequests()
                    .antMatchers("/**").access("hasRole('ROLE_USER')");
        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth
                    .inMemoryAuthentication()
                    .withUser("admin").password("admin").roles("USER", "ADMIN")
                    .and()
                    .withUser("user").password("user").roles("USER")
                    .and()
                    .withUser("other").password("other").roles("OTHER");
        }
    }

}
