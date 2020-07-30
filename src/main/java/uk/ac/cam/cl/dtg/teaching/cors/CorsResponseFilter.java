package uk.ac.cam.cl.dtg.teaching.cors;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;

@Provider
@ServerInterceptor
public class CorsResponseFilter implements ContainerResponseFilter {

  public static final String[] CORS_HEADERS =
      new String[] {
        "Access-Control-Allow-Headers", "Content-Type, api_key, Authorization",
        "Access-Control-Allow-Origin", "*",
        "Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, PATCH, OPTIONS"
      };

  @Override
  public void filter(
      ContainerRequestContext requestContext, ContainerResponseContext responseContext)
      throws IOException {
    MultivaluedMap<String, Object> headers = responseContext.getHeaders();
    for (int i = 0; i < CORS_HEADERS.length; i += 2) {
      headers.putSingle(CORS_HEADERS[i], CORS_HEADERS[i + 1]);
    }
  }
}
