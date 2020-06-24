import io.ossim.swagger.SwaggerDocPlugin
import omar.TestApi
import org.junit.Test

class TestSwaggerApi {
//    @Test
    void TestClassReflection() {
        Set<Class> res = new SwaggerDocPlugin().getSwaggerAPIClasses()
        assert res.contains(TestApi.class)
        res.forEach { println(it) }
    }
}
