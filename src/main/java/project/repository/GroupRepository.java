package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.model.entity.group.GroupEntity;

import java.util.Date;
import java.util.List;

@Repository
public interface GroupRepository extends JpaRepository<GroupEntity, Long> {



}

