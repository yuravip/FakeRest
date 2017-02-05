package com.fakerest.logic;

import groovy.lang.Binding;
import groovy.lang.GroovyShell;
import spark.Request;

public class ConditionLogic {

    public static boolean isSuitable(String condition, Request request) {
        Binding binding = new Binding();

        for (RequestElement requestElement : RequestElement.values()) {
            if (requestElement.containedInCondition(condition)) {
                condition = requestElement.getProcessCondition().apply(condition, request, binding);
            }
        }

        GroovyShell groovyShell = new GroovyShell(binding);
        Object result = groovyShell.evaluate(condition);
        return result instanceof Boolean ? (Boolean)result : false;
    }

    private enum RequestElement {
        ip((condition, request, binding) -> {
            final String elementName = "@" + RequestElement.ip.name();
            final String replacedElementName = "_" + RequestElement.ip.name();
            final String elementValue = request.ip();
            binding.setVariable(replacedElementName, elementValue);
            return condition.replaceAll(elementName, replacedElementName);
        }),
        port((condition, request, binding) -> {
            final String elementName = "@" + RequestElement.port.name();
            final String replacedElementName = "_" + RequestElement.port.name();
            final int elementValue = request.port();
            binding.setVariable(replacedElementName, elementValue);
            return condition.replaceAll(elementName, replacedElementName);
        }),
        contentLength((condition, request, binding) -> {
            final String elementName = "@" + RequestElement.contentLength.name();
            final String replacedElementName = "_" + RequestElement.contentLength.name();
            final int elementValue = request.contentLength();
            binding.setVariable(replacedElementName, elementValue);
            return condition.replaceAll(elementName, replacedElementName);
        });

        private RequestElementProcessor processCondition;

        RequestElement(RequestElementProcessor requestElementProcessor) {
            this.processCondition = requestElementProcessor;
        }

        private boolean containedInCondition(String condition) {
            return condition.indexOf("@" + this.name()) != -1;
        }

        public RequestElementProcessor getProcessCondition() {
            return processCondition;
        }
    }

    @FunctionalInterface
    private interface RequestElementProcessor {
        String apply(String condition, Request request, Binding binding);
    }
}