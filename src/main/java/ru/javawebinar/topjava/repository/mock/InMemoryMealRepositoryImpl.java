package ru.javawebinar.topjava.repository.mock;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private Map<Integer, List<Meal>> prepository = new HashMap();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            if(prepository.isEmpty()) {
                prepository.put(authUserId(), Collections.singletonList(meal));
            } else {
                List<Meal> pmeals = new ArrayList<Meal>(prepository.get(authUserId()));
                pmeals.add(meal);
                sortByDate(pmeals);
                prepository.put(authUserId(), pmeals);
            }
            return meal;
        }
        // treat case: update, but absent in storage
        return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    private void sortByDate(List<Meal> pmeals) {
        Collections.sort(pmeals, new Comparator<Meal>() {
            @Override
            public int compare(Meal o1, Meal o2) {
                return o1.getDateTime().compareTo(o2.getDateTime());
            }
        });
    }

    @Override
    public boolean delete(int id) {
        if(repository.containsKey(id)) {
            repository.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public Meal get(int id) {
        return repository.get(id);
    }

    @Override
    public Collection<Meal> getAll() {
        return repository.values();
    }
}

