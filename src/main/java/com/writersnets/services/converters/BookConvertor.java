package com.writersnets.services.converters;

import com.writersnets.models.exceptions.TextConvertingException;

/**
 * Created by mhenr on 01.11.2017.
 */
public interface BookConvertor<T> {
    String toHtml(T text) throws TextConvertingException;
}
