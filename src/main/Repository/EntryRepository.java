package main.Repository;

import main.Model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EntryRepository extends JpaRepository<Entry, Long> {
    Optional<Entry> findByEid(String eid);

    List<Entry> findByUid_Uuid(String uuid);



}