FeatureFun
======

FeatureFun contains a collection of library/tools for advanced feature engineering, such as using extended s-expression
based feature transformation, to derive features on top of other features, or convert a light weighted model (logistical
regression or decision tree) into a feature, in an intuitive way without touching any code.

Sample use cases:

1. Feature normalization
    "(min 1 (max (+ (* slope x) intercept) 0))" : scale feature x with slope and intercept, and normalize to [0,1]

2. Feature combination
   "(‐ (log2 (+ 5 impressions)) (log2 (+ 1 clicks)))" : combine #impression and #clicks into a smoothed CTR style feature

3. Nonlinear featurization
   "(if (> query_doc_matches 0) 0 1)" : negation of a query/document matching feature

4. cascading modeling
   "(sigmoid (....))"  : convert a logistic regression model into a feature

5. Model combination (e.g. combine decision tree and linear regression)


Expr: A super fast and simple evaluator for mathematical s-expressions written in Java.

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
      <version>0.0.8</version>
    </dependency>

## Gradle

  dependencies {
    compile "com.linkedin.featurefun:expr:0.0.8"
  }