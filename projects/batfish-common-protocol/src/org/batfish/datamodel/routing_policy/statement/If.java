package org.batfish.datamodel.routing_policy.statement;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.batfish.datamodel.routing_policy.expr.BooleanExpr;
import org.batfish.datamodel.routing_policy.expr.BooleanExprs;

import com.fasterxml.jackson.annotation.JsonCreator;

public class If extends AbstractStatement {

   /**
    *
    */
   private static final long serialVersionUID = 1L;

   private List<Statement> _falseStatements;

   private BooleanExpr _guard;

   private List<Statement> _trueStatements;

   @JsonCreator
   public If() {
      _falseStatements = new ArrayList<Statement>();
      _trueStatements = new ArrayList<Statement>();
   }

   public List<Statement> getFalseStatements() {
      return _falseStatements;
   }

   public BooleanExpr getGuard() {
      return _guard;
   }

   public List<Statement> getTrueStatements() {
      return _trueStatements;
   }

   public void setFalseStatements(List<Statement> falseStatements) {
      _falseStatements = falseStatements;
   }

   public void setGuard(BooleanExpr guard) {
      _guard = guard;
   }

   public void setTrueStatements(List<Statement> trueStatements) {
      _trueStatements = trueStatements;
   }

   @Override
   public List<Statement> simplify() {
      List<Statement> simpleTrueStatements = new ArrayList<Statement>();
      List<Statement> simpleFalseStatements = new ArrayList<Statement>();
      BooleanExpr simpleGuard = _guard.simplify();
      for (Statement trueStatement : _trueStatements) {
         simpleTrueStatements.addAll(trueStatement.simplify());
      }
      for (Statement falseStatement : _falseStatements) {
         simpleFalseStatements.addAll(falseStatement.simplify());
      }
      if (simpleGuard.equals(BooleanExprs.True.toStaticBooleanExpr())) {
         return simpleTrueStatements;
      }
      else if (simpleGuard.equals(BooleanExprs.False.toStaticBooleanExpr())) {
         return simpleFalseStatements;
      }
      else if (simpleTrueStatements.size() == 0
            && simpleFalseStatements.size() == 0) {
         return Collections.<Statement> emptyList();
      }
      else {
         If simple = new If();
         simple.setGuard(simpleGuard);
         simple.setTrueStatements(simpleTrueStatements);
         simple.setFalseStatements(simpleFalseStatements);
         return Collections.singletonList(simple);
      }
   }

}