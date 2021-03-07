package com.patronovskiy.notesHTTPService.domain;

import java.util.ArrayList;

//класс для представления переменных приложения, которые изменяются приложением в процессе работы
//сами переменные хранятся в applicationVariables.json
public class AppVariables {
    //текущий id - последний id, присвоенный заметке
    private long id;
    //список всех действующих присвоенных id
    private ArrayList<Long> idList;

    public AppVariables(long id, ArrayList idList) {
        this.id = id;
        this.idList = idList;
    }

    public AppVariables() {
    }

    //получить текущий id - последний id, присвоенный заметке
    public long getId() {
        return id;
    }

    //получить список всех действующих присвоенных id
    public ArrayList<Long> getIdList() {
        return idList;
    }

    //увеличиваем текущий id на 1 и получаем его
    public long incrementAndGetId() {
        return ++id;
    }

    //добавить id в список
    public void addNoteIdToList(long id) {
        this.idList.add(id);
    }

    //удалить id из списка
    public void removeNoteFromList(Long id) {
        this.idList.remove(id);
    }
}
