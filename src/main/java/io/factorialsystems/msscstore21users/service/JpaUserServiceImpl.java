package io.factorialsystems.msscstore21users.service;

import io.factorialsystems.msscstore21users.dto.UserDTO;
import io.factorialsystems.msscstore21users.entity.User;
import io.factorialsystems.msscstore21users.repository.UserRepository;
import io.factorialsystems.msscstore21users.utils.PageRequestBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class JpaUserServiceImpl implements JpaUserService {
    private final UserRepository userRepository;

    @Override
    public Page<UserDTO> findAll(int pageNumber, int pageSize) {
        log.info("Retrieving List of Users");
        Pageable pageable = PageRequestBuilder.build(pageNumber, pageSize);
        final Page<User> userPage = userRepository.findAll(pageable);
        return userPage.map(User::toShortDTO);
    }

    @Override
    public UserDTO findById(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("User with id %s not found", id)));
        return user.toLongDTO();
    }

    @Override
    public UserDTO editUser(String id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("User with id %s not found", id)));
        var u = userDTO.toEntity();
        u.setId(id);
        return userRepository.save(u).toLongDTO();
    }
}
