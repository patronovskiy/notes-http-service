package com.patronovskiy.notesHTTPService.domain;

//класс, представляющий заметку
public class Note {
    private Long id;            //id заметки
    private String title;       //имя заметки
    private String content;     //содержание заметки

    public Note(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    //если имя заметки не передано, берем для него заданное количество символов из текста
    public Note(Long id, String content, int charsNumber) {
        this.id = id;
        this.content = content;
        this.title = content.substring(0, charsNumber);
    }

    public Note() {
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
}