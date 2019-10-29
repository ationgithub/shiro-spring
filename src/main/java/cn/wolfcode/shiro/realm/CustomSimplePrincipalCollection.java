package cn.wolfcode.shiro.realm;

import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;

import java.util.Collection;

public class CustomSimplePrincipalCollection extends SimplePrincipalCollection {

    private Object primary;

    public CustomSimplePrincipalCollection() {
    }


    public CustomSimplePrincipalCollection(Object primary, Object principal, String realmName) {
        super(principal, realmName);
        this.primary = primary;
    }

    public CustomSimplePrincipalCollection(Object principal, String realmName) {
        super(principal, realmName);
    }

    public CustomSimplePrincipalCollection(Collection principals, String realmName) {
        super(principals, realmName);
    }

    public CustomSimplePrincipalCollection(PrincipalCollection principals) {
        super(principals);
    }

    @Override
    public Object getPrimaryPrincipal() {
        return primary;
    }

    public Object getCustomPrimaryPrincipal() {
        return primary;
    }
}
