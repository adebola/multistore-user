package io.factorialsystems.msscstore21users.service;

import io.factorialsystems.msscstore21users.dto.UserDTO;
import org.springframework.data.domain.Page;

public interface JpaUserService {
    Page<UserDTO> findAll(int pageNumber, int pageSize);
    UserDTO findById(String id);
    UserDTO editUser(String id, UserDTO userDTO);
}
