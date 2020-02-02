package life.HK.Community.system.service;

import life.HK.Community.system.entity.User;
import life.HK.Community.system.enums.UserStatus;
import life.HK.Community.system.exception.UserNameAlreadyExistException;
import life.HK.Community.system.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * @author gaoyishu
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    // 保存用户信息方法
    public void saveUser(Map<String, String> registerUser) {
        Optional<User> optionalUser = userRepository.findByUsername(registerUser.get("username"));
        //
        if (optionalUser.isPresent()) {
            throw new UserNameAlreadyExistException("用户名已经存在.请使用其他的用户名");
        }
        User user = new User();
        user.setUsername(registerUser.get("username"));
        user.setPassword(bCryptPasswordEncoder.encode(registerUser.get("password")));
        user.setUserNickName(registerUser.get("userNickName"));
        user.setWeChat(registerUser.get("weChat"));
        user.setRoles("DEV,PM");
        user.setStatus(UserStatus.CAN_USE);
        // controller 层的 user 保存在 uerRepository 内部
        userRepository.save(user);
    }

    // 查找用户
    public User findUserByUserName(String name) {
        return userRepository.findByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("No user found with username " + name));
    }

    public void deleteUserByUserName(String name) {
        userRepository.deleteByUsername(name);
    }


    public Page<User> getAllUser(int pageNum, int pageSize) {
        return userRepository.findAll(PageRequest.of(pageNum, pageSize));
    }
}
