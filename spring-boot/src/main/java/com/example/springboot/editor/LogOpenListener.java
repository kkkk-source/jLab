package com.example.springboot.editor;

import java.io.File;

public class LogOpenListener implements EventListener {

  private File log;

  public LogOpenListener(String filePath) {
    this.log = new File(filePath);
  }

  @Override
  public void update(String eventType, File file) {
    System.out.println("Save to log " + log + ": Someone has performed " + eventType + " operation with the following file: " + file.getName());
  }

}
