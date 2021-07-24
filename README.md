# Stocks app
Тестовое задание для мобильной школы Яндекс - приложение для мониторинга цен акций на бирже.
# Стек технологий
* RxJava2 - запросы к Finnhub API в фоновом потоке
* SQL - сохранение информации об избранных акциях в базе данных
* Picasso - асинхронная подгрузка изображений
***
# Скриншоты
Список акций </br>
<img src="https://github.com/iamwhoiam0/InvestmentsApplication/blob/master/screenshots/Stocks_screenshot.jpg" width="250" /> </br>
Список избранных акций </br>
<img src="https://github.com/iamwhoiam0/InvestmentsApplication/blob/master/screenshots/Favourite_screenshot.jpg" width="250" />
***
# API
Источником актуальных данных об акциях был выбран [Finnhub api](https://finnhub.io/docs/api)
# В доработке
* Создание activity с выводом графиков акций.
* Использование WebSocket для постоянного обновления цен акций.
* рефакторинг уже имеющегося кода
