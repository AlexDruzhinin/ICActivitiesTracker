package ic.devops.uploadingfiles.dao;

import ic.devops.uploadingfiles.model.User;

import java.util.List;

public interface UserRepository {

    User findByName(String name);
    User findById(int id);
    List<User> findAll();
}
