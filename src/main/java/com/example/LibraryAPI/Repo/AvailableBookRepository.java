package com.example.LibraryAPI.Repo;

import com.example.LibraryAPI.AvailableBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AvailableBookRepository extends JpaRepository<AvailableBook, Long> {
}
