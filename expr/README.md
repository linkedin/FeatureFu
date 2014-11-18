expr
======

Super fast and simple evaluator for mathematical s-expressions written in Java. 

Using it is as simple as:

```java
        VariableRegistry variableRegistry=new VariableRegistry();
        Expr expression = Expression.parse("(sigmoid (+ (* a x) b))",variableRegistry);

        Variable x = variableRegistry.findVariable("x");
        Variable a = variableRegistry.findVariable("a");
        Variable b = variableRegistry.findVariable("b");
        expression.evaluate();

        Map<String,Double> varMap = new HashMap<String,Double>();

        varMap.put("x",0.2);
        varMap.put("a",0.6);
        varMap.put("b",0.8);

        variableRegistry.refresh(varMap);
        expression.evaluate();
```

```command line
$java -cp expr-1.0.jar com.linkedin.featurefun.expr.Expression "(+ 0.5 (* (/ 15 1000) (ln (- 55 12))))"

=(0.5+((15.0/1000.0)*ln((55.0-12.0))))
=0.5564180017354035
tree
└── +
    ├── 0.5
    └── *
        ├── /
        |   ├── 15.0
        |   └── 1000.0
        └── ln
            └── -
                ├── 55.0
                └── 12.0

```


## Maven

expr is available under:

    <dependency>
      <groupId>com.linkedin.featurefun</groupId>
      <artifactId>expr</artifactId>
      <version>1.0</version>
    </dependency>

