package edu.agh.wfiis.solid.srp.example1;

import edu.agh.wfiis.solid.srp.example1.model.Constraint;
import edu.agh.wfiis.solid.srp.example1.model.Constraints;
import edu.agh.wfiis.solid.srp.example1.model.InvalidHeaderException;
import edu.agh.wfiis.solid.srp.example1.model.MuleMessage;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRestRequest {
    protected MuleMessage muleMessage;
    protected Constraints validationConstraints;

    public HttpRestRequest(MuleMessage muleMessage) {
        this.muleMessage = muleMessage;
    }

    public MuleMessage validate(Constraints validationConstraints) throws InvalidHeaderException {
        this.validationConstraints = validationConstraints;

        List<HeaderValidationError> headerValidationErrors = validateHeaders(
                muleMessage.getHeaders(),
                validationConstraints.getHeaderConstraints());

        if(!headerValidationErrors.isEmpty()) {
            throw new InvalidHeaderException(headerValidationErrors.get(0).getError());
        }

        setMissingMessageHeadersDefaultValues(validationConstraints.getHeaderConstraints());
        return muleMessage;
    }

    private List<HeaderValidationError> validateHeaders(Map<String, String> headerValuesByNames, List<Constraint> headerConstraints) {
        List<HeaderValidationError> errorMessages = new ArrayList<>();

        for (Constraint constraint : headerConstraints) {
            String headerName = constraint.getHeaderName();
            String headerValue = headerValuesByNames.get(headerName);

            if (headerValue == null && constraint.isHeaderRequired()) {
                errorMessages.add(new MissingHeaderValidationError(headerName));
            }

            if (headerValue != null) {
                if (!constraint.validate(headerValue)) {
                    errorMessages.add(new InvalidHeaderValueValidationError(headerName, headerValue));
                }
            }
        }

        return errorMessages;
    }

    private void setMissingMessageHeadersDefaultValues(List<Constraint> headerConstraints) {
        for (Constraint constraint : headerConstraints) {
            tryToSetDefaultHeaderValue(constraint);
        }
    }

    private boolean tryToSetDefaultHeaderValue(Constraint constraint) {
        if (muleMessage.getHeader(constraint.getHeaderName()) == null && constraint.getDefaultValue() != null) {
            setDefaultHeaderValue(constraint, muleMessage);
            return true;
        }
        return false;
    }

    private void setDefaultHeaderValue(Constraint constraint, MuleMessage muleMessage){
        muleMessage.setHeader(constraint.getHeaderName(), constraint.getDefaultValue());
    }
}

interface HeaderValidationError {
    String getError();
}

class MissingHeaderValidationError implements HeaderValidationError {
    private final String headerName;

    MissingHeaderValidationError(String headerName) {
        this.headerName = headerName;
    }

    @Override
    public String getError() {
        return MessageFormat.format("Required header {0} not specified", headerName);
    }
}

class InvalidHeaderValueValidationError implements HeaderValidationError {
    private final String headerName;
    private final String headerValue;

    InvalidHeaderValueValidationError(String headerName, String headerValue) {
        this.headerName = headerName;
        this.headerValue = headerValue;
    }

    @Override
    public String getError() {
        return MessageFormat.format("Invalid value {0} format for header {1}.", headerValue, headerName);
    }
}