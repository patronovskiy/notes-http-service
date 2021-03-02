package com.patronovskiy.notesHTTPService.controllers;

import com.patronovskiy.notesHTTPService.dao.FileSystemStorageNoteDAO;
import com.patronovskiy.notesHTTPService.dao.NoteDAO;
import com.patronovskiy.notesHTTPService.domain.Note;
import com.patronovskiy.notesHTTPService.domain.RequestNote;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

//метод-контроллер для endpoint-а работы с заметками "/notes"
@RestController
@RequestMapping("/notes")
public class NotesController {

    //объект доступа к заметкам
    NoteDAO noteDAO = new FileSystemStorageNoteDAO();

    //путь к директории для хранения файлов
    @Value("${files-storage-path}")
    String fileStoragePath;

    //количество символов, которые переходят в название из текста
    @Value("${chars-number}")
    int charsNumber;

    //путь к переменным приложения
    @Value("${variables-path}")
    String pathToVariables;

    //todo возвращать не заметку, а ResponseEntity??
    //метод, сохраняющий заметку
    @PostMapping()
    public ResponseEntity saveNote(@RequestBody RequestNote requestNote) {
        //проверяем запрос
        if(requestNote == null || requestNote.getContent() == null) {
            return ResponseEntity.badRequest().body("Неверный запрос");
        }

        //если в порядке, сохраняем заметку и возвращаем ответ с заметкой и кодом 200
        Note note = noteDAO.save(requestNote, fileStoragePath, charsNumber, pathToVariables);
        return ResponseEntity.ok(note);
    }

    @GetMapping("/{id}")
    public Note getNoteById(long id) {
        //todo
        return null;
    }

    @GetMapping()
    public ArrayList<Note> getNotes() {
        //todo
        return null;
    }

    @PutMapping("/{id}")
    public void updateNote() {
       //todo
    }

    @DeleteMapping("/{id}")
    public void deleteNote() {
        //todo
    }

}