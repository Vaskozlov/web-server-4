package org.vaskozov.lab4.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.ejb.EJB;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.vaskozov.lab4.bean.RequestResults;
import org.vaskozov.lab4.lib.RequestParameters;
import org.vaskozov.lab4.lib.RequestValidationError;
import org.vaskozov.lab4.lib.Result;
import org.vaskozov.lab4.service.InAreaCheckerInterface;
import org.vaskozov.lab4.service.PointsValidationStorageInterface;
import org.vaskozov.lab4.service.RequestDataValidationService;

import java.io.IOException;

@WebServlet(urlPatterns = {"/check"}, asyncSupported = true)
public class AreaCheckServlet extends HttpServlet {
    private static final Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();

    @EJB(name = "java:global/lab4/PointsValidationStorage")
    private transient PointsValidationStorageInterface validationStorage;

    @EJB(name = "java:global/lab4/InAreaChecker")
    private transient InAreaCheckerInterface inAreaChecker;

    @EJB
    private transient RequestDataValidationService requestDataValidationService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        final long executionBeginNs = System.nanoTime();
        final var requestParameters = getParametersFromRequest(request);

        if (requestParameters.isError()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            gson.toJson(requestParameters.getError(), response.getWriter());
            return;
        }

        final RequestResults responseResult = formResponseResult(
                executionBeginNs,
                requestParameters.getValue()
        );

        validationStorage.save(request.getParameter("login"), responseResult);
        response.setStatus(200 + (responseResult.isInArea() ? 0 : 1));

        gson.toJson(responseResult, response.getWriter());
    }

    private Result<RequestParameters, RequestValidationError> getParametersFromRequest(HttpServletRequest request) {
        return requestDataValidationService.validateRequestData(
                request.getParameter("x"),
                request.getParameter("y"),
                request.getParameter("r")
        );
    }

    private RequestResults formResponseResult(long executionBegin, RequestParameters transformedData) {
        final boolean isInArea = inAreaChecker.check(
                transformedData.x(),
                transformedData.y(),
                transformedData.r()
        );

        RequestResults result = new RequestResults();

        result.setX(transformedData.x());
        result.setY(transformedData.y());
        result.setR(transformedData.r());
        result.setInArea(isInArea);
        result.setExecutionTimeNs(System.nanoTime() - executionBegin);

        return result;
    }
}
