package com.ng.runz.config;

import com.ng.runz.exception.ResourceNotFoundException;
import com.ng.runz.response.ApiResponse;
import com.ng.runz.service.JwtUtilService;
import com.ng.runz.service.impl.UserPrincipalServiceDetail;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtilService jwtUtilService;
    private final ObjectMapper objectMapper;
    private final ApplicationContext applicationContext;
    public JwtFilter(JwtUtilService jwtUtilService,ObjectMapper objectMapper,ApplicationContext applicationContext){
        this.jwtUtilService=jwtUtilService;
        this.objectMapper=objectMapper;
        this.applicationContext=applicationContext;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if(header!=null&&header.startsWith("Bearer")){

            try {

                String jwtToken = header.substring(7);
//                System.out.println(jwtToken);
                String username = jwtUtilService.getUsernameFromJwtToken(jwtToken);
//                System.out.println(username);
                if(username!=null&&SecurityContextHolder.getContext().getAuthentication()==null){

                        if(jwtUtilService.validateToken(jwtToken)){

                            UserDetails userDetails = applicationContext.getBean(UserPrincipalServiceDetail.class).loadUserByUsername(username);
                            ObjectMapper om = new ObjectMapper();
//                            System.out.println(om.writeValueAsString(userDetails));
                            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
//                            usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);

                        }else{
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setHeader("Content-Type","application/json");
                            ApiResponse jwtResponse = new ApiResponse("error","invalid token, you may login and try again.");
                            String jwtResponseStr = objectMapper.writeValueAsString(jwtResponse);
                            response.getWriter().write(jwtResponseStr);
                            return;
                        }


                }else{
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setHeader("Content-Type","application/json");
                    ApiResponse jwtResponse = new ApiResponse("error","invalid token, you may login and try again.");
                    String jwtResponseStr = objectMapper.writeValueAsString(jwtResponse);
                    response.getWriter().write(jwtResponseStr);
                    return;
                }
            } catch (UsernameNotFoundException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setHeader("Content-Type","application/json");
                ApiResponse jwtResponse = new ApiResponse("error","invalid token, you may login and try again.");
                String jwtResponseStr = objectMapper.writeValueAsString(jwtResponse);
                response.getWriter().write(jwtResponseStr);
                return;
            }catch (ResourceNotFoundException e){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setHeader("Content-Type","application/json");
                ApiResponse jwtResponse = new ApiResponse("error",e.getMessage());
                String jwtResponseStr = objectMapper.writeValueAsString(jwtResponse);
                response.getWriter().write(jwtResponseStr);
                return;
            }catch (Exception e){
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.setHeader("Content-Type","application/json");
                ApiResponse jwtResponse = new ApiResponse("error yes",e.getMessage());
                String jwtResponseStr = objectMapper.writeValueAsString(jwtResponse);
                response.getWriter().write(jwtResponseStr);
                return;
            }
        }

        filterChain.doFilter(request,response);
    }
}
