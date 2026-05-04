package com.expensetracker.config;

import com.expensetracker.model.Category;
import com.expensetracker.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;

    @Override
    public void run(String... args) {
        List<Category> defaults = List.of(
                new Category("Food", "Meals, groceries, snacks and dining"),
                new Category("Travel", "Transport, fuel, rides and tickets"),
                new Category("Shopping", "Clothes, electronics and personal purchases"),
                new Category("Bills", "Utilities, rent, subscriptions and recurring bills"),
                new Category("Entertainment", "Movies, games, events and leisure"),
                new Category("Health", "Medicines, doctor visits and wellness"),
                new Category("Education", "Courses, books and learning materials"),
                new Category("Other", "Uncategorized expenses")
        );

        defaults.forEach(category ->
                categoryRepository.findByNameIgnoreCase(category.getName())
                        .orElseGet(() -> categoryRepository.save(category)));
    }
}
