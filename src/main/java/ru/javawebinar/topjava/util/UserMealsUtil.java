package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );

        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(23, 59), 2000);

    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        List<UserMealWithExceed> userMealWithExceeds = new ArrayList<UserMealWithExceed>();

        HashMap<LocalDate, Integer> dateCaloriesCalculationHMap = new HashMap<>();

        for (UserMeal userMeal : mealList) {
            LocalDate mealDate = userMeal.getDateTime().toLocalDate();
            LocalTime mealTime = userMeal.getDateTime().toLocalTime();
            //if (okTime(mealTime, startTime, endTime)) {
            if(TimeUtil.isBetween(mealTime, startTime, endTime)) {
                dateCaloriesCalculationHMap.put(mealDate, (dateCaloriesCalculationHMap.getOrDefault(mealDate, 0) + userMeal.getCalories()));
            }
        }

        for (UserMeal userMeal : mealList) {
            LocalTime mealTime = userMeal.getDateTime().toLocalTime();
            LocalDate mealDate = userMeal.getDateTime().toLocalDate();
            //if (okTime(mealTime, startTime, endTime)) {
            if(TimeUtil.isBetween(mealTime, startTime, endTime)) {
                userMealWithExceeds.add(new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), (dateCaloriesCalculationHMap.get(mealDate).intValue() <= caloriesPerDay)));
            }
        }

        System.out.println(Arrays.toString(userMealWithExceeds.toArray()));

        return userMealWithExceeds;
    }

    private static boolean okTime(LocalTime time, LocalTime startTime, LocalTime endTime) {
        if (time.isAfter(startTime) && time.isBefore(endTime)) {
            return true;
        }
        return false;
    }
}