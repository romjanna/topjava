package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static ru.javawebinar.topjava.web.SecurityUtil.authUserId;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        //checkNew(meal);
        return service.create(meal, authUserId());
    }

    public void delete(int id) throws NotFoundException {
        log.info("delete {]", id);
        service.delete(id, authUserId());
    }

    public Meal get(int id) throws NotFoundException {
        log.info("get {}", id);
        return service.get(id, authUserId());
    }

    public void update(Meal meal) {
        log.info("update {}", meal);
    }

    public List<Meal> getAll() {
        log.info("getAll");
        return service.getAll(authUserId());
    }
}