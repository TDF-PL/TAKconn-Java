# TakConn
## Założenia projektu

Projekt ma na celu stworzenie narzędzia integracji serwera TAK z dowolnym oprogramowaniem wykorzystującym kod Java lub C#, w zakresie wymiany komunikatów formatu CoT. 
Oprogramowanie umożliwia wysyłanie wiadomości formatu CoT na serwer TAK przy wykorzystaniu zdefiniowanych sposobów komunikacji.

## Stan projektu

Obecnie zaimplementowane funkcjonalności w języku Java:
- Połączenie TCP bez uwierzytelniania.
- Połączenie TCP przy uwierzytelnieniu certyfikatem.
- Połączenie UDP.

Lista funkcjonalności w trakcie implementacji:
- Połączenie TCP przy uwierzytelnieniu użytkownikiem i hasłem.
- Połączenie TCP przy uwierzytelnieniu certyfikatem, użytkownikiem i hasłem.

Kod w języku C# jest w trakcie tworzenia.

## Certifikaty
Potrzebne są pliki truststore.p12 i user.p12 w katalogach java/src/main/resources/cot/cert i c#/src/WOT.TAK.Client/cot/cert

## Plik konfiguracyjny (Java)

Plik konfiguracyjny się w katalogu java/src/main/resources/. Jego struktura wygląda następująco:

    tak.server.url=<Tutaj należy podać url serwera TAK>
    tak.server.port=<Tutaj należy podać port serwera TAK>

    cot.path=<Wszystkie pliki .cot w podanym katalogu zostaną przesłane na serwer TAK>
    cot.responses=<Przychodzące wiadomości z serwera zapisywane są tutaj>
    cot.schema=<Położenie schematu xsd weryfikującego poprawność plików CoT>

    server.certificate.verification=<Wyłączenie weryfikacji certyfikatu serwera. Obecnie konieczne do poprawnego funkcjonowania aplikacji.>

    trust.store.path=<Położenie trust store>
    trust.store.password=<Hasło do trust store>

    key.store.path=<Położenie key store>
    key.store.password=<Hasło do key store>

    connection.mode=<Tryb komunikacji z serwerem. Można wybrać udp, tls, tlsCred, plain, credentials>

    user.name=<Nazwa użytkownika stosowana jeżeli wybrano tryby tlsCred lub credentials>
    user.password=<Hasło użytkownika stosowana jeżeli wybrano tryby tlsCred lub credentials>