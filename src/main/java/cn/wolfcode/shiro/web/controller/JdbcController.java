//package cn.wolfcode.shiro.web.controller;
//
//import cn.wolfcode.shiro.domain.User;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.RowMapper;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.ModelMap;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import javax.annotation.Resource;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.List;
//
////测试类
//@Controller
//@RequestMapping("/jdbc")
//public class JdbcController {
//    @Resource
//    private JdbcTemplate jdbcTemplate;
//    @RequestMapping("/userlist")
//    public String getUserList(ModelMap map) {
//        String sql = "SELECT * FROM user";
//        List<User> userList = jdbcTemplate.query(sql, new RowMapper<User>() {
//            User user = null;
//            @Override
//            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
//                user = new User();
//                user.setId(rs.getLong("id"));
//                user.setUsername(rs.getString("username"));
//                user.setPassword(rs.getString("password"));
//                return user;
//            }
//        });
//        for (User user : userList) {
//            System.out.println(user.getUsername());
//        }
//        map.addAttribute("users", userList);
//        return "user";
//    }
//
//}
