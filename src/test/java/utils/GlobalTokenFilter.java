package utils;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;


public class GlobalTokenFilter implements Filter {

    String token;

    public GlobalTokenFilter(String token) {
        this.token = token;
    }

    @Override
    public Response filter(FilterableRequestSpecification filterableRequestSpecification, FilterableResponseSpecification filterableResponseSpecification, FilterContext filterContext) {
        System.out.println("Request: " + filterableRequestSpecification.getMethod() + " " + filterableRequestSpecification.getURI());
        filterableRequestSpecification.header("Authorization", "Bearer " + token);
        Response response = filterContext.next(filterableRequestSpecification, filterableResponseSpecification);
        System.out.println("Response: " + filterableResponseSpecification.getStatusCode());
        return response;
    }
}
