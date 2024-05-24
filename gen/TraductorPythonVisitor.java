public class TraductorPythonVisitor extends LatinoBaseVisitor{
    private int nivelIdent = 0;

    @Override
    public Object visitSi(LatinoParser.SiContext ctx) {
        System.out.print(" ".repeat(nivelIdent * 4) + "if ");
        visitExp(ctx.exp());
        System.out.println(":");

        nivelIdent++;
        visitSecnovac(ctx.secnovac());

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
        if (ctx.ID() != null) {
            System.out.print(ctx.ID().getText());
            if (!ctx.dictargs().isEmpty()) {

            }
        }
        System.out.print(ctx.getText());
        return super.visitVal(ctx);
    }

    @Override
    public Object visitDictargs(LatinoParser.DictargsContext ctx) {
        if (ctx.ID() != null) {
            System.out.print('[');
            System.out.print(ctx.ID().getText());
            System.out.print(']');
            visitDictargs(ctx.dictargs());
        } else if (ctx.expv() != null) {
            System.out.print('[');
            visitExpv(ctx.expv());
            System.out.print(']');
        }
        return 0;
    }

    @Override
    public Object visitSecnovac(LatinoParser.SecnovacContext ctx) {
        visitChildren(ctx);
        nivelIdent--;
        return 0;
    }
}
