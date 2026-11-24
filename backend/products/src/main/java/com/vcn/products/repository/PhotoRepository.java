package com.vcn.products.repository;

import com.vcn.products.model.Photo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PhotoRepository extends JpaRepository<Photo, Long> {

    @Modifying
    @Query("DELETE FROM Photo p WHERE p.publicId = :publicId")
    void deleteByPublicId(@Param("publicId") String publicId);
}
