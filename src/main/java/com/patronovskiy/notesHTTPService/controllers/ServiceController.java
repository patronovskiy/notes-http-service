package com.patronovskiy.notesHTTPService.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.patronovskiy.notesHTTPService.dao.FileSystemStorageNoteDAO;
import com.patronovskiy.notesHTTPService.dao.NoteDAO;
import com.patronovskiy.notesHTTPService.domain.AppVariables;
import com.patronovskiy.notesHTTPService.domain.ResponseMessages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

//контроллер для обслуживания и управления сервисом
@RestController
@RequestMapping("/service")
public class ServiceController {

    //ключ для очистки хранилища
    //todo значение в application.properties должно быть недоступно извне
    @Value("${clear-key}")
    int clearKey;

    //путь к директории для хранения файлов
    @Value("${files-storage-path}")
    String fileStoragePath;

    //путь к переменным приложения
    @Value("${variables-path}")
    String pathToVariables;

    NoteDAO noteDAO = new FileSystemStorageNoteDAO();

    //метод для очистки хранилища и сброса сервиса в первоначальное состояние (id обнуляется)
    @GetMapping("/clear")
    public ResponseEntity clearServise(@RequestParam int key) {
        if(key == clearKey) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                AppVariables appVariables = objectMapper.readValue(new File(pathToVariables), AppVariables.class);
                ArrayList<Long> ids = appVariables.getIdList();
                for (Long id : ids) {
                    noteDAO.deleteNote(id, fileStoragePath, pathToVariables);
                }
                //обнуляем переменные
                //здесь нужно либо снова прочитать внешний файл applicationVAriables.json
                //либо очистить список вручную
                appVariables.clear();
                objectMapper.writeValue(new File(pathToVariables), appVariables);
                return ResponseEntity.ok(ResponseMessages.ALL_NOTES_DELETED_MESSAGE.getMessage());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return new ResponseEntity(ResponseMessages.BAD_REQUEST_MESSAGE.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
