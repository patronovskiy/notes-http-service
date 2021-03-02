package com.patronovskiy.notesHTTPService.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patronovskiy.notesHTTPService.domain.AppVariables;
import com.patronovskiy.notesHTTPService.domain.FullNote;
import com.patronovskiy.notesHTTPService.domain.Note;
import com.patronovskiy.notesHTTPService.domain.RequestNote;

import java.io.File;
import java.io.IOException;

//класс доступа к заметкам, хранящимся в файловой системе
public class FileSystemStorageNoteDAO implements NoteDAO {

    //метод для сохранения заметки
    //принимает заметку, путь к директории, куда ее нужно сохранить,
    // количество символов тектса заметки, которые сохраняются в названии, если название не указано
    //путь к файлу со значениями переменных для работы приложения
    @Override
    public Note save(Note requestNote, String fileStoragePath, int charsNumber, String pathToVariables) {

        long id = getNewId(pathToVariables);

        try {
            Note note = new FullNote(requestNote, id, charsNumber);
            ObjectMapper mapper = new ObjectMapper();
            //для имени файла берем id и записываем json
            //todo создать директорию, если не существует
            mapper.writeValue(new File(fileStoragePath + note.getId() + ".json"), note);
            System.out.println("файл создан");
            return note;

        } catch (IOException exception1) {
            System.out.println("Проблема при сохранении заметки");
            exception1.printStackTrace();
        } catch (Exception exception2) {
            System.out.println("Проблема при сохранении заметки");
            exception2.printStackTrace();
        }
        return null;
    }


    private long getNewId(String pathToVariables) {
        long id = -1;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            //читаем значение текущего id из файла и берем следующее
            AppVariables appVariables = objectMapper.readValue(new File(pathToVariables), AppVariables.class);
            id = appVariables.incrementAndGetId();
            appVariables.addNoteIdToList(id);
            //сохраняем значение id в файл
            objectMapper.writeValue(new File(pathToVariables), appVariables);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return id;
    }

}
