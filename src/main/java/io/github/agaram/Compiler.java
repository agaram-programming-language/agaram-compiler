package io.github.agaram;

import io.github.agaram.grammar.ExprVisitor;
import io.github.agaram.grammar.Stmt;
import io.github.agaram.grammar.StmtVisitor;
import io.github.agaram.memory.Environment;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class Compiler {

    public static final Environment environment = new Environment();

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("அகரம் : script_file_name.அ");
            System.exit(64);
        } else if (args.length == 1) {
            runFile(args[0]);
        }
    }

    private static void runFile(String path) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(path));
        run(new String(bytes, Charset.defaultCharset()));
    }

    private static void run(String s) {
        compile(s);
    }


    public static void compile(String code) {
        try {
            Tokenizer tokenizer = new Tokenizer(code);
            interpretTokens(tokenizer.getTokens());
        }
        catch (Exception e) {
            System.out.println(e.getMessage() + "\n");
        }
    }

    private static void interpretTokens(List<Token> tokens) {
        Parser parser = new Parser(tokens);
        try {
            List<Stmt> stmts = parser.parse();

            ExprVisitor exprVisitor = new ExprVisitor(null, environment);
            StmtVisitor visitor = new StmtVisitor(environment, exprVisitor);
            exprVisitor.setStmtVisitor(visitor);
            for ( Stmt stmt: stmts ) {
               System.out.println(stmt.toString());
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage() + "\n");
            //System.exit(1);
        }


    }




}
