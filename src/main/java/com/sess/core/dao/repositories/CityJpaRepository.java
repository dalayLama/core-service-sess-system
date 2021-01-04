package com.sess.core.dao.repositories;

import com.sess.core.cities.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityJpaRepository extends JpaRepository<City, Long> {
}
