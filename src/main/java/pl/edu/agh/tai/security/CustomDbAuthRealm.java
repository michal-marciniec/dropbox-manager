package pl.edu.agh.tai.security;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import pl.edu.agh.tai.database.AuthenticationManager;

/**
 * Custom authentication and authorization realm. Uses AuthenticationManager class to perform checking operations.
 */
public class CustomDbAuthRealm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        AuthenticationManager auth = new AuthenticationManager();
        try {

            auth.initialize();

            String username = principalCollection.getPrimaryPrincipal().toString();
            SimpleAuthorizationInfo sai = auth.getUsersAuthorizationInfo(username);

            return sai;

        } catch (Exception e) {
            throw new AuthorizationException(e);
        } finally {
            auth.close();
        }
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        AuthenticationManager auth = new AuthenticationManager();
        try {

            auth.initialize();

            UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
            if (auth.validateUser(token.getUsername(), token.getPassword()))
                return new SimpleAuthenticationInfo(authenticationToken.getPrincipal(), authenticationToken.getCredentials(), authenticationToken.toString());
            else
                throw new AuthenticationException();

        } catch (Exception e) {
            throw new AuthorizationException(e);
        } finally {
            auth.close();
        }

    }
}
