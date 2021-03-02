package com.patronovskiy.notesHTTPService.domain;

//класс заметки, приходящей по запросу для сохранения
public class RequestNote extends Note {
    private String title;       //имя заметки
    private String content;     //содержание заметки

    public RequestNote(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public RequestNote() {
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}
