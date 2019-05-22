package br.com.cereal.cerealsul.controller;

import br.com.cereal.cerealsul.model.User;
import br.com.cereal.cerealsul.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/users")
public class UserController {
    /*
    @RequestMapping("/login")
    public boolean login(@RequestBody User user) {
        return user.getUsername().equals("user") && user.getPassword().equals("password");
    }

    @RequestMapping("/user")
    public Principal user(HttpServletRequest request) {
        String authToken = request.getHeader("Authorization").substring("Basic".length()).trim();
        return () -> new String(Base64.getDecoder().decode(authToken)).split(":")[0];
    }
    */
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public List<User> listUser() {
        return userService.findAll();
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public User create(@RequestBody User user) {
        return userService.save(user);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public User findOne(@PathVariable long id) {
        return userService.findOne(id);
    }

    @RequestMapping(value = "/user/login/{username}", method = RequestMethod.GET)
    public User findByName(@PathVariable String username) {
        return userService.findByUsername(username);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public User update(@PathVariable long id, @RequestBody User user) {
        user.setId(id);
        return userService.save(user);
    }

    @RequestMapping(value = "/user/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable(value = "id") Long id) {
        userService.delete(id);
    }
}