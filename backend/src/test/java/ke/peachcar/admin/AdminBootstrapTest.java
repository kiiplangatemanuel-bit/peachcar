package ke.peachcar.admin;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.ApplicationArguments;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

class AdminBootstrapTest {
    private AdminUserRepository admins;
    private PasswordEncoder encoder;
    private AdminBootstrap bootstrap;
    private ApplicationArguments args;

    @BeforeEach
    void setUp() {
        admins = mock(AdminUserRepository.class);
        encoder = mock(PasswordEncoder.class);
        bootstrap = new AdminBootstrap(admins, encoder);
        args = mock(ApplicationArguments.class);
    }

    @Test
    void createsInitialAdminWithEncodedPassword() {
        ReflectionTestUtils.setField(bootstrap, "email", "Owner@Peachcar.co.ke");
        ReflectionTestUtils.setField(bootstrap, "password", "a-long-test-password");
        when(admins.findByEmailIgnoreCase("owner@peachcar.co.ke")).thenReturn(Optional.empty());
        when(encoder.encode("a-long-test-password")).thenReturn("encoded-password");

        bootstrap.run(args);

        verify(admins).save(argThat(admin ->
                admin.getEmail().equals("owner@peachcar.co.ke")
                        && admin.getPasswordHash().equals("encoded-password")));
    }

    @Test
    void skipsBootstrapWhenOptionalAndCredentialsAreAbsent() {
        ReflectionTestUtils.setField(bootstrap, "email", "");
        ReflectionTestUtils.setField(bootstrap, "password", "");

        bootstrap.run(args);

        verifyNoInteractions(admins, encoder);
    }

    @Test
    void failsWhenBootstrapIsRequiredButCredentialsAreAbsent() {
        ReflectionTestUtils.setField(bootstrap, "email", "");
        ReflectionTestUtils.setField(bootstrap, "password", "");
        ReflectionTestUtils.setField(bootstrap, "bootstrapRequired", true);

        assertThatThrownBy(() -> bootstrap.run(args))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Administrator bootstrap requires");
        verifyNoInteractions(admins, encoder);
    }

    @Test
    void doesNotResetAnExistingAdministratorPasswordOnRestart() {
        AdminUser existing = new AdminUser("owner@peachcar.co.ke", "existing-hash");
        ReflectionTestUtils.setField(bootstrap, "email", "owner@peachcar.co.ke");
        ReflectionTestUtils.setField(bootstrap, "password", "a-long-test-password");
        when(admins.findByEmailIgnoreCase("owner@peachcar.co.ke")).thenReturn(Optional.of(existing));

        bootstrap.run(args);

        verify(admins, never()).save(any());
        verifyNoInteractions(encoder);
        assertThat(existing.getPasswordHash()).isEqualTo("existing-hash");
    }
}
