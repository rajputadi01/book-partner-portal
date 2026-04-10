package com.capg.portal.finance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.capg.portal.finance.entity.Discount;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, String> {

}
