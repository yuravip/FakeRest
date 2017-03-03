package one.kastordriver.fakerest.logic;

import one.kastordriver.fakerest.bean.Answer;
import one.kastordriver.fakerest.bean.Condition;
import one.kastordriver.fakerest.bean.Cookie;
import one.kastordriver.fakerest.bean.Route;
import spark.Request;
import spark.Response;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AnswerLogic {

    public static Object handle(Route route, Request request, Response response) throws Exception {
        try {
            Optional<Condition> suitableCondition = route.getConditions().stream()
                    .filter(condition -> condition.isSuitable(request))
                    .findFirst();

            return processAnswer(suitableCondition.isPresent() ? suitableCondition.get().getAnswer()
                                                               : route.getDefaultAnswer(), response);
        } catch (Exception ex) {
            System.out.println(ex);
            throw ex;
        }
    }

    private static Object processAnswer(Answer answer, Response response) {
        processStatus(response, answer.getStatus());
        processHeaders(response, answer.getHeaders());
        processCookies(response, answer.getCookies());
        processRemoveCookies(response, answer.getRemoveCookies());

        return answer.getBody();
    }

    private static void processStatus(Response response, Integer status) {
        if (status != null) {
            response.status(status);
        }
    }

    private static void processHeaders(Response response, Map<String, String> headers) {
        if (headers != null) {
            headers.forEach((key, value) -> response.header(key, value));
        }
    }

    private static void processCookies(Response response, List<Cookie> cookies) {
        if (cookies != null) {
            cookies.forEach(cookie -> {
                response.cookie(cookie.getPath(), cookie.getName(), cookie.getValue(),
                        cookie.getMaxAge(), cookie.isSecure());
            });
        }
    }

    private static void processRemoveCookies(Response response, List<String> cookiesForRemove) {
        if (cookiesForRemove != null) {
            cookiesForRemove.forEach(cookieForRemove -> response.removeCookie(cookieForRemove));
        }
    }
}