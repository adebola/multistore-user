package io.factorialsystems.msscstore21users.service;

import io.factorialsystems.msscstore21users.dto.UserDTO;
import io.factorialsystems.msscstore21users.entity.Authority;
import io.factorialsystems.msscstore21users.entity.User;
import io.factorialsystems.msscstore21users.exception.BusinessEntityNotFoundException;
import io.factorialsystems.msscstore21users.repository.AuthorityRepository;
import io.factorialsystems.msscstore21users.repository.UserRepository;
import io.factorialsystems.msscstore21users.security.TenantContext;
import io.factorialsystems.msscstore21users.utils.JwtTokenWrapper;
import io.factorialsystems.msscstore21users.utils.PageRequestBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class JpaUserServiceImpl implements JpaUserService {
    private final S3Service s3Service;
    private final JpaAuditService auditService;
    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;

    public static final String EDIT_USER = "Edit-User";
    public static final String CHANGE_USER_AVATAR = "Change-User-Avatar";

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
    public Page<UserDTO> findAllByTenant(int pageNumber, int pageSize) {
        final String tenantId = TenantContext.getCurrentTenant();

        if (tenantId == null) {
            throw new IllegalStateException("No Store found");
        }

        return localFindByTenantId(pageNumber, pageSize, tenantId);
    }

    @Override
    public UserDTO findByIdAndTenantId(String id) {
        final String tenantId = TenantContext.getCurrentTenant();

        if (tenantId == null) {
            throw new IllegalStateException("No Store found");
        }

        return userRepository
                .findByIdAndTenantId(id, tenantId)
                .map(User::toLongDTO).orElseThrow(() -> new BusinessEntityNotFoundException(String.format("User with id %s not found", id)));
    }

    @Override
    public Page<UserDTO> findByTenantId(Integer pageNumber, Integer pageSize, String id) {
        return localFindByTenantId(pageNumber, pageSize, id);
    }

    @Override
    @Transactional
    public void uploadAvatar(String id, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("User with id %s not found", id)));

        String fileName = s3Service.uploadFile(file);

        user.setAvatarImageUrl(fileName);
        userRepository.save(user);

        final String auditMessage = String.format("Avatar changed for User %s by %s", user.getUserName(), JwtTokenWrapper.getUserName());
        auditService.audit(CHANGE_USER_AVATAR, auditMessage);
    }

    @Override
    @Transactional
    public void addRoles(String id, String[] roles) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("User with id %s not found", id)));

        List<Authority> authorities = authorityRepository.findAllById(List.of(roles));

        log.info("Adding roles {} to user {}", Arrays.toString(roles), user.getUserName());

        // Ensure the authorities exists in the database
        if (authorities.isEmpty()) {
            log.error("No authorities found for roles {}", Arrays.toString(roles));
            throw new BusinessEntityNotFoundException("No roles found for user %s");
        }

        // Ensure all the authorities requested for exist
        if (authorities.size() != roles.length) {
            log.error("Authorities size {} does not match roles size {}, Some Roles do not exist {}", authorities.size(), roles.length, Arrays.toString(roles));
            throw new RuntimeException("Some roles do not exist");
        }

        final String tenantId = TenantContext.getCurrentTenant();

        // Make sure the authorities belong to the same tenant / store and the user does not already have the authorities
        for (Authority authority : authorities) {
            if (!tenantId.equals(authority.getTenantId())) {
                log.error("Authority with id {} does not belong to tenant {}", authority.getId(), tenantId);
                throw new RuntimeException("Authority does not belong to store");
            }

            if (user.getAuthorities().contains(authority)) {
                log.error("User already has authority {}", authority.getAuthority());
                throw new RuntimeException("User already has some of the roles");
            }
        }

        // Add the authorities
        user.getAuthorities().addAll(authorities);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void removeRoles(String id, String[] roles) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("User with id %s not found", id)));

        List<Authority> authorities = authorityRepository.findAllById(List.of(roles));

        // Ensure the user has the roles to be removed
        if (!user.getAuthorities().containsAll(authorities)) {
            log.error("User does not have all the roles to be removed {}", Arrays.toString(roles));
            throw new RuntimeException("User does not have all the roles to be removed");
        }

        authorities.forEach(user.getAuthorities()::remove);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public UserDTO editUser(String id, UserDTO userDTO) {
        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("User with id %s not found", id)));

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());

        final String auditMessage = String.format("user %s modified by %s", user.getUserName(), JwtTokenWrapper.getUserName());
        auditService.audit(EDIT_USER, auditMessage);
        log.info(auditMessage);

        return userRepository.save(user).toLongDTO();
    }

    private Page<UserDTO> localFindByTenantId(Integer pageNumber, Integer pageSize, String id) {
        log.info("Retrieving List of Users for Tenant {}", id);
        Pageable pageable = PageRequestBuilder.build(pageNumber, pageSize);
        final Page<User> userPage = userRepository.findByTenantId(id, pageable);
        return userPage.map(User::toShortDTO);
    }
}
