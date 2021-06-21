package com.java.multithreading;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MultiThreadedHttpServer {

  public static String book = "/Users/sandishkumarhn/Downloads/optik-dag-spark-flow-etl-correlate_optik-flow-etl-correlate_2019-11-05T01_47_37+00_00_1.log";

  public static int NUM_THREADS = 4;
  public static void main(String[] args) throws IOException {
    String text = new String(Files.readAllBytes(Paths.get(book)));
    startServer(text);
  }

  public static void startServer(String text) throws IOException {
    HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
    server.createContext("/search", new WordCounthandler(text));

    Executor executor = Executors.newFixedThreadPool(NUM_THREADS);
    server.setExecutor(executor);
    server.start();
  }

  private static class WordCounthandler implements HttpHandler {
    private String text;

    public WordCounthandler(String text) {
      this.text = text;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
      String query = httpExchange.getRequestURI().getQuery();
      String[] keyValue = query.split("=");
      String action = keyValue[0];
      String word = keyValue[1];
      if(!action.equalsIgnoreCase("word")) {
        httpExchange.sendResponseHeaders(400, 0);
      }

      long count = coutnWord(word);

      byte[] response = Long.toString(count).getBytes();
      httpExchange.sendResponseHeaders(200, response.length);
      OutputStream outputStream = httpExchange.getResponseBody();
      outputStream.write(response);
      outputStream.close();
    }

    public long coutnWord(String word) {
      long count = 0;
      int index = 0;
      while (index >= 0) {
        index =  text.indexOf(word, index);
        if (index >= 0) {
          count++;
          index++;
        }
      }
      return count;
    }
  }
}
