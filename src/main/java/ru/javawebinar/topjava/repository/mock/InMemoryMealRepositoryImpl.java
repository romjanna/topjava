package ru.javawebinar.topjava.repository.mock;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static ru.javawebinar.topjava.util.MealsUtil.DEFAULT_USER_ID;

public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, List<Meal>> prepository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach((meal) -> save(meal, DEFAULT_USER_ID));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            if(prepository.isEmpty()) {
                prepository.put(userId, Collections.singletonList(meal));
            } else {
                List<Meal> pmeals;
                if(prepository.get(userId) != null) {
                    pmeals = new ArrayList<>(prepository.get(userId));
                } else {
                    pmeals = new ArrayList<>();
                }
                pmeals.add(meal);
                sortByDate(pmeals);
                prepository.put(userId, pmeals);
            }
        }
        return meal;
        // treat case: update, but absent in storage
        //return repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    private void sortByDate(List<Meal> pmeals) {
        pmeals.sort(Comparator.comparing(Meal::getDateTime).reversed());
    }

    @Override
    public boolean delete(int id, int userId) {
        return prepository.get(userId).removeIf(meal-> meal.getId().equals(id));
    }

    @Override
    public Meal get(int id, int userId) {
        return prepository.get(userId).get(id);
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return prepository.get(userId);
    }
}

