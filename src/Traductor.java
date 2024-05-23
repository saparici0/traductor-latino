import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;

public class Traductor {
    public static void main(String[] args) throws Exception {
        // create a CharStream that reads from standard input / file
        // create a lexer that feeds off of input CharStream
        LatinoLexer lexer;

        if (args.length>0)
            lexer = new LatinoLexer(CharStreams.fromFileName(args[0]));
        else
            lexer = new LatinoLexer(CharStreams.fromStream(System.in));

        // create a buffer of tokens pulled from the lexer
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        // create a parser that feeds off the tokens buffer
        LatinoParser parser = new LatinoParser(tokens);
        ParseTree tree = parser.gram(); // begin parsing at init rule

//        // Create a generic parse tree walker that can trigger callbacks
//        ParseTreeWalker walker = new ParseTreeWalker();
//        // Walk the tree created during the parse, trigger callbacks
//        walker.walk(new TraductorPython(), tree);
//        System.out.println(); // print a \n after translation

        TraductorPythonVisitor loader = new TraductorPythonVisitor();
        loader.visit(tree);
    }
}
