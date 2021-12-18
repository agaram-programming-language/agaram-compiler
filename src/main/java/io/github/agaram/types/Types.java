package io.github.agaram.types;

import io.github.agaram.functions.Return;
import io.github.agaram.grammar.Stmt;
import io.github.agaram.grammar.StmtVisitor;
import io.github.agaram.memory.Environment;
import lombok.RequiredArgsConstructor;

import java.util.List;

public class Types {

    @RequiredArgsConstructor
    public static class AgBoolean implements AgaramType {
        public final boolean result;
        @Override
        public String toString() {
            if ( this.result ) {
                return "உண்மை";
            }
            else {
                return "பொய்";
            }
        }

        @Override
        public Object getValue() {
            return result;
        }
    }


    @RequiredArgsConstructor
    public static class AgDouble implements AgaramType {
        public final double result;

        @Override
        public Object getValue() {
            return result;
        }
    }


    @RequiredArgsConstructor
    public static class AgString implements AgaramType {
        public final String result;

        @Override
        public Object getValue() {
            return result;
        }
    }

    @RequiredArgsConstructor
    public  static class AgFunction implements AgaramType{

        private final Stmt.FunctionStmt declaration;

        public Object call(StmtVisitor stmtVisitor, List<AgaramType> arguments) {
            Environment environment = new Environment(stmtVisitor.environment);
            for (int i = 0; i < declaration.parameters.size(); i++) {
                environment.define(declaration.parameters.get(i).value,
                        typeConvert(arguments.get(i).getValue()));
            }
            try {
                stmtVisitor.executeBlock(declaration.body, environment);
            }
            catch (Return r) {
                return r.value;
            }
            return null;
        }

        private AgaramType typeConvert(Object value) {
            if (value instanceof  String ) {
                return new AgString((String) value);
            }
            else if ( value instanceof Double ) {
                return new AgDouble((Double) value);
            }
            else if (value instanceof Boolean ) {
                return new AgBoolean((Boolean) value);
            }
            return null;
        }

        @Override
        public Object getValue() {
            return null;
        }
    }

}
