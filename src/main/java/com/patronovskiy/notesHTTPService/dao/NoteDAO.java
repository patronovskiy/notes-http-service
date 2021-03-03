package com.patronovskiy.notesHTTPService.dao;

import com.patronovskiy.notesHTTPService.domain.Note;

//интерфейс, декларирующий методы для доступа к заметкам
public interface NoteDAO {
    public Note save(Note note, String fileStoragePath, int charsNumber, String pathToVariables);

    public Note getById(long id, String fileStorageDirectory);

    public Note update(Note note, String fileStorageDirectory);

}
