package org.testcontainers.repro.util;

import lombok.NoArgsConstructor;
import okhttp3.MediaType;

@NoArgsConstructor
public class Constants {
  public static final String TXN_EVENTS_URI = "/v1/txnEvents";

  public static final String TRANSACTION_WS_APP_NAME = "gateway-transaction-ws";

  public static final String SLASH = "/";

  public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
}
