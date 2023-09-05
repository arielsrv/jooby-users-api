package com.github.core.http;

import java.io.IOException;

/**
 * The type Response.
 *
 * @param <T> the type parameter
 */
public class Response<T> {

  private final int code;
  private final T data;

  /**
   * Instantiates a new Response.
   *
   * @param code the code
   * @param data the data
   */
  public Response(int code, T data) {
    this.code = code;
    this.data = data;
  }

  /**
   * Verify ok or fail.
   *
   * @throws IOException the io exception
   */
  public void verifyOkOrFail() throws IOException {
    if ((200 <= this.code) && (this.code <= 299)) {
      return;
    }
    throw new IOException();
  }

  /**
   * Gets data.
   *
   * @return the data
   */
  public T getData() {
    return this.data;
  }
}
