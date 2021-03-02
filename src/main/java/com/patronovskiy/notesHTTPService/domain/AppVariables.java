package com.patronovskiy.notesHTTPService.domain;

import java.util.ArrayList;

public class AppVariables {
    private long id;
    private ArrayList<Long> idList;

    public AppVariables(long id, ArrayList idList) {
        this.id = id;
        this.idList = idList;
    }

    public AppVariables() {
    }

    public long getId() {
        return id;
    }

    public ArrayList<Long> getIdList() {
        return idList;
    }

    public long incrementAndGetId() {
        return ++id;
    }

    public void addNoteIdToList(long id) {
        this.idList.add(id);
    }
}
