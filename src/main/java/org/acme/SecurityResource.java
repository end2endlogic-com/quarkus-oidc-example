package org.acme;

import io.quarkus.logging.Log;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.*;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

@Path("/auth")
public class SecurityResource {

    @ConfigProperty(name = "aws.cognito.user-pool-id")
    String userPoolId;

    @ConfigProperty(name = "aws.cognito.client-id")
    String clientId;

    private final CognitoIdentityProviderClient cognitoClient =
        CognitoIdentityProviderClient.builder().build();

    @POST
    @Path("/login")
    @Produces("application/json")
    @PermitAll
    public Response login(
        @QueryParam("userId") String userId,
        @QueryParam("password") String password
    ) {
        Log.info("Logging in");
        AdminInitiateAuthRequest authRequest =
            AdminInitiateAuthRequest.builder()
                .userPoolId(userPoolId)
                .clientId(clientId)
                .authFlow(AuthFlowType.ADMIN_USER_PASSWORD_AUTH)
                .authParameters(
                    Map.of("USERNAME", userId, "PASSWORD", password)
                )
                .build();

        try {
            AdminInitiateAuthResponse authResponse =
                cognitoClient.adminInitiateAuth(authRequest);
            AuthenticationResultType authResult =
                authResponse.authenticationResult();

            String accessToken = authResult.accessToken();
            String refreshToken = authResult.refreshToken();
            Map<String, String> rc = new HashMap<>();
            rc.put("accessToken", accessToken);
            rc.put("refreshToken", refreshToken);
            return Response.ok(rc).build();
        } catch (NotAuthorizedException e) {
            Log.error("Authentication failed for user: " + userId, e);
            throw new SecurityException("Invalid credentials");
        } catch (Exception e) {
            Log.error("Unexpected error during authentication", e);
            throw new SecurityException(
                "Authentication failed: " + e.getMessage()
            );
        }
    }


}
