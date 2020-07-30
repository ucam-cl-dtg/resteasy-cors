package uk.ac.cam.cl.dtg.teaching.cors;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;

@Provider
@ServerInterceptor
@PreMatching
public class CorsRequestFilter implements ContainerRequestFilter {

  @Override
  public void filter(ContainerRequestContext requestContext) throws IOException {
    if (isCorsPreflight(requestContext)) {
      ResponseBuilder b = Response.ok();
      for (int i = 0; i < CorsResponseFilter.CORS_HEADERS.length; i += 2) {
        b.header(CorsResponseFilter.CORS_HEADERS[i], CorsResponseFilter.CORS_HEADERS[i + 1]);
      }
      requestContext.abortWith(b.build());
    }
  }

  public static boolean isCorsPreflight(ContainerRequestContext request) {
    MultivaluedMap<String, String> headers = request.getHeaders();
    String origin = headers.getFirst("Origin");
    if (origin == null) return false;

    // origin can be not null even for non-cross-site requests
    String host = headers.getFirst("Host");
    if (host != null) {
      UriInfo u = request.getUriInfo();
      String scheme = u.getBaseUri().getScheme();
      String serverOrigin = scheme + "://" + host;
      if (origin.equals(serverOrigin)) {
        return false;
      }
    }

    return (headers.containsKey("Access-Control-Request-Method")
        && "OPTIONS".equalsIgnoreCase(request.getMethod()));
  }
}
