package com.fondosBTG.services;

import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SmsServiceTest {

    @InjectMocks
    private SmsService smsService;

    @Value("${twilio.account-sid}")
    private String accountSid = "testAccountSid";

    @Value("${twilio.auth-token}")
    private String authToken = "testAuthToken";

    @Value("${twilio.from-number}")
    private String fromNumber = "+1234567890";

    @BeforeEach
    void setUp() {
        smsService = new SmsService(accountSid, authToken);
        smsService.setFromNumber(fromNumber);
    }

    @Test
    public void testEnviarSms() {
        String toNumber = "+0987654321";
        String messageContent = "Mensaje de prueba";

        try (MockedStatic<Message> mockedMessage = Mockito.mockStatic(Message.class)) {

            // Crear un mock de MessageCreator en lugar de Message.Creator
            MessageCreator mockMessageCreator = mock(MessageCreator.class);
            mockedMessage.when(() -> Message.creator(
                            any(PhoneNumber.class), any(PhoneNumber.class), anyString()))
                    .thenReturn(mockMessageCreator);

            smsService.enviarSms(toNumber, messageContent);

            mockedMessage.verify(() -> Message.creator(
                    new PhoneNumber(toNumber), new PhoneNumber(fromNumber), messageContent));
            verify(mockMessageCreator, times(1)).create();
        }
    }
}
