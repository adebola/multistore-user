package io.factorialsystems.msscstore21users.service;

import io.factorialsystems.msscstore21users.dto.UserDTO;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

public interface JpaUserService {
    Page<UserDTO> findAll(int pageNumber, int pageSize);
    UserDTO findById(String id);
    UserDTO editUser(String id, UserDTO userDTO);
    Page<UserDTO> findAllByTenant(int pageNumber, int pageSize);
    UserDTO findByIdAndTenantId(String id);
    Page<UserDTO> findByTenantId(Integer pageNumber, Integer pageSize, String id);
    void uploadAvatar(String id, MultipartFile file);
    void addRoles(String id, String[] roles);
    void removeRoles(String id, String[] roles);
}
