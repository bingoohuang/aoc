package org.n3r.aoc.file;

import org.apache.ftpserver.ftplet.Authority;
import org.apache.ftpserver.ftplet.AuthorizationRequest;
import org.apache.ftpserver.ftplet.User;

import java.util.List;

public class FtpUser implements User {
    private final String login;
    private final String pass;
    private final String homePath;

    public FtpUser(String homePath, final String login, final String pass) {
        this.homePath = homePath;
        this.login = login;
        this.pass = pass;
    }

    @Override
    public AuthorizationRequest authorize(final AuthorizationRequest authRequest) {
        return authRequest;
    }

    @Override
    public boolean getEnabled() {
        return true;
    }

    @Override
    public String getHomeDirectory() {
        return homePath;
    }

    @Override
    public int getMaxIdleTime() {
        return 0;
    }

    @Override
    public String getName() {
        return this.login;
    }

    @Override
    public String getPassword() {
        return pass;
    }

    @Override
    public List<Authority> getAuthorities() {
        return null;
    }

    @Override
    public List<Authority> getAuthorities(Class<? extends Authority> aClass) {
        return null;
    }
}
