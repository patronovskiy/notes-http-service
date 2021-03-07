package com.patronovskiy.notesHTTPService.ApiTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patronovskiy.notesHTTPService.domain.AppVariables;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;


//тесты для POST запроса (сохранение заметок)
@SpringBootTest
@AutoConfigureMockMvc
public class SaveNoteTest {

    @Autowired
    private MockMvc mockMvc;

    //количество символов, которое берется из content, если нет поля title в запросе
    //задано в application.properties
    @Value("${chars-number}")
    int charsNumber;

    //путь к переменным приложения
    @Value("${variables-path}")
    String pathToVariables;

    //случай 1: нет поля title и длина поля content >= charsNumber
    String expectedContent1 = "test content 2 12345678910 123456789";
    String expectedTitle1 = expectedContent1.substring(0, charsNumber);
    String json1 = "{\"content\":\"" + expectedContent1 +"\"}";

    //случай 2: нет поля title и длина поля content < charsNumber
    String expectedContent2 = "test";
    String expectedTitle2 =
            charsNumber > expectedContent2.length() ? expectedContent2.substring(0, charsNumber) : expectedContent2;
    String json2 = "{\"content\":\"" + expectedContent2 +"\"}";

    //случай 3: заданы оба поля
    String expectedTitle3 = "title 3";
    String expectedContent3 = "this is expected content 3";
    String json3 = "{\"title\":\"" + expectedTitle3 + "\", \"content\":\"" + expectedContent3 +"\"}";

    //случай 4: не передано поле content - ошибка
    String json4 = "{\"title\":\"will be exception\"}";

    @Test
    public void shouldReturnFullNoteInJson() throws Exception {

        //извлекаем список существующих id на данный момент
        ObjectMapper objectMapper = new ObjectMapper();
        AppVariables appVariables = objectMapper.readValue(new File(pathToVariables), AppVariables.class);
        //последний использованный id - вновь создаваемые заметки имеют id, следующий по порядку
        Long lastId = appVariables.getId();

        //при экранировании кавычек слэши остаются в substring в методе containsString()
        //поэтому подставляем кавычки через переменную
        final char quote = (char) 34;

        //проверяем случай 1
        this.mockMvc.perform(post("/notes")
                .content(json1)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                //ожидаем статус 200
                .andExpect(status().isOk())
                //ожидаем, что определен id
                .andExpect(content().string(containsString(quote+"id"+quote+":")))
                //ожидаем, что title выставлен (кол-во символов в файле application.properties)
                .andExpect(content().string(containsString(quote+"title"+quote+":"+quote+""+expectedTitle1)))
                //ожидаем, что content соответсвует переданному
                .andExpect(content().string(containsString(quote+"content"+quote+":"+quote+""+expectedContent1)));


        //проверяем случай 2
        this.mockMvc.perform(post("/notes")
                .content(json2)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                //ожидаем статус 200
                .andExpect(status().isOk())
                //ожидаем, что определен id
                .andExpect(content().string(containsString(quote+"id"+quote+":")))
                //ожидаем, что title выставлен (кол-во символов в файле application.properties)
                .andExpect(content().string(containsString(quote+"title"+quote+":"+quote+""+expectedTitle2)))
                //ожидаем, что content соответсвует переданному
                .andExpect(content().string(containsString(quote+"content"+quote+":"+quote+""+expectedContent2)));

        //проверяем случай 3
        this.mockMvc.perform(post("/notes")
                .content(json3)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                //ожидаем статус 200
                .andExpect(status().isOk())
                //ожидаем, что определен id
                .andExpect(content().string(containsString(quote+"id"+quote+":")))
                //ожидаем, что title выставлен (кол-во символов в файле application.properties)
                .andExpect(content().string(containsString(quote+"title"+quote+":"+quote+""+expectedTitle3)))
                //ожидаем, что content соответсвует переданному
                .andExpect(content().string(containsString(quote+"content"+quote+":"+quote+""+expectedContent3)));

        //удаляем заметки после тестов
        this.mockMvc.perform(delete("/notes/" + (lastId + 1)))
                //ожидаем статус 200
                .andExpect(status().isOk());
        this.mockMvc.perform(delete("/notes/" + (lastId + 2)))
                //ожидаем статус 200
                .andExpect(status().isOk());
        this.mockMvc.perform(delete("/notes/" + (lastId + 3)))
                //ожидаем статус 200
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnBadRequestResponse() throws Exception {
        //проверяем случай 4
        this.mockMvc.perform(post("/notes")
                .content(json4)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                //ожидаем код ошибки bad request (400)
                .andExpect(status().isBadRequest());
    }

}
