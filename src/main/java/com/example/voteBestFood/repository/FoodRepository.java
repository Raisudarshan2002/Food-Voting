package com.example.voteBestFood.repository;

import com.example.voteBestFood.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {

    @Transactional
    @Modifying
    @Query("UPDATE Food f SET f.upvotes = f.upvotes + 1 WHERE f.id = :id")
    void incrementUpvote(@Param("id") Long id);

    @Transactional
    @Modifying
    @Query("UPDATE Food f SET f.downvotes = f.downvotes + 1 WHERE f.id = :id")
    void incrementDownvote(@Param("id") Long id);

    List<Food> findAllByOrderByUpvotesDesc();
}
