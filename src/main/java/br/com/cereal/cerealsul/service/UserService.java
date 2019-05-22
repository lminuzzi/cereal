package br.com.cereal.cerealsul.service;

import br.com.cereal.cerealsul.model.User;

import java.util.List;

public interface UserService {
    User save(User user);
    List<User> findAll();
    User findOne(long id);
    void delete(long id);
    User findByUsername(String username);
}
