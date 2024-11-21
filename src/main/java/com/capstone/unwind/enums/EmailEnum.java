package com.capstone.unwind.enums;

public enum EmailEnum {
    WELCOME_EMAIL(3),
    PASSWORD_RESET(2),
    ORDER_CONFIRMATION(1);

    private final long templateId;

    EmailEnum(long templateId) {
        this.templateId = templateId;
    }

    public long getTemplateId() {
        return templateId;
    }
}
