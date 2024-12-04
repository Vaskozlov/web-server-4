package org.vaskozov.lab4.service;

import jakarta.ejb.Stateless;
import lombok.Getter;
import org.vaskozov.lab4.lib.RequestParameters;
import org.vaskozov.lab4.lib.RequestValidationError;
import org.vaskozov.lab4.lib.Result;

import java.util.Arrays;

@Stateless
public class RequestDataValidationService {
    private static final double DOUBLE_COMPARISON_ERROR = 1e-9;

    private static final String AVAILABLE_R_VALUES_AS_STRING = "1.0, 2.0, 3.0, 4.0";

    @Getter
    private static final double[] availableRValues = Arrays.stream(AVAILABLE_R_VALUES_AS_STRING.split(", "))
            .mapToDouble(Double::parseDouble)
            .toArray();

    private static Result<Double, RequestValidationError> validateNumber(String value, String valueName) {
        try {
            return Result.success(Double.parseDouble(value));
        } catch (NumberFormatException e) {
            return Result.error(new RequestValidationError(
                    valueName,
                    "%s must be a number".formatted(valueName)
            ));
        }
    }

    private static Result<Double, RequestValidationError> createMissingFieldError(String fieldName) {
        return Result.error(new RequestValidationError(
                fieldName,
                "Request must contain %s".formatted(fieldName)
        ));
    }

    private static Result<Double, RequestValidationError> validateRequestField(String field, String fieldName) {
        if (field == null) {
            return createMissingFieldError(fieldName);
        }

        return validateNumber(field, fieldName);
    }

    public Result<RequestParameters, RequestValidationError> validateRequestData(String x, String y, String r) {
        final Result<Double, RequestValidationError> xResult = validateRequestField(x, "x");
        final Result<Double, RequestValidationError> yResult = validateRequestField(y, "y");
        final Result<Double, RequestValidationError> rResult = validateRequestField(r, "r");

        if (xResult.isError()) {
            return Result.error(xResult.getError());
        }

        if (yResult.isError()) {
            return Result.error(yResult.getError());
        }

        if (rResult.isError()) {
            return Result.error(rResult.getError());
        }

        final double rValue = rResult.getValue();

        var requestParametersBuilder = RequestParameters.builder()
                .x(xResult.getValue())
                .y(yResult.getValue());

        for (double rVal : availableRValues) {
            if (Math.abs(rValue - rVal) <= DOUBLE_COMPARISON_ERROR) {
                return Result.success(requestParametersBuilder.r(rVal).build());
            }
        }

        return Result.error(new RequestValidationError(
                "r",
                "r must be in [%s]".formatted(AVAILABLE_R_VALUES_AS_STRING)
        ));
    }
}
