# notes-http-service
A simple http service for working with notes in json format

##Общее описание
* Работает на порту 8080


## Requests

##Настройки
###application.properties
1. files-storage-path - путь к директории для хранения файлов 
2. variables-path - путь к данным о текущем состоянии хранилища (изменяется программой при работе) 
3. chars-number - количество символов, которые попадают из текста заметки в название при отсутсвии названия
4. server.port - номер порта