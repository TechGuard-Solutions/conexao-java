package org.techguard.slack;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import io.github.cdimascio.dotenv.Dotenv;

public class slack {

    static Dotenv dotenv = Dotenv.load();

    private static final Slack slack = Slack.getInstance(); // Tornar 'slack' estático
    private static final Logger LOGGER = LogManager.getLogger(slack.class);

    private static final String CODIGO1 = "xoxb-7992589832721-8050099410404";
    private static final String CODIGO2 = "-570DTS1Re6NOdz74nq3oIz3t";
    private static final String CODIGO = CODIGO1+CODIGO2;

    private static final String CANAL = "D081S81KQKB"; // Ler do ambiente

    public static void enviarMensagemSlack(String mensagem) throws IOException, SlackApiException {
        if (CODIGO == null || CANAL == null) {
            LOGGER.error("Token ou Channel ID não configurados. Verifique as variáveis de ambiente SLACK_BOT_CODIGO e SLACK_CANAL.");
            return; // Ou lance uma exceção
        }

        MethodsClient methods = slack.methods(CODIGO);
        ChatPostMessageRequest request = ChatPostMessageRequest.builder()
                .channel(CANAL)
                .text(mensagem)
                .build();

        LOGGER.info("Enviando mensagem para o Slack: {}", mensagem);

        try {
            ChatPostMessageResponse response = methods.chatPostMessage(request);

            if (response.isOk()) {
                LOGGER.info("Mensagem enviada com sucesso! Timestamp: {}", response.getTs());
            } else {
                LOGGER.error("Erro ao enviar mensagem para o Slack: {}", response.getError());
            }
        } catch (IOException e) {
            LOGGER.error("Erro de IO ao enviar mensagem para o Slack: {}", e.getMessage(), e);
            throw e; // Relança a exceção para ser tratada na classe chamadora
        } catch (SlackApiException e) {
            LOGGER.error("Erro de API do Slack ao enviar mensagem: {}", e.getMessage(), e);
            throw e; // Relança a exceção para ser tratada na classe chamadora
        }
    }
}