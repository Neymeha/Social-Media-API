package com.neymeha.socialmediasecurityapi.repository;

import com.neymeha.socialmediasecurityapi.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository <Status, Long>  {
}
