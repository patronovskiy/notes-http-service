package com.patronovskiy.notesHTTPService.domain;

public enum ResponseMessages {
    NOTE_NOT_FOUND_MESSAGE("Заметка не найдена"),
    NOTE_DELETED_MESSAGE("Заметка успешно удалена"),
    BAD_REQUEST_MESSAGE("Неверный запрос"),
    INCORRECT_ID_MESSAGE("Некорректный id"),
    INCORRECT_PARAMETERS_MESSAGE("Некорректно переданы параметры"),
    NOTES_BY_QUERY_NOT_FOUND_MESSAGE("Заметки, удовлетворяющие запросу, не найдены");

    private String message;

    ResponseMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
