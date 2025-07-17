# RestMax – System wspomagający zarządzanie siecią restauracji

## Opis projektu
**RestMax** to aplikacja konsolowa z graficznym interfejsem użytkownika (GUI) wspierająca zarządzanie małą siecią restauracji. Jej celem jest uproszczenie i automatyzacja kluczowych procesów biznesowych, takich jak:
- Zarządzanie różnymi typami restauracji (dostawy, drive-thru, własny budynek)  
- Obsługa pracowników i ich kontraktów  
- Definiowanie i przypisywanie kategorii menu oraz dań (sezonowych i stałych)  
- Obsługa klientów i ich recenzji  
- Zarządzanie promocjami (GiftCard, Voucher, VoucherGiftCard)  
- Zapewnienie trwałości danych poprzez serializację ekstensji obiektów  

Każdy element systemu został zaprojektowany zgodnie z wymogami kursu „Modelowanie i Analiza Systemów informacyjnych” na PJATK, obejmując pełną dokumentację UML oraz implementację konstrukcji obiektowych w języku Java.

---

## Funkcjonalności Systemu
🏢 Zarządzanie Restauracjami
- Przechowywanie informacji o nazwie i pełnym adresie restauracji

Trzy typy restauracji:
- Delivery - obsługa dostaw (z maksymalnym dystansem dostawy)
- DriveThru - obsługa zamówień z samochodu (z ograniczeniem wysokości pojazdu)
- OwnBuilding - restauracja w osobnym budynku (z informacją o miejscach parkingowych)

👥 Zarządzanie Pracownikami
- Rejestracja pracowników z danymi osobowymi
- Walidacja i unikalność numeru PESEL
- System kontraktów pracowniczych z restauracjami
- Możliwość pracy w wielu lokalizacjach jednocześnie

🍽️ Zarządzanie Menu
- Dynamiczne zarządzanie - możliwość przekształcenia dania sezonowego w stałe
- Kategorie menu - unikalne nazwy kategorii (np. "Dania główne", "Desery")

Dania menu w dwóch typach:
- Sezonowe - z datą końca dostępności i limitami zamówień
- Stałe - z informacjami o popularności i statusie dania popisowego


👤 Zarządzanie Klientami
- Rejestracja klientów z walidacją danych
- System recenzji powiązanych z klientami
- Integracja z systemem promocji

🎫 System Promocji i Zniżek
- System aktywacji i dezaktywacji promocji

Trzy typy promocji:
- GiftCard - karta z określoną kwotą do wydania
- Voucher - zniżka procentowa na konkretne danie
- VoucherGiftCard - promocja łączona

--- 

## Diagramy UML
1. **Diagram przypadków użycia**  
   Przedstawia podstawowe funkcje systemu: zarządzanie restauracjami, pracownikami, menu, klientami, promocjami i trwałością danych.
2. **Diagram aktywności**  
   Szczegółowy przebieg przypadku użycia „Przypisanie kategorii menu do restauracji”.
3. **Diagram stanu**  
   Modeluje życie obiektu `MenuCategory` – od utworzenia, przez przypisanie do restauracji, aż po ewentualne usunięcie.
4. **Diagram analityczny**  
   Wstępny model konceptualny z klasami domenowymi i relacjami.
5. **Diagram projektowy**  
   Diagram projektowy wzbogacony o metody, typy kolekcji i decyzje implementacyjne.
   
--- 

## Wykorzystane konstrukcje UML i ich implementacja

| Konstrukcja UML                              | Implementacja w Javie                                                                                                                                                                     |
|-----------------------------------------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| **1. Asocjacja zwykła**<br/>MenuCategory ↔ MenuItem | - `MenuItem` posiada referencję do `MenuCategory` <br/> - `MenuCategory` trzyma listę `List<MenuItem>` <br/> - Metody `addMenuItem()`, `removeMenuItem()` oraz unikalność nazw |
| **2. Asocjacja kwalifikowana**<br/>Restaurant ↔ MenuCategory | - W `Restaurant` pole `TreeMap<String,MenuCategory> menuCategoriesMap` (klucz: nazwa) <br/> - W `MenuCategory` pole `List<Restaurant> restaurantList` <br/> - Metody `addMenuCategory()`, `getMenuCategoryByName()` |
| **3. Asocjacja z atrybutem (klasa asocjacyjna)**<br/>Restaurant ↔ Employee | - Klasa `Contract` przechowuje `Employee`, `Restaurant`, datę i typ kontraktu <br/> - `Employee` i `Restaurant` posiadają listy `List<Contract>` <br/> - Metody `addContract()`, `endContract()` |
| **4. Kompozycja**<br/>Client ↔ Review         | - `Client` trzyma listę `List<Review>` <br/> - `Review` zawsze ma referencję do `Client` <br/> - Metody `addReview()`, `removeReview()`, `setClient()` gwarantujące spójność |
| **5. Wielodziedziczenie (polimorfizm)**<br/>Client ↔ Discount | - Bazowa klasa `Discount` oraz klasy potomne `GiftCard`, `Voucher`, `VoucherGiftcard` <br/> - `VoucherGiftcard` dziedziczy po `Voucher` i implementuje interfejs `IGiftcard` <br/> - Polimorficzne metody `getTotalDiscountAmount()`, `isApplicable()` |
| **6. Overlapping**<br/>Restaurant typów (DriveThru, Delivery, OwnBuilding) | - W `Restaurant` atrybut `EnumSet<RestaurantClass> classes` <br/> - Interfejsy `IDriveThru`, `IDelivery`, `IOwnBuilding` implementowane przez `Restaurant` <br/> - Kombinacja typów i metod z interfejsów |
| **7. Dynamiczna transformacja**<br/>MenuItemSeasonal → MenuItemFixed | - Abstrakcyjna klasa `MenuItem`, podklasy `MenuItemSeasonal` i `MenuItemFixed` <br/> - Metoda `makeFixed()` tworzy nowy obiekt `MenuItemFixed` na podstawie `MenuItemSeasonal` i usuwa stary obiekt |
| **8. Trwałość danych (ekstensje)**<br/>ObjectPlus | - Klasa bazowa `ObjectPlus` gromadzi ekstensje wszystkich obiektów w statycznej mapie <br/> - Serializacja (`saveExtent()`) i deserializacja (`loadExtent()`) całej mapy ekstensji do pliku |

--- 

## Technologie i narzędzia
- **Język programowania:** Java  
- **IDE:** IntelliJ IDEA  
- **Kolekcje:** `List`, `TreeMap`, `EnumSet`  
- **Serializacja:** `ObjectOutputStream` / `ObjectInputStream` w `ObjectPlus`  
- **Diagramy UML:** Lucidchart
- **GUI:** Swing
   

