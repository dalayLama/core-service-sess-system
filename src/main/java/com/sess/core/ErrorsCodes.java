package com.sess.core;

public interface ErrorsCodes {

    String VALIDATE_ERROR = "100";

    String UNKNOWN_ERROR = "900";

    String NOT_NULLABLE_USER_ID = "";

    String FAILED_SAVE_NEW_USER = "";

    String READ_DTO_USER_DATA_ERROR = "";

    String WRITE_DTO_USER_DATA_ERROR = "";

    String WRITE_DTO_EVENT_DATA_ERROR = "";

    String NOT_NULLABLE_GROUP_ID = "";

    String GROUP_NOT_FOUND = "";

    String USER_NOT_FOUND = "";

    String USER_ALREADY_EXIST_IN_GROUP = "";

    String GROUP_TITLE_ALREADY_EXISTS = "";

    String USER_NOT_FOUND_IN_GROUP = "";
    String NOT_NULLABLE_RUNNING_TYPE_ID = "";
    String RUNNING_TYPE_CAPTION_ALREADY_EXISTS_IN_GROUP = "";
    String RUNNING_TYPE_NOT_FOUND = "";
    String NOT_NULLABLE_EVENT_ID = "";
    String DURING_CREATE_FACTUAL_DATES_MUST_BE_NULL = "";
    String EVENT_CAN_NOT_BE_CHANGED = "";
}
