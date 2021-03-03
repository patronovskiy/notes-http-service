package com.patronovskiy.notesHTTPService.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patronovskiy.notesHTTPService.domain.AppVariables;
import com.patronovskiy.notesHTTPService.domain.Note;
import org.springframework.web.client.HttpClientErrorException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

//класс доступа к заметкам, хранящимся в файловой системе
public class FileSystemStorageNoteDAO implements NoteDAO {

    //метод для сохранения заметки
    //принимает заметку, путь к директории, куда ее нужно сохранить,
    // количество символов тектса заметки, которые сохраняются в названии, если название не указано
    //путь к файлу со значениями переменных для работы приложения
    @Override
    public Note save(Note note, String fileStoragePath, int charsNumber, String pathToVariables) {

        long id = getNewId(pathToVariables);

        try {
            note.checkAndSetTitle(charsNumber);
            note.setId(id);
            ObjectMapper mapper = new ObjectMapper();
            //проверяем, существует ли директория
            //todo создать директорию, если не существует
            if (!Files.exists(Paths.get(fileStoragePath))) {
                new File(fileStoragePath).mkdirs();
            }
            //для имени файла берем id и записываем json
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


    //метод для чтения заметки по id из заданной директории
    //имя заметки совпадает с id
    @Override
    public Note getById(long id, String fileStorageDirectory) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            Note note = objectMapper.readValue(new File(fileStorageDirectory +id + ".json"), Note.class);
            return note;
        } catch (IOException exception) {
            System.out.println("Проблема при чтении заметки по id");
        }
        return null;
    }

    @Override
    public Note update(Note note, String fileStorageDirectory) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(fileStorageDirectory + note.getId() + ".json"), note);
        } catch (IOException exception) {
            System.out.println("Проблема при перезаписывании заметки");
        }
        return null;
    }
}
