import org.apache.directory.api.ldap.model.constants.LdapSecurityConstants;
import org.apache.directory.api.ldap.model.password.PasswordUtil;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.apache.directory.api.ldap.model.password.PasswordUtil.findAlgorithm;

public class Test
{

    public static void main(String[] args)
    {
        String password = "password";

        byte[] bytes = PasswordUtil.createStoragePassword(password, LdapSecurityConstants.HASH_METHOD_SSHA512 );

        String str = new String(bytes, StandardCharsets.UTF_8);

        System.out.println(password);
        System.out.println(str);





    }
}

