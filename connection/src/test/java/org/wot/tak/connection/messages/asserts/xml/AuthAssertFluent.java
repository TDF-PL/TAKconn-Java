package org.wot.tak.connection.messages.asserts.xml;

import org.wot.tak.connection.protocol.xml.Auth;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public final class AuthAssertFluent {
    private final Auth auth;

    public AuthAssertFluent(Auth auth) {
        this.auth = auth;
    }

    public AuthAssertFluent hasUsername(String username) {
        assertThat(auth.getCot().getUsername()).isEqualTo(username);
        return this;
    }

    public AuthAssertFluent hasPassword(String password) {
        assertThat(auth.getCot().getPassword()).isEqualTo(password);
        return this;
    }

    public AuthAssertFluent hasUid(String uid) {
        assertThat(auth.getCot().getUid()).isEqualTo(uid);
        return this;
    }
}
