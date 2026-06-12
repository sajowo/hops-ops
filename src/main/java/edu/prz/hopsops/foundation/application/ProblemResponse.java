package edu.prz.hopsops.foundation.application;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProblemResponse {

  String type;
  String title;
  String description;
  Integer status;
  String detail;
  String instance;
  List<String> violations;
}
