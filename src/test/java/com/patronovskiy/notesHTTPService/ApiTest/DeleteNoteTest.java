package com.patronovskiy.notesHTTPService.ApiTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patronovskiy.notesHTTPService.domain.AppVariables;
import com.patronovskiy.notesHTTPService.domain.ResponseMessages;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;


//тесты для DELETE запроса (удаление заметки)
@SpringBootTest
@AutoConfigureMockMvc
public class DeleteNoteTest {
    @Autowired
    private MockMvc mockMvc;

    //путь к переменным приложения
    @Value("${variables-path}")
    String pathToVariables;

    //при экранировании кавычек слэши остаются в substring в методе containsString()
    //поэтому подставляем кавычки через переменную
    final char quote = (char) 34;

    @Test
    public void shouldDeleteNote() throws Exception {
        //случай 1 - заметка успешно удалена, код ОК 200

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
        Long id = appVariables.getId();

        //проверяем, что заметка ищется по id
        this.mockMvc.perform(get("/notes/"+ id))
                .andDo(print())
                //ожидаем статус 200
                .andExpect(status().isOk())
                //ожидаем, что в ответе пришла заметка с заданным id и полями title и content
                .andExpect(content().string(containsString(quote+"id"+quote+":"+id)));

        //удаляем заметку по id
        this.mockMvc.perform(delete("/notes/" + id))
                //ожидаем статус 200
                .andExpect(status().isOk())
                //ожидаем в ответе сообщение об успешном удалении заметки
                .andExpect(content().string(containsString(ResponseMessages.NOTE_DELETED_MESSAGE.getMessage())));

        //проверяем, что теперь заметка не ищется по id
        this.mockMvc.perform(get("/notes/"+ id))
                .andDo(print())
                //ожидаем статус Not Found 404
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldReturnNotFoundResponse() throws Exception {
        //случай 2 - запрос на удаление по несуществующему id - возвращаем Not Found 404

        //извлекаем список существующих id на данный момент
        ObjectMapper objectMapper = new ObjectMapper();
        AppVariables appVariables = objectMapper.readValue(new File(pathToVariables), AppVariables.class);
        Long id = appVariables.getId();

        //удаляем заметку по id
        this.mockMvc.perform(delete("/notes/" + id + 10))
                //ожидаем статус Not Found 404
                .andExpect(status().isNotFound())
                //ожидаем в ответе сообщение об успешном удалении заметки
                .andExpect(content().string(containsString(ResponseMessages.NOTE_NOT_FOUND_MESSAGE.getMessage())));
    }
}
