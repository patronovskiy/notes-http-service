package com.patronovskiy.notesHTTPService.controllers;

import com.patronovskiy.notesHTTPService.dao.FileSystemStorageNoteDAO;
import com.patronovskiy.notesHTTPService.dao.NoteDAO;
import com.patronovskiy.notesHTTPService.domain.Note;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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


    //метод, сохраняющий заметку
    //В запросе передается body в формате JSON
    //В ответе возвращается новая заметка в формате JSON
    @PostMapping()
    public ResponseEntity saveNote(@RequestBody Note requestNote) {
        //проверяем запрос
        if(requestNote.getContent() == null) {
            return ResponseEntity.badRequest().body("Неверный запрос");
        }

        //если в порядке, сохраняем заметку и возвращаем ответ с заметкой и кодом 200
        Note note = noteDAO.saveNote(requestNote, fileStoragePath, charsNumber, pathToVariables);
        return ResponseEntity.ok(note);
    }

    @GetMapping("/{id}")
    public ResponseEntity getNoteById(@PathVariable long id) {
        Note note = noteDAO.getNoteById(id, fileStoragePath);
        //проверяем, есть ли заметка, если ее нет - возвращаем код 404
        if (note == null) {
            return new ResponseEntity("Заметка не найдена", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(note);
    }

    //метод, возвращающий все заметки, если параметр query не указан
    //или заметки, содержащие текст параметра query в заголовке или тексте заметки
    @GetMapping()
    public ResponseEntity getNotes(HttpServletRequest request) {
        String query = request.getParameter("query");
        if(request.getParameterMap().size() > 1 ||
           (request.getParameterMap().size() == 1 && query == null)) {
            return ResponseEntity.badRequest().body("Неверно переданы параметры");
        } else if (query != null) {
            ArrayList<Note> notesByQuery = noteDAO.getNotesByQuery(query, fileStoragePath, pathToVariables);
            return ResponseEntity.ok(notesByQuery);
        } else {
            ArrayList<Note> notes = noteDAO.getAllNotes(fileStoragePath, pathToVariables);
            return ResponseEntity.ok(notes);
        }
    }

    //метод для редактирования заметки по ее id
    //Для редактирования доступны: заголовок, текст заметки
    //id передается как path-параметр
    //Редактируемые параметры передаются в body запроса в формате JSON
    //todo не возвращать ничего?
    @PutMapping("/{id}")
    public ResponseEntity updateNote(@PathVariable long id, @RequestBody Note requestNote) {
        //ищем заметку в хранилище по id
        Note note = noteDAO.getNoteById(id, fileStoragePath);
        //проверяем, есть ли заметка, если ее нет - возвращаем код 404
        if (note == null) {
            return new ResponseEntity("Заметка не найдена", HttpStatus.NOT_FOUND);
        }
        //проверяем, что в запросе переданы заголовок и/или текст и обновляем заметку
        if (requestNote.getTitle() != null) {
            note.setTitle(requestNote.getTitle());
        }
        if(requestNote.getContent() != null) {
            note.setContent(requestNote.getContent());
        }
        noteDAO.updateNote(note, fileStoragePath);
        return ResponseEntity.ok(note);
    }

    @DeleteMapping("/{id}")
    public void deleteNote() {
        //todo
    }

}
