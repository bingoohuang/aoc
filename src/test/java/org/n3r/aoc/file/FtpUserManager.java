package org.n3r.aoc.file;

import org.apache.ftpserver.ftplet.*;
import org.apache.ftpserver.usermanager.UsernamePasswordAuthentication;

public class FtpUserManager implements UserManager {
    private final String user;
    private final String pass;
    private final String homePath;

    public FtpUserManager(String homePath, String user, String pass) {
        this.homePath = homePath;
        this.user = user;
        this.pass = pass;
    }

    @Override
    public User authenticate(final Authentication inAuth) throws AuthenticationFailedException {
        UsernamePasswordAuthentication upa = (UsernamePasswordAuthentication) inAuth;
        String login = upa.getUsername();
        String password = upa.getPassword();
        if (user.equals(login) && pass.equals(password))
            return new FtpUser(homePath, login, password);

        throw new AuthenticationFailedException();
    }

    @Override
    public String getAdminName() throws FtpException {
        return null;
    }

    @Override
    public boolean isAdmin(String s) throws FtpException {
        return false;
    }

    @Override
    public User getUserByName(final String login) throws FtpException {
        return null;
    }

    @Override
    public String[] getAllUserNames() throws FtpException {
        return new String[0];
    }

    @Override
    public void delete(String s) throws FtpException {
    }

    @Override
    public void save(User user) throws FtpException {
    }

    @Override
    public boolean doesExist(String s) throws FtpException {
        return false;
    }
}
