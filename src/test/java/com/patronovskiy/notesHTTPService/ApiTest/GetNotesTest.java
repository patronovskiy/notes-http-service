package com.patronovskiy.notesHTTPService.ApiTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patronovskiy.notesHTTPService.domain.AppVariables;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

//тесты для GET запроса (получение списка всех заметок или заметок по запросу)
@SpringBootTest
@AutoConfigureMockMvc
public class GetNotesTest {

    @Autowired
    MockMvc mockMvc;

    //путь к переменным приложения
    @Value("${variables-path}")
    String pathToVariables;

    //при экранировании кавычек слэши остаются в substring в методе containsString()
    //поэтому подставляем кавычки через переменную
    final char quote = (char) 34;

    @Test
    public void shouldReturnAllNotesInJson() throws Exception{

        //извлекаем список существующих id на данный момент
        ObjectMapper objectMapper = new ObjectMapper();
        AppVariables appVariables = objectMapper.readValue(new File(pathToVariables), AppVariables.class);
        ArrayList<Long> allIds = appVariables.getIdList();

        //случай 1 - возвращаются заметки по всем существующим id
        for (long id : allIds) {
            this.mockMvc.perform(get("/notes"))
                    .andDo(print())
                    //ожидаем статус 200
                    .andExpect(status().isOk())
                    //ожидаем, что в ответе пришла заметка с заданным id и полями title и content
                    .andExpect(content().string(containsString(quote+"id"+quote+":"+id)));
        }
    }

    @Test
    public void shouldReturnNotesByQueryInJson() throws Exception {
        //случай 2 - возвращаются заметки по запросу query

        //извлекаем список существующих id на данный момент
        ObjectMapper objectMapper = new ObjectMapper();
        AppVariables appVariables = objectMapper.readValue(new File(pathToVariables), AppVariables.class);
        //последний использованный id - вновь создаваемые заметки имеют id, следующий по порядку
        Long lastId = appVariables.getId();

        //сохраняем заметки с запросом в title и content
        //в запросе указываем дату и время теста, чтобы не отрабатывали другие заметки
        String query = "mock testing query " + LocalDate.now() + LocalTime.now();
        String testJson1 = "{\"title\":\"" + query + "\", \"content\":\"query test content\"}";
        String testJson2 = "{\"title\":\"" + "query test title" + "\", \"content\":\"this is " + query +"\"}";
        this.mockMvc.perform(post("/notes")
                .content(testJson1)
                .contentType(MediaType.APPLICATION_JSON));
        this.mockMvc.perform(post("/notes")
                .content(testJson2)
                .contentType(MediaType.APPLICATION_JSON));

        //ищем заметки по запросу
        this.mockMvc.perform(get("/notes").param("query", query))
                .andDo(print())
                //ожидаем статус 200
                .andExpect(status().isOk())
                //ожидаем, что найдены обе заметки по запросу
                .andExpect(content().string(containsString(quote+"title"+quote+":"+quote+""+query)))
                .andExpect(content().string(containsString(quote+"content"+quote+":"+quote+"query test content")))
                .andExpect(content().string(containsString(quote+"title"+quote+":"+quote+"query test title")))
                .andExpect(content().string(containsString(quote+"content"+quote+":"+quote+"this is " + query)));

        //удаляем заметки после тестов
        this.mockMvc.perform(delete("/notes/" + (lastId + 1)))
                //ожидаем статус 200
                .andExpect(status().isOk());
        this.mockMvc.perform(delete("/notes/" + (lastId + 2)))
                //ожидаем статус 200
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnNotFoundByQueryResponse() throws Exception {
        //случай 3 - заметки по запросу нет, возвращаем код 404
        String notExistingQuery = "Такой заметки нет " + LocalDate.now() + " " + LocalTime.now();
        this.mockMvc.perform(get("/notes").param("query", notExistingQuery))
                .andDo(print())
                //ожидаем статус Not Found 404
                .andExpect(status().isNotFound());
    }

}
