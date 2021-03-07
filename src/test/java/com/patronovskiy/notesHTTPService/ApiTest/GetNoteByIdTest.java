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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

//тест медода для получения заметки по id (GET)
@SpringBootTest
@AutoConfigureMockMvc
public class GetNoteByIdTest {

    @Autowired
    private MockMvc mockMvc;

    //путь к переменным приложения
    @Value("${variables-path}")
    String pathToVariables;

    //при экранировании кавычек слэши остаются в substring в методе containsString()
    //поэтому подставляем кавычки через переменную
    final char quote = (char) 34;

    @Test
    public void shouldReturnNoteInJsonById() throws Exception{
        //предварительно создаем заметку
        String content = "first content " + LocalDate.now() + LocalTime.now();
        String testJson = "{\"title\":\"" + "title 1" + "\", \"content\":\"" + content +"\"}";
        this.mockMvc.perform(post("/notes")
                .content(testJson)
                .contentType(MediaType.APPLICATION_JSON))
                //ожидаем, что статус OK 200
                .andExpect(status().isOk());

        //извлекаем список существующих id на данный момент
        //id этой заметки - последний сохраненный id
        ObjectMapper objectMapper = new ObjectMapper();
        AppVariables appVariables = objectMapper.readValue(new File(pathToVariables), AppVariables.class);
        Long validId = appVariables.getId();

        //проверяем случай 1 - получение заметки по валидному id
        this.mockMvc.perform(get("/notes/"+ validId))
                .andDo(print())
                //ожидаем статус 200
                .andExpect(status().isOk())
                //ожидаем, что в ответе пришла заметка с заданным id и полями title и content
                .andExpect(content().string(containsString(quote+"id"+quote+":"+validId)))
                .andExpect(content().string(containsString(quote+"title"+quote+":")))
                .andExpect(content().string(containsString(quote+"content"+quote+":")));

        //удаляем заметку после теста
        this.mockMvc.perform(delete("/notes/" + validId))
                //ожидаем статус 200
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnBadRequestResponse() throws Exception {
        //проверяем случай 2 - получение заметки по невалидному id (отрицательное число) - ошибка bad request
        String invalidId1 = "-1";
        this.mockMvc.perform(get("/notes/"+ invalidId1))
                .andDo(print())
                //ожидаем статус BadRequest 400
                .andExpect(status().isBadRequest());

        //проверяем случай 3 - получение заметки по невалидному id (не число) - ошибка bad request
        String invalidId2 = "BadId";
        this.mockMvc.perform(get("/notes/"+ invalidId2))
                .andDo(print())
                //ожидаем статус BadRequest 400
                .andExpect(status().isBadRequest());
    }

    public void shouldReturnNotFoundResponse() throws Exception {
        //проверяем случай 4 - заметка не найдена - ошибка not found
        //берем id, не входящий в список
        ObjectMapper objectMapper = new ObjectMapper();
        AppVariables appVariables = objectMapper.readValue(new File(pathToVariables), AppVariables.class);
        Long notExistingId = appVariables.getId() + 10;

        this.mockMvc.perform(get("/notes/"+ notExistingId))
                .andDo(print())
                //ожидаем статус NotFound 404
                .andExpect(status().isNotFound());
    }

}
