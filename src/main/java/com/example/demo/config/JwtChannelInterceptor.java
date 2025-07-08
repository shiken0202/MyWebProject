package com.example.demo.config;

import com.example.demo.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

@Configuration
public class JwtChannelInterceptor implements ChannelInterceptor {
    @Autowired
    JwtUtils jwtUtils;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor= MessageHeaderAccessor.getAccessor(message,StompHeaderAccessor.class);
        if(StompCommand.CONNECT.equals(accessor.getCommand())){
            String authHeader=accessor.getFirstNativeHeader("Authorization");
        /*
        前端設定：
        connectHeaders: {
        'Authorization': `Bearer ${token}` // 後端的 accessor.getFirstNativeHeader() 就是在讀取這個值
        },
        */
            if(authHeader!=null && authHeader.startsWith("Bearer ")){
                String jwt= authHeader.substring(7);
                String username=jwtUtils.extractUsername(jwt);
                if(username!=null && jwtUtils.validateToken(jwt,username)){
                    String role=jwtUtils.extractClaim(jwt,claims -> claims.get("role", String.class));
                    UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
                            username,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_"+role))
                    );
                    accessor.setUser(authenticationToken);
                }
            }

        }
        return message;
    }
}
