package nl.tudelft.sem.template.help;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.domain.user.Name;
import nl.tudelft.sem.template.domain.user.UserRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class UserRepositoryTest implements UserRepository {
    List<AppUser> users = new ArrayList<>();

    @Override
    public Optional<AppUser> findByEmail(Email email) {
        for (AppUser user : users) {
            if (user.getEmail().equals(email)) {
                return Optional.ofNullable(user);
            }
        }
        return Optional.ofNullable(null);
    }

    @Override
    public boolean existsByEmail(Email email) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<AppUser> findByFirstNameAndLastName(Name firstName, Name lastName) {
        List<AppUser> matching = new ArrayList<>();
        for (AppUser user : users) {
            if (user.getFirstName().equals(firstName) && user.getLastName().equals(lastName)) {
                matching.add(user);
            }
        }
        return matching;
    }

    @Override
    public List<AppUser> findByFirstName(Name firstName) {
        List<AppUser> matching = new ArrayList<>();
        for (AppUser user : users) {
            if (user.getFirstName().equals(firstName)) {
                matching.add(user);
            }
        }
        return matching;
    }

    @Override
    public <S extends AppUser> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public List<AppUser> findAll() {
        return users;
    }

    @Override
    public List<AppUser> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<AppUser> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public <S extends AppUser> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends AppUser> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends AppUser> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public List<AppUser> findAllById(Iterable<String> strings) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public <S extends AppUser> long count(Example<S> example) {
        return 0;
    }

    @Override
    public void deleteById(String s) {
        AppUser appUser = null;
        for (AppUser user : users) {
            if (String.valueOf(user.getId()).equals(s)) {
                appUser = user;
            }
        }
        if (appUser != null) {
            users.remove(appUser);
        }
    }

    @Override
    public void delete(AppUser entity) {

    }

    @Override
    public void deleteAll(Iterable<? extends AppUser> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends AppUser> S save(S entity) {
        users.add(entity);
        return entity;
    }

    @Override
    public <S extends AppUser> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<AppUser> findById(Long id) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == id) {
                AppUser user = users.get(i);
                return Optional.ofNullable(user);
            }
        }
        return Optional.ofNullable(null);
    }

    @Override
    public Optional<AppUser> findById(String s) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == Long.parseLong(s)) {
                AppUser user = users.get(i);
                return Optional.ofNullable(user);
            }
        }
        return Optional.ofNullable(null);
    }

    @Override
    public boolean existsById(String s) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == Integer.parseInt(s)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends AppUser> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public void deleteInBatch(Iterable<AppUser> entities) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public AppUser getOne(String s) {
        return null;
    }

    @Override
    public <S extends AppUser> boolean exists(Example<S> example) {
        return false;
    }
}
