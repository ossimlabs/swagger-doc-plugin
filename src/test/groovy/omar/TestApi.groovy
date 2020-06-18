package omar

import io.swagger.annotations.Api
import io.swagger.annotations.ApiImplicitParam
import io.swagger.annotations.ApiImplicitParams
import io.swagger.annotations.ApiOperation

@Api
class TestApi {
    @ApiOperation(value = "Get the capabilities of the server",
            produces = 'application/xml',
            httpMethod = "GET",
            nickname = "getCapabilities")
    @ApiImplicitParams([
            @ApiImplicitParam(name = 'service', value = 'OGC Service type', allowableValues = "WFS", defaultValue = 'WFS', paramType = 'query', dataType = 'string', required = true),
            @ApiImplicitParam(name = 'version', value = 'Version to request', allowableValues = "1.1.0", defaultValue = '1.1.0', paramType = 'query', dataType = 'string', required = true),
            @ApiImplicitParam(name = 'request', value = 'Request type', allowableValues = "GetCapabilities", defaultValue = 'GetCapabilities', paramType = 'query', dataType = 'string', required = true),
    ])
    def testApiEndpoint() {}
}
