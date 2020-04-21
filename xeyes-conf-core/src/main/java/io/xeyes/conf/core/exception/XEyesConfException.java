package io.xeyes.conf.core.exception;

/**
 * XEyes conf 统一的异常
 * 继承自RuntimeException
 *
 */
public class XEyesConfException extends RuntimeException {

    private static final long serialVersionUID = 42L;

    public XEyesConfException(String msg) {
        super(msg);
    }

    public XEyesConfException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public XEyesConfException(Throwable cause) {
        super(cause);
    }

}
