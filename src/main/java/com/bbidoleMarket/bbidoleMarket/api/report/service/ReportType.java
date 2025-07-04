package com.bbidoleMarket.bbidoleMarket.api.report.service;

public enum ReportType {
    CHAT_ROOM,
    POST,
    USER,
    NONE; // NONE is used when the type is not recognized or not specified

    public static ReportType fromString(String type) {
        for (ReportType reportType : ReportType.values()) {
            if (reportType.name().equalsIgnoreCase(type)) {
                return reportType;
            }
        }
        return NONE;
    }
}
