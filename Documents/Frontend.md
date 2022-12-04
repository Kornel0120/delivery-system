#Frontend

Framework: Angular
IDE: VSCode

Port: 4200

##Megvalósult funkciók:

###Bejelentkezés

Felhasználó sikeresen betud jelentkezni és megkapja a felhasználójához generált tokeneket. A tokeneket session storageben tárolja a frontend.
Bejelentkezés után a shipments oldalra navigálódik a felhasználó.

####Ismert bentmaradt hibák

Bejelentkezés során a navbar elemei többször is megjelennek. Ennek a hibának nem találtam meg az okát.
Oldal újratöltésnél a session storageból törlődnek a tokenek így a usernek újra be kell jelentkeznie. Ezt egy 406-os hibakód váltja ki, de a megoldását még nem találtam meg.

###Regisztráció

Felhasználó sikeresen tud regisztrálni ami után be is tud jelentkezni a felhasználójával.


###Tokenrefresh

Ha a felhasználónak lejár az access tokenje akkor az auth interceptorban a 401-es hiba váltja ki a token refresh végbemenetelét.
Ez funkció sikeresen végbemegy és nics megtalált hiba.

###Kijelentkezés

A felhasználó manuálisan is kitud jelentkezni, ekkor a funkció kiüríti a session storage-t és a token a refresh token táblából is eltávolítja.

###Összes csomag legérdezése

Bejelentkezett felhasználó az összes csomagot letudja kérdezni, melyet a shipments oldalon tud megtekinteni egy táblázat formájában.

Ezen oldal kinézete eltérő attól függően milyen jogosultásggal rendelkezik a felhasználó. Például Employee vagy Admin role-al rendelkező felhasználó tud csomagot leadni csomagautomatához, de egy User jogosultásggal rendelkező felhasználó csak felvenni tudja a csomagot.

###Csomag felvétel

Mint ahogy az előző pontban említettem, jogosultásgtól függően tudnak a felhasználók sikeresen csomagot felvenni és leadni.
Felvenni csak User jogosultsággal rendelkező felhasználó tud. Ennél a funkciónal a kék gombra kattintva a frontend automatikusan elküldi a megfelelő adatokkal a módosítási kérelmet a backendre.

Ennél a funkciónál szerettem volna megvalósítani azt is, hogy mielőtt elvégezné a frontend a módosítási műveletet az előtt a felhasználótól jelszótt kérjen és ellenőrízze, hogy megfelelő jelszót adott-e meg.
Azonban a fejlesztés során jelentkezett kisebb nehezségek miatt nem maradt erre időm.

####Ismert bentmaradt hibák

A felsorolásnál csak azoknál a csomagoknál szerettem volna megjelníteni az felvétel gombot ahol a userId egyezik a felhasználó userId-val, azonban vmilyen oknál fogva a userService undefined usert ad vissza.

Ezt a funkciót egy *ngIf-el valósítottam volna meg a gomb html tagjében.

###Csomag kézbesítés

Csomagot kézbesíteni Employee jogosultásggal rendelkező felhasználó tud, amelyet a kék gombra kattintva tud megtenni. Ekkor egy modal jelenik meg ahol megtudja adni azt, hogy milyen szállítási státussba került a csomag valamint azt, hogy meddig átvehető a csomag.

A dátum megadásának módjára több lehetőséget is elkészítettem, melyek közül a táblázat felett elhelyezett legördülő listából lehet választani.

Ezek a lehetőségek:

- Automatikus dátum megadás: Ennél a lehetőségnél a leadás dátumától 5 napig felvehető a csomag.
- Időtartam megadása napban: Ennél a lehetőségnél egy számot kérek a felhasználótól amely minimum 5 és maximum 30 lehet. A leadás dátumától számítva a megadott napig felvehető a csomag.
- Begépelhető dátum: Itt stringként kérem be a dátumot a felhasználótól, amelyet a component-ben dátum típussá alakítok.
- Naptáras megoldás: Itt egy egyszerű date inputot használok ahol kiválaszthatja a felhasználó a dátumot.

###ismert bentmaradt hibák

A felvételhez hasonlóan ennél a funkciónál is szerettem volna megvalósítani azt, hogy csak azoknál a csomagoknál jelenjen meg a leadás gomb amely tartalmazza a dolgozó id-jét. Itt is az undefined currentUser hiba miatt nem valósult meg.

##Profil oldal

Itt a felhasználó megtekintheti az adatait és ezen az oldalon ki is jelentkezhet az oldalról.

Ezzel az oldallal nem foglalkoztam sokat, így kinézet téren gyengélkedik.


##További hibák

Kinézeti hibák maradtak a frontenden.