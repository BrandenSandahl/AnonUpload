package com.theironyard.services;

import com.theironyard.entities.AnonFile;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by branden on 3/16/16 at 11:19.
 */
public interface AnonFileRepository extends CrudRepository<AnonFile, Integer>{
    List<AnonFile> findByIsPermanentFalse();
    AnonFile findFirstByIsPermanentFalseOrderByCreatedAsc();

}
