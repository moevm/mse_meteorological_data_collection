# mse_meteorological_data_collection
## Возможности
На данный момент приложение умеет делать следующие:
* Показывать текущую погоду по районе, в котором вы находитесь.
* Показывать текущую погоду на 5 дней вперед с интервалом в 3 часа.

## Cборка на Android
* Телефон
    - В настройках телефона разрешить установку приложений из непроверенных источников ([Ссылка на видео](https://www.youtube.com/watch?v=6Xken07bluM))
    - Скачать и установить [APK](https://github.com/moevm/mse_meteorological_data_collection/raw/android/app-debug.apk)
* Эмулятор
    - Скачать JDK(Java Development Kit)([Ссылка](https://www.oracle.com/technetwork/java/javase/downloads/index.html))
    - Скачать Android SDK
            MAC([Ссылка](https://dl.google.com/android/repository/sdk-tools-darwin-4333796.zip))
            Windows([Ссылка](https://dl.google.com/android/repository/sdk-tools-windows-4333796.zip))
            Linux([Ссылка](https://dl.google.com/android/repository/sdk-tools-linux-4333796.zip))
    - Скачать Android Studio([Ссылка](https://developer.android.com/studio))
    - Склонировать проект с ветки android
    - Открыть Android Studio
    - Когда откроется окно "Welcome to Android Studio" нажмите "Open an existing Android studio project" и выберите склонированный проект.
    - При запуске проекта начнется его сборка(будут устанавливаться необходимые библиотеки, генрироваться некоторый код и другое), это может занять некоторое время.Когда проект соберется, вы увидите зеленые галочки под всеми пунктами внизу экрана, а так же кнопка запуска станет активной.
    - После запуска Android Studio вам необходимо указать AndroidSdk. Для этого нажмите File->Project Structure->SDK Location. Затем выберите местоположение SDK.
    - Далее вам необходимо нажать Tools->AVD->create virtual device. Выбрать любое понравившиеся устройство и нажать next. Далее необходимо выбрать версию андройда, необходима версия не ниже 21(если не знаете, выбирайте Lollipop 21 x86 Android 5.0) и нажимаете Далее, а затем Finish.Вы создали эмулятор.
    - Возле кнопки запуска есть поле Available devices. Вам необходимо выбрать только,что созданный эмулятор.
    - Теперь можете нажать кнопку запуска проекта. Если эмулятор не открыт, то он отроется.Дальше соберется apk-debug, установиться на эмуляторе и приложение откроется.
    - Приложение теперь работает
## Cборка на IOS
* Скачать Xcode для macOs (Xcode можно скачать только для macOs);
* Cлонить проект с ветки ios;
* Открыть проект;
* Загрузить симулятор устройства iphone 7 (В левом верхнем углу Xcode: WeatherApp -> Add Additional Simulators... -> "+" -> Device Type Iphone 7 (IOS 13.0) -> Create;
* Запустить проект на загруженном симуляторе;
* Приложение вылетит по причине отсутствия правильно-выбранной локации устройства. Для того, чтобы ее установить, необходимо в настройках симулятора выбрать следующее: Debug -> Location -> Apple;
* Запустить проект повторно.
