public class TraductorPythonVisitor extends LatinoBaseVisitor{
    @Override
    public Object visitSi(LatinoParser.SiContext ctx) {
        System.out.print("if ");
        visitExp(ctx.exp());
        System.out.println(":");

        for(int i = 0; i < ctx.osi().toArray().length; i++) {
            visitOsi(ctx.osi(i));
        }

        return visitChildren(ctx);
    }
}
