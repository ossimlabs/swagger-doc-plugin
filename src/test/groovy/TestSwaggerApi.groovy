import omar.TestApi
import org.gradle.GreetingPlugin
import org.junit.Test

class TestSwaggerApi {
    @Test
    void TestClassReflection() {
        Set<Class> res = new GreetingPlugin().getSwaggerAPIClasses()
        assert res.contains(TestApi.class)
        res.forEach { println(it) }
    }
}
