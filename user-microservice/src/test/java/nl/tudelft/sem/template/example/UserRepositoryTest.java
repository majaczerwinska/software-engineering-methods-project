package nl.tudelft.sem.template.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import nl.tudelft.sem.template.domain.user.AppUser;
import nl.tudelft.sem.template.domain.user.Email;
import nl.tudelft.sem.template.domain.user.UserRepository;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class UserRepositoryTest implements UserRepository {
    List<AppUser> users = new ArrayList<>();

    @Override
    public Optional<AppUser> findByEmail(Email email) {
        return Optional.empty();
    }

    @Override
    public boolean existsByEmail(Email email) {
        return false;
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
    public void deleteById(String s) {

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
        return null;
    }

    @Override
    public <S extends AppUser> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<AppUser> findById(String s) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == Integer.parseInt(s)) {
                AppUser user = users.get(i);
                return Optional.ofNullable(user);
            }
        }
        return null;
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
    public <S extends AppUser> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends AppUser> boolean exists(Example<S> example) {
        return false;
    }
}
