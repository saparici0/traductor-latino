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

    @Override
    public Object visitExp(LatinoParser.ExpContext ctx) {
        if (ctx.OPI() != null) {
            System.out.print(ctx.OPI().getText());
            visitExp(ctx.exp(0));
        } else if (ctx.val() != null) {
            visitVal(ctx.val());
            for (int i = 0; i < ctx.OP().toArray().length; i++) {
                System.out.print(ctx.OP(i).getText());
                visitExp(ctx.exp(i));
            }
        } else {
            System.out.print("(");
            visitExp(ctx.exp(0));
            System.out.print(")");
            for (int i = 0; i < ctx.OP().toArray().length; i++) {
                System.out.print(ctx.OP(i).getText());
                visitExp(ctx.exp(i));
            }
        }

        return 0;
    }

    @Override
    public Object visitVal(LatinoParser.ValContext ctx) {
        System.out.print(ctx.getText());
        return super.visitVal(ctx);
    }
}
