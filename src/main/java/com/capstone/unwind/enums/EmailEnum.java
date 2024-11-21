package com.capstone.unwind.enums;

public enum EmailEnum {
    BASIC_MAIL(3),
    TRANSACTION_MAIL(4);

    private final long templateId;

    EmailEnum(long templateId) {
        this.templateId = templateId;
    }

    public long getTemplateId() {
        return templateId;
    }
}
