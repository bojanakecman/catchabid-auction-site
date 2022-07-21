package at.ac.ase.interceptor;

import at.ac.ase.util.NotificationUser;
import java.util.ArrayList;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class NotificationUserInterceptor implements ChannelInterceptor {


    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor =
            MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            Object raw = message
                .getHeaders()
                .get(SimpMessageHeaderAccessor.NATIVE_HEADERS);
            if (raw instanceof Map) {

                Object name = ((Map) raw).get("email");

                System.out.println("Registering user: " + ((ArrayList) name).get(0).toString());

                accessor.setUser(new NotificationUser(((ArrayList) name).get(0).toString()));

            }
        }
        return message;
    }
}