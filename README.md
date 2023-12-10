# TakConn
## Założenia projektu

Projekt ma na celu stworzenie narzędzia integracji serwera TAK z dowolnym oprogramowaniem uruchamianym na maszynie
wirtualnej Java (Java Virtual Machine, JVM) lub kompilowanym z kodu bajtowego Javy do kodu maszynowego (np. przy użyciu
Graal VM). Integracja odbywa się w zakresie możliwości wymiany komunikatów protokołu COT (Cursor On Target), zarówno w
wersji klasycznej w formacie XML, jak i w wersji nowszej opartej na formacie Protocol Buffers.

## Stan projektu

Obecnie zaimplementowane funkcjonalności:
- Połączenie TCP/SSL do serwera przy uwierzytelnieniu certyfikatem, z negocjacją formatu protokołu XML->Protobuff
- Połączenie TCP do serwera bez uwierzytelniania, z negocjacją formatu protokołu XML->Protobuff.

Obecnie zaimplementowane funkcjonalności w formie prototypu:
- Połączenie TCP do serwera.
- Połączenie UDP do serwera.

Lista funkcjonalności w trakcie implementacji:
- Połączenie TCP do serwera przy uwierzytelnieniu użytkownikiem i hasłem.
- Połączenie TCP do serwera przy uwierzytelnieniu certyfikatem, użytkownikiem i hasłem.

Planowane funkcjonalności:
- Komunikacja Mesh UDP typu Multicast bezpośrednio z innymi urządzeniami.
- Komunikacja Mesh Point-Point TCP z konkretnym urzadzeniem, z negocjacją formatu protokołu XML->Protobuff.

## Przykład: Połączenie TCP/SSL do serwera przy uwierzytelnieniu certyfikatem, z negocjacją formatu protokołu XML->Protobuff

## Certifikaty
Potrzebne są pliki `truststore.p12` i `user.p12` w katalogach `java/src/main/resources/cot/cert`

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