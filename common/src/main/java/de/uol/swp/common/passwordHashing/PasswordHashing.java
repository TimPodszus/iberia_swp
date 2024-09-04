package de.uol.swp.common.passwordHashing;

import org.apache.directory.api.ldap.model.constants.LdapSecurityConstants;
import org.apache.directory.api.ldap.model.password.PasswordUtil;

public class PasswordHashing
{
    public static byte[] hashPassword(String password)
    {
        return PasswordUtil.createStoragePassword(password, LdapSecurityConstants.HASH_METHOD_SSHA512 );
    }
}
