import omar.TestApi
import org.gradle.GreetingPlugin
import org.junit.Test

class TestSwaggerApi {
    @Test
    void TestClassReflection() {
        new GreetingPlugin().getSwaggerAPIClasses().contains(TestApi.class)
    }
}
