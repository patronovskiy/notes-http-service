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
import java.time.LocalDate;
import java.time.LocalTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;


//тесты для PUT запроса (редактирование заметок)
@SpringBootTest
@AutoConfigureMockMvc
public class UpdateNoteTest {

    @Autowired
    private MockMvc mockMvc;

    //путь к переменным приложения
    @Value("${variables-path}")
    String pathToVariables;

    //при экранировании кавычек слэши остаются в substring в методе containsString()
    //поэтому подставляем кавычки через переменную
    final char quote = (char) 34;

    @Test
    public void shouldReturnUpdatedNoteInJson() throws Exception {
        //случай 1 - заметка редактируется по id и возвращается в ответе
        //сохраняем заметку и проверяем, что она содержит content1
        String content1 = "first content " + LocalDate.now() + LocalTime.now();
        String testJson1 = "{\"title\":\"" + "title 1" + "\", \"content\":\"" + content1 +"\"}";
        this.mockMvc.perform(post("/notes")
                .content(testJson1)
                .contentType(MediaType.APPLICATION_JSON));

        //извлекаем список существующих id на данный момент
        //id этой заметки - последний сохраненный id
        ObjectMapper objectMapper = new ObjectMapper();
        AppVariables appVariables = objectMapper.readValue(new File(pathToVariables), AppVariables.class);
        Long id = appVariables.getId();

        this.mockMvc.perform(get("/notes/" + id))
                .andDo(print())
                //ожидаем статус 200
                .andExpect(status().isOk())
                //ожидаем, что найдены обе заметки по запросу
                .andExpect(content().string(containsString(quote+"content"+quote+":"+quote+ content1)));

        //редактируем заметку по id - должна вернуться отредактированная заметкаб статус ОК 200
        String content2 = "second content " + LocalDate.now() + LocalTime.now();
        String testJson2 = "{\"title\":\"" + "title 2" + "\", \"content\":\"" + content2 +"\"}";
        this.mockMvc.perform(put("/notes/" + id)
                .content(testJson2)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                //ожидаем статус 200
                .andExpect(status().isOk())
                //ожидаем, что вернется заметка с новым content
                .andExpect(content().string(containsString(quote+"content"+quote+":"+quote+ content2)));

        //проверяем, что заметка сохранилась
        this.mockMvc.perform(get("/notes/" + id))
                .andDo(print())
                //ожидаем статус 200
                .andExpect(status().isOk())
                //ожидаем, что вернется заметка с новым content
                .andExpect(content().string(containsString(quote+"content"+quote+":"+quote+ content2)));

        //удаляем заметку после теста
        this.mockMvc.perform(delete("/notes/" + id))
                //ожидаем статус 200
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnNotFoundResponse() throws Exception {
        //случай 2 - запрос на редактирование несуществующей заметки
        ObjectMapper objectMapper = new ObjectMapper();
        AppVariables appVariables = objectMapper.readValue(new File(pathToVariables), AppVariables.class);
        Long id = appVariables.getId();

        this.mockMvc.perform(get("/notes/" + (id+10)))
                .andDo(print())
                //ожидаем статус Not Found 404
                .andExpect(status().isNotFound());
    }

}
