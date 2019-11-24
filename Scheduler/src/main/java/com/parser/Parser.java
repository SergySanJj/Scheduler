package com.parser;

import java.util.logging.Logger;

public interface Parser<T> {
    public static Logger parserLogger = Logger.getLogger("ParserLogger");

    public T parse(String xmlPath);
}
