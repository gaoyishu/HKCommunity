package life.HK.Community.security.entity;

import lombok.Data;


/**
 * @author gaoyishu
 */
@Data
public class LoginUser {

    private String username;
    private String password;
    private Boolean rememberMe;

}
