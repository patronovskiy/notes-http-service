package com.patronovskiy.notesHTTPService;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patronovskiy.notesHTTPService.domain.Note;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

//класс для тестирования класса Note
public class NoteTest {

    @Test
    void testNote() {
        String fullJson =
                "{" +
                        "\"title\":\"testTitle\"," +
                        "\"content\":\"test content\"" +
                        "}";
        String jsonWithoutTitle =
                "{" +
                        "\"content\":\"test content 2\"" +
                "}";

        ObjectMapper objectMapper = new ObjectMapper();
        try{
            Note noteFull = objectMapper.readValue(fullJson, Note.class);
            Note noteHalf = objectMapper.readValue(jsonWithoutTitle, Note.class);
            noteHalf.checkAndSetTitle(4);
            Note note = new Note(1L, "name", "text");

            assertEquals("testTitle", noteFull.getTitle());
            assertEquals("test content", noteFull.getContent());
            assertEquals("test", noteHalf.getTitle());
            assertEquals("test content 2", noteHalf.getContent());
            assertEquals(1L, note.getId());
            assertEquals("name", note.getTitle());
            assertEquals("text", note.getContent());

        } catch (Exception exception) {
            System.out.println("Проблема при тестировании класса Note");
            exception.printStackTrace();
        }
    }



}
