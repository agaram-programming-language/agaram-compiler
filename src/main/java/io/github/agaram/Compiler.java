package io.github.agaram;

import io.github.agaram.grammar.Stmt;
import io.github.agaram.memory.Environment;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Compiler {

    public static final Environment environment = new Environment();

    public static void main(String[] args) throws IOException {
        if (args.length > 1) {
            System.out.println("அகரம் : script_file_name.அ");
            System.exit(64);
        } else if (args.length == 1) {
            compileFile(args[0]);
        }
    }

    private static void compileFile(String path) throws IOException {
        Path sourceFilePath = Paths.get(path);
        byte[] bytes = Files.readAllBytes(sourceFilePath);
        List<String> compiledTokens = compile(new String(bytes, Charset.defaultCharset()));
        if (!compiledTokens.isEmpty()) {
            // Write to a new file.
            String sourceFileName = sourceFilePath.getFileName().toString();
            String compiledFileName = sourceFileName.substring(0, sourceFileName.length() - 2) + ".compiled";
            Files.write(Paths.get(sourceFilePath.getParent() + "/" + compiledFileName), Collections.singleton(String.join("\n", compiledTokens)));
        }
    }


    public static List<String> compile(String code) {
        try {
            Tokenizer tokenizer = new Tokenizer(code);
            return compileTokens(tokenizer.getTokens());
        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n");
            return List.of();
        }
    }

    private static List<String> compileTokens(List<Token> tokens) {
        Parser parser = new Parser(tokens);
        try {

            List<Stmt> stmts = parser.parse();

            List<String> compiledStatements = new ArrayList<>();

            for (Stmt stmt : stmts) {
                compiledStatements.add(stmt.toString());
            }

            return compiledStatements;

        } catch (Exception e) {
            System.out.println(e.getMessage() + "\n");
            //System.exit(1);
            return List.of();
        }


    }


}
