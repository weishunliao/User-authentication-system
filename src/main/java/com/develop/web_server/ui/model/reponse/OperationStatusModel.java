package com.develop.web_server.ui.model.reponse;

public class OperationStatusModel {
    private String operationResult;
    private String operationName;

    public String getOperationResult() {
        return operationResult;
    }

    public void setOperationResult(String operationResult) {
        this.operationResult = operationResult;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    @Override
    public String toString() {
        return "OperationStatusModel{" +
                "operationResult='" + operationResult + '\'' +
                ", operationName='" + operationName + '\'' +
                '}';
    }
}


