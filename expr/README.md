expr
======

Super fast and simple evaluator for mathematical s-expressions written in Java. 

Using it is as simple as:

```java
Scope scope = Scope.create();   
Variable a = scope.getVariable("a");   
Expression expr = Parser.parse("3 + a * 4", scope);   
a.setValue(4);   
System.out.println(expr.evaluate());   
a.setValue(5);   
System.out.println(expr.evaluate());
```


## Maven

expr is available under:

    <dependency>
      <groupId>com.linkedin.featurefun</groupId>
      <artifactId>expr</artifactId>
      <version>1.0</version>
    </dependency>

