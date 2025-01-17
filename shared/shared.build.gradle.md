![Инструмент Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
## build.gragle.kts

Сборщик для KMP модуля. Содержит в себе зависимости, а также получает переменныя из local.properties.
Все зависисмости их назначение представлены ниже в таблице.

### Зависимости
| Зависимость | Назначение |
|-------------|------------|
|Ktor|Http запросы к CRM|
|Supabase|Запросы к Supabase|
|Okio|Управление файловой системой|
|Kotlinx date time|Управление датой и временнем. Парсинг из формата Long в нужный формат и наоборот|
|Moko resources|Генерация мультиплатформенных ресурсов: изображения в формате png, строки в формате XML|
|Atomicfu (Mutex)|Реализация mutex|
|Koin|Создание модулей для их последующего многочисленного использования на Android платформе. Реализация DI для платформы IOS представлена в KoinHelper|
|Coroutines|Дополнительный функционал для корутин|
|Data store|Локлаьное хранение простых данных|

### Плагины
| Плагин | Назначение |
|-------------|------------|
|Kotlin multiplatform|Подключение KMP в проект|
|Skie|Получение модуля shared из нативного IOS кода. Расширение возможностей для доступа к Kotlin коду в shared модуле из нативного IOS кода|
|Android Library|Подключение android библиотек|
|dev.icerock.mobile.multiplatform-resources|Подключение мультиплатформенных ресурсов|
|com.codingfeline.buildkonfig|Кодогенерация переменных|
|plugin.serialization|Сериаллизация для версии Kotlin|

### Функции
| Функция | Назначение |
|-------------|------------|
|getStringValueFromLocalProperties|Получение значения переменной из файла local.properties|
|Project.getLocalProperty|Получение файла local.properties и чтение его содержимого|
|Лямбда buildkonfig|Кодогенерация перменных в kotlin объекте в пакете, указанном внутри ляюбды|
|multiplatformResources|Генерация мультиплатформенных ресурсов|
