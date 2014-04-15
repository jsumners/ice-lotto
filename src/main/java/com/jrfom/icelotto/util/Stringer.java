package com.jrfom.icelotto.util;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Stringer {
  private static final Logger log = LoggerFactory.getLogger(Stringer.class);

  public static <T> String jsonString(T obj) {
    return Stringer.jsonString(obj, false);
  }

  public static <T> String jsonString(T obj, boolean pretty) {
    ObjectMapper mapper = new ObjectMapper();
    String json = "<could not serialize object>";

    try {
      if (pretty) {
        json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
      } else {
        json = mapper.writeValueAsString(obj);
      }
    } catch (JsonProcessingException e) {
      log.error("Could not create JSON string: `{}`", e.getMessage());
      log.debug(e.toString());
    }

    return json;
  }

  public static String readClasspathFileToString(String fileName) {
    log.debug("Reading `{}` to string", fileName);
    URL fileUrl = Resources.getResource(fileName);
    String contents = null;

    try {
      contents = Resources.toString(fileUrl, Charset.forName("UTF-8"));
    } catch (IOException e) {
      log.error("Could not read resource: `{}`", e.getMessage());
      log.debug(e.toString());
    }

    return contents;
  }
}