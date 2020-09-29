package main.java.DAO_design.DAO;

/**
 * DAO设计模式如何处理异常
 */
public class DaoException extends RuntimeException {
    public DaoException(Throwable cause) {
        super(cause);
    }

    protected DaoException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    public DaoException(String message) {
        super(message);
    }

    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoException() {
        super();
    }
}
