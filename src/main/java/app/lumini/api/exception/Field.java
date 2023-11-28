package app.lumini.api.exception;

public class Field {

    private String field;
    private String code;
    private String message;

    public static Field from(String field, String code, String message) {
        Field error = new Field();
        error.field = field;
        error.code = code;
        error.message = message;
        return error;
    }

    public String getField() {
        return field;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
