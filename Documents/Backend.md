#Backend

Framework: Spring boot
IDE: Intellij

Port: 8080

Használt dependency-k: Jpa,Hibernate,Lombok,Spring security,Jwt

##Megvalósult funkciók:

##Regisztráció

Két regisztrációs funkció került implementálásra.

Publikus regisztráció: Ezzel a regisztrációs fukncióval alapértelmezetten User jogosultsággal fog rendelkezni az új felhasználó. Ezt a regisztrációs fuknciót bárki elérheti.

Admin regisztráció: Ezt a regisztrációs fuknciót csak admin jogosultsággal rendelkező felhasználó használhatja és megadhatja azt, hogy milyen jogosultsággal rendelkezzen az új felhasználó. Jogosultságok közül rendelkezhet Admin,Employee és User jogosultsággal.
Ezzel a funkcióval tudunk futárt vagy admint regisztrálni.

##Bejelentkezés

Login funkció során email címmel és jelszóval ellenőrizzük a belépni kivánó egyén identitását. Ha valamelyik hibás akkor egyértelműen nem engedjük belépni.
Sikeres bejelentkezés után a bejelentkezett felhasználó megkapja az access tokenjét és a refresh tokenjét is. A refresh token itt eltárolódik egy adatbázis táblában is.

Access token 10 percig érvényes míg a refresh token megközelítőleg 8 óráig.

### Adatbázisba előre felvitt felhasználók

| Jogosultság | Email 					| Jelszó |
|:-----------:|:-----------------------:|:------:|
| Admin       | Admin@Admin.com      	| 123456 |
| Employee    | Employee@Employee.com 	| 123456 |
| User        | User@User.com 			| 123456 |

##Token refresh

Ha a felhasználónak lejár az access tokenje akkor a refresh token elküldésével és validálásával új access tokent generálunk a felhasználó számára.

##Csomagok kezelése

Lekérdezhető az összes csomag, amely a frontenden lévő shipments oldalnál kerül alkalmazásra.

Csomag módosítható amely bemenetnek megkapja a módosítható csomag azonosítóját amelyre azért van szükség, hogy megtaláljuk a csomagot az adatbázisban. Bemenetnek megkapja továbbá a csomag módosított szállítási státuszát és a felvételi időpontját. Ez a két adatat az, amit módosítunk a backenden.
Ez a funkció egy ResponseEntityvel tér vissza amelyben értesíti a felhasználót, hogy sikeresen módosult a csomag, és információt is biztosít a csomagról.

A fejlesztés kezdeténél terveztem ezeket az adatok megjeleníteni a frontenden is azonban ez nem készült el.

Nem felhasznált funkciók:

-Csomag lekérdezése id alapján.
-Csomag létrehozása.
-Csomag törlése.

A fejlesztés kezdeténél szerettem volna ha frontenden van lehetőség csomagot létrehozni és lekérdezni is.

##Csomag státuszok

Lekérdezhetőek a létező státuszok is, amellyel a frotnend csomagkézbesítő modal-ján lévő legördülő menü töltődik fel.

##Felhasználók profiljának lekérdezése email alapján

Ez a funkció a frontenden található profil oldalra ad adatot. Mindhárom jogosultsággal elérhető.

##Dolgozókra és ügyfelekre vonatkozó crud műveletek

readAll,readById,create,update,delete -> csak Admin jogosultsággal használhatóak.

Ezek a funkciók nem kerültek alkalmazásra a frontenden.

##Saját Kivételek

A kivételekre nem fektettem nagy hangsúlyt, a refreshtoken exception-t leszámítva a kivételek csak kiterjesztik a kivétel osztályt. 

#Test

##Manuális tesztek

Manuális teszteket postman alkalmazással végeztem melyek közül pár tesztről képet is készítettem. Ezek a képek a Postman mappában találhatóak.

##Unit tesztek

Unit tesztek készültek a controllerekre és servicekre, azonban azoknál funkcióknál amelyeket a frontend fejlesztés során implementáltam és a feljesztés kezdeténél nem volt tervben a unit testek elmaradtak.

Testek amiket még írnék:

Entity,Model,Dto tesztek valamint az előbb említett kimaradt funkciók.



