package com.team.teamreadioserver.post.repository;

import com.team.teamreadioserver.post.entity.Post;
import com.team.teamreadioserver.post.entity.PostImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostImgRepository extends JpaRepository<PostImg, Integer> {

}
