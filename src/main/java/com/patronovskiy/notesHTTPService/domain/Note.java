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

    public Note() {
    }

    //метод для проверки наличия поля title
    //при его отсутвии - устанавливаем заданное количество символов из поля content
    public void checkAndSetTitle(int charsNumber) {
        if (this.title == null) {
            if (this.content.length() >= charsNumber) {
                this.title = content.substring(0, charsNumber);
            } else {
                this.title = this.content;
            }
        }
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
