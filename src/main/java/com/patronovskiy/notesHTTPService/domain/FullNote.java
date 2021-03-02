package com.patronovskiy.notesHTTPService.domain;

public class FullNote extends Note {
    private Long id;            //id заметки
    private String title;       //имя заметки
    private String content;     //содержание заметки

    public FullNote(Long id, String title, String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    //если имя заметки не передано, берем для него заданное количество символов из текста
    public FullNote(Long id, String content, int charsNumber) {
        this.id = id;
        this.content = content;
        this.title = content.substring(0, charsNumber);
    }

    public FullNote(Note note, Long id, int charsNumber) {
        this.id = id;
        this.content = note.getContent();
        if(note.getTitle() == null) {
            this.title = this.content.substring(0, charsNumber);
        } else {
            this.title = note.getTitle();
        }
    }

    public FullNote() {
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
