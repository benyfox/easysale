package com.example.onlinemarketplace.service.impl;

import com.example.onlinemarketplace.dto.request.AssistantRequest;
import com.example.onlinemarketplace.dto.response.AssistantResponse;
import com.example.onlinemarketplace.dto.response.SellerDto;
import com.example.onlinemarketplace.model.Order;
import com.example.onlinemarketplace.model.Product;
import com.example.onlinemarketplace.model.Seller;
import com.example.onlinemarketplace.service.AssistantService;
import com.example.onlinemarketplace.service.OrderService;
import com.example.onlinemarketplace.service.ProductService;
import com.example.onlinemarketplace.service.SellerService;
import de.kherud.llama.InferenceParameters;
import de.kherud.llama.LlamaModel;
import de.kherud.llama.args.MiroStat;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Log4j2
public class AssistantServiceImpl implements AssistantService {

    private final LlamaModel model;
    private final OrderService orderService;
    private final ProductService productService;
    private final SellerService sellerService;

    @Override
    public void chatStream(AssistantRequest requestBody, HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.TEXT_EVENT_STREAM_VALUE);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setCharacterEncoding("UTF-8");

        StringBuilder message = new StringBuilder();
        message.append("Это диалог между пользователем и ассистентом EasySale, дружелюбным чат-ботом.\n")
                .append("Ассистент услужлив, добр, честен, умеет писать и всегда отвечает на любые запросы ")
                .append("немедленно и точно, а так же помогает проводить анализ и давать советы по финансовым ")
                .append("данным о продажах пользователя и его конкурентов.\n")
                .append("Ассистент отвечает на конкретные вопросы, которые ему задают.\n")
                .append("При ответе Ассистент: не указывай имя отвечающего.\n")
                .append("Ассистент не генерирует ответы за пользователя и отвечает ")
                .append("только на вопросы, заданные пользователем.\n")
                .append("Ассистент: Привет! Я ваш персональный финансовый ассистент EasySale. Чем могу помочь?\n");

        requestBody.getMessages().forEach(
                chatMessage -> message.append(
                        Objects.equals(chatMessage.getRole(), "user") ? "Пользователь" : "Ассистент")
                        .append(": ")
                        .append(chatMessage.getText())
                        .append("\n")
        );

        InferenceParameters inferParams = new InferenceParameters(message.toString())
                .setTemperature(0.3f)
                .setPenalizeNl(true)
                .setMiroStat(MiroStat.V2)
                .setStopStrings("Пользователь:");

        try {
            for (LlamaModel.Output output : model.generate(inferParams)) {
                String chunk = output.toString();

                try {
                    response.getWriter().write("data: " + "{\"text\": \"" + chunk + " \"}\n\n");
                    response.flushBuffer();
                } catch (ClientAbortException e) {
                    System.err.println("Client closed connection: " + e.getMessage());
                    break;
                } catch (IOException e) {
                    log.error("IO Exception: ", e);
                    response.getWriter().write("data: " + "{\"error\": \"" + e.getMessage() + " \"}\n\n");
                    response.flushBuffer();
                }
            }
        } catch (Exception e) {
            log.error("Error while processing chat stream: ", e);
            response.getWriter().write("data: " + "{\"error\": \"" + e.getMessage() + " \"}\n\n");
            response.flushBuffer();
        } finally {
            response.getWriter().close();
        }
    }

    @Override
    public void analyse(
            HttpServletResponse response,
            String username
    ) throws IOException {
        response.setContentType(MediaType.TEXT_EVENT_STREAM_VALUE);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setCharacterEncoding("UTF-8");

        StringBuilder message = new StringBuilder();
        message.append("Это диалог между пользователем и ассистентом EasySale, дружелюбным чат-ботом.\n")
                .append("Ассистент услужлив, добр, честен, умеет писать и всегда отвечает на любые запросы ")
                .append("немедленно и точно, а так же помогает проводить анализ и давать советы по финансовым ")
                .append("данным о продажах пользователя и его конкурентов.\n")
                .append("Ассистент отвечает на конкретные вопросы, которые ему задают.\n")
                .append("При ответе Ассистент: не указывай имя отвечающего.\n")
                .append("Ассистент не генерирует ответы за пользователя и отвечает ")
                .append("только на вопросы, заданные пользователем.\n")
                .append("Ассистент: Привет! Я ваш персональный финансовый ассистент EasySale. Чем могу помочь?\n");

        Seller seller = sellerService._findByUserUsername(username);
        List<Product> products = productService.getProductsBySeller(seller);
        List<Order> orders = orderService.findBySeller(seller);

        message.append("Пользователь: Вот список моих товаров, пожалуйста, проанализируй мои показатели:\n");
        products.forEach(product -> {
            long ordersCount = orders.stream()
                    .filter(order -> order.getProducts().contains(product))
                    .count();

            if (ordersCount == 0) {
                return;
            }

            message.append("Продукт: ")
                    .append(product.getName())
                    .append(", цена: ")
                    .append(product.getPrice())
                    .append(", продано: ")
                    .append(ordersCount)
                    .append("\n");
        });

        log.info("Analyse message: {}", message.toString());

        InferenceParameters inferParams = new InferenceParameters(message.toString())
                .setTemperature(0.3f)
                .setPenalizeNl(true)
                .setMiroStat(MiroStat.V2)
                .setStopStrings("Пользователь:");

        try {
            for (LlamaModel.Output output : model.generate(inferParams)) {
                String chunk = output.toString();

                try {
                    response.getWriter().write("data: " + "{\"text\": \"" + chunk + " \"}\n\n");
                    response.flushBuffer();
                } catch (ClientAbortException e) {
                    System.err.println("Client closed connection: " + e.getMessage());
                    break;
                } catch (IOException e) {
                    log.error("IO Exception: ", e);
                    response.getWriter().write("data: " + "{\"error\": \"" + e.getMessage() + " \"}\n\n");
                    response.flushBuffer();
                }
            }
        } catch (Exception e) {
            log.error("Error while processing chat stream: ", e);
            response.getWriter().write("data: " + "{\"error\": \"" + e.getMessage() + " \"}\n\n");
            response.flushBuffer();
        } finally {
            response.getWriter().close();
        }
    }
}