package com.lucasavs.cryptoexchange.service;

import com.lucasavs.cryptoexchange.dto.UserCreateRequest;
import com.lucasavs.cryptoexchange.dto.UserDto;
import com.lucasavs.cryptoexchange.dto.UserLoginRequest;
import com.lucasavs.cryptoexchange.dto.UserUpdateRequest;
import com.lucasavs.cryptoexchange.entity.User;
import com.lucasavs.cryptoexchange.exception.ResourceAlreadyExistException;
import com.lucasavs.cryptoexchange.exception.ResourceNotFoundException;
import com.lucasavs.cryptoexchange.mapper.UserMapper;
import com.lucasavs.cryptoexchange.repository.UserRepository;
import com.lucasavs.cryptoexchange.security.TokenService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = UserServiceImpl.class)
class UserServiceTest {

    @MockitoBean
    UserRepository userRepository;
    @MockitoBean
    UserMapper userMapper;
    @MockitoBean
    PasswordEncoder passwordEncoder;
    @MockitoBean
    AuthenticationManager authenticationManager;
    @MockitoBean
    TokenService tokenService;

    @Autowired
    UserService userService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @Nested
    @DisplayName("Tests for login()")
    class LoginTests {
        @Test
        void shouldReturnTokenWhenCredentialsAreValid() {
            // Arrange
            UserLoginRequest userLoginRequest = new UserLoginRequest();
            userLoginRequest.setEmail("teste@email.com");
            userLoginRequest.setPassword("12345678");
            User userPrincipal = new User();
            userPrincipal.setEmail("teste@email.com");

            Authentication mockAuthentication = mock(Authentication.class);
            when(mockAuthentication.getPrincipal()).thenReturn(userPrincipal);
            when(authenticationManager.authenticate(any())).thenReturn(mockAuthentication);
            when(tokenService.generateToken(userPrincipal)).thenReturn("aValidJwtToken");

            // Act
            String generatedToken = userService.login(userLoginRequest);

            // Assert
            assertEquals("aValidJwtToken", generatedToken);
            verify(authenticationManager).authenticate(any());
            verify(tokenService).generateToken(userPrincipal);
        }

        @Test
        void shouldThrowBadCredentialsExceptionWhenCredentialsAreInvalid() {
            // Arrange
            UserLoginRequest request = new UserLoginRequest();
            request.setEmail("wrong@email.com");
            request.setPassword("badpass");

            when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Invalid credentials"));

            // Act & Assert
            assertThrows(BadCredentialsException.class, () -> userService.login(request));
            verify(authenticationManager).authenticate(any());
            verifyNoInteractions(tokenService);
        }
    }

    @Nested
    @DisplayName("Tests for findAll()")
    class FindAllTests {
        @Test
        void shouldReturnListOfUserDtosWhenFindingAllUsers() {
            // Arrange
            User user1 = new User("teste1@email.com", "12345678");
            User user2 = new User("teste2@email.com", "12345678");
            User user3 = new User("teste3@email.com", "12345678");

            List<User> rawUserList = new ArrayList<User>();
            rawUserList.add(user1);
            rawUserList.add(user2);
            rawUserList.add(user3);

            UserDto dto1 = new UserDto();
            dto1.setEmail("teste1@email.com");
            UserDto dto2 = new UserDto();
            dto2.setEmail("teste1@email.com");
            UserDto dto3 = new UserDto();
            dto3.setEmail("teste1@email.com");

            when(userRepository.findAll()).thenReturn(rawUserList);
            when(userMapper.toDto(user1)).thenReturn(dto1);
            when(userMapper.toDto(user2)).thenReturn(dto2);
            when(userMapper.toDto(user3)).thenReturn(dto3);

            // Act
            List<UserDto> result = userService.findAll();

            // Assert
            assertEquals(List.of(dto1, dto2, dto3), result);
            verify(userRepository, times(1)).findAll();
            verify(userMapper).toDto(user1);
            verify(userMapper).toDto(user2);
            verify(userMapper).toDto(user3);
        }
    }

    @Nested
    @DisplayName("Tests for findById()")
    class FindByIdTests {
        @Test
        void shouldReturnUserDtoWhenUserExists() {
            // Arrange
            UUID userId = UUID.randomUUID();
            User user = new User("teste@email.com", "12345678");
            user.setId(userId);

            UserDto userDto = new UserDto();
            userDto.setId(userId);
            userDto.setEmail("teste@email.com");

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(userMapper.toDto(user)).thenReturn(userDto);

            // Act
            UserDto result = userService.findById(userId);

            // Assert
            assertEquals(userDto, result);
            verify(userRepository).findById(userId);
            verify(userMapper).toDto(user);
        }

