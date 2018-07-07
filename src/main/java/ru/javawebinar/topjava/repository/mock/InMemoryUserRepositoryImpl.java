package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);

    private Map<Integer, User> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        repository.entrySet().removeIf(u-> u.getKey().equals(id));
        return false;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if(user.isNew()) {
            user.setId(counter.incrementAndGet());
            repository.put(user.getId(), user);
            return user;
        }
        return repository.computeIfPresent(user.getId(), (id, oldUser) -> user);
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        List<User> users = (List<User>) repository.values();
        Collections.sort(users, new Comparator<User>() {
            @Override
            public int compare(User o1, User o2) {
                return o2.getName().compareTo(o1.getName());
            }
        });
        return users;
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = repository.values().stream()
                .filter(user -> user.getEmail().equals(email))
                .collect(Collectors.toList());
        log.info("getByEmail {}", email);
        if(!users.isEmpty()) {
            return users.get(0);
        }
        return null;
    }
}
