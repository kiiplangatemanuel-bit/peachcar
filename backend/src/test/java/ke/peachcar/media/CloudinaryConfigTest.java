package ke.peachcar.media;

import com.cloudinary.Cloudinary;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class CloudinaryConfigTest {
    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withUserConfiguration(CloudinaryConfig.class);

    @Test
    void createsCloudinaryClientFromRenderEnvironmentVariableNames() {
        contextRunner
                .withPropertyValues(
                        "CLOUDINARY_CLOUD_NAME=peachcar-test",
                        "CLOUDINARY_API_KEY=test-key",
                        "CLOUDINARY_API_SECRET=test-secret")
                .run(context -> assertThat(context).hasSingleBean(Cloudinary.class));
    }

    @Test
    void leavesCloudinaryDisabledWhenNoCloudNameIsConfigured() {
        contextRunner.run(context -> assertThat(context).doesNotHaveBean(Cloudinary.class));
    }

    @Test
    void failsFastWhenCloudinaryConfigurationIsIncomplete() {
        contextRunner
                .withPropertyValues("CLOUDINARY_CLOUD_NAME=peachcar-test")
                .run(context -> assertThat(context).hasFailed()
                        .getFailure()
                        .hasRootCauseMessage("Cloudinary is partially configured. Set CLOUDINARY_CLOUD_NAME, CLOUDINARY_API_KEY and CLOUDINARY_API_SECRET together."));
    }
}
