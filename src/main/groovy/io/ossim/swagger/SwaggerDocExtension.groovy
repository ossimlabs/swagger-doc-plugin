package io.ossim.swagger

public class SwaggerDocExtension {
    private String prefix = "test"

    String setPrefix(String p) {
        prefix = p
    }

    String getPrefix() {
        return prefix;
    }
}
