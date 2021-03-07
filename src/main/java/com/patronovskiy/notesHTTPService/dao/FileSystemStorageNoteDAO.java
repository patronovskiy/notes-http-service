package com.patronovskiy.notesHTTPService.dao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patronovskiy.notesHTTPService.domain.AppVariables;
import com.patronovskiy.notesHTTPService.domain.Note;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;

//класс доступа к заметкам, хранящимся в файловой системе
public class FileSystemStorageNoteDAO implements NoteDAO {

    //МЕТОДЫ ДЛЯ РАБОТЫ С ЗАМЕТКАМИ В ФАЙЛОВОМ ХРАНИЛИЩЕ

    //метод для сохранения заметки
    //принимает заметку, путь к директории, куда ее нужно сохранить,
    //количество символов тектса заметки, которые сохраняются в названии, если название не указано
    //путь к файлу со значениями переменных для работы приложения
    @Override
    public Note saveNote(Note note, String fileStoragePath, int charsNumber, String pathToVariables) {

        long id = getNewId(pathToVariables);

        try {
            note.checkAndSetTitle(charsNumber);
            note.setId(id);
            ObjectMapper mapper = new ObjectMapper();
            //проверяем, существует ли директория
            //создать директорию, если не существует
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

    //метод для чтения заметки по id из заданной директории
    //имя заметки совпадает с id
    @Override
    public Note getNoteById(long id, String fileStorageDirectory) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            Note note = objectMapper.readValue(new File(fileStorageDirectory +id + ".json"), Note.class);
            return note;
        } catch (IOException exception) {
            System.out.println("Проблема при чтении заметки по id");
        }
        return null;
    }

    //метод для обновления заметок
    @Override
    public Note updateNote(Note note, String fileStorageDirectory) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(fileStorageDirectory + note.getId() + ".json"), note);
        } catch (IOException exception) {
            System.out.println("Проблема при перезаписывании заметки");
        }
        return null;
    }

    //метод для получения всех заметок списком
    @Override
    public ArrayList<Note> getAllNotes(String fileStoragePath, String pathToVariables) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            //получаем список id всех заметок
            ArrayList<Long> ids = objectMapper.readValue(new File(pathToVariables), AppVariables.class).getIdList();
            //обходим список по id и сохраняем в список заметок
            ArrayList<Note> notes = new ArrayList<>();
            for (Long id : ids) {
                Note note = getNoteById(id, fileStoragePath);
                notes.add(note);
            }
            return notes;
        } catch (IOException exception) {
            System.out.println("Проблема при поиске заметок");
        }
        return null;
    }

    //метод для полученис списка заметок, содержащих текст запроса query
    //в названии или тексте заметки
    @Override
    public ArrayList<Note> getNotesByQuery(String query, String fileStoragePath, String pathToVariables) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            //получаем список id всех заметок
            ArrayList<Long> ids = objectMapper.readValue(new File(pathToVariables), AppVariables.class).getIdList();
            //обходим список по id и сохраняем в список заметок
            ArrayList<Note> notes = new ArrayList<>();
            for (Long id : ids) {
                Note note = getNoteById(id, fileStoragePath);
                if(note.getTitle().contains(query) || note.getContent().contains(query)) {
                    notes.add(note);
                }
            }
            return notes;
        } catch (IOException exception) {
            System.out.println("Проблема при поиске заметок по текстовому запросу");
        }
        return null;
    }

    //метод для удаления заметки по id
    @Override
    public void deleteNote(long id, String fileStoragePath, String pathToVariables) {
            try {
                File note = new File(fileStoragePath + id + ".json");
                if (note.delete() == false) {
                    System.out.println("Файл не может быть удален");
                    throw new FileNotFoundException(" заметка с id=" + id + " не найдена");
                }
                ObjectMapper objectMapper = new ObjectMapper();
                AppVariables appVariables = objectMapper.readValue(new File(pathToVariables), AppVariables.class);
                appVariables.removeNoteFromList(id);
                objectMapper.writeValue(new File(pathToVariables), appVariables);
            } catch (IOException exception) {
                System.out.println("Проблема при удалении файла");
                exception.printStackTrace();
            }
    }

    //ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ

    //метод для получения и учета нового id
    private long getNewId(String pathToVariables) {
        long id = -1;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            //читаем значение текущего id из файла и берем следующее
            AppVariables appVariables = objectMapper.readValue(new File(pathToVariables), AppVariables.class);
            id = appVariables.incrementAndGetId();
            appVariables.addNoteIdToList(id);
            //сохраняем значение id в файл с переменными приложения
            objectMapper.writeValue(new File(pathToVariables), appVariables);

        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return id;
    }
}
