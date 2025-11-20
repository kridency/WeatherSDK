# Simple Weather SDK

Library that can be used by other developers to easily access a weather API and retrieve weather data for a given location.
Includes classes to retrieve data from open `openweathermap.org`.

## Requirements

- **Java 17 or higher**: Library requires Java 17 as the minimum runtime version
- **Maven 3.x or higher**: Library requires Maven 3.x+ for building from source

## Features

- **Weather data in JSON format**: offers custom JSON response according to API Key and city name provided:
```json
{
"weather": {
"main": "Clouds",
"description": "scattered clouds",
},
"temperature": {
"temp": 269.6,
"feels_like": 267.57,
},
"visibility": 10000,
"wind": {
"speed": 1.38,
},
"datetime": 1675744800,
"sys": {
"sunrise": 1675751262,
"sunset": 1675787560
},
"timezone": 3600,
"name": "Zocca",
}
```
- **Data transfer object types to map JSON**: offers type model to support object mapping

## Build & Install

Checkout sources from github repository:
```shell
# git clone https://github.com/kridency/WeatherSDK.git
```

In order to make package including dependencies run maven in library directory:
```shell
# mvn package
```

Copy package jar to your project `/lib` directory and include dependency in `pom.xml` to use objects and methods from the library:
```xml
<dependency>
   <groupId>com.example</groupId>
   <artifactId>weather-sdk</artifactId>
   <version>1.0.0</version>
   <scope>system</scope>
   <systemPath>${project.basedir}/lib/weather-sdk-1.0.0-SNAPSHOT.jar</systemPath>
   <type>jar</type>
</dependency>
```
## Проверка API

Для проверки API данного проекта вы можете использовать разные инструменты:

* [cURL](https://curl.se/) - консольная утилита.
* [Postman](https://www.postman.com/) - приложения для отправки запросов и тестирования API
* [Insomnia](https://insomnia.rest/) - еще одно приложения для тестирования API

Ниже приведены запросов в формате cURL, вы можете данные из запросов перенести в любое другое приложение. Например, в
Postman ответы будут такими:

* **Просмотр статистики и другой служебной информации о состоянии поисковых индексов и самого движка**

```Postman
GET /api/statistics
```

Успешный ответ:

```json
{
  "result": true,
  "statistics": {
    "total": {
      "sites": 3,
      "pages": 14,
      "lemmas": 2925,
      "indexing": true
    },
    "detailed": [
      {
        "url": "https://ipfran.ru",
        "name": "Ipfran",
        "status": "INDEXED",
        "statusTime": "2023-11-02T02:23:46",
        "error": "",
        "pages": 5,
        "lemmas": 1084
      },
      {
        "url": "https://www.svetlovka.ru",
        "name": "Svetlovka",
        "status": "INDEXED",
        "statusTime": "2023-11-02T09:01:12",
        "error": "",
        "pages": 4,
        "lemmas": 1417
      },
      {
        "url": "https://www.playback.ru",
        "name": "PlayBack",
        "status": "INDEXED",
        "statusTime": "2023-11-02T01:32:09",
        "error": "",
        "pages": 5,
        "lemmas": 424
      }
    ]
  }
}
```

* **Поиск страниц по переданному поисковому запросу**

```Postman
POST /api/search2?site=https://www.svetlovka.ru&offset=0&limit=10&query=Порядок пользования помещением 
(Body параметры: 
query=Порядок пользования помещением 
site=https://www.svetlovka.ru
offset=0 
limit=10)
```

Успешный ответ:

```json
{
  "result": true,
  "count": 2,
  "data": [
    {
      "site": "https://www.svetlovka.ru",
      "siteName": "Svetlovka",
      "uri": "https://www.svetlovka.ru/about/dokumenty/",
      "title": "Документы",
      "snippet": "...ителей на платные услуги 69 Кб Порядок предоставления льгот 212 Кб Об установлении стоимости аренды <b>помещений</b> Государственного бюджетного учреждения культуры города Москвы \"Центральная городская молодежная биб...<br>... Изменения в приказе о платных мероприятиях 199 Кб Приказ о внесении изменений в прейскурант 288 Кб <b>Порядок</b> уничтожения персональных данных 322 Кб Перечень реализуемых товаров 206 Кб Правила рассмотрения зап...<br>... персональных данных 460 Кб Положение об обработке персональных данных пользователей 596 Кб Правила <b>пользования</b> с Приложениями 959 Кб Планы и отчеты План по улучшению качества работы 2020 229 Кб Презентация по и...<br>",
      "relevance": 1.0
    },
    {
      "site": "https://www.svetlovka.ru",
      "siteName": "Svetlovka",
      "uri": "https://www.svetlovka.ru",
      "title": "Центральная городская молодежная библиотека им. М. А. Светлова",
      "snippet": "...2023 День Светлова! 23.05.2023 Ежегодная акция \"Библионочь\" в Светловке 23.03.2023 Предоставление в <b>пользование</b> пианино 23.03.2023 Московская неделя детской книги 11.03.2023 Девичник \"С заботой о себе\" в Светлов...<br>",
      "relevance": 0.16666667
    }
  ],
  "error": null
}
```

Пустой ввод:

```json
{
  "result": false,
  "error": "Задан пустой поисковый запрос"
}

```

* **Запуск полной индексации страниц из файла конфигурации**

```Postman
GET /api/startIndexing
```

Успешный ответ:

```json
{
  "result": true
}
```

* **Остановка текущей индексации (остановка процесса рекурсивного обхода URL. Проверенные страницы сохранятся в БД)**

```Postman
GET /api/stopIndexing
```

Успешный ответ:

```json
{
  "result": true
}
```

* **Добавление или обновление отдельной страницы (страница должна принадлежать корневому сайту из конфигурации)**

```Postman
POST /api/indexPage  
(Body параметр: url=https://www.playback.ru/dostavka.html)
```

Успешный ответ:

```json
{
  "result": true
}
```
Ошибка:

```json
{
  "result": false,
  "error": "Данная страница находится за пределами сайтов, указанных в конфигурационном файле"
}

```


## Принцип работы приложения:

1. В конфигурационном файле перед запуском приложения задаются
   адреса сайтов, по которым движок должен осуществлять поиск.

2. При выборе команды  "GET /api/startIndexing" поисковый движок самостоятельно обходит все страницы
   заданных сайтов и индексирует их так, чтобы потом находить наиболее релевантные страницы по любому
   поисковому запросу.
   Найденные URL страниц дополнительно сохраняются в текстовые файлы в корне проекта. 1 файл = 1 сайту. Url имеют
   иерархическую структуру.
   Примечание!!! В Index service классе количество страниц для каждого сайта искусственно ограничено до 5
   для ускорения процесса, иначе индексация (поиск лемм) будет занимать несколько дней. Для 5 страниц одного сайта время
   индексакции будет занимать около 2 часов.

3. Пользователь присылает запрос через API движка. Запрос — это набор слов, по которым нужно найти страницы сайта.

4. Запрос определённым образом трансформируется в список слов, переведённых в базовую форму. Например, для существительных —
   именительный падеж, единственное число.

5. В индексе ищутся страницы, на которых встречаются все эти слова.

6. Результаты поиска ранжируются, сортируются и отдаются пользователю.

