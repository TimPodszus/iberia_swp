package de.uol.swp.common.passwordHashing;

import org.apache.directory.api.ldap.model.constants.LdapSecurityConstants;
import org.apache.directory.api.ldap.model.password.PasswordUtil;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;

public class PasswordHashing implements Serializable
{
    public static String hashPassword(String password)
    {
        byte[] bytes = PasswordUtil.createStoragePassword(password, LdapSecurityConstants.HASH_METHOD_SHA512 );
        return new String(bytes ,StandardCharsets.UTF_8);

    }

    public static boolean compareCredentials(String password, String hashedPassword)
    {
        return PasswordUtil.compareCredentials(password.getBytes(StandardCharsets.UTF_8), hashedPassword.getBytes(StandardCharsets.UTF_8));
    }
}
