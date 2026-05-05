# Projekt: System Zarządzania Serwisem Napraw (Model Klient-Serwer)

## 1. Skład zespołu
Zgodnie z wymogami, poniżej znajduje się skład zespołu wraz z przypisaniem odpowiedzialności za poszczególne moduły:
* Cezary Markiewicz – Architektura klient-serwer, implementacja wielowątkowości i limitu MAX_CLIENTS na serwerze, przygotowanie modeli danych (Serializable).
* Radosław Muzal - Mechanizm serializacji/deserializacji obiektów, obsługa wyjątków strumieniowych (ClassCastException), napisanie testów zautomatyzowanych w czystej Javie.

## 2. Instrukcja techniczna


**Wymagania: Środowisko Java (np. JDK 17/21) oraz dowolne środowisko IDE (np. IntelliJ IDEA, Visual Studio Code, Eclipse). Projekt nie wymaga zewnętrznych narzędzi budowania (Maven/Gradle) ani zewnętrznych bibliotek. Cały kod znajduje się w pakiecie `projekt`.

Krok 1: Uruchomienie Serwera: 
1. Otwórz folder z projektem w swoim środowisku programistycznym.
2. Odszukaj plik `ServerMain.java` w pakiecie `projekt`.
3. Uruchom klasę `ServerMain`. W konsoli powinien pojawić się komunikat o starcie serwera, inicjalizacji modeli danych w mapie oraz nasłuchiwaniu na połączenia na porcie 8080.

Krok 2: Uruchomienie Klienta (Testowanie ręczne):
1. Po pomyślnym starcie serwera, odszukaj plik `ClientMain.java`.
2. Uruchom tę klasę. W konsoli klienta zobaczysz logi z nawiązania połączenia (OK) oraz przetworzone strumieniowo (Stream API) kolekcje urządzeń, a następnie przechwycony celowy błąd rzutowania (`ClassCastException`).
3. Aby przetestować wielowątkowość i limit połączeń (`MAX_CLIENTS = 2`), uruchom klasę `ClientMain.java` kilkukrotnie (najlepiej szybko, 3-4 razy). Nadmiarowi klienci automatycznie otrzymają z serwera status `REFUSED` i zakończą działanie.

Krok 3: Uruchamianie testów zautomatyzowanych (Vanilla Java):
Projekt wykorzystuje autorskie środowisko testowe oparte na czystej Javie, aby wyeliminować problemy z zależnościami bibliotek.
1. Odszukaj plik `SystemTests.java` w pakiecie `projekt`.
2. Uruchom jego główną metodę `main()`.
3. Skrypt testowy automatycznie (w tle) podniesie instancję serwera, podłączy wirtualnych klientów, przetestuje zachowanie przy przekroczeniu limitu połączeń, wywoła pułapkę rzutowania oraz przetestuje metody `equals` i serializację obiektów.
4. Wyniki (sukces lub błędy) zostaną czytelnie wypisane w konsoli ze stosownymi asercjami.

## 3. Deklaracja użycia AI

* Wykorzystane narzędzia i modele: Gemini 3.1 Pro.
* W jakich częściach projektu z nich korzystano:
  * Konsultacje dotyczące dostosowania własnej domeny "Serwisu Napraw" do rygorystycznych wymogów akademickich (gniazda, serializacja).
  * Generowanie skryptu testującego działanie aplikacji na lokalnych gniazdach sieciowych (E2E) w podejściu czystej Javy (bez frameworka JUnit), w celu uniknięcia problemów z kompilacją pakietów.
  * Debugowanie problemów z konfiguracją pakietów (`package projekt;`) w środowisku Visual Studio Code.
* Przykładowe (kluczowe) prompty:
  * "Jak zaimplementować bezpieczną wielowątkowość i odrzucenie klienta (REFUSED) przy przekroczeniu MAX_CLIENTS w czystej Javie za pomocą Socketów?"
   * "Rozwiąż błąd kompilatora The declared package does not match the expected package w edytorze VS Code."
