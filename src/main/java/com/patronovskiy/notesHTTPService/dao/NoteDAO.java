package com.patronovskiy.notesHTTPService.dao;

import com.patronovskiy.notesHTTPService.domain.Note;

//интерфейс, декларирующий методы для доступа к заметкам
public interface NoteDAO {
    public Note save(Note note, String fileStorageDirectory, int charsNumber, String pathToVariables);

}
