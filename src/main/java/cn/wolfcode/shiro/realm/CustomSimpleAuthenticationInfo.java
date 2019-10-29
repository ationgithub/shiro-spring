package cn.wolfcode.shiro.realm;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.MergableAuthenticationInfo;
import org.apache.shiro.authc.SaltedAuthenticationInfo;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.subject.MutablePrincipalCollection;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CustomSimpleAuthenticationInfo implements MergableAuthenticationInfo, SaltedAuthenticationInfo {

    /**
     * The principals identifying the account associated with this AuthenticationInfo instance.
     */
    protected PrincipalCollection principals;
    /**
     * The credentials verifying the account principals.
     */
    protected Object credentials;

    /**
     * Any salt used in hashing the credentials.
     *
     * @since 1.1
     */
    protected ByteSource credentialsSalt;

    /**
     * Default no-argument constructor.
     */
    public CustomSimpleAuthenticationInfo() {
    }


    public CustomSimpleAuthenticationInfo(Object primary, Object principal, Object credentials, String realmName) {
        this.principals = new CustomSimplePrincipalCollection(primary, principal, realmName);
        this.credentials = credentials;
    }


    public CustomSimpleAuthenticationInfo(Object principal, Object hashedCredentials, ByteSource credentialsSalt, String realmName) {
        this.principals = new CustomSimplePrincipalCollection(principal, realmName);
        this.credentials = hashedCredentials;
        this.credentialsSalt = credentialsSalt;
    }


    public CustomSimpleAuthenticationInfo(PrincipalCollection principals, Object credentials) {
        this.principals = new CustomSimplePrincipalCollection(principals);
        this.credentials = credentials;
    }

    public CustomSimpleAuthenticationInfo(PrincipalCollection principals, Object hashedCredentials, ByteSource credentialsSalt) {
        this.principals = new CustomSimplePrincipalCollection(principals);
        this.credentials = hashedCredentials;
        this.credentialsSalt = credentialsSalt;
    }


    public PrincipalCollection getPrincipals() {
        return principals;
    }


    public void setPrincipals(PrincipalCollection principals) {
        this.principals = principals;
    }

    public Object getCredentials() {
        return credentials;
    }


    public void setCredentials(Object credentials) {
        this.credentials = credentials;
    }


    public ByteSource getCredentialsSalt() {
        return credentialsSalt;
    }


    public void setCredentialsSalt(ByteSource salt) {
        this.credentialsSalt = salt;
    }


    @SuppressWarnings("unchecked")
    public void merge(AuthenticationInfo info) {
        if (info == null || info.getPrincipals() == null || info.getPrincipals().isEmpty()) {
            return;
        }

        if (this.principals == null) {
            this.principals = info.getPrincipals();
        } else {
            if (!(this.principals instanceof MutablePrincipalCollection)) {
                this.principals = new SimplePrincipalCollection(this.principals);
            }
            ((MutablePrincipalCollection) this.principals).addAll(info.getPrincipals());
        }

        //only mess with a salt value if we don't have one yet.  It doesn't make sense
        //to merge salt values from different realms because a salt is used only within
        //the realm's credential matching process.  But if the current instance's salt
        //is null, then it can't hurt to pull in a non-null value if one exists.
        //
        //since 1.1:
        if (this.credentialsSalt == null && info instanceof SaltedAuthenticationInfo) {
            this.credentialsSalt = ((SaltedAuthenticationInfo) info).getCredentialsSalt();
        }

        Object thisCredentials = getCredentials();
        Object otherCredentials = info.getCredentials();

        if (otherCredentials == null) {
            return;
        }

        if (thisCredentials == null) {
            this.credentials = otherCredentials;
            return;
        }

        if (!(thisCredentials instanceof Collection)) {
            Set newSet = new HashSet();
            newSet.add(thisCredentials);
            setCredentials(newSet);
        }

        // At this point, the credentials should be a collection
        Collection credentialCollection = (Collection) getCredentials();
        if (otherCredentials instanceof Collection) {
            credentialCollection.addAll((Collection) otherCredentials);
        } else {
            credentialCollection.add(otherCredentials);
        }
    }


    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SimpleAuthenticationInfo)) return false;

        CustomSimpleAuthenticationInfo that = (CustomSimpleAuthenticationInfo) o;

        //noinspection RedundantIfStatement
        if (principals != null ? !principals.equals(that.principals) : that.principals != null) return false;

        return true;
    }


    public int hashCode() {
        return (principals != null ? principals.hashCode() : 0);
    }


    public String toString() {
        return principals.toString();
    }
}
