package one.kastordriver.fakerest.logic.request;

import groovy.lang.Binding;
import org.springframework.stereotype.Component;
import spark.Request;

@Component
public class QueryParamRequestElement extends RequestElement {

    private static final String ELEMENT_NAME = "queryParam";

    @Override
    public String getElementName() {
        return ELEMENT_NAME;
    }

    @Override
    public String processCondition(String condition, Request request, Binding binding) {
        return processComplexRequestElement(condition, binding, request::queryParams);

    }
}
