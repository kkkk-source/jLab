package com.example.springboot.editor;

import java.io.File;

public interface EventListener {
  void update(String eventType, File file);
}
