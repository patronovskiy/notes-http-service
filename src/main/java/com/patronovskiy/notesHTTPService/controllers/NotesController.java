package com.patronovskiy.notesHTTPService.controllers;

import com.patronovskiy.notesHTTPService.domain.Note;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

//метод-контроллер для endpoint-а "/notes"
@RestController
@RequestMapping("/notes")
public class NotesController {

    @GetMapping("/${id}")
    public Note getNoteById(long id) {
        //todo
    }

    @GetMapping()
    public ArrayList<Note> getNotes() {
        //todo
    }

    @PostMapping()
    public Note saveNote(Note note) {
        //todo
    }

    @PutMapping("/${id}")
    public void updateNote() {
       //todo
    }

    @DeleteMapping("/${id}")
    public void deleteNote() {
        //todo
    }

}
