package com.fiap.soat12.tc_group_7.repository;

import com.fiap.soat12.tc_group_7.entity.ToolCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ToolCategoryRepository extends JpaRepository<ToolCategory, Long> {
    ToolCategory findByToolCategoryName(String toolCategoryName);
    List<ToolCategory> findByActiveTrue();
}
