package cn.wolfcode.shiro.realm;

import cn.wolfcode.shiro.dao.IPermissionDAO;
import cn.wolfcode.shiro.dao.IRoleDAO;
import cn.wolfcode.shiro.dao.IUserDAO;
import cn.wolfcode.shiro.domain.User;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import java.util.ArrayList;
import java.util.List;

public class UserRealm extends AuthorizingRealm {
    private IUserDAO userDAO;
    private IRoleDAO roleDAO;
    private IPermissionDAO permissionDAO;

    public void setUserDAO(IUserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void setRoleDAO(IRoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    public void setPermissionDAO(IPermissionDAO permissionDAO) {
        this.permissionDAO = permissionDAO;
    }

    //认证操作，身份验证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //从token中获取登录的用户名， 查询数据库返回用户信息
        String username = (String) authenticationToken.getPrincipal();
        User user = userDAO.getUserByUsername(username);

        if(user == null){
            return null;
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getUsername()), getName());
//        CustomSimpleAuthenticationInfo info = new  CustomSimpleAuthenticationInfo(user.getId(), user.getPassword(), ByteSource.Util.bytes(user.getUsername()), getName());
//        CustomSimpleAuthenticationInfo(admin.getId(), list, admin.getPsd(), this.getClass().getName());
        return info;
    }



    @Override
    public String getName() {
        return "UserRealm";
    }

    //授权操作
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {

        User user = (User) principalCollection.getPrimaryPrincipal();

        List<String> permissions = new ArrayList<String>();
        List<String> roles = new ArrayList<>();

        if("admin".equals(user.getUsername())){
            //拥有所有权限
            permissions.add("*:*");
            //查询所有角色
            roles = roleDAO.getAllRoleSn();
        }else{
            //根据用户id查询该用户所具有的角色
            roles = roleDAO.getRoleSnByUserId(user.getId());
            //根据用户id查询该用户所具有的权限
            permissions = permissionDAO.getPermissionResourceByUserId(user.getId());
        }

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.addStringPermissions(permissions);
        info.addRoles(roles);
        return info;
    }

    //清除缓存
    public void clearCached() {
        //获取当前等的用户凭证，然后清除
        PrincipalCollection principals = SecurityUtils.getSubject().getPrincipals();
        super.clearCache(principals);
    }


}
