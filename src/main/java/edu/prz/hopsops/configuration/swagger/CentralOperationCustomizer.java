package edu.prz.hopsops.configuration.swagger;

import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.val;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;

@Component
public class CentralOperationCustomizer implements OperationCustomizer {

  public static final String QUERY = "query";

  @Override
  public Operation customize(Operation operation, HandlerMethod handlerMethod) {
    val beanClass = handlerMethod.getBeanType();
    if (!beanClass.getName().startsWith("edu.prz.hopsops")) {
      return operation;
    }
    val tag = beanClass.getAnnotation(Tag.class);
    if (tag == null) {
      throw new IllegalArgumentException(
          "@Tag annotation is missing for " + handlerMethod.getBeanType().getName());
    }
    operation.setOperationId(createOperationId(handlerMethod.getMethod().getName(), tag.name()));
    customizePageable(operation);

    return operation;
  }

  private String createOperationId(String methodName, String resourceName) {
    String rn = resourceName
        .replace(" ", "")
        .replace("/", "-");
    rn = kebabToCamel(rn);
    return methodName + "Of" + rn;
  }

  private String kebabToCamel(String kebab) {
    return Arrays.stream(kebab.split("-"))
        .map(s -> Character.toUpperCase(s.charAt(0)) + s.substring(1).toLowerCase())
        .collect(Collectors.joining());
  }

  public void customizePageable(Operation operation) {
    if (operation.getParameters() == null) {
      return;
    }

    val pageable = operation.getParameters().stream()
        .filter(p -> QUERY.equals(p.getIn()) && p.getSchema() != null
            && "#/components/schemas/Pageable".equals(p.getSchema().get$ref()))
        .findFirst();
    if (pageable.isEmpty()) {
      return;
    }

    List<Parameter> parameters = new ArrayList<>();
    parameters.add(new Parameter()
        .name("page")
        .description("Number of page to retrieve (0..N)")
        .in(QUERY)
        .schema(new IntegerSchema())
        .required(false));
    parameters.add(new Parameter()
        .name("size")
        .description("Number of records per page")
        .in(QUERY)
        .schema(new IntegerSchema())
        .required(false));
    parameters.add(new Parameter()
        .name("sort")
        .description("Sorting criteria in the format: attrName,asc|desc. "
            + "Order is optional, default ascending. "
            + "Multiple sort criteria are supported.")
        .in(QUERY)
        .schema(new StringSchema())
        .required(false));

    operation.getParameters().remove(pageable.get());
    operation.getParameters().addAll(parameters);
  }
}