        @Test
        void shouldThrowResourceNotFoundExceptionWhenUserDoesNotExist() {
            // Arrange
            UUID nonExistingId = UUID.randomUUID();
            when(userRepository.findById(nonExistingId)).thenReturn(Optional.empty());

            // Act + Assert
            assertThrows(ResourceNotFoundException.class, () -> userService.findById(nonExistingId));
            verify(userRepository).findById(nonExistingId);
        }
    }

    @Nested
    @DisplayName("Tests for save()")
    class SaveTests {
        @Test
        void shouldSaveAndReturnUserDto() {
            // Arrange
            UserCreateRequest request = new UserCreateRequest();
            request.setEmail("test@email.com");
            request.setPassword("12345678");

            User savedUser = new User();
            savedUser.setId(UUID.randomUUID());
            savedUser.setEmail(request.getEmail());
            String passwordHash = "bCryptedPassword";
            savedUser.setPasswordHash(passwordHash);

            UserDto expectedDto = new UserDto();
            expectedDto.setId(savedUser.getId());
            expectedDto.setEmail(savedUser.getEmail());

            when(passwordEncoder.encode(request.getPassword())).thenReturn(passwordHash);
            when(userRepository.save(any(User.class))).thenReturn(savedUser);
            when(userMapper.toDto(savedUser)).thenReturn(expectedDto);

            // Act
            UserDto result = userService.save(request);

            // Assert
            verify(userRepository).save(userArgumentCaptor.capture());
            User capturedUser = userArgumentCaptor.getValue();
            assertEquals(request.getEmail(), capturedUser.getEmail());
            assertEquals(passwordHash, capturedUser.getPasswordHash());
            assertEquals(expectedDto.getId(), result.getId());
            assertEquals(expectedDto.getEmail(), result.getEmail());
            verify(passwordEncoder).encode(request.getPassword());
            verify(userMapper).toDto(savedUser);
        }

        @Test
        void shouldThrowResourceAlreadyExistExceptionWhenEmailIsDuplicate() {
            // Arrange
            var request = new UserCreateRequest();
            request.setEmail("existing@email.com");
            request.setPassword("12345678");
            var passwordHash = "aPasswordHash";

            when(passwordEncoder.encode(request.getPassword())).thenReturn(passwordHash);
            when(userRepository.save(any(User.class))).thenThrow(new DataIntegrityViolationException("Duplicate entry"));

            // Act & Assert
            assertThrows(ResourceAlreadyExistException.class, () -> {
                userService.save(request);
            });
            verify(userRepository).save(any(User.class));
            verifyNoInteractions(userMapper); // fails before mapper
        }
    }

    @Nested
    @DisplayName("Tests for update()")
    class UpdateTests {
        @Test
        void shouldUpdateAndReturnUserDtoWhenEmailAndPasswordAreProvided() {
            // Arrange
            UUID userId = UUID.randomUUID();
            var request = new UserUpdateRequest();
            request.setEmail("new.email@test.com");
            request.setPassword("new-password");
            String newPasswordHash = "hashed-new-password";

            var existingUser = new User();
            existingUser.setId(userId);

            when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
            when(passwordEncoder.encode(request.getPassword())).thenReturn(newPasswordHash);
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(userMapper.toDto(any(User.class))).thenReturn(new UserDto());

            // Act
            userService.update(userId, request);

            // Assert
            verify(userRepository).save(userArgumentCaptor.capture());
            User capturedUser = userArgumentCaptor.getValue();
            assertEquals(request.getEmail(), capturedUser.getEmail());
            assertEquals(newPasswordHash, capturedUser.getPasswordHash());
            verify(userMapper).toDto(capturedUser);
        }
    }

    @Nested
    @DisplayName("Tests for deleteById()")
    class DeleteByIdTests {
        @Test
        void shouldCallRepositoryDeleteById() {
            // This test ensures the service correctly delegates the delete call to the repository.
            // This is a critical interaction test to prevent the functionality from being silently broken.

            // Arrange
            UUID userId = UUID.randomUUID();
            doNothing().when(userRepository).deleteById(userId);

            // Act
            userService.deleteById(userId);

            // Assert
            verify(userRepository).deleteById(userId);
        }
    }
}