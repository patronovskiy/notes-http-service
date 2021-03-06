package com.patronovskiy.notesHTTPService.domain;

//перечисление для сообщений в http-ответах
//передаются в теле сообщения, если нет тела json
public enum ResponseMessages {
    NOTE_NOT_FOUND_MESSAGE("Заметка не найдена"),
    NOTE_DELETED_MESSAGE("Заметка успешно удалена"),
    BAD_REQUEST_MESSAGE("Неверный запрос"),
    INCORRECT_ID_MESSAGE("Некорректный id"),
    INCORRECT_PARAMETERS_MESSAGE("Некорректно переданы параметры"),
    NOTES_BY_QUERY_NOT_FOUND_MESSAGE("Заметки, удовлетворяющие запросу, не найдены"),
    ALL_NOTES_DELETED_MESSAGE("Все заметки успешно удалены");

    private String message;

    ResponseMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
