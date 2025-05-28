package com.team.teamreadioserver.event.repository;

import com.team.teamreadioserver.event.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Integer> {
    List<Event> findAllByOrderByEventCreateAtDesc();

//    Optional<Event> findByEventTitle(String EventTitle); 제목을 완전하게 적어야 하는 문제가 있음
    List<Event> findByEventTitleContainingIgnoreCase(String keyword); //제목을 정확하게 검색하지 않아도 검색 가능

}
