package com.patronovskiy.notesHTTPService.ApiTest;

import com.patronovskiy.notesHTTPService.controllers.NotesController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NotesControllerTest {
    @Autowired
    private NotesController notesController;

    @Test
    public void test() throws Exception {
            assertThat(notesController).isNotNull();
    }

    //todo проверить запросы на несуществующие эндпойнты
}