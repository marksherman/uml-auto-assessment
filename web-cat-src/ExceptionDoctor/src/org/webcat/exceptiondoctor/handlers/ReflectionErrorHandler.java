package org.webcat.exceptiondoctor.handlers;

import org.webcat.exceptiondoctor.ExceptionHandlerInterface;

public class ReflectionErrorHandler
    implements ExceptionHandlerInterface
{
    public Throwable wrapException(Throwable exToWrap)
    {
        return exToWrap;
    }
}
