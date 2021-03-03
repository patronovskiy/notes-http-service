package com.patronovskiy.notesHTTPService.dao;

import com.patronovskiy.notesHTTPService.domain.Note;

import java.util.ArrayList;

//интерфейс, декларирующий методы для доступа к заметкам
public interface NoteDAO {
    Note saveNote(Note note, String fileStoragePath, int charsNumber, String pathToVariables);

    Note getNoteById(long id, String fileStorageDirectory);

    Note updateNote(Note note, String fileStorageDirectory);

    ArrayList<Note> getAllNotes(String fileStoragePath, String pathToVariables);

    ArrayList<Note> getNotesByQuery(String query, String fileStoragePath, String pathToVariables);
}
